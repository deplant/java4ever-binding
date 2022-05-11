package tech.deplant.java4ever.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import tech.deplant.java4ever.binding.GraphQL;
import tech.deplant.java4ever.binding.Net;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class Async {

    @Test
    public void testJacksonRead() throws JsonProcessingException {
        String s = "{" +
                "\"publicKey\": \"public1\"," +
                "\"secretKey\": \"secret1\"" +
                "}";
        record Cred(String publicKey, String secretKey){};
// with 3.0 (or with 2.10 as alternative)
        ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                // and possibly other configuration, modules, then:
                .build();

        Cred c = mapper.readValue(s, Cred.class);

        log.debug(c.publicKey + ", " + c.secretKey);
    }



    // mapper.convertValue(map, MyPojo.class);

}
