package tech.deplant.java4ever.binding.ffi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.*;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class EverSdkContext implements tc_response_handler_t.Function {

	private final static System.Logger logger = System.getLogger(EverSdkContext.class.getName());

	private final int id;
	private final AtomicInteger requestCount = new AtomicInteger();
	private final Client.ClientConfig clientConfig;
	private final long timeout;
	@JsonIgnore private final Map<Integer, RequestData> requests = new ConcurrentHashMap<>();
	@JsonIgnore private final Map<Integer, Consumer<JsonNode>> subscriptions = new ConcurrentHashMap<>();
	@JsonIgnore private final Queue<RequestData> cleanupQueue = new ConcurrentLinkedDeque<>();

	public EverSdkContext(int id, Client.ClientConfig clientConfig) {
		this.id = id;
		this.clientConfig = clientConfig;
		this.timeout = extractTimeout(clientConfig);
	}

	/**
	 * Most used call to EVER-SDK with some output object
	 *
	 * @param functionName
	 * @param functionInputs record of input type, usually ParamsOf...
	 * @param resultClass    class of output type record, usually ResultOf...class
	 * @param <R> Class of the result object
	 * @param <P> Class of the function params object
	 * @param <AR> Class of the AppObject result callbacks
	 * @param <AP> Class of the AppObject param calls
	 * @return output type record, usually ResultOf...
	 * @throws EverSdkException
	 */
	public <R,P,AP,AR> CompletableFuture<R> callAsync(final String functionName,
	                                             final P functionInputs,
	                                             final Class<R> resultClass,
	                                             final Consumer<JsonNode> eventConsumer,
	                                             final AppObject<AP,AR> appObject) throws EverSdkException {
		final int requestId = requestCountNextVal();
		// let's clean previous requests and their native memory sessions
		cleanup();

		// it's better to explicitly mark requests as void to not recheck this every time in response handler
		boolean hasResponse = !resultClass.equals(Void.class);

		var request = new RequestData<R>(hasResponse,
		                                 Arena.ofShared(),
		                                 new ReentrantLock(),
		                                 resultClass,
		                                 new CompletableFuture<>(),
		                                 eventConsumer,
		                                 appObject);
		this.requests.put(requestId, request);

		// with reentrant lock, multiple results on any given single request will be processed one by one
		request.queueLock().lock();
		try {
			var paramsJson = processParams(functionInputs);
			NativeMethods.tcRequest(this.id, functionName, paramsJson, request.nativeArena(), requestId, this);
			logger.log(System.Logger.Level.TRACE,
			           () -> EverSdk.LOG_FORMAT.formatted(this.id, requestId, functionName, "SEND", paramsJson));
		} finally {
			request.queueLock().unlock(); // снимаем блокировку
		}
		if (!hasResponse) {
			request.responseFuture().complete(null);
		}
		return request.responseFuture();
	}

	public int requestCountNextVal() {
		return this.requestCount.incrementAndGet();
	}

	public void addResponse(int requestId, String responseString) {
		var request = this.requests.get(requestId);
		if (request.hasResponse()) {
			request.queueLock().lock();
			try {
				if (!request.responseFuture().isDone()) {
					logger.log(System.Logger.Level.TRACE,
					           () -> "CTX:%d REQ:%d RESP:%s".formatted(this.id,requestId, responseString));
					request.responseFuture()
					       .complete(JsonContext.SDK_JSON_MAPPER().readValue(responseString, request.responseClass()));
				} else {
					logger.log(System.Logger.Level.ERROR,
					           "Slot for this request not found on processing response! CTX:%d REQ:%d RESP:%s".formatted(
							           this.id,
							           requestId,
							           responseString));
				}
			} catch (JsonProcessingException ex2) {
				// successful response but parsing failed
				logger.log(System.Logger.Level.ERROR,
				           () -> "CTX:%d REQ:%d EVER-SDK Response deserialization failed! %s".formatted(
						           this.id,
						           requestId,
						           ex2.toString()));
				request.responseFuture()
				       .completeExceptionally(new EverSdkException(new EverSdkException.ErrorResult(-500,
				                                                                                    "EVER-SDK response deserialization failed!"),
				                                                   ex2.getCause()));
			} finally {
				request.queueLock().unlock(); // снимаем блокировку
			}
		}
	}

	private void addError(int requestId, String responseString) {
		var request = requests.get(requestId);
		if (request.responseFuture() instanceof CompletableFuture<?> future) {
			request.queueLock().lock();
			try {
				// These errors are sent by SDK, response_type=1
				//String everSdkError = ex.getCause().getMessage();
				logger.log(System.Logger.Level.WARNING,
				           () -> "CTX:%d REQ:%d ERR:%s".formatted(this.id, requestId, responseString));
				// let's try to parse error response
				EverSdkException.ErrorResult sdkResponse = JsonContext.SDK_JSON_MAPPER()
				                                                      .readValue(responseString,
				                                                                 EverSdkException.ErrorResult.class);
				future.completeExceptionally(new EverSdkException(sdkResponse));
			} catch (JsonProcessingException ex1) {
				// if error response parsing failed
				logger.log(System.Logger.Level.ERROR,
				           () -> "CTX:%d REQ:%d EVER-SDK Error deserialization failed! %s".formatted(
						           this.id,
						           requestId,
						           ex1.toString()));
				future.completeExceptionally(new EverSdkException(new EverSdkException.ErrorResult(-500,
				                                                                                   "EVER-SDK Error deserialization failed!"),
				                                                  ex1.getCause()));
			} finally {
				request.queueLock().unlock(); // снимаем блокировку
			}
		} else {
			logger.log(System.Logger.Level.ERROR,
			           "Slot for this request not found on processing error response! CTX:%d REQ:%d ERR:%s".formatted(
					           this.id,
					           requestId,
					           responseString));
		}

	}

	private void addEvent(int requestId, String responseString) {
		var request = this.requests.get(requestId);
		if (request.subscriptionHandler() instanceof Consumer<?> handler) {
			try {
				JsonNode node = JsonContext.ABI_JSON_MAPPER().readTree(responseString);
				try {
					((Consumer<JsonNode>) handler).accept(node);
				} catch (Exception ex1) {
					logger.log(System.Logger.Level.ERROR,
					           () -> "REQ:%d EVENT:%s Subscribe Event Action processing failed! %s".formatted(requestId, responseString, ex1.toString()));
				}
			} catch (JsonProcessingException ex2) {
				logger.log(System.Logger.Level.ERROR,
				           () -> "REQ:%d EVENT:%s Subscribe Event JSON deserialization failed! %s".formatted(requestId, responseString, ex2.toString()));
			}
		} else {
			logger.log(System.Logger.Level.ERROR,
			           "Slot for this request not found on processing subscription event! CTX:%d REQ:%d EVENT:%s".formatted(
					           this.id,
					           requestId,
					           responseString));
		}
	}

	private void finishRequest(int requestId) {
		if (this.requests.get(requestId) instanceof RequestData request) {
			this.requests.remove(requestId);
			this.cleanupQueue.add(request);
		}
		this.subscriptions.remove(requestId);
	}

	private void cleanup() {
		while (this.cleanupQueue.poll() instanceof RequestData request) {
			if (request.nativeArena() instanceof Arena a) {
				request.queueLock().lock();
				try {
					a.close();
				} finally {
					request.queueLock().unlock(); // снимаем блокировку
				}
			}
		}
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

	private long extractTimeout(Client.ClientConfig cfg) {
		return switch (cfg) {
			case null -> 60000L;
			case Client.ClientConfig cl -> switch (cl.network()) {
				case null -> 60000L;
				case Client.NetworkConfig ntwrk -> Objects.requireNonNullElse(ntwrk.queryTimeout(),60000L);
			};
		};
	}

	private <R> R awaitSyncResponse(CompletableFuture<R> requestId, Class<R> resultClass) throws EverSdkException {
		try {
			// waiting for response synchronously
			String responseString = (String) this.requests.get(requestId).responseFuture().get(this.timeout, TimeUnit.MILLISECONDS);
			logger.log(System.Logger.Level.TRACE, () -> "CTX:%d REQ:%d RESP:%s".formatted(
					this.id,
					requestId,
					responseString));
			// let's try to parse response
			return JsonContext.SDK_JSON_MAPPER().readValue(responseString, resultClass);
		} catch (InterruptedException ex3) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "CTX:%d REQ:%d EVER-SDK Call interrupted! %s}".formatted(
					           this.id,
					           requestId,
					           ex3.toString()));
			throw new EverSdkException(new EverSdkException.ErrorResult(-400, "EVER-SDK call interrupted!"),
			                           ex3.getCause());
		} catch (TimeoutException ex4) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "CTX:%d REQ:%d EVER-SDK Call expired on Timeout! %s".formatted(
					           this.id,
					           requestId,
					           ex4.toString()));
			throw new EverSdkException(new EverSdkException.ErrorResult(-408, "EVER-SDK call expired on Timeout!"),
			                           ex4.getCause());

		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

