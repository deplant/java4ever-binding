package tech.deplant.java4ever.binding.ffi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;
import tech.deplant.java4ever.binding.EverSdk;

import java.lang.foreign.Arena;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class EverSdkContext {

	private final static System.Logger logger = System.getLogger(EverSdkContext.class.getName());
	private final int id;
	private final AtomicInteger requestCount;

	private final Client.ClientConfig clientConfig;
	@JsonIgnore private final Map<Integer, NativeUpcallHandler> requests;
	@JsonIgnore private final Map<Integer, CompletableFuture<String>> responses;
	@JsonIgnore private final Map<Integer, SdkSubscription> subscriptions;

	public EverSdkContext(int id, Client.ClientConfig clientConfig) {
		this.id = id;
		this.clientConfig = clientConfig;
		this.requestCount = new AtomicInteger();
		this.requests = new HashMap<>();
		this.responses = new HashMap<>();
		this.subscriptions = new HashMap<>();
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
		addRequest(requestId, functionName, params);
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
		addRequest(requestId, functionName, params);
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
		addRequest(requestId, functionName, params);
		finishResponse(requestId);
	}

	public int requestCountNextVal() {
		return this.requestCount.incrementAndGet();
	}

	public void addResponse(int requestId, String responseString) {
		if (this.responses.containsKey(requestId)) {
			this.responses.get(requestId).complete(responseString);
		}
	}

	public void addError(int requestId, String responseString) {
		this.responses.get(requestId).completeExceptionally(new CompletionException(responseString, null));
	}

	public void addEvent(int requestId, String responseString) {
		var subscription = this.subscriptions.get(requestId);
		subscription.events().add(responseString);
		JsonNode node = null;
		try {
			node = JsonContext.ABI_JSON_MAPPER().readTree(responseString);
			subscription.eventConsumer().accept(node);
		} catch (JsonProcessingException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "REQ:%d JSON Tree deserialization failed on processing event! String: %s Cause: %s".formatted(
					           requestId,
					           responseString,
					           e.getMessage() + e.getCause()));
		}
	}

	public void finishRequest(int requestId) {
		this.requests.remove(requestId);
		//this.responses.remove(requestId);
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
			long timeout = Optional.ofNullable(this.clientConfig.network().queryTimeout()).orElse(60000L);
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
				                                                            "SDK Error message deserialization failed! Check getCause() for actual response. Message: %s JSON Err: %s".formatted(e.getCause().getMessage(),ex.getMessage())),
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
			                                                  requestId,"EVER-SDK call interrupted!"));
			throw new EverSdkException(new EverSdkException.ErrorResult(-400, "EVER-SDK call interrupted!"), e);
		} catch (TimeoutException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "CTX:%d REQ:%d ERR:%s".formatted(this.id,
			                                                  requestId,"EVER-SDK Execution expired on Timeout!"));
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
	private <P> void addRequest(int requestId, String functionName, P params) {
		final NativeUpcallHandler handler = new NativeUpcallHandler(this.id);
		this.requests.put(requestId, handler);
		this.responses.put(requestId, new CompletableFuture<>());
		var paramsJson = processParams(params);
		NativeMethods.tcRequest(this.id, functionName, paramsJson, Arena.ofAuto(), requestId, handler);
		logger.log(System.Logger.Level.TRACE,
		           () -> EverSdk.LOG_FORMAT.formatted(this.id, requestId, functionName, "SEND", paramsJson));
	}
}
