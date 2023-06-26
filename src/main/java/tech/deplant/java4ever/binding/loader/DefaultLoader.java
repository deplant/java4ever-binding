package tech.deplant.java4ever.binding.loader;

import tech.deplant.java4ever.utils.Fls;
import tech.deplant.java4ever.utils.SystemContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public record DefaultLoader(ClassLoader loader) implements LibraryLoader {
	public void loadJarDll(String name) {
		InputStream in = loader().getResourceAsStream(name);
		byte[] buffer = new byte[1024];
		int read = -1;
		File temp = null;
		try {
			temp = File.createTempFile(name, "");
			FileOutputStream fos = new FileOutputStream(temp);

			while ((read = in.read(buffer)) != -1) {
				fos.write(buffer, 0, read);
			}
			fos.close();
			in.close();

			System.load(temp.getAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void load() {
		if (SystemContext.OS().equals(SystemContext.OperatingSystem.WINDOWS) &&
		    SystemContext.PROCESSOR().equals(SystemContext.ProcessorArchitecture.X86_64)) {
			loadJarDll("sdk/win32_x86_64/ton_client.dll");
		} else if (SystemContext.OS().equals(SystemContext.OperatingSystem.LINUX) &&
		           SystemContext.PROCESSOR().equals(SystemContext.ProcessorArchitecture.X86_64)) {
			loadJarDll(Fls.resourceToAbsolute(DefaultLoader.class, "sdk/linux_x86_64/libton_client.so"));
		} else if (SystemContext.OS().equals(SystemContext.OperatingSystem.MAC) &&
		           SystemContext.PROCESSOR().equals(SystemContext.ProcessorArchitecture.X86_64)) {
			loadJarDll(Fls.resourceToAbsolute(DefaultLoader.class, "sdk/macos_x86_64/libton_client.dylib"));
		} else if (SystemContext.OS().equals(SystemContext.OperatingSystem.MAC) &&
		           SystemContext.PROCESSOR().equals(SystemContext.ProcessorArchitecture.ARM_64)) {
			loadJarDll(Fls.resourceToAbsolute(DefaultLoader.class, "sdk/macos_aarch64/libton_client.dylib"));
		} else {
			throw new RuntimeException("Unsupported architecture, use other loaders for your custom EVER-SDK library!");
		}

	}

}
