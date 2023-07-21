package tech.deplant.java4ever.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import tech.deplant.java4ever.binding.*;

import java.util.Map;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class SubscribeTests {

	private final static System.Logger logger = System.getLogger(SubscribeTests.class.getName());

	@Test
	public void subscribe_to_account() throws JsonProcessingException, EverSdkException, InterruptedException {
		var endpoint = "http://185.20.226.96/graphql";
		var configJson = "{\"network\":{\"endpoints\":[\"" + endpoint + "\"]}}";
		var ctx = EverSdkContext.builder().setConfigJson(configJson).buildNew();
		Client.version(ctx);

		String queryText = """
				subscription {
							transactions(
									filter: {
										account_addr: { in: ["%s"] }
									}
				                ) {
								id
								account_addr
								balance_delta
							}
						}
				""".formatted("0:856f54b9126755ce6ecb7c62b7ad8c94353f7797c03ab82eda63d11120ed3ab7");

		logger.log(System.Logger.Level.DEBUG,queryText);

		Net.subscribe(ctx,
		              queryText,
		              JsonContext.SDK_JSON_MAPPER().valueToTree(Map.of()),
		              handler -> logger.log(System.Logger.Level.WARNING,
		                                    "code: %d, msg: %s".formatted(handler.responseType(), handler.params())));
	}
}
