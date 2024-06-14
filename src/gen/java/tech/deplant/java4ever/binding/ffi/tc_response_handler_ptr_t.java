// Generated by jextract

package tech.deplant.java4ever.binding.ffi;

import java.lang.invoke.*;
import java.lang.foreign.*;
import java.nio.ByteOrder;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.lang.foreign.ValueLayout.*;
import static java.lang.foreign.MemoryLayout.PathElement.*;

/**
 * {@snippet lang=c :
 * typedef void (*tc_response_handler_ptr_t)(void *, tc_string_data_t, uint32_t, _Bool)
 * }
 */
public class tc_response_handler_ptr_t {

    tc_response_handler_ptr_t() {
        // Should not be called directly
    }

    /**
     * The function pointer signature, expressed as a functional interface
     */
    public interface Function {
        void apply(MemorySegment request_ptr, MemorySegment params_json, int response_type, boolean finished);
    }

    private static final FunctionDescriptor $DESC = FunctionDescriptor.ofVoid(
        ton_client.C_POINTER,
        tc_string_data_t.layout(),
        ton_client.C_INT,
        ton_client.C_BOOL
    );

    /**
     * The descriptor of this function pointer
     */
    public static FunctionDescriptor descriptor() {
        return $DESC;
    }

    private static final MethodHandle UP$MH = ton_client.upcallHandle(tc_response_handler_ptr_t.Function.class, "apply", $DESC);

    /**
     * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
     * The lifetime of the returned segment is managed by {@code arena}
     */
    public static MemorySegment allocate(tc_response_handler_ptr_t.Function fi, Arena arena) {
        return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
    }

    private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

    /**
     * Invoke the upcall stub {@code funcPtr}, with given parameters
     */
    public static void invoke(MemorySegment funcPtr,MemorySegment request_ptr, MemorySegment params_json, int response_type, boolean finished) {
        try {
             DOWN$MH.invokeExact(funcPtr, request_ptr, params_json, response_type, finished);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
}

