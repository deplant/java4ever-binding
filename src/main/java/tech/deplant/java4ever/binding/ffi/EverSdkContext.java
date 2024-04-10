package tech.deplant.java4ever.binding.ffi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.JsonContext;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EverSdkContext implements tc_response_handler_t {

	private final static System.Logger logger = System.getLogger(EverSdkContext.class.getName());

	private final int id;
	private final AtomicInteger requestCount = new AtomicInteger();
	private final Client.ClientConfig clientConfig;

	//private final Queue<Integer> requestRemoveQueue = new ConcurrentLinkedDeque<>();

	@JsonIgnore private final Map<Integer, Arena> requests = new ConcurrentHashMap<>();
	@JsonIgnore private final Map<Integer, CompletableFuture<String>> responses = new ConcurrentHashMap<>();
	@JsonIgnore private final Map<Integer, EverSdkSubscription> subscriptions = new ConcurrentHashMap<>();

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
	 * @param subscription
	 * @param resultClass
	 * @param <T>          type of function result
	 * @param <P>          type of function params
	 * @return
	 * @throws EverSdkException
	 */
	public <T, P> T callEvent(String functionName,
	                          P params,
	                          EverSdkSubscription subscription,
	                          Class<T> resultClass) throws EverSdkException {
		final int requestId = requestCountNextVal();
		this.subscriptions.put(requestId, subscription);
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
			           "Slot for this request not found on processing response! CTX:%d REQ:%d RESP:%s".formatted(this.id,
			                                                                                                     requestId,
			                                                                                                     responseString));
		}
	}

	public void addError(int requestId, String responseString) {
		if (this.responses.containsKey(requestId)) {
			this.responses.get(requestId).completeExceptionally(new CompletionException(responseString, null));
		} else {
			logger.log(System.Logger.Level.ERROR,
			           "Slot for this request not found on processing error response! CTX:%d REQ:%d ERR:%s".formatted(
					           this.id,
					           requestId,
					           responseString));
		}

	}

	public void addEvent(int requestId, String responseString) {
		if (this.subscriptions.containsKey(requestId)) {
			this.subscriptions.get(requestId).acceptEvent(requestId, responseString);
		} else {
			logger.log(System.Logger.Level.ERROR,
			           "Slot for this request not found on processing subscription event! CTX:%d REQ:%d EVENT:%s".formatted(
					           this.id,
					           requestId,
					           responseString));
		}
	}

	public void finishRequest(int requestId) {
		if (this.requests.get(requestId) instanceof Arena a) {
			a.close();
		}
		this.requests.remove(requestId);
/*		requestRemoveQueue.add(requestId);
		if (requestRemoveQueue.size() > 10) {
			int removedRequestId = requestRemoveQueue.poll();
			logger.log(System.Logger.Level.TRACE, "Removed REQ:%d".formatted(removedRequestId));

		}
		logger.log(System.Logger.Level.DEBUG,
		           () -> "Requests current size: " + this.requests.size());*/
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

	private long callTimeout() {
		return Optional.ofNullable(this.clientConfig.network())
		               .map(config -> Optional.ofNullable(config.queryTimeout()).orElse(60000L))
		               .orElse(60000L);
	}

	private <R> R awaitSyncResponse(int requestId, Class<R> resultClass) throws EverSdkException {
		try {
			// waiting for response synchronously
			String responseString = this.responses.get(requestId).get(callTimeout(), TimeUnit.MILLISECONDS);
			logger.log(System.Logger.Level.TRACE, () -> STR."CTX:\{this.id} REQ:\{requestId} RESP:\{responseString}");
			// let's try to parse response
			return JsonContext.SDK_JSON_MAPPER().readValue(responseString, resultClass);
		} catch (ExecutionException ex) {
			try {
				// These errors are sent by SDK, response_type=1
				String everSdkError = ex.getCause().getMessage();
				logger.log(System.Logger.Level.WARNING,
				           () -> STR."CTX:\{this.id} REQ:\{requestId} ERR:\{everSdkError}");
				// let's try to parse error response
				EverSdkException.ErrorResult sdkResponse = JsonContext.SDK_JSON_MAPPER()
				                                                      .readValue(everSdkError,
				                                                                 EverSdkException.ErrorResult.class);
				throw new EverSdkException(sdkResponse, ex);
			} catch (JsonProcessingException ex1) {
				// if error response parsing failed
				logger.log(System.Logger.Level.ERROR,
				           () -> STR."CTX:\{this.id} REQ:\{requestId} EVER-SDK Error deserialization failed! \{ex1.toString()}");
				throw new EverSdkException(new EverSdkException.ErrorResult(-500,
				                                                            "EVER-SDK Error deserialization failed!"),
				                           ex1.getCause());
			}
		} catch (JsonProcessingException ex2) {
			// successful response but parsing failed
			logger.log(System.Logger.Level.ERROR,
			           () -> STR."CTX:\{this.id} REQ:\{requestId} EVER-SDK Response deserialization failed! \{ex2.toString()}");
			throw new EverSdkException(new EverSdkException.ErrorResult(-500,
			                                                            "EVER-SDK response deserialization failed!"),
			                           ex2.getCause());
		} catch (InterruptedException ex3) {
			logger.log(System.Logger.Level.ERROR,
			           () -> STR."CTX:\{this.id} REQ:\{requestId} EVER-SDK Call interrupted! \{ex3.toString()}");
			throw new EverSdkException(new EverSdkException.ErrorResult(-400, "EVER-SDK call interrupted!"),
			                           ex3.getCause());
		} catch (TimeoutException ex4) {
			logger.log(System.Logger.Level.ERROR,
			           () -> STR."CTX:\{this.id} REQ:\{requestId} EVER-SDK Call expired on Timeout! \{ex4.toString()}");
			throw new EverSdkException(new EverSdkException.ErrorResult(-408, "EVER-SDK call expired on Timeout!"),
			                           ex4.getCause());

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
		//final NativeUpcallHandler handler = new NativeUpcallHandler(this.id, hasResponse);
		var arena = Arena.ofShared();
		this.requests.put(requestId, Arena.ofShared());
		if (hasResponse) {
			this.responses.put(requestId, new CompletableFuture<>());
		}
		var paramsJson = processParams(params);
		NativeMethods.tcRequest(this.id, functionName, paramsJson, arena, requestId, this);
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

	/**
	 * @param request_id
	 * @param params_json   memory segment with native response string
	 * @param response_type Type of response with following possible values:
	 *                      RESULT = 1, real response.
	 *                      NOP = 2, no operation. In combination with finished = true signals that the request handling was finished.
	 *                      APP_REQUEST = 3, request some data from application. See Application objects
	 *                      APP_NOTIFY = 4, notify application with some data. See Application objects
	 *                      RESERVED = 5..99 – reserved for protocol internal purposes. Application (or binding) must ignore this response.
	 *                      CUSTOM >= 100 - additional function data related to request handling. Depends on the function.
	 * @param finished
	 */
	@Override
	public void apply(int request_id, MemorySegment params_json, int response_type, boolean finished) {
		try (var arena = Arena.ofShared()) {
			final String responseString = NativeStrings.toJava(params_json, arena);
			if (logger.isLoggable(System.Logger.Level.TRACE)) {
				logger.log(System.Logger.Level.TRACE,
				           STR."REQ:\{request_id} TYPE:\{response_type} FINISHED:\{finished} JSON:\{responseString}");
			}
			if (response_type == ton_client.tc_response_success()/* && hasResponse()*/) {
				addResponse(request_id, responseString);
			} else if (response_type == ton_client.tc_response_error()) {
				addError(request_id, responseString);
			} else if (response_type >= 100) {
				addEvent(request_id, responseString);
			}

			// if "final" flag received, let's remove everything
			if (finished) {
				EverSdk.getContext(this.id).finishRequest(request_id);
			}
		} catch (Exception e) {
			logger.log(System.Logger.Level.ERROR,
			           STR."REQ:\{request_id} TYPE:\{response_type} EVER-SDK Unexpected upcall error! \{e.toString()}");
		}
	}
}
