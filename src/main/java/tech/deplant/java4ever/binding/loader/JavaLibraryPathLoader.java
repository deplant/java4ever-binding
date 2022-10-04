package tech.deplant.java4ever.binding.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record JavaLibraryPathLoader(String libraryName) implements LibraryLoader {

	public static final JavaLibraryPathLoader TON_CLIENT = new JavaLibraryPathLoader("ton_client");
	private static Logger log = LoggerFactory.getLogger(JavaLibraryPathLoader.class);

	@Override
	public void load() {
		System.loadLibrary(this.libraryName);
		log.trace("Library loaded: " + this.libraryName + " on path: " + System.getProperty("java.library.path"));
	}

}
