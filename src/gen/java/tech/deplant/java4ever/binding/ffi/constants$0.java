// Generated by jextract

package tech.deplant.java4ever.binding.ffi;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
final class constants$0 {

    // Suppresses default constructor, ensuring non-instantiability.
    private constants$0() {}
    static final StructLayout const$0 = MemoryLayout.structLayout(
        RuntimeHelper.POINTER.withName("content"),
        JAVA_INT.withName("len"),
        MemoryLayout.paddingLayout(4)
    ).withName("");
    static final VarHandle const$1 = constants$0.const$0.varHandle(MemoryLayout.PathElement.groupElement("content"));
    static final VarHandle const$2 = constants$0.const$0.varHandle(MemoryLayout.PathElement.groupElement("len"));
    static final FunctionDescriptor const$3 = FunctionDescriptor.ofVoid(
        JAVA_INT,
        MemoryLayout.structLayout(
            RuntimeHelper.POINTER.withName("content"),
            JAVA_INT.withName("len"),
            MemoryLayout.paddingLayout(4)
        ).withName(""),
        JAVA_INT,
        JAVA_BOOLEAN
    );
    static final MethodHandle const$4 = RuntimeHelper.upcallHandle(tc_response_handler_t.class, "apply", constants$0.const$3);
    static final MethodHandle const$5 = RuntimeHelper.downcallHandle(
        constants$0.const$3
    );
}


