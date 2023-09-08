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
import tech.deplant.java4ever.binding.gql.AccountFilter;
import tech.deplant.java4ever.binding.gql.Query;
import tech.deplant.java4ever.binding.gql.StringFilter;
import tech.deplant.java4ever.binding.loader.DefaultLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class ConfigTests {

	@Test
	public void check_ever_sdk_version() throws JsonProcessingException, EverSdkException {
		assertEquals(DefaultLoader.EVER_SDK_VERSION, Client.version(EverSdkContext.builder().buildNew()).version());
	}

	@Test
	public void make_query() throws JsonProcessingException, EverSdkException {
		System.out.println(Query.accounts("balance boc", new AccountFilter(
				new StringFilter("0:d707caf6df3a7c2bb0b64915613eca9d8f17ca1de0b938dfdcbb9b4ff30c4526",
				                 null,
				                 null,
				                 null,
				                 null,
				                 null,
				                 null,
				                 null),
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null
		), null, null, null, null, null).toGraphQLQuery());
	}

	@Test
	public void context_id_increments_as_we_are_using_the_same_lib_for_each_context() throws JsonProcessingException, EverSdkException {
		assertEquals(1, EverSdkContext.builder().buildNew().id());
		assertEquals(2, EverSdkContext.builder().buildNew().id());
		assertEquals(3, EverSdkContext.builder().buildNew().id());
	}

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
		assertEquals(DefaultLoader.BINDING_LIBRARY_VERSION, Client.config(ctx).binding().version());
		assertEquals(DefaultLoader.BINDING_LIBRARY_NAME, Client.config(ctx).binding().library());
	}


}
