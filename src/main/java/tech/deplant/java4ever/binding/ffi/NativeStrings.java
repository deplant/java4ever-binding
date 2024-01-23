package tech.deplant.java4ever.binding.ffi;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.charset.StandardCharsets;

import static java.lang.foreign.ValueLayout.JAVA_BYTE;
import static tech.deplant.java4ever.binding.ffi.tc_string_data_t.*;

class NativeStrings {
	public static MemorySegment toRust(final String text, final Arena arena) {
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

	public static String toJava(MemorySegment seg, Arena arena) {
		if (tc_string_data_t.len$get(seg) > 0) {
			MemorySegment contentAddress = tc_string_data_t.content$get(seg);
			MemorySegment content = contentAddress.asSlice(0, len$get(seg));
			byte[] str = content.toArray(JAVA_BYTE);
			return new String(str, StandardCharsets.UTF_8);
		} else {
			return "";
		}
	}
}
