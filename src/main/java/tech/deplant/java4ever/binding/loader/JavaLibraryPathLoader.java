package tech.deplant.java4ever.binding.loader;

import lombok.extern.log4j.Log4j2;

@Log4j2
public record JavaLibraryPathLoader(String libraryName) implements LibraryLoader {

    public static final JavaLibraryPathLoader TON_CLIENT = new JavaLibraryPathLoader("ton_client");

    @Override
    public void load() {
        System.loadLibrary(this.libraryName);
        log.trace("Library loaded: {} on path: {}", () -> this.libraryName, () -> System.getProperty("java.library.path"));
    }

}
