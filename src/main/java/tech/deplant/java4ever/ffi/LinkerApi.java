package tech.deplant.java4ever.ffi;
// Generated by jextract

import jdk.incubator.foreign.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static jdk.incubator.foreign.CLinker.*;

final class LinkerApi {
    private final static CLinker LINKER = CLinker.getInstance();
    private final static ClassLoader LOADER = LinkerApi.class.getClassLoader();
    private final static MethodHandles.Lookup MH_LOOKUP = MethodHandles.lookup();
    private final static SegmentAllocator THROWING_ALLOCATOR = (x, y) -> {
        throw new AssertionError("should not reach here");
    };

//    static SymbolLookup lookup() {
//        return name -> SymbolLookup.loaderLookup().lookup(name).or(() -> CLinker.systemLookup().lookup(name));
//    }

    private LinkerApi() {
    }

    static <T> T requireNonNull(T obj, String symbolName) {
        if (obj == null) {
            throw new UnsatisfiedLinkError("unresolved symbol: " + symbolName);
        }
        return obj;
    }

    static MemorySegment lookupGlobalVariable(SymbolLookup LOOKUP, String name, MemoryLayout layout) {
        return LOOKUP.lookup(name).map(s -> s.address().asSegment(layout.byteSize(), ResourceScope.newImplicitScope())).orElse(null);
    }

    static MethodHandle downcallHandle(SymbolLookup LOOKUP, String name, String desc, FunctionDescriptor fdesc, boolean variadic) {
        return LOOKUP.lookup(name).map(
                addr -> {
                    MethodType mt = MethodType.fromMethodDescriptorString(desc, LOADER);
                    return variadic ?
                            VarargsInvoker.make(addr, mt, fdesc) :
                            LINKER.downcallHandle(addr, mt, fdesc);
                }).orElse(null);
    }

    static MethodHandle downcallHandle(String desc, FunctionDescriptor fdesc, boolean variadic) {
        if (variadic) {
            throw new AssertionError("Cannot get here!");
        }
        MethodType mt = MethodType.fromMethodDescriptorString(desc, LOADER);
        return LINKER.downcallHandle(mt, fdesc);
    }

    static <Z> MemoryAddress upcallStub(Class<Z> fi, Z z, FunctionDescriptor fdesc, String mtypeDesc) {
        return upcallStub(fi, z, fdesc, mtypeDesc, ResourceScope.newImplicitScope());
    }

    static <Z> MemoryAddress upcallStub(Class<Z> fi, Z z, FunctionDescriptor fdesc, String mtypeDesc, ResourceScope scope) {
        try {
            MethodHandle handle = MH_LOOKUP.findVirtual(fi, "apply",
                    MethodType.fromMethodDescriptorString(mtypeDesc, LOADER));
            handle = handle.bindTo(z);
            return LINKER.upcallStub(handle, fdesc, scope);
        } catch (Throwable ex) {
            throw new AssertionError(ex);
        }
    }

    static MemorySegment asArray(MemoryAddress addr, MemoryLayout layout, int numElements, ResourceScope scope) {
        return addr.asSegment(numElements * layout.byteSize(), scope);
    }

    // Internals only below this point

    private static class VarargsInvoker {
        private static final MethodHandle INVOKE_MH;

