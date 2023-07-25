package tech.deplant.java4ever.binding.ffi;

import tech.deplant.java4ever.binding.CallbackHandler;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;

public class SdkResponseHandler implements tc_response_handler_t {

	private final static System.Logger logger = System.getLogger(SdkResponseHandler.class.getName());
	private final CompletableFuture<String> result = new CompletableFuture<>();

	private final Consumer<CallbackHandler> handlerConsumer;

	public SdkResponseHandler(Consumer<CallbackHandler> consumer) {
		this.handlerConsumer = consumer;
	}

	/**
	 * @param x0 uint32_t request_id
	 * @param x1 tc_string_data_t params_json
	 * @param x2 uint32_t response_type
	 * @param x3 bool finished
	 */
	@Override
	public void apply(int x0, MemorySegment x1, int x2, boolean x3) {
		try (Arena offHeapMemory = Arena.openShared()) {
			if (x2 == ton_client.tc_response_success()) {
				this.result.complete(SdkBridge.toString(x1, offHeapMemory.scope()));
				logger.log(System.Logger.Level.TRACE, () -> "REQID: %d, RESULT".formatted(x0));
			} else if (x2 == ton_client.tc_response_error()) {
				//throw new RuntimeException(EverSdkBridge.toString(x1, scope));
				this.result.completeExceptionally(new CompletionException(SdkBridge.toString(x1,offHeapMemory.scope()),
				                                                          null));
				//this.result.complete(tc_string_data_t.toString(x1, scope));
				logger.log(System.Logger.Level.WARNING, () -> "REQID: %d, ERROR".formatted(x0));
			} else if (x2 == ton_client.tc_response_nop()) {
				// NOP = 2, no operation. In combination with finished = true signals that the request handling was finished.
				logger.log(System.Logger.Level.TRACE, () -> "REQID: %d, NOP".formatted(x0));
				if (x3) {
					this.result.complete(null);
				}
			} else if (x2 == ton_client.tc_response_app_request()) {
				// APP_REQUEST = 3, request some data from application. See Application objects
				logger.log(System.Logger.Level.TRACE, () -> "REQID: %d, APP_REQUEST".formatted(x0));
			} else if (x2 == ton_client.tc_response_app_notify()) {
				// APP_NOTIFY = 4, notify application with some data. See Application objects
				logger.log(System.Logger.Level.TRACE, () -> "REQID: %d, APP_NOTIFY".formatted(x0));
			} else if (x2 > ton_client.tc_response_app_notify() && x2 < ton_client.tc_response_custom()) {
				// RESERVED = 5..99 – reserved for protocol internal purposes. Application (or binding) must ignore this response.
				// Nevertheless the binding must check the finished flag to release data, associated with request.
				logger.log(System.Logger.Level.TRACE, () -> "REQID: %d, RESERVED".formatted(x0));
				if (x3) {
					this.result.complete(SdkBridge.toString(x1,offHeapMemory.scope()));
				}
			} else {
				// CUSTOM >= 100 - additional function data related to request handling. Depends on the function.
				logger.log(System.Logger.Level.TRACE,
				           () -> "REQID: %d, CUSTOM #%d, %s".formatted(x0, x2, SdkBridge.toString(x1,offHeapMemory.scope())));
				if (this.handlerConsumer != null) {
					this.handlerConsumer.accept(new CallbackHandler(SdkBridge.toString(x1,offHeapMemory.scope()), x2));
				}
			}
		}
	}

	public CompletableFuture<String> result() {
		return this.result;
	}
}
