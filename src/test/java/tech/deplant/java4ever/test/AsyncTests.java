package tech.deplant.java4ever.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class AsyncTests {

	@Test
	public void equals_endpoints_in_context() throws JsonProcessingException, EverSdkException {
		var endpoint = "https://net.ton.dev/graphql";
		var configJson = "{\"network\":{\"endpoints\":[\"" + endpoint + "\"]}}";
		var ctx = new ContextBuilder()
				.setConfigJson(configJson)
				.buildNew();
		Client.version(ctx);
		//assertEquals(Client.config(ctx).network().endpoints()[0], endpoint);
	}

	@Test
	public void check_json_merge() throws JsonProcessingException {
		var endpoint = "https://net.ton.dev/graphql";
		var configJson = "{\"network\":{\"endpoints\":[\"" + endpoint + "\"]}}";
		Client.ClientConfig defaults = ContextBuilder.DEFAULT_MAPPER.readValue(configJson, Client.ClientConfig.class);
		var mergedConfig = new Client.ClientConfig(new Client.BindingConfig("java4ever","1.5.0"),defaults.network(),defaults.crypto(),defaults.abi(),defaults.boc(),defaults.proofs(),
		                                           defaults.localStoragePath());
		System.out.println(ContextBuilder.DEFAULT_MAPPER.writeValueAsString(mergedConfig));
	}

//    @Test
//    public void testGo2() throws com.fasterxml.jackson.core.JsonProcessingException {
//        log.debug(JsonContext.MAPPER.writeValueAsString(new tech.deplant.java4ever.binding.Abi.Signer.Keys(
//                new tech.deplant.java4ever.binding.Crypto.KeyPair("pk", "sk"))));
//        log.debug(JsonContext.MAPPER.writeValueAsString(new tech.deplant.java4ever.binding.Abi.Signer.External(
//                "pk")));
//        log.debug(JsonContext.MAPPER.writeValueAsString(tech.deplant.java4ever.binding.Abi.MessageBodyType.InternalOutput));
//    }

}
