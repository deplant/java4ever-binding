package tech.deplant.java4ever.binding;

import tech.deplant.java4ever.binding.ffi.NativeMethods;
import tech.deplant.java4ever.binding.ffi.SdkBridge;
import tech.deplant.java4ever.binding.loader.DefaultLoaderContext;
import tech.deplant.java4ever.binding.loader.LibraryLoader;

public class Sdk {

	public final static ScopedValue<SdkContext> CONTEXT = ScopedValue.newInstance();

	public SdkContext createContext() {

	}

	public SdkContext createContext() {
		final var createContextResponse = this.jsonMapper.readValue(NativeMethods.tcCreateContext(mergedJson),
		                                                            EverSdkContext.Builder.ResultOfCreateContext.class);
	}

	public static Sdk load() {
		return load(DefaultLoaderContext.SINGLETON(ClassLoader.getSystemClassLoader()));
	}
	public static Sdk load(LibraryLoader loader) {
		loader.load();
		return new Sdk();
	}
}
