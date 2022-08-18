package tech.deplant.java4ever.binding.ffi;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static java.lang.foreign.ValueLayout.JAVA_BYTE;

public class EverSdkUtils {

    public static String toJavaString(MemorySegment segment, int len) {
        byte[] bytes = new byte[len];
        MemorySegment.copy(segment, JAVA_BYTE, start, bytes, 0, len);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    static MemorySegment fromJavaString(SegmentAllocator allocator, String s, Charset charset) {
        if (StandardCharsets.UTF_8 == charset) { // "==" is OK here as StandardCharsets.UTF_8 == Charset.forName("UTF8")
            return allocator.allocateUtf8String(s);
        } else {
            return allocator.allocateArray(ValueLayout.JAVA_BYTE, (s + "\0").getBytes(charset));
        }
    }

    private static int strlen(MemorySegment segment, long start) {
        // iterate until overflow (String can only hold a byte[], whose length can be expressed as an int)
        for (int offset = 0; offset >= 0; offset++) {
            byte curr = segment.get(JAVA_BYTE, start + offset);
            if (curr == 0) {
                return offset;
            }
        }
        throw new IllegalArgumentException("String too large");
    }

}
