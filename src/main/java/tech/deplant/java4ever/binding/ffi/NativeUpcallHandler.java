package tech.deplant.java4ever.binding.ffi;

import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.commons.Strings;
import tech.deplant.java4ever.binding.EverSdkContext;
import tech.deplant.java4ever.binding.SdkContext;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;

public record NativeUpcallHandler(SdkContext context, CompletableFuture<String> result, Consumer<JsonNode> handlerConsumer) implements tc_response_handler_t {

	private final static System.Logger logger = System.getLogger(NativeUpcallHandler.class.getName());

	public NativeUpcallHandler(SdkContext context, Consumer<JsonNode> handlerConsumer) {
		this(context,new CompletableFuture<>(), handlerConsumer);
	}

	public NativeUpcallHandler(SdkContext context) {
		this(context,new CompletableFuture<>(), null);
	}

	/**
	 *
	 * @param request_id
	 * @param params_json
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
		final String responseString = Strings.notEmptyElse(SdkBridge.toJavaString(params_json, Arena.ofAuto()), "{}");
		logger.log(System.Logger.Level.TRACE, () -> "REQ:%d TYPE:%d FINISHED:%s".formatted(request_id, response_type,finished));
		logger.log(System.Logger.Level.ALL, () -> "JSON: %s".formatted(params_json));
		if (response_type == ton_client.tc_response_success()) {
			context().addResponse(request_id,responseString);
		} else if (response_type == ton_client.tc_response_error()) {
			context().addError(request_id,responseString);
		} else if (response_type >= 100) {
			if (handlerConsumer() != null) {
				context().addEvent(request_id,responseString,handlerConsumer());
			}
		}

		// if we received "final" flag, let's remove ourselves from saved responses
		if (finished) {
			context().finishRequest(request_id);
		}

	}
}
