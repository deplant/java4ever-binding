package tech.deplant.java4ever.jextract;

import tech.deplant.java4ever.utils.SystemContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FfiGenerator {

	private static System.Logger logger = System.getLogger(FfiGenerator.class.getName());

	public static void main(String[] args) {
		try {
			// jextract
			// --source
			// ton_client/ton_client.h
			// --output
			// ton_client/java
			// --target-package
			// tech.deplant.java4ever.ffi
			// --header-class-name
			// ton_client
			logger.log(System.Logger.Level.INFO, () -> "Begging generation of FFI bridge...");
			Process p = new ProcessBuilder()
					.inheritIO()
					.directory(new File(jextractPath()))
					.command(
							jextractPath(),
							"--source",
							"ton_client/ton_client.h",
							"--output",
							"ton_client/java",
							"--target-package",
							"tech.deplant.java4ever.ffi",
							"--header-class-name",
							"ton_client"
					)
					.start();
			return p.onExit().get(30, TimeUnit.SECONDS).exitValue();
		} catch (ExecutionException  | InterruptedException  | TimeoutException  | IOException e) {
			logger.log(System.Logger.Level.ERROR, e::getMessage);
			return -1;
		}
	}
}
