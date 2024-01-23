package tech.deplant.java4ever.unit;


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
import tech.deplant.java4ever.binding.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(WeAreOnline.class)
public class ConfigTests {

	private static final Logger log = LoggerFactory.getLogger(ConfigTests.class);

	@BeforeAll
	public static void loadSdk() {
		EverSdk.load();
	}

	@Test
	@OnlineMeans(url = TestEnv.NODESE_URL, connectTimeout = 500, readTimeout = 1500)
	public void context_id_increments_as_we_open_new_contexts() {
		int ctxId = TestEnv.newContext();
		int startId = ctxId;
		assertTrue(startId >= 1);
		ctxId = TestEnv.newContext();
		assertTrue(ctxId >= startId + 1);
		ctxId = TestEnv.newContext();
		assertTrue(ctxId >= startId + 2);
	}

//	@Test
//	public void binding_config_from_json_rewrites_to_preset_config() throws JsonProcessingException, EverSdkException {
//		var configJson = "{\"binding\":{\"library\":\"ton-client-java\",\"version\":\"1.5.0\"}}";
//		new EverSdkContext.Builder().setConfigJson(configJson);
//		var ctx = new EverSdkContext.Builder()
//				.setConfigJson(configJson)
//				.buildNew();
//		Client.version(ctx);
//		assertEquals(DefaultLoader.BINDING_LIBRARY_VERSION, Client.config(ctx).binding().version());
//		assertEquals(DefaultLoader.BINDING_LIBRARY_NAME, Client.config(ctx).binding().library());
//	}

}
