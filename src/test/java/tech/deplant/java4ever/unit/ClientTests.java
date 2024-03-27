package tech.deplant.java4ever.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yegor256.OnlineMeans;
import com.yegor256.WeAreOnline;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.EverSdk;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.binding.loader.DefaultLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(WeAreOnline.class)
public class ClientTests {

	private static final Logger log = LoggerFactory.getLogger(ClientTests.class);

	@BeforeAll
	public static void loadSdk() {
		EverSdk.load(new AbsolutePathLoader("c:/opt/sdk/ton_client.dll"));
	}

	@Test
	@OnlineMeans(url = TestEnv.NODESE_URL, connectTimeout = 500, readTimeout = 1500)
	public void sdk_version_equals_constant() throws EverSdkException {
		int ctxId = TestEnv.newContext();
		assertEquals(DefaultLoader.EVER_SDK_VERSION, Client.version(ctxId).version());
	}

	@Test
	@OnlineMeans(url = TestEnv.NODESE_URL, connectTimeout = 500, readTimeout = 1500)
	public void api_reference_version_equals_constant() throws EverSdkException {
		int ctxId = TestEnv.newContext();
		assertEquals(DefaultLoader.EVER_SDK_VERSION, Client.getApiReference(ctxId).api().get("version").asText());
	}

}