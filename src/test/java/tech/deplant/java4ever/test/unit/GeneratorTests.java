package tech.deplant.java4ever.test.unit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.generator.ParserEngine;
import tech.deplant.java4ever.binding.generator.reference.ApiReference;
import tech.deplant.java4ever.binding.io.JsonResource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class GeneratorTests {

	@Test
	public void parsed_api_json_should_be_equal_to_unparsed() throws JsonProcessingException {
		var jsonStr = new JsonResource("api.json").get();

		var reference = ContextBuilder.DEFAULT_MAPPER.readValue(jsonStr, ApiReference.class);
		var cachedStr = ContextBuilder.DEFAULT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL)
		                                             .writeValueAsString(reference);
		System.out.println(cachedStr);
		//assertEquals(ContextBuilder.DEFAULT_MAPPER.readTree(jsonStr),
		//             ContextBuilder.DEFAULT_MAPPER.readTree(cachedStr));
	}

	@Test
	public void generate_api() throws JsonProcessingException {
		ParserEngine.parse();
		//System.out.println(helloWorld.toString());
	}

}
