package tech.deplant.java4ever.binding.ffi;

import tech.deplant.java4ever.binding.loader.LibraryLoader;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.nio.charset.StandardCharsets;

import static java.lang.foreign.ValueLayout.JAVA_BYTE;
import static tech.deplant.java4ever.binding.ffi.tc_string_data_t.*;

public class SdkBridge {

	private final static System.Logger logger = System.getLogger(SdkBridge.class.getName());

	public static String tcCreateContext(LibraryLoader loader,
	                                     String configJson) {
		loader.load();
		Arena offHeapMemory = Arena.ofAuto();
		SegmentAllocator textAllocator = SegmentAllocator.prefixAllocator(offHeapMemory.allocate(constants$0.const$0));
		MemorySegment handle = ton_client.tc_create_context(toRustString(configJson, offHeapMemory));
		String s = toJavaString(
				ton_client.tc_read_string(
						textAllocator,
						handle
				),
				offHeapMemory
		);
		logger.log(System.Logger.Level.TRACE,"Context created!");
		ton_client.tc_destroy_string(handle);
		return s;
	}

	public static void tcRequest(int contextId, String functionName, String params, Arena arena, int requestId, SdkResponseHandler handler) {
		ton_client.tc_request(contextId,
		                      SdkBridge.toRustString(functionName, arena),
		                      SdkBridge.toRustString(params, arena),
		                      requestId,
		                      tc_response_handler_t.allocate(handler, arena));
	}

	public static MemorySegment toRustString(final String text, Arena arena) {
		MemorySegment nativeString = arena.allocateUtf8String(text);
		MemorySegment stringDataSegment = arena.allocate(constants$0.const$0);
		content$set(stringDataSegment, 0, nativeString); //TODO dubious, recheck
		len$set(stringDataSegment,
		        0,
		        ((int) nativeString.byteSize()) - 1
		        // minus 1 because last symbol is u0000 in UTF-8
		);
		return stringDataSegment;
	}

	public static String toJavaString(MemorySegment seg, Arena arena) {
		if (tc_string_data_t.len$get(seg) > 0) {
			MemorySegment contentAddress = tc_string_data_t.content$get(seg);
			MemorySegment content = contentAddress.asSlice(0, len$get(seg));
			byte[] str = content.toArray(JAVA_BYTE);
			return new String(str, StandardCharsets.UTF_8);
		} else {
			return "";
		}
	}

	private static int strLength(MemorySegment seg) {
		return len$get(seg);
	}

/*	private static MemorySegment fromJavaString(SegmentAllocator allocator, String s, Charset charset) {
		if (StandardCharsets.UTF_8 == charset) { // "==" is OK here as StandardCharsets.UTF_8 == Charset.forName("UTF8")
			return allocator.allocateUtf8String(s);
		} else {
			return allocator.allocateArray(ValueLayout.JAVA_BYTE, (s + "\0").getBytes(charset));
		}
	}*/

}
