package tech.deplant.java4ever.binding.loader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record JavaLibraryPathLoader(String libraryName) implements LibraryLoader {

    public static final JavaLibraryPathLoader TON_CLIENT = new JavaLibraryPathLoader("ton_client");

    @Override
    public void load() {
        System.loadLibrary(this.libraryName);
        log.trace("Library loaded: " + this.libraryName + " on path: " + System.getProperty("java.library.path"));
    }

}
