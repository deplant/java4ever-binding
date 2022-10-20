package tech.deplant.java4ever.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.Context;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AsyncTests {

	private static final Logger log = LoggerFactory.getLogger(Context.class);

	@Test
	public void equals_endpoints_in_context() throws JsonProcessingException, EverSdkException {
		var endpoint = "https://net.ton.dev/graphql";
		var configJson = "{\"network\":{\"endpoints\":[\"" + endpoint + "\"]}}";
		var ctx = new ContextBuilder()
				.setConfigJson(configJson)
				.buildNew(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
		assertEquals(Client.config(ctx).network().endpoints()[0], endpoint);
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
