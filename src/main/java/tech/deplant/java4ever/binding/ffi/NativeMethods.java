package tech.deplant.java4ever.binding.ffi;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public class NativeMethods {

	public static String tcCreateContext(String configJson) {
		Arena offHeapMemory = Arena.ofAuto();
		//SegmentAllocator textAllocator = SegmentAllocator.prefixAllocator(offHeapMemory.allocate(constants$0.const$0));
		MemorySegment handle = ton_client.tc_create_context(NativeStrings.toRust(configJson, offHeapMemory));
		String s = NativeStrings.toJava(
				ton_client.tc_read_string(
						RuntimeHelper.CONSTANT_ALLOCATOR,
						handle
				),
				offHeapMemory
		);
		ton_client.tc_destroy_string(handle);
		return s;
	}

	public static void tcRequest(int contextId, String functionName, String params, Arena arena, int requestId, NativeUpcallHandler handler) {
		ton_client.tc_request(contextId,
		                      NativeStrings.toRust(functionName, arena),
		                      NativeStrings.toRust(params, arena),
		                      requestId,
		                      tc_response_handler_t.allocate(handler, arena));
	}

}
