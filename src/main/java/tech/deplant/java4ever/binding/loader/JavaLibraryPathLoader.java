package tech.deplant.java4ever.binding.loader;

public class JavaLibraryPathLoader implements LibraryLoader {

    @Override
    public void load() {
        System.loadLibrary(LIB_NAME);
    }
}
