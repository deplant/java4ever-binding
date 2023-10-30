package tech.deplant.java4ever.binding.ffi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.deplant.commons.Strings;
import tech.deplant.java4ever.binding.EverSdkContext;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Net;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SdkResponseHandler implements tc_response_handler_t {

	private final static System.Logger logger = System.getLogger(SdkResponseHandler.class.getName());

	private final static String LOG_FORMAT = "FUNC:%s CTX:%d REQ:%d %s:%s";

	private final int requestId;

	private final EverSdkContext ctx;

	private final String functionName;

	private final String params;
	private final CompletableFuture<String> result = new CompletableFuture<>();
	private final List<String> eventList = new ArrayList<>();
	private final Consumer<String> handlerConsumer;
	private final Predicate<String> eventFilter;
	private long handle = 0L;

	public SdkResponseHandler(EverSdkContext ctx,
	                          int requestId,
	                          String functionName,
	                          String params,
	                          Consumer<String> consumer,
	                          Predicate<String> filter) {
		this.ctx = ctx;
		this.requestId = requestId;
		this.functionName = Strings.notEmpty(functionName);
		this.params = params;
		this.handlerConsumer = consumer;
		this.eventFilter = filter;
	}

	public int requestId() {
		return this.requestId;
	}

	public void saveHandle(long handle) {
		this.handle = handle;
	}

	public void request(ExecutorService executor, SegmentScope scope) {
		logger.log(System.Logger.Level.TRACE,
		           () -> LOG_FORMAT.formatted(functionName,ctx.id(),requestId,"SEND",params));
		ton_client.tc_request(ctx.id(),
		                      SdkBridge.toRustString(functionName, scope),
		                      SdkBridge.toRustString(params, scope),
		                      requestId,
		                      tc_response_handler_t.allocate(this, SegmentScope.auto()));
	}

	/**
	 * @param x0 uint32_t request_id
	 * @param x1 tc_string_data_t params_json
	 * @param x2 uint32_t response_type
	 * @param x3 bool finished
	 */
	@Override
	public void apply(int x0, MemorySegment x1, int x2, boolean x3) {
		final String responseString = Strings.notEmptyElse(SdkBridge.toString(x1, SegmentScope.auto()), "{}");
		if (x2 == ton_client.tc_response_success()) {
			this.result.complete(responseString);
			logger.log(System.Logger.Level.TRACE, () -> "REQ:%d RESULT".formatted(x0));
		} else if (x2 == ton_client.tc_response_error()) {
			//throw new RuntimeException(EverSdkBridge.toString(x1, scope));
			this.result.completeExceptionally(new CompletionException(responseString, null));
			//this.result.complete(tc_string_data_t.toString(x1, scope));
			logger.log(System.Logger.Level.WARNING, () -> "REQ:%d ERROR".formatted(x0));
		} else if (x2 == ton_client.tc_response_nop()) {
			// NOP = 2, no operation. In combination with finished = true signals that the request handling was finished.
			logger.log(System.Logger.Level.TRACE, () -> "REQ:%d NOP".formatted(x0));
			if (x3) {
				this.result.complete(responseString);
			}
		} else if (x2 == ton_client.tc_response_app_request()) {
			// APP_REQUEST = 3, request some data from application. See Application objects
			logger.log(System.Logger.Level.TRACE, () -> "REQ:%d APP_REQUEST".formatted(x0));
		} else if (x2 == ton_client.tc_response_app_notify()) {
			// APP_NOTIFY = 4, notify application with some data. See Application objects
			logger.log(System.Logger.Level.TRACE, () -> "REQ:%d APP_NOTIFY".formatted(x0));
		} else if (x2 > ton_client.tc_response_app_notify() && x2 < ton_client.tc_response_custom()) {
			// RESERVED = 5..99 – reserved for protocol internal purposes. Application (or binding) must ignore this response.
			// Nevertheless the binding must check the finished flag to release data, associated with request.
			logger.log(System.Logger.Level.TRACE, () -> "REQ:%d RESERVED".formatted(x0));
			if (x3) {
				this.result.complete(responseString);
			}
		} else {
			// CUSTOM >= 100 - additional function data related to request handling. Depends on the function.
			logger.log(System.Logger.Level.TRACE, () -> "REQ:%d CUSTOM #%d: %s".formatted(x0, x2, responseString));
			if (this.handlerConsumer != null) {
				//TODO Possible shit
				this.eventList.add(responseString);
				if (this.eventFilter == null || this.eventFilter.test(responseString)) {
					// I think there's no reason to fail everything if unsubscribe failed...
					var consumer = this.handlerConsumer;
					if (this.handle > 0) {
						consumer = this.handlerConsumer.andThen(event -> {
							try {
								Net.unsubscribe(ctx, new Net.ResultOfSubscribeCollection(this.handle));
							} catch (EverSdkException e) {
								logger.log(System.Logger.Level.ERROR,
								           () -> "Unsubscribe failed! REQ:%d HANDLE:%d".formatted(requestId,
								                                                                       this.handle));
							}
						});
						logger.log(System.Logger.Level.TRACE, () -> "Unsubscribing. HANDLE:%d".formatted(this.handle));
					}
					ctx.responses().remove(requestId);
					logger.log(System.Logger.Level.TRACE, () -> "Removing request by event acception: " + requestId);
					consumer.accept(responseString);
				}
			}
		}
	}

	public String result(ObjectMapper mapper, long timeout, TimeUnit unit) throws EverSdkException {
		final String result;
		try {
			result = this.result.get(timeout, unit);
			logger.log(System.Logger.Level.TRACE,
			           () -> LOG_FORMAT.formatted(functionName,ctx.id(),requestId,"RESP",result));
			return result;
		} catch (CompletionException | ExecutionException e) {
			// These errors are sent by SDK, response_type=1
			EverSdkException.ErrorResult sdkResponse = null;
			// let's try to parse error response
			try {
				sdkResponse = mapper.readValue(e.getCause().getMessage(), EverSdkException.ErrorResult.class);
				// in case of contract custom exit code ("require" error)
				final EverSdkException.ErrorResult responseCopy = sdkResponse;
				logger.log(System.Logger.Level.WARNING,
				           () -> LOG_FORMAT.formatted(functionName,ctx.id(),requestId,"ERR",responseCopy));
					throw new EverSdkException(responseCopy, e);
			} catch (JsonProcessingException ex) {
				// if error response parsing failed
				logger.log(System.Logger.Level.ERROR,
				           () -> LOG_FORMAT.formatted(functionName,ctx.id(),requestId,"ERR",new EverSdkException.ErrorResult(-500,
				                                                                                                            "SDK Error Response deserialization failed! Check getCause() for actual response.")));
				throw new EverSdkException(new EverSdkException.ErrorResult(-500,
				                                                            "SDK Error Response deserialization failed! Check getCause() for actual response."),
				                           ex);
			}
		} catch (InterruptedException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> LOG_FORMAT.formatted(functionName,ctx.id(),requestId,"ERR",new EverSdkException.ErrorResult(-400,
			                                                                                                             "EVER-SDK call interrupted!")));
			throw new EverSdkException(new EverSdkException.ErrorResult(-400, "EVER-SDK call interrupted!"), e);
		} catch (TimeoutException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> LOG_FORMAT.formatted(functionName,ctx.id(),requestId,"ERR",new EverSdkException.ErrorResult(-408,
			                                                                                                             "EVER-SDK Execution expired on Timeout! Current timeout: " +
			                                                                                                             timeout)));
			throw new EverSdkException(new EverSdkException.ErrorResult(-408,
			                                                            "EVER-SDK Execution expired on Timeout! Current timeout: " +
			                                                            timeout), e);

		}

	}
}
