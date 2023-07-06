// Generated by jextract

package tech.deplant.java4ever.binding.ffi;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.lang.foreign.*;
import static java.lang.foreign.ValueLayout.*;
public class ton_client  {

    public static final OfByte C_CHAR = Constants$root.C_CHAR$LAYOUT;
    public static final OfShort C_SHORT = Constants$root.C_SHORT$LAYOUT;
    public static final OfInt C_INT = Constants$root.C_LONG$LAYOUT;
    public static final OfInt C_LONG = Constants$root.C_LONG$LAYOUT;
    public static final OfLong C_LONG_LONG = Constants$root.C_LONG_LONG$LAYOUT;
    public static final OfFloat C_FLOAT = Constants$root.C_FLOAT$LAYOUT;
    public static final OfDouble C_DOUBLE = Constants$root.C_DOUBLE$LAYOUT;
    public static final OfAddress C_POINTER = Constants$root.C_POINTER$LAYOUT;
    /**
     * {@snippet :
     * #define true 1
     * }
     */
    public static int true_() {
        return (int)1L;
    }
    /**
     * {@snippet :
     * #define false 0
     * }
     */
    public static int false_() {
        return (int)0L;
    }
    /**
     * {@snippet :
     * #define __bool_true_false_are_defined 1
     * }
     */
    public static int __bool_true_false_are_defined() {
        return (int)1L;
    }
    /**
     * {@snippet :
     * typedef long long int64_t;
     * }
     */
    public static final OfLong int64_t = Constants$root.C_LONG_LONG$LAYOUT;
    /**
     * {@snippet :
     * typedef unsigned long long uint64_t;
     * }
     */
    public static final OfLong uint64_t = Constants$root.C_LONG_LONG$LAYOUT;
    /**
     * {@snippet :
     * typedef long long int_least64_t;
     * }
     */
    public static final OfLong int_least64_t = Constants$root.C_LONG_LONG$LAYOUT;
    /**
     * {@snippet :
     * typedef unsigned long long uint_least64_t;
     * }
     */
    public static final OfLong uint_least64_t = Constants$root.C_LONG_LONG$LAYOUT;
    /**
     * {@snippet :
     * typedef long long int_fast64_t;
     * }
     */
    public static final OfLong int_fast64_t = Constants$root.C_LONG_LONG$LAYOUT;
    /**
     * {@snippet :
     * typedef unsigned long long uint_fast64_t;
     * }
     */
    public static final OfLong uint_fast64_t = Constants$root.C_LONG_LONG$LAYOUT;
    /**
     * {@snippet :
     * typedef int int32_t;
     * }
     */
    public static final OfInt int32_t = Constants$root.C_LONG$LAYOUT;
    /**
     * {@snippet :
     * typedef unsigned int uint32_t;
     * }
     */
    public static final OfInt uint32_t = Constants$root.C_LONG$LAYOUT;
    /**
     * {@snippet :
     * typedef int int_least32_t;
     * }
     */
    public static final OfInt int_least32_t = Constants$root.C_LONG$LAYOUT;
    /**
     * {@snippet :
     * typedef unsigned int uint_least32_t;
     * }
     */
    public static final OfInt uint_least32_t = Constants$root.C_LONG$LAYOUT;
    /**
     * {@snippet :
     * typedef int int_fast32_t;
     * }
     */
    public static final OfInt int_fast32_t = Constants$root.C_LONG$LAYOUT;
    /**
     * {@snippet :
     * typedef unsigned int uint_fast32_t;
     * }
     */
    public static final OfInt uint_fast32_t = Constants$root.C_LONG$LAYOUT;
    /**
     * {@snippet :
     * typedef short int16_t;
     * }
     */
    public static final OfShort int16_t = Constants$root.C_SHORT$LAYOUT;
    /**
     * {@snippet :
     * typedef unsigned short uint16_t;
     * }
     */
    public static final OfShort uint16_t = Constants$root.C_SHORT$LAYOUT;
    /**
     * {@snippet :
     * typedef short int_least16_t;
     * }
     */
    public static final OfShort int_least16_t = Constants$root.C_SHORT$LAYOUT;
    /**
     * {@snippet :
     * typedef unsigned short uint_least16_t;
     * }
     */
    public static final OfShort uint_least16_t = Constants$root.C_SHORT$LAYOUT;
    /**
     * {@snippet :
     * typedef short int_fast16_t;
     * }
     */
    public static final OfShort int_fast16_t = Constants$root.C_SHORT$LAYOUT;
    /**
     * {@snippet :
     * typedef unsigned short uint_fast16_t;
     * }
     */
    public static final OfShort uint_fast16_t = Constants$root.C_SHORT$LAYOUT;
    /**
     * {@snippet :
     * typedef signed char int8_t;
     * }
     */
    public static final OfByte int8_t = Constants$root.C_CHAR$LAYOUT;
    /**
     * {@snippet :
     * typedef unsigned char uint8_t;
     * }
     */
    public static final OfByte uint8_t = Constants$root.C_CHAR$LAYOUT;
    /**
     * {@snippet :
     * typedef signed char int_least8_t;
     * }
     */
    public static final OfByte int_least8_t = Constants$root.C_CHAR$LAYOUT;
    /**
     * {@snippet :
     * typedef unsigned char uint_least8_t;
     * }
     */
    public static final OfByte uint_least8_t = Constants$root.C_CHAR$LAYOUT;
    /**
     * {@snippet :
     * typedef signed char int_fast8_t;
     * }
     */
    public static final OfByte int_fast8_t = Constants$root.C_CHAR$LAYOUT;
    /**
     * {@snippet :
     * typedef unsigned char uint_fast8_t;
     * }
     */
    public static final OfByte uint_fast8_t = Constants$root.C_CHAR$LAYOUT;
    /**
     * {@snippet :
     * typedef long long intptr_t;
     * }
     */
    public static final OfLong intptr_t = Constants$root.C_LONG_LONG$LAYOUT;
    /**
     * {@snippet :
     * typedef unsigned long long uintptr_t;
     * }
     */
    public static final OfLong uintptr_t = Constants$root.C_LONG_LONG$LAYOUT;
    /**
     * {@snippet :
     * typedef long long intmax_t;
     * }
     */
    public static final OfLong intmax_t = Constants$root.C_LONG_LONG$LAYOUT;
    /**
     * {@snippet :
     * typedef unsigned long long uintmax_t;
     * }
     */
    public static final OfLong uintmax_t = Constants$root.C_LONG_LONG$LAYOUT;
    /**
     * {@snippet :
     * enum tc_response_types.tc_response_success = 0;
     * }
     */
    public static int tc_response_success() {
        return (int)0L;
    }
    /**
     * {@snippet :
     * enum tc_response_types.tc_response_error = 1;
     * }
     */
    public static int tc_response_error() {
        return (int)1L;
    }
    /**
     * {@snippet :
     * enum tc_response_types.tc_response_nop = 2;
     * }
     */
    public static int tc_response_nop() {
        return (int)2L;
    }
    /**
     * {@snippet :
     * enum tc_response_types.tc_response_app_request = 3;
     * }
     */
    public static int tc_response_app_request() {
        return (int)3L;
    }
    /**
     * {@snippet :
     * enum tc_response_types.tc_response_app_notify = 4;
     * }
     */
    public static int tc_response_app_notify() {
        return (int)4L;
    }
    /**
     * {@snippet :
     * enum tc_response_types.tc_response_custom = 100;
     * }
     */
    public static int tc_response_custom() {
        return (int)100L;
    }
    public static MethodHandle tc_create_context$MH() {
        return RuntimeHelper.requireNonNull(constants$0.tc_create_context$MH,"tc_create_context");
    }
    /**
     * {@snippet :
     * tc_string_handle_t* tc_create_context(tc_string_data_t config);
     * }
     */
    public static MemorySegment tc_create_context(MemorySegment config) {
        var mh$ = tc_create_context$MH();
        try {
            return (java.lang.foreign.MemorySegment)mh$.invokeExact(config);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle tc_destroy_context$MH() {
        return RuntimeHelper.requireNonNull(constants$0.tc_destroy_context$MH,"tc_destroy_context");
    }
    /**
     * {@snippet :
     * void tc_destroy_context(uint32_t context);
     * }
     */
    public static void tc_destroy_context(int context) {
        var mh$ = tc_destroy_context$MH();
        try {
            mh$.invokeExact(context);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle tc_request$MH() {
        return RuntimeHelper.requireNonNull(constants$1.tc_request$MH,"tc_request");
    }
    /**
     * {@snippet :
     * void tc_request(uint32_t context, tc_string_data_t function_name, tc_string_data_t function_params_json, uint32_t request_id, tc_response_handler_t response_handler);
     * }
     */
    public static void tc_request(int context, MemorySegment function_name, MemorySegment function_params_json, int request_id, MemorySegment response_handler) {
        var mh$ = tc_request$MH();
        try {
            mh$.invokeExact(context, function_name, function_params_json, request_id, response_handler);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle tc_request_ptr$MH() {
        return RuntimeHelper.requireNonNull(constants$1.tc_request_ptr$MH,"tc_request_ptr");
    }
    /**
     * {@snippet :
     * void tc_request_ptr(uint32_t context, tc_string_data_t function_name, tc_string_data_t function_params_json, void* request_ptr, tc_response_handler_ptr_t response_handler);
     * }
     */
    public static void tc_request_ptr(int context, MemorySegment function_name, MemorySegment function_params_json, MemorySegment request_ptr, MemorySegment response_handler) {
        var mh$ = tc_request_ptr$MH();
        try {
            mh$.invokeExact(context, function_name, function_params_json, request_ptr, response_handler);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle tc_request_sync$MH() {
        return RuntimeHelper.requireNonNull(constants$1.tc_request_sync$MH,"tc_request_sync");
    }
    /**
     * {@snippet :
     * tc_string_handle_t* tc_request_sync(uint32_t context, tc_string_data_t function_name, tc_string_data_t function_params_json);
     * }
     */
    public static MemorySegment tc_request_sync(int context, MemorySegment function_name, MemorySegment function_params_json) {
        var mh$ = tc_request_sync$MH();
        try {
            return (java.lang.foreign.MemorySegment)mh$.invokeExact(context, function_name, function_params_json);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle tc_read_string$MH() {
        return RuntimeHelper.requireNonNull(constants$1.tc_read_string$MH,"tc_read_string");
    }
    /**
     * {@snippet :
     * tc_string_data_t tc_read_string(const tc_string_handle_t* handle);
     * }
     */
    public static MemorySegment tc_read_string(SegmentAllocator allocator, MemorySegment handle) {
        var mh$ = tc_read_string$MH();
        try {
            return (java.lang.foreign.MemorySegment)mh$.invokeExact(allocator, handle);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    public static MethodHandle tc_destroy_string$MH() {
        return RuntimeHelper.requireNonNull(constants$1.tc_destroy_string$MH,"tc_destroy_string");
    }
    /**
     * {@snippet :
     * void tc_destroy_string(const tc_string_handle_t* handle);
     * }
     */
    public static void tc_destroy_string(MemorySegment handle) {
        var mh$ = tc_destroy_string$MH();
        try {
            mh$.invokeExact(handle);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
    /**
     * {@snippet :
     * #define INT64_MAX 9223372036854775807
     * }
     */
    public static long INT64_MAX() {
        return 9223372036854775807L;
    }
    /**
     * {@snippet :
     * #define INT64_MIN -9223372036854775808
     * }
     */
    public static long INT64_MIN() {
        return -9223372036854775808L;
    }
    /**
     * {@snippet :
     * #define UINT64_MAX -1
     * }
     */
    public static long UINT64_MAX() {
        return -1L;
    }
    /**
     * {@snippet :
     * #define __INT_LEAST64_MIN -9223372036854775808
     * }
     */
    public static long __INT_LEAST64_MIN() {
        return -9223372036854775808L;
    }
    /**
     * {@snippet :
     * #define __INT_LEAST64_MAX 9223372036854775807
     * }
     */
    public static long __INT_LEAST64_MAX() {
        return 9223372036854775807L;
    }
    /**
     * {@snippet :
     * #define __UINT_LEAST64_MAX -1
     * }
     */
    public static long __UINT_LEAST64_MAX() {
        return -1L;
    }
    /**
     * {@snippet :
     * #define __INT_LEAST32_MIN -2147483648
     * }
     */
    public static int __INT_LEAST32_MIN() {
        return (int)-2147483648L;
    }
    /**
     * {@snippet :
     * #define __INT_LEAST32_MAX 2147483647
     * }
     */
    public static int __INT_LEAST32_MAX() {
        return (int)2147483647L;
    }
    /**
     * {@snippet :
     * #define __UINT_LEAST32_MAX 4294967295
     * }
     */
    public static int __UINT_LEAST32_MAX() {
        return (int)4294967295L;
    }
    /**
     * {@snippet :
     * #define __INT_LEAST16_MIN -32768
     * }
     */
    public static int __INT_LEAST16_MIN() {
        return (int)-32768L;
    }
    /**
     * {@snippet :
     * #define __INT_LEAST16_MAX 32767
     * }
     */
    public static int __INT_LEAST16_MAX() {
        return (int)32767L;
    }
    /**
     * {@snippet :
     * #define __UINT_LEAST16_MAX 65535
     * }
     */
    public static int __UINT_LEAST16_MAX() {
        return (int)65535L;
    }
    /**
     * {@snippet :
     * #define __INT_LEAST8_MIN -128
     * }
     */
    public static int __INT_LEAST8_MIN() {
        return (int)-128L;
    }
    /**
     * {@snippet :
     * #define __INT_LEAST8_MAX 127
     * }
     */
    public static int __INT_LEAST8_MAX() {
        return (int)127L;
    }
    /**
     * {@snippet :
     * #define __UINT_LEAST8_MAX 255
     * }
     */
    public static int __UINT_LEAST8_MAX() {
        return (int)255L;
    }
    /**
     * {@snippet :
     * #define INT_LEAST64_MIN -9223372036854775808
     * }
     */
    public static long INT_LEAST64_MIN() {
        return -9223372036854775808L;
    }
    /**
     * {@snippet :
     * #define INT_LEAST64_MAX 9223372036854775807
     * }
     */
    public static long INT_LEAST64_MAX() {
        return 9223372036854775807L;
    }
    /**
     * {@snippet :
     * #define UINT_LEAST64_MAX -1
     * }
     */
    public static long UINT_LEAST64_MAX() {
        return -1L;
    }
    /**
     * {@snippet :
     * #define INT_FAST64_MIN -9223372036854775808
     * }
     */
    public static long INT_FAST64_MIN() {
        return -9223372036854775808L;
    }
    /**
     * {@snippet :
     * #define INT_FAST64_MAX 9223372036854775807
     * }
     */
    public static long INT_FAST64_MAX() {
        return 9223372036854775807L;
    }
    /**
     * {@snippet :
     * #define UINT_FAST64_MAX -1
     * }
     */
    public static long UINT_FAST64_MAX() {
        return -1L;
    }
    /**
     * {@snippet :
     * #define INT32_MAX 2147483647
     * }
     */
    public static int INT32_MAX() {
        return (int)2147483647L;
    }
    /**
     * {@snippet :
     * #define INT32_MIN -2147483648
     * }
     */
    public static int INT32_MIN() {
        return (int)-2147483648L;
    }
    /**
     * {@snippet :
     * #define UINT32_MAX 4294967295
     * }
     */
    public static int UINT32_MAX() {
        return (int)4294967295L;
    }
    /**
     * {@snippet :
     * #define INT_LEAST32_MIN -2147483648
     * }
     */
    public static int INT_LEAST32_MIN() {
        return (int)-2147483648L;
    }
    /**
     * {@snippet :
     * #define INT_LEAST32_MAX 2147483647
     * }
     */
    public static int INT_LEAST32_MAX() {
        return (int)2147483647L;
    }
    /**
     * {@snippet :
     * #define UINT_LEAST32_MAX 4294967295
     * }
     */
    public static int UINT_LEAST32_MAX() {
        return (int)4294967295L;
    }
    /**
     * {@snippet :
     * #define INT_FAST32_MIN -2147483648
     * }
     */
    public static int INT_FAST32_MIN() {
        return (int)-2147483648L;
    }
    /**
     * {@snippet :
     * #define INT_FAST32_MAX 2147483647
     * }
     */
    public static int INT_FAST32_MAX() {
        return (int)2147483647L;
    }
    /**
     * {@snippet :
     * #define UINT_FAST32_MAX 4294967295
     * }
     */
    public static int UINT_FAST32_MAX() {
        return (int)4294967295L;
    }
    /**
     * {@snippet :
     * #define INT16_MAX 32767
     * }
     */
    public static int INT16_MAX() {
        return (int)32767L;
    }
    /**
     * {@snippet :
     * #define INT16_MIN -32768
     * }
     */
    public static int INT16_MIN() {
        return (int)-32768L;
    }
    /**
     * {@snippet :
     * #define UINT16_MAX 65535
     * }
     */
    public static int UINT16_MAX() {
        return (int)65535L;
    }
    /**
     * {@snippet :
     * #define INT_LEAST16_MIN -32768
     * }
     */
    public static int INT_LEAST16_MIN() {
        return (int)-32768L;
    }
    /**
     * {@snippet :
     * #define INT_LEAST16_MAX 32767
     * }
     */
    public static int INT_LEAST16_MAX() {
        return (int)32767L;
    }
    /**
     * {@snippet :
     * #define UINT_LEAST16_MAX 65535
     * }
     */
    public static int UINT_LEAST16_MAX() {
        return (int)65535L;
    }
    /**
     * {@snippet :
     * #define INT_FAST16_MIN -32768
     * }
     */
    public static int INT_FAST16_MIN() {
        return (int)-32768L;
    }
    /**
     * {@snippet :
     * #define INT_FAST16_MAX 32767
     * }
     */
    public static int INT_FAST16_MAX() {
        return (int)32767L;
    }
    /**
     * {@snippet :
     * #define UINT_FAST16_MAX 65535
     * }
     */
    public static int UINT_FAST16_MAX() {
        return (int)65535L;
    }
    /**
     * {@snippet :
     * #define INT8_MAX 127
     * }
     */
    public static int INT8_MAX() {
        return (int)127L;
    }
    /**
     * {@snippet :
     * #define INT8_MIN -128
     * }
     */
    public static int INT8_MIN() {
        return (int)-128L;
    }
    /**
     * {@snippet :
     * #define UINT8_MAX 255
     * }
     */
    public static int UINT8_MAX() {
        return (int)255L;
    }
    /**
     * {@snippet :
     * #define INT_LEAST8_MIN -128
     * }
     */
    public static int INT_LEAST8_MIN() {
        return (int)-128L;
    }
    /**
     * {@snippet :
     * #define INT_LEAST8_MAX 127
     * }
     */
    public static int INT_LEAST8_MAX() {
        return (int)127L;
    }
    /**
     * {@snippet :
     * #define UINT_LEAST8_MAX 255
     * }
     */
    public static int UINT_LEAST8_MAX() {
        return (int)255L;
    }
    /**
     * {@snippet :
     * #define INT_FAST8_MIN -128
     * }
     */
    public static int INT_FAST8_MIN() {
        return (int)-128L;
    }
    /**
     * {@snippet :
     * #define INT_FAST8_MAX 127
     * }
     */
    public static int INT_FAST8_MAX() {
        return (int)127L;
    }
    /**
     * {@snippet :
     * #define UINT_FAST8_MAX 255
     * }
     */
    public static int UINT_FAST8_MAX() {
        return (int)255L;
    }
    /**
     * {@snippet :
     * #define INTPTR_MIN -9223372036854775808
     * }
     */
    public static long INTPTR_MIN() {
        return -9223372036854775808L;
    }
    /**
     * {@snippet :
     * #define INTPTR_MAX 9223372036854775807
     * }
     */
    public static long INTPTR_MAX() {
        return 9223372036854775807L;
    }
    /**
     * {@snippet :
     * #define UINTPTR_MAX -1
     * }
     */
    public static long UINTPTR_MAX() {
        return -1L;
    }
    /**
     * {@snippet :
     * #define PTRDIFF_MIN -9223372036854775808
     * }
     */
    public static long PTRDIFF_MIN() {
        return -9223372036854775808L;
    }
    /**
     * {@snippet :
     * #define PTRDIFF_MAX 9223372036854775807
     * }
     */
    public static long PTRDIFF_MAX() {
        return 9223372036854775807L;
    }
    /**
     * {@snippet :
     * #define SIZE_MAX -1
     * }
     */
    public static long SIZE_MAX() {
        return -1L;
    }
    /**
     * {@snippet :
     * #define INTMAX_MIN -9223372036854775808
     * }
     */
    public static long INTMAX_MIN() {
        return -9223372036854775808L;
    }
    /**
     * {@snippet :
     * #define INTMAX_MAX 9223372036854775807
     * }
     */
    public static long INTMAX_MAX() {
        return 9223372036854775807L;
    }
    /**
     * {@snippet :
     * #define UINTMAX_MAX -1
     * }
     */
    public static long UINTMAX_MAX() {
        return -1L;
    }
    /**
     * {@snippet :
     * #define SIG_ATOMIC_MIN -2147483648
     * }
     */
    public static int SIG_ATOMIC_MIN() {
        return (int)-2147483648L;
    }
    /**
     * {@snippet :
     * #define SIG_ATOMIC_MAX 2147483647
     * }
     */
    public static int SIG_ATOMIC_MAX() {
        return (int)2147483647L;
    }
    /**
     * {@snippet :
     * #define WINT_MIN 0
     * }
     */
    public static int WINT_MIN() {
        return (int)0L;
    }
    /**
     * {@snippet :
     * #define WINT_MAX 65535
     * }
     */
    public static int WINT_MAX() {
        return (int)65535L;
    }
    /**
     * {@snippet :
     * #define WCHAR_MAX 65535
     * }
     */
    public static int WCHAR_MAX() {
        return (int)65535L;
    }
    /**
     * {@snippet :
     * #define WCHAR_MIN 0
     * }
     */
    public static int WCHAR_MIN() {
        return (int)0L;
    }
}


