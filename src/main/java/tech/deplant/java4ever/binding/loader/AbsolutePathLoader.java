package tech.deplant.java4ever.binding.loader;

import java.nio.file.Path;
import java.nio.file.Paths;

public record AbsolutePathLoader(Path filepath) implements LibraryLoader {

	public AbsolutePathLoader {
		if (!filepath.isAbsolute()) {
			throw new IllegalArgumentException(
					"Filepath of AbsolutePathLoader should be absolute. Filepath: " + filepath);
		}
	}

	public AbsolutePathLoader(String filePathString) {
		this(Paths.get(filePathString));
	}

	public AbsolutePathLoader ofUserDir(String fileName) {
		return new AbsolutePathLoader(System.getProperty("user.dir"));
	}

	public AbsolutePathLoader ofSystemEnv(String envName) {
		return new AbsolutePathLoader(System.getenv(envName));
	}

	@Override
	public void load() {
		System.load(this.filepath.toString());
	}
}