//	/**
//	 * Most used call to EVER-SDK with some output object
//	 *
//	 * @param functionName
//	 * @param params       record of input type, usually ParamsOf...
//	 * @param <P>
//	 * @throws EverSdkException
//	 */
//	private <P> void addRequest(int requestId, String functionName, P params, boolean hasResponse) {
//		//final NativeUpcallHandler handler = new NativeUpcallHandler(this.id, hasResponse);
//		var arena = Arena.ofShared();
//		var request = new RequestData(hasResponse, arena, new ReentrantLock());
//		cleanup();
//		this.requests.put(requestId, request);
//		request.queueLock().lock();
//		try {
//			if (hasResponse) {
//				this.responses.put(requestId, new CompletableFuture<>());
//			}
//			var paramsJson = processParams(params);
//			NativeMethods.tcRequest(this.id, functionName, paramsJson, arena, requestId, this);
//			logger.log(System.Logger.Level.TRACE,
//			           () -> EverSdk.LOG_FORMAT.formatted(this.id, requestId, functionName, "SEND", paramsJson));
//		} finally {
//			request.queueLock().unlock(); // снимаем блокировку
//		}
//	}

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
		try {
			final String responseString = NativeStrings.toJava(params_json);
			if (logger.isLoggable(System.Logger.Level.TRACE)) {
				logger.log(System.Logger.Level.TRACE,
				           "REQ:%d TYPE:%d FINISHED:%s JSON:%s".formatted(
						           this.id,
						           response_type,
						           String.valueOf(finished),
						           responseString));
			}
			switch (tc_response_types.of(response_type)) {
				case tc_response_types.TC_RESPONSE_SUCCESS -> addResponse(request_id, responseString);
				case tc_response_types.TC_RESPONSE_ERROR -> addError(request_id, responseString);
				case tc_response_types.TC_RESPONSE_CUSTOM -> addEvent(request_id, responseString);
			}

			// if "final" flag received, let's remove everything
			if (finished) {
				finishRequest(request_id);
			}
		} catch (Exception e) {
			logger.log(System.Logger.Level.ERROR,
			           "REQ:%d TYPE:%d EVER-SDK Unexpected upcall error! %s".formatted(request_id, response_type, e.toString()));
		}
	}

	record RequestData<R>(boolean hasResponse,
	                      Arena nativeArena,
	                      ReentrantLock queueLock,
	                      Class<R> responseClass,
	                      CompletableFuture<R> responseFuture,
	                      Consumer<JsonNode> subscriptionHandler,
	                      AppObject appObject) {
	}

	record SubscriptionHandler(Consumer<JsonNode> eventAction) {
	}
}
