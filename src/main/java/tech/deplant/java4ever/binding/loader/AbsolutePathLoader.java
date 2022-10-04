package tech.deplant.java4ever.binding.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public record AbsolutePathLoader(Path filepath) implements LibraryLoader {

	private static Logger log = LoggerFactory.getLogger(AbsolutePathLoader.class);

	public AbsolutePathLoader {
		if (!filepath.isAbsolute()) {
			throw new IllegalArgumentException(
					"Filepath of AbsolutePathLoader should be absolute. Filepath: " + filepath);
		}
	}

	public AbsolutePathLoader(String filePathString) {
		this(Paths.get(filePathString));
	}

	public static AbsolutePathLoader ofUserDir(String fileName) {
		return new AbsolutePathLoader(System.getProperty("user.dir"));
	}

	public static AbsolutePathLoader ofSystemEnv(String envName) {
		String path = System.getenv(envName);
		log.trace("Path from ENV: " + path);
		return new AbsolutePathLoader(path);
	}

	@Override
	public void load() {
		System.load(this.filepath.toString());
	}
}
