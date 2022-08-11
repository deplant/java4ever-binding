package tech.deplant.java4ever.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.Context;
import tech.deplant.java4ever.binding.loader.JavaLibraryPathLoader;


@Slf4j
public class AsyncTests {

    @Test
    public void testGenerics() throws JsonProcessingException {
        var ctx = Context.create(JavaLibraryPathLoader.TON_CLIENT,
                new Client.ClientConfig(
                        new Client.NetworkConfig(null, new String[]{"http://80.78.241.3/"}, null,
                                null, null, null,
                                null, null, null,
                                null, null, null,
                                null, null, null, null, null
                        ),
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );

        var result = Client.config(ctx).join();
        log.debug(result.network().endpoints()[0]);
    }

    @Test
    public void testGo2() throws com.fasterxml.jackson.core.JsonProcessingException {
        log.debug(tech.deplant.java4ever.binding.JsonContext.MAPPER.writeValueAsString(new tech.deplant.java4ever.binding.Abi.Signer.Keys(
                new tech.deplant.java4ever.binding.Crypto.KeyPair("pk", "sk"))));
        log.debug(tech.deplant.java4ever.binding.JsonContext.MAPPER.writeValueAsString(new tech.deplant.java4ever.binding.Abi.Signer.External(
                "pk")));
        log.debug(tech.deplant.java4ever.binding.JsonContext.MAPPER.writeValueAsString(tech.deplant.java4ever.binding.Abi.MessageBodyType.InternalOutput));
    }

}
