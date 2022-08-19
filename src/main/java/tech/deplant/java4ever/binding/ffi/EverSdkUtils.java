package tech.deplant.java4ever.binding.ffi;

import java.lang.foreign.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static java.lang.foreign.ValueLayout.JAVA_BYTE;
import static tech.deplant.java4ever.binding.ffi.tc_string_data_t.len$get;

public class EverSdkUtils {

    public static String toJavaString(MemorySegment segment, int len) {
        byte[] bytes = new byte[len];
        MemorySegment.copy(segment, JAVA_BYTE, 0L, bytes, 0, len);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String toJavaString(MemorySegment seg, MemorySession scope) {
        if (tc_string_data_t.len$get(seg) > 0) {
            MemoryAddress addr = tc_string_data_t.content$get(seg);
            byte[] str = MemorySegment.ofAddress(addr, len$get(seg), scope).toArray(JAVA_BYTE);
            return new String(str, StandardCharsets.UTF_8);
        } else {
            return "";
        }
    }

    static int strLength(MemorySegment seg) {
        return len$get(seg);
    }


    static MemorySegment fromJavaString(SegmentAllocator allocator, String s, Charset charset) {
        if (StandardCharsets.UTF_8 == charset) { // "==" is OK here as StandardCharsets.UTF_8 == Charset.forName("UTF8")
            return allocator.allocateUtf8String(s);
        } else {
            return allocator.allocateArray(ValueLayout.JAVA_BYTE, (s + "\0").getBytes(charset));
        }
    }

}
