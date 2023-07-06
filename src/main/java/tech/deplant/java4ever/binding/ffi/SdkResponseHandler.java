package tech.deplant.java4ever.binding.ffi;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class SdkResponseHandler implements tc_response_handler_t {

	private final static System.Logger logger = System.getLogger(SdkResponseHandler.class.getName());
	private final CompletableFuture<String> result = new CompletableFuture<>();

	/**
	 * @param x0 uint32_t request_id
	 * @param x1 tc_string_data_t params_json
	 * @param x2 uint32_t response_type
	 * @param x3 bool finished
	 */
	@Override
	public void apply(int x0, MemorySegment x1, int x2, boolean x3) {
		try (Arena arenaScope = Arena.openShared()) {
			if (x2 == 0) {
				this.result.complete(SdkBridge.toString(x1/*, arenaScope*/));
				logger.log(System.Logger.Level.TRACE, () -> "REQID: %d, RESULT".formatted(x0));
			} else if (x2 == 1) {
				//throw new RuntimeException(EverSdkBridge.toString(x1, scope));
				this.result.completeExceptionally(new CompletionException(SdkBridge.toString(x1/*, arenaScope*/), null));
				//this.result.complete(tc_string_data_t.toString(x1, scope));
				logger.log(System.Logger.Level.WARNING, () -> "REQID: %d, ERROR".formatted(x0));
			} else if (x2 == 2) {
				// NOP = 2, no operation. In combination with finished = true signals that the request handling was finished.
				logger.log(System.Logger.Level.TRACE, () -> "REQID: %d, NOP".formatted(x0));
				if (x3) {
					this.result.complete(SdkBridge.toString(x1/*, arenaScope*/));
				}
			} else if (x2 == 3) {
				// APP_REQUEST = 3, request some data from application. See Application objects
				logger.log(System.Logger.Level.TRACE, () -> "REQID: %d, APP_REQUEST".formatted(x0));
			} else if (x2 == 4) {
				// APP_NOTIFY = 4, notify application with some data. See Application objects
				logger.log(System.Logger.Level.TRACE, () -> "REQID: %d, APP_NOTIFY".formatted(x0));
			} else if (x2 >= 5 && x2 <= 99) {
				// RESERVED = 5..99 – reserved for protocol internal purposes. Application (or binding) must ignore this response.
				// Nevertheless the binding must check the finished flag to release data, associated with request.
				logger.log(System.Logger.Level.TRACE, () -> "REQID: %d, RESERVED".formatted(x0));
				if (x3) {
					this.result.complete(SdkBridge.toString(x1/*, arenaScope*/));
				}
			} else {
				// CUSTOM >= 100 - additional function data related to request handling. Depends on the function.
				logger.log(System.Logger.Level.TRACE, () -> "REQID: %d, CUSTOM".formatted(x0));
			}
		}
	}

	public CompletableFuture<String> result() {
		return this.result;
	}
}
