package tech.deplant.java4ever.binding.ffi;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.SegmentAllocator;
import java.nio.charset.StandardCharsets;

import static tech.deplant.java4ever.binding.ffi.tc_string_data_t.*;

public class EverSdkBridge {

	public static EverSdkResponse tcRequest(int contextId, int requestId, String functionName, String params) {
		final var response = new EverSdkResponse();
		ton_client.tc_request(contextId,
		                      ofString(functionName, MemorySession.openShared()),
		                      ofString(params, MemorySession.openShared()),
		                      requestId,
		                      tc_response_handler_t.allocate(response, MemorySession.openShared()));
		return response;
	}

	public static String tcCreateContext(MemorySession scope, String configJson) {
		return toString(
				ton_client.tc_read_string(
						SegmentAllocator.newNativeArena(scope),
						ton_client.tc_create_context(ofString(configJson, scope))
				),
				scope
		);
	}

	public static MemorySegment ofString(String text, MemorySession scope) {
		MemorySegment stringData = allocate(scope);
		MemorySegment nativeString = EverSdkUtils.fromJavaString(SegmentAllocator.newNativeArena(scope),
		                                                         text,
		                                                         StandardCharsets.UTF_8);
		content$set(stringData, 0, nativeString.address());
		len$set(stringData, 0, ((int) nativeString.byteSize()) - 1 // minus 1 because last symbol is u0000 in UTF-8
		);
		return stringData;
	}

	public static String toString(MemorySegment seg, MemorySession scope) {
		return EverSdkUtils.toJavaString(seg, scope);
	}
}
