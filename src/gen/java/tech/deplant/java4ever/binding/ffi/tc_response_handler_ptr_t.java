// Generated by jextract

package tech.deplant.java4ever.binding.ffi;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
/**
 * {@snippet :
 * void (*tc_response_handler_ptr_t)(void* request_ptr,struct  params_json,unsigned int response_type,_Bool finished);
 * }
 */
public interface tc_response_handler_ptr_t {

    void apply(java.lang.foreign.MemorySegment request_ptr, java.lang.foreign.MemorySegment params_json, int response_type, boolean finished);
    static MemorySegment allocate(tc_response_handler_ptr_t fi, SegmentScope scope) {
        return RuntimeHelper.upcallStub(constants$0.tc_response_handler_ptr_t_UP$MH, fi, constants$0.tc_response_handler_ptr_t$FUNC, scope);
    }
    static tc_response_handler_ptr_t ofAddress(MemorySegment addr, SegmentScope scope) {
        MemorySegment symbol = MemorySegment.ofAddress(addr.address(), 0, scope);
        return (java.lang.foreign.MemorySegment _request_ptr, java.lang.foreign.MemorySegment _params_json, int _response_type, boolean _finished) -> {
            try {
                constants$0.tc_response_handler_ptr_t_DOWN$MH.invokeExact(symbol, _request_ptr, _params_json, _response_type, _finished);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


