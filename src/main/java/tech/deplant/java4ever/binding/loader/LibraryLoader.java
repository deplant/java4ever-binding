package tech.deplant.java4ever.binding.loader;

public interface LibraryLoader {
    static LibraryLoader ofType(LibraryLoaderType type, String value) {
		return switch (type) {
            case ENV -> AbsolutePathLoader.ofSystemEnv(value);
            case PATH -> new AbsolutePathLoader(value);
            case JAVALIB -> new JavaLibraryPathLoader(value);
            case DEFAULT -> new DefaultLoader(Thread.currentThread().getContextClassLoader());
		};
	}

	void load();
}
