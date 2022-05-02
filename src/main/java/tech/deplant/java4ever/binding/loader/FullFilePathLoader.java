package tech.deplant.java4ever.binding.loader;

public record FullFilePathLoader(String filepath) implements LibraryLoader {

    @Override
    public void load() {
        System.load(filepath);
    }
}
