package tech.deplant.java4ever.binding.response;

import jdk.incubator.foreign.MemorySegment;
import jdk.incubator.foreign.ResourceScope;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.ffi.tc_response_handler_t;
import tech.deplant.java4ever.ffi.tc_string_data_t;

import java.util.concurrent.CompletableFuture;

@Log4j2
public class EverResponse implements tc_response_handler_t {

    private CompletableFuture<String> result = new CompletableFuture<>();

    /**
     * @param x0 uint32_t request_id
     * @param x1 tc_string_data_t params_json
     * @param x2 uint32_t response_type
     * @param x3 bool finished
     */
    @Override
    public void apply(int x0, MemorySegment x1, int x2, byte x3) {
        try (ResourceScope scope = ResourceScope.newSharedScope()) {
            if (x2 == 0) {
                this.result.complete(tc_string_data_t.toString(x1, scope));
                //log.trace("REQID:" + x0 + " returned RESULT");
            } else if (x2 == 1) {
                this.result.completeExceptionally(new RuntimeException(tc_string_data_t.toString(x1, scope)));
                //this.result.complete(tc_string_data_t.toString(x1, scope));
                //log.warn("REQID:" + x0 + " returned ERROR");
            } else if (x2 == 2) {
                // NOP = 2, no operation. In combination with finished = true signals that the request handling was finished.
                //log.trace("REQID:" + x0 + " returned NOP");
            } else if (x2 == 3) {
                // APP_REQUEST = 3, request some data from application. See Application objects
                //log.trace("REQID:" + x0 + " returned APP_REQUEST");
            } else if (x2 == 4) {
                // APP_NOTIFY = 4, notify application with some data. See Application objects
                //log.trace("REQID:" + x0 + " returned APP_NOTIFY");
            } else if (x2 >= 5 && x2 <= 99) {
                // RESERVED = 5..99 – reserved for protocol internal purposes. Application (or binding) must ignore this response.
                // Nevertheless the binding must check the finished flag to release data, associated with request.
                log.trace("REQID:" + x0 + " returned RESERVED");
            } else {
                // CUSTOM >= 100 - additional function data related to request handling. Depends on the function.
                log.trace("REQID:" + x0 + " returned CUSTOM");
            }
        }
    }

    public CompletableFuture<String> result() {
        return this.result;
    }
}
