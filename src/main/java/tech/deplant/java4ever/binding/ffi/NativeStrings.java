package tech.deplant.java4ever.binding.ffi;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.charset.StandardCharsets;

import static java.lang.foreign.ValueLayout.JAVA_BYTE;
import static tech.deplant.java4ever.binding.ffi.tc_string_data_t.*;

class NativeStrings {
	public static MemorySegment toRust(final String text, final Arena nativeSession) {
		MemorySegment nativeString = nativeSession.allocateUtf8String(text);
		MemorySegment stringDataSegment = nativeSession.allocate(constants$0.const$0);
		content$set(stringDataSegment, 0, nativeString);
		len$set(stringDataSegment,
		        0,
		        ((int) nativeString.byteSize()) - 1
		        // minus 1 because last symbol is u0000 in UTF-8
		);
		return stringDataSegment;
	}

	public static String toJava(MemorySegment seg) {
		if (tc_string_data_t.len$get(seg) > 0) {
			final MemorySegment content = tc_string_data_t.content$get(seg).asSlice(0, len$get(seg));
			return new String(content.toArray(JAVA_BYTE), StandardCharsets.UTF_8);
		} else {
			return "";
		}
	}
}
