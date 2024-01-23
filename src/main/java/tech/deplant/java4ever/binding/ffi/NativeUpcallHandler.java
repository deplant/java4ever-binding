package tech.deplant.java4ever.binding.ffi;

import tech.deplant.commons.Strings;
import tech.deplant.java4ever.binding.EverSdk;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

record NativeUpcallHandler(int contextId) implements tc_response_handler_t {

	private final static System.Logger logger = System.getLogger(NativeUpcallHandler.class.getName());

	/**
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
		final String responseString = Strings.notEmptyElse(NativeStrings.toJava(params_json, Arena.ofAuto()), "{}");
		logger.log(System.Logger.Level.TRACE,
		           () -> "CTX:%d REQ:%d TYPE:%d FINISHED:%s JSON:%s".formatted(contextId, request_id, response_type, finished, responseString));
		if (response_type == ton_client.tc_response_success()) {
			EverSdk.getContext(contextId()).addResponse(request_id, responseString);
		} else if (response_type == ton_client.tc_response_error()) {
			EverSdk.getContext(contextId()).addError(request_id, responseString);
		} else if (response_type >= 100) {
			//if (handlerConsumer() != null) {
			EverSdk.getContext(contextId()).addEvent(request_id, responseString);
			//}
		}

		// if "final" flag received, let's remove everything
		if (finished) {
			EverSdk.getContext(contextId()).finishRequest(request_id);
		}

	}
}