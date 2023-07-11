package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class JsonContext {

	private static ObjectMapper lazySdkMapper;
	private static ObjectMapper lazyAbiMapper;

	public static ObjectMapper SDK_JSON_MAPPER() {
		if (lazySdkMapper == null) {
			lazySdkMapper = JsonMapper.builder()
			                     .addModule(new ParameterNamesModule())
			                     .addModule(new Jdk8Module())
			                     .addModule(new JavaTimeModule())
			                     .build()
			                     .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
			                     .setSerializationInclusion(NON_NULL);
		}
		return lazySdkMapper;
	}

	public static ObjectMapper ABI_JSON_MAPPER() {
		if (lazyAbiMapper == null) {
			lazyAbiMapper = JsonMapper.builder()
			                                  .addModule(new ParameterNamesModule())
			                                  .addModule(new Jdk8Module())
			                                  .addModule(new JavaTimeModule())
			                                  .build()
			                                  .setSerializationInclusion(JsonInclude.Include.NON_NULL);
		}
		return lazyAbiMapper;
	}

}
