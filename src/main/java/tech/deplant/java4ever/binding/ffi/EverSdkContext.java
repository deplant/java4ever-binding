package tech.deplant.java4ever.binding.ffi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;

import java.lang.foreign.Arena;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class EverSdkContext {

	private final static System.Logger logger = System.getLogger(EverSdkContext.class.getName());

	private final int id;
	private final AtomicInteger requestCount = new AtomicInteger();
	private final Client.ClientConfig clientConfig;

	private final Queue<Integer> requestRemoveQueue = new ConcurrentLinkedDeque<>();

	@JsonIgnore private final Map<Integer, NativeUpcallHandler> requests = new ConcurrentHashMap<>();
	@JsonIgnore private final Map<Integer, CompletableFuture<String>> responses = new ConcurrentHashMap<>();
	@JsonIgnore private final Map<Integer, SdkSubscription> subscriptions = new ConcurrentHashMap<>();

	public EverSdkContext(int id, Client.ClientConfig clientConfig) {
		this.id = id;
		this.clientConfig = clientConfig;
	}

//	public <R> R sync(R calleeFunction) {
//		return ScopedValue.where(EverSdk.CONTEXT, this)
//		                  .get(() -> calleeFunction);
//	}

	/**
	 * Call for methods that use app_object as one of params
	 *
	 * @param functionName
	 * @param params
	 * @param appObject
	 * @param clazz
	 * @param <T>
	 * @param <P>
	 * @param <A>
	 * @return
	 * @throws EverSdkException
	 */
	public <T, P, A> T callAppObject(String functionName,
	                                 P params,
	                                 A appObject,
	                                 Class<T> clazz) throws EverSdkException {
		return call(functionName, params, clazz);
	}

	/**
	 * Call that uses event as consumer param
	 *
	 * @param functionName
	 * @param params
	 * @param consumer
	 * @param resultClass
	 * @param <T>          type of function result
	 * @param <P>          type of function params
	 * @return
	 * @throws EverSdkException
	 */
	public <T, P> T callEvent(String functionName,
	                          P params,
	                          Consumer<JsonNode> consumer,
	                          Class<T> resultClass) throws EverSdkException {
		final int requestId = requestCountNextVal();
		this.subscriptions.put(requestId, new SdkSubscription(consumer));
		addRequest(requestId, functionName, params, true);
		return awaitSyncResponse(requestId, resultClass);
	}

	/**
	 * Most used call to EVER-SDK with some output object
	 *
	 * @param functionName
	 * @param params       record of input type, usually ParamsOf...
	 * @param resultClass  class of output type record, usually ResultOf...class
	 * @param <T>
	 * @param <P>
	 * @return output type record, usually ResultOf...
	 * @throws EverSdkException
	 */
	public <T, P> T call(String functionName, P params, Class<T> resultClass) throws EverSdkException {
		final int requestId = requestCountNextVal();
		addRequest(requestId, functionName, params, true);
		return awaitSyncResponse(requestId, resultClass);
	}

	/**
	 * Calls to EVER-SDK without outputs
	 *
	 * @param functionName
	 * @param params
	 * @param <P>
	 * @throws EverSdkException
	 */
	public <P> void callVoid(String functionName, P params) throws EverSdkException {
		final int requestId = requestCountNextVal();
		addRequest(requestId, functionName, params, false);
	}

	public int requestCountNextVal() {
		return this.requestCount.incrementAndGet();
	}

	public void addResponse(int requestId, String responseString) {
		if (this.responses.containsKey(requestId)) {
			this.responses.get(requestId).complete(responseString);
		} else {
			logger.log(System.Logger.Level.ERROR,
			           () -> "Slot for this request not found on processing response! CTX:%d REQ:%d RESP:%s".formatted(this.id, requestId, responseString));
		}
	}

	public void addError(int requestId, String responseString) {
		if (this.responses.containsKey(requestId)) {
			this.responses.get(requestId).completeExceptionally(new CompletionException(responseString, null));
		} else {
			logger.log(System.Logger.Level.ERROR,
			           () -> "Slot for this request not found on processing error response! CTX:%d REQ:%d ERR:%s".formatted(this.id, requestId, responseString));
		}

	}

	public void addEvent(int requestId, String responseString) {
		if (this.subscriptions.containsKey(requestId)) {
			var subscription = this.subscriptions.get(requestId);
			subscription.events().add(responseString);
			try {
				JsonNode node = JsonContext.ABI_JSON_MAPPER().readTree(responseString);
				subscription.eventConsumer().accept(node);
			} catch (JsonProcessingException e) {
				logger.log(System.Logger.Level.ERROR,
				           () -> "REQ:%d JSON Tree deserialization failed on processing event! String: %s Cause: %s".formatted(
						           requestId,
						           responseString,
						           e.getMessage() + e.getCause()));
			}
		} else {
			logger.log(System.Logger.Level.ERROR,
			           () -> "Slot for this request not found on processing subscription event! CTX:%d REQ:%d EVENT:%s".formatted(this.id, requestId, responseString));
		}
	}

	public void finishRequest(int requestId) {
		requestRemoveQueue.add(requestId);
		if (requestRemoveQueue.size() > 10) {
			this.requests.remove(requestRemoveQueue.poll());
		}
		logger.log(System.Logger.Level.DEBUG,
		           () -> "Requests current size: " + this.requests.size());
		this.subscriptions.remove(requestId);
	}

	public void finishResponse(int requestId) {
		this.responses.remove(requestId);
	}

	private <P> String processParams(P params) {
		try {
			return (null == params) ? "" : JsonContext.SDK_JSON_MAPPER().writeValueAsString(params);
		} catch (JsonProcessingException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "Parameters serialization failed!" + e.getMessage() + e.getCause());
			throw new IllegalArgumentException("Parameters serialization failed!", e);
		}
	}

	private <R> R awaitSyncResponse(int requestId, Class<R> resultClass) throws EverSdkException {
		try {
			long timeout = Optional.ofNullable(this.clientConfig.network())
			                       .map(config -> Optional.ofNullable(config.queryTimeout()).orElse(60000L))
			                       .orElse(60000L);
			String responseString = this.responses.get(requestId)
			                                      .get(timeout,
			                                           TimeUnit.MILLISECONDS);
			logger.log(System.Logger.Level.TRACE,
			           () -> "CTX:%d REQ:%d RESP:%s".formatted(this.id, requestId, responseString));
			return JsonContext.SDK_JSON_MAPPER().readValue(responseString, resultClass);
		} catch (CompletionException | ExecutionException e) {
			// These errors are sent by SDK, response_type=1
			EverSdkException.ErrorResult sdkResponse = null;
			// let's try to parse error response
			try {
				String errorString = e.getCause().getMessage();
				sdkResponse = JsonContext.SDK_JSON_MAPPER().readValue(errorString, EverSdkException.ErrorResult.class);
				logger.log(System.Logger.Level.WARNING,
				           () -> "CTX:%d REQ:%d ERR:%s".formatted(this.id, requestId, errorString));
				throw new EverSdkException(sdkResponse, e);
			} catch (JsonProcessingException ex) {
				// if error response parsing failed
				logger.log(System.Logger.Level.ERROR,
				           () -> "CTX:%d REQ:%d ERR:%s".formatted(this.id,
				                                                  requestId,
				                                                  "SDK Error message deserialization failed! Check getCause() for actual response."));
				throw new EverSdkException(new EverSdkException.ErrorResult(-500,
				                                                            "SDK Error message deserialization failed! Check getCause() for actual response. Message: %s JSON Err: %s".formatted(
						                                                            e.getCause().getMessage(),
						                                                            ex.getMessage())),
				                           ex);
			}
		} catch (JsonProcessingException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "Successful response deserialization failed!" + e.getMessage() + e.getCause());
			throw new EverSdkException(new EverSdkException.ErrorResult(-500,
			                                                            "Successful response deserialization failed! Check getCause() for actual response."));
		} catch (InterruptedException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "CTX:%d REQ:%d ERR:%s".formatted(this.id,
			                                                  requestId, "EVER-SDK call interrupted!"));
			throw new EverSdkException(new EverSdkException.ErrorResult(-400, "EVER-SDK call interrupted!"), e);
		} catch (TimeoutException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "CTX:%d REQ:%d ERR:%s".formatted(this.id,
			                                                  requestId, "EVER-SDK Execution expired on Timeout!"));
			throw new EverSdkException(new EverSdkException.ErrorResult(-408,
			                                                            "EVER-SDK Execution expired on Timeout!"), e);

		} finally {
			finishResponse(requestId);
		}
	}

	/**
	 * Most used call to EVER-SDK with some output object
	 *
	 * @param functionName
	 * @param params       record of input type, usually ParamsOf...
	 * @param <P>
	 * @throws EverSdkException
	 */
	private <P> void addRequest(int requestId, String functionName, P params, boolean hasResponse) {
		final NativeUpcallHandler handler = new NativeUpcallHandler(this.id, hasResponse);
		this.requests.put(requestId, handler);
		if (hasResponse) {
			this.responses.put(requestId, new CompletableFuture<>());
		}
		var paramsJson = processParams(params);
		NativeMethods.tcRequest(this.id, functionName, paramsJson, Arena.ofAuto(), requestId, handler);
		logger.log(System.Logger.Level.TRACE,
		           () -> EverSdk.LOG_FORMAT.formatted(this.id, requestId, functionName, "SEND", paramsJson));
	}

	public int id() {
		return this.id;
	}

	public int requestCount() {
		return this.requestCount.get();
	}

	public Client.ClientConfig config() {
		return this.clientConfig;
	}

}
