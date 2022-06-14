package tech.deplant.java4ever.binding.loader;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class JavaLibraryPathLoader implements LibraryLoader {

    @Override
    public void load() {
        System.loadLibrary(LIB_NAME);
        log.trace("Library loaded: {} on path: {}", () -> LIB_NAME, () -> System.getProperty("java.library.path"));
    }
}
