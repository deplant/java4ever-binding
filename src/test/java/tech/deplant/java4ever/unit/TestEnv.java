package tech.deplant.java4ever.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.EverSdk;

public class TestEnv {
	public static final String NODESE_URL = "https://nodese.truequery.tech";
	public static final String NODESE_ENDPOINT = "https://nodese.truequery.tech/graphql";

	static int newContext() {
		try {
			return EverSdk.createWithEndpoint(NODESE_ENDPOINT).orElseThrow();
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
