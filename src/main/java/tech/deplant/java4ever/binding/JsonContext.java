package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class JsonContext {

	private static ObjectMapper lazySdkMapper;
	private static ObjectMapper lazyAbiMapper;

	private static final TypeReference<Map<String, Object>> MAP_STRING_OBJECT_TYPE = new TypeReference<Map<String, Object>>(){};

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

	public static Map<String, Object> readAsMap(ObjectMapper mapper, String json) throws JsonProcessingException {
		return mapper.readValue(json, MAP_STRING_OBJECT_TYPE);
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