        static {
            try {
                INVOKE_MH = MethodHandles.lookup().findVirtual(VarargsInvoker.class, "invoke", MethodType.methodType(Object.class, SegmentAllocator.class, Object[].class));
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }

        private final Addressable symbol;
        private final MethodType varargs;
        private final FunctionDescriptor function;

        private VarargsInvoker(Addressable symbol, MethodType type, FunctionDescriptor function) {
            this.symbol = symbol;
            this.varargs = type;
            this.function = function;
        }

        static MethodHandle make(Addressable symbol, MethodType type, FunctionDescriptor function) {
            VarargsInvoker invoker = new VarargsInvoker(symbol, type, function);
            MethodHandle handle = INVOKE_MH.bindTo(invoker).asCollector(Object[].class, type.parameterCount());
            if (type.returnType().equals(MemorySegment.class)) {
                type = type.insertParameterTypes(0, SegmentAllocator.class);
            } else {
                handle = MethodHandles.insertArguments(handle, 0, THROWING_ALLOCATOR);
            }
            return handle.asType(type);
        }

        private static Class<?> unboxIfNeeded(Class<?> clazz) {
            if (clazz == Boolean.class) {
                return boolean.class;
            } else if (clazz == Void.class) {
                return void.class;
            } else if (clazz == Byte.class) {
                return byte.class;
            } else if (clazz == Character.class) {
                return char.class;
            } else if (clazz == Short.class) {
                return short.class;
            } else if (clazz == Integer.class) {
                return int.class;
            } else if (clazz == Long.class) {
                return long.class;
            } else if (clazz == Float.class) {
                return float.class;
            } else if (clazz == Double.class) {
                return double.class;
            } else {
                return clazz;
            }
        }

        private Object invoke(SegmentAllocator allocator, Object[] args) throws Throwable {
            // one trailing Object[]
            int nNamedArgs = function.argumentLayouts().size();
            assert (args.length == nNamedArgs + 1);
            // The last argument is the array of vararg collector
            Object[] unnamedArgs = (Object[]) args[args.length - 1];

            int argsCount = nNamedArgs + unnamedArgs.length;
            Class<?>[] argTypes = new Class<?>[argsCount];
            MemoryLayout[] argLayouts = new MemoryLayout[nNamedArgs + unnamedArgs.length];

            int pos = 0;
            for (pos = 0; pos < nNamedArgs; pos++) {
                argTypes[pos] = varargs.parameterType(pos);
                argLayouts[pos] = function.argumentLayouts().get(pos);
            }

            assert pos == nNamedArgs;
            for (Object o : unnamedArgs) {
                argTypes[pos] = normalize(o.getClass());
                argLayouts[pos] = variadicLayout(argTypes[pos]);
                pos++;
            }
            assert pos == argsCount;

            MethodType mt = MethodType.methodType(varargs.returnType(), argTypes);
            FunctionDescriptor f = (function.returnLayout().isEmpty()) ?
                    FunctionDescriptor.ofVoid(argLayouts) :
                    FunctionDescriptor.of(function.returnLayout().get(), argLayouts);
            MethodHandle mh = LINKER.downcallHandle(symbol, allocator, mt, f);
            // flatten argument list so that it can be passed to an asSpreader MH
            Object[] allArgs = new Object[nNamedArgs + unnamedArgs.length];
            System.arraycopy(args, 0, allArgs, 0, nNamedArgs);
            System.arraycopy(unnamedArgs, 0, allArgs, nNamedArgs, unnamedArgs.length);

            return mh.asSpreader(Object[].class, argsCount).invoke(allArgs);
        }

        private Class<?> promote(Class<?> c) {
            if (c == byte.class || c == char.class || c == short.class || c == int.class) {
                return long.class;
            } else if (c == float.class) {
                return double.class;
            } else {
                return c;
            }
        }

        private Class<?> normalize(Class<?> c) {
            c = unboxIfNeeded(c);
            if (c.isPrimitive()) {
                return promote(c);
            }
            if (MemoryAddress.class.isAssignableFrom(c)) {
                return MemoryAddress.class;
            }
            if (MemorySegment.class.isAssignableFrom(c)) {
                return MemorySegment.class;
            }
            throw new IllegalArgumentException("Invalid type for ABI: " + c.getTypeName());
        }

        private MemoryLayout variadicLayout(Class<?> c) {
            if (c == long.class) {
                return C_LONG_LONG;
            } else if (c == double.class) {
                return C_DOUBLE;
            } else if (MemoryAddress.class.isAssignableFrom(c)) {
                return C_POINTER;
            } else {
                throw new IllegalArgumentException("Unhandled variadic argument class: " + c);
            }
        }
    }
}
