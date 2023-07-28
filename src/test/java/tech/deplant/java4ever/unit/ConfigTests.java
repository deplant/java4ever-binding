package tech.deplant.java4ever.unit;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.EverSdkContext;
import tech.deplant.java4ever.binding.EverSdkException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class ConfigTests {

	@Test
	public void network_config_from_json_equals_result_config() throws JsonProcessingException, EverSdkException {
		var endpoint = "https://net.ton.dev/graphql";
		var configJson = "{\"network\":{\"endpoints\":[\"" + endpoint + "\"]}}";
		var ctx = EverSdkContext.builder()
				.setConfigJson(configJson)
				.buildNew();
		Client.version(ctx);
		assertEquals(Client.config(ctx).network().endpoints()[0], endpoint);
	}

	@Test
	public void binding_config_from_json_rewrites_to_preset_config() throws JsonProcessingException, EverSdkException {
		var configJson = "{\"binding\":{\"library\":\"ton-client-java\",\"version\":\"1.5.0\"}}";
		new EverSdkContext.Builder().setConfigJson(configJson);
		var ctx = new EverSdkContext.Builder()
				.setConfigJson(configJson)
				.buildNew();
		Client.version(ctx);
		assertEquals("2.3.0", Client.config(ctx).binding().version());
		assertEquals("java4ever", Client.config(ctx).binding().library());
	}


}
