package tech.deplant.java4ever.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import tech.deplant.java4ever.binding.*;
import tech.deplant.java4ever.binding.loader.JavaLibraryPathLoader;


@Log4j2
public class Async {

    @Test
    public void testGenerics() throws JsonProcessingException {
        var ctx = new Context(new JavaLibraryPathLoader(),
                new Client.ClientConfig(
                    new Client.NetworkConfig(null, new String[]{"http://80.78.241.3/"},null,
                        null,null,null,
                        null,null,null,
                        null,null,null,
                        null,null,null,null,null
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

//    @Test
//    public void testJacksonRead() throws JsonProcessingException {
//        String s = "{" +
//                "\"publicKey\": \"public1\"," +
//                "\"secretKey\": \"secret1\"" +
//                "}";
//        record Cred(String publicKey, String secretKey){}
//// with 3.0 (or with 2.10 as alternative)
//        ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
//                .addModule(new ParameterNamesModule())
//                .addModule(new Jdk8Module())
//                .addModule(new JavaTimeModule())
//                // and possibly other configuration, modules, then:
//                .build();
//
//        Cred c = mapper.readValue(s, Cred.class);
//
//        log.debug(c.publicKey + ", " + c.secretKey);
//    }
}
