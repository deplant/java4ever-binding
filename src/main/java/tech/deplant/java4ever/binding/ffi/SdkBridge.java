package tech.deplant.java4ever.binding.ffi;

import tech.deplant.java4ever.binding.loader.LibraryLoader;

import java.lang.foreign.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static java.lang.foreign.ValueLayout.JAVA_BYTE;
import static tech.deplant.java4ever.binding.ffi.tc_string_data_t.*;

public class SdkBridge {

	public static String tcCreateContext(LibraryLoader loader,
	                                     String configJson) {
		loader.load();
		//try (Arena scope = SegmentScope.openShared()) {
			return toString(
					ton_client.tc_read_string(
							SegmentAllocator.nativeAllocator(SegmentScope.auto()),
							ton_client.tc_create_context(ofString(configJson))
					)/*,
					scope*/
			);
		//}
	}

	public static SdkResponseHandler tcRequest(int contextId, int requestId, String functionName, String params) {
		final var response = new SdkResponseHandler();
		ton_client.tc_request(contextId,
		                      ofString(functionName),
		                      ofString(params),
		                      requestId,
		                      tc_response_handler_t.allocate(response, Arena.openShared().scope()));
		return response;
	}

	public static MemorySegment ofString(String text/*, Arena scope*/) {
		MemorySegment stringData = allocate(SegmentAllocator.nativeAllocator(SegmentScope.auto()));
		MemorySegment nativeString = fromJavaString(SegmentAllocator.nativeAllocator(SegmentScope.auto()),
		                                            text,
		                                            StandardCharsets.UTF_8);
		content$set(stringData, 0, nativeString); //TODO dubious, recheck
		len$set(stringData, 0, ((int) nativeString.byteSize()) - 1 // minus 1 because last symbol is u0000 in UTF-8
		);
		return stringData;
	}

	public static String toString(MemorySegment seg/*, Arena scope*/) {
		return toJavaString(seg/*, scope*/);
	}

	private static String toJavaString(MemorySegment segment, int len) {
		byte[] bytes = new byte[len];
		MemorySegment.copy(segment, JAVA_BYTE, 0L, bytes, 0, len);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	private static String toJavaString(MemorySegment seg/*, Arena scope*/) {
		if (tc_string_data_t.len$get(seg) > 0) {
			MemorySegment addressSegment = tc_string_data_t.content$get(seg);
			byte[] str = MemorySegment.ofAddress(addressSegment.address(), len$get(seg), SegmentScope.auto()).toArray(JAVA_BYTE);
			return new String(str, StandardCharsets.UTF_8);
		} else {
			return "";
		}
	}

	private static int strLength(MemorySegment seg) {
		return len$get(seg);
	}

	private static MemorySegment fromJavaString(SegmentAllocator allocator, String s, Charset charset) {
		if (StandardCharsets.UTF_8 == charset) { // "==" is OK here as StandardCharsets.UTF_8 == Charset.forName("UTF8")
			return allocator.allocateUtf8String(s);
		} else {
			return allocator.allocateArray(ValueLayout.JAVA_BYTE, (s + "\0").getBytes(charset));
		}
	}

}
