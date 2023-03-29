package tech.deplant.java4ever.test.unit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.generator.ParserEngine;
import tech.deplant.java4ever.binding.generator.reference.ApiReference;
import tech.deplant.java4ever.binding.io.JsonResource;
import tech.deplant.java4ever.binding.loader.AbsolutePathLoader;
import tech.deplant.java4ever.utils.regex.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class GeneratorTests {


	private final static System.Logger logger = System.getLogger(GeneratorTests.class.getName());

	@Test
	public void parsed_api_json_should_be_equal_to_unparsed() throws JsonProcessingException {
		var jsonStr = new JsonResource("api.json").get();

		var reference = ContextBuilder.DEFAULT_MAPPER.readValue(jsonStr, ApiReference.class);
		var cachedStr = ContextBuilder.DEFAULT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL)
		                                             .writeValueAsString(reference);
		assertEquals(ContextBuilder.DEFAULT_MAPPER.readTree(jsonStr),
		             ContextBuilder.DEFAULT_MAPPER.readTree(cachedStr));
	}

	@Test
	public void parsed_api_from_client() throws JsonProcessingException, EverSdkException {
		var apiReference = ParserEngine.ofEverSdkLibrary(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
	}

	@Test
	public void generate_api_from_json() throws IOException, EverSdkException {
		//var apiReference = ParserEngine.ofEverSdkLibrary(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
		var apiReference = ParserEngine.ofJsonResource("api.json");
		ParserEngine.parse(apiReference);
	}

	@Test
	public void process_links() {
		String description = "Generates a key pair for signing from the secret key **NOTE:** " +
		                     "In the result the secret key is actually " +
		                     "the concatenation of secret and public keys (128 symbols hex string) " +
		                     "by design of [NaCL](http://nacl.cr.yp.to/sign.html). " +
		                     "\nSee also [the stackexchange question](https://crypto.stackexchange.com/questions/54353/).";
		var expr = new Then(
				new Symbol('['),
				new NotAnyOf(new Word("[]()")),
				Special.PLUS,
				new Symbol(']'),
				new Symbol('('),
				new NotAnyOf(new Word("[]()")),
				Special.PLUS,
				new Symbol(')'));

		String processedDescription = description;
		if (description != null) {
			var matcher = expr.toPattern().matcher(description);
			while (matcher.find()) {
				logger.log(System.Logger.Level.DEBUG, matcher.group());
				processedDescription = description.replace(matcher.group(), "{@link " + matcher.group() + "}");
			}
			logger.log(System.Logger.Level.DEBUG, processedDescription);
		}
	}

}
