package tech.deplant.java4ever.unit;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Execution(ExecutionMode.CONCURRENT)
public class GqlTests {

	@Test
	public void make_query() throws IOException, InterruptedException {
		var client = HttpClient.newHttpClient();
		// {"Status":"Success","Data":{"AuthToken":"0ee3b982-f3a5-479e-ae7d-18a457fba20f","ExpirationDateUtc":"2023-09-19T08:08:29"}}
		var request = HttpRequest.newBuilder()
		                         .uri(URI.create("https://ferma-test.ofd.ru/api/kkt/cloud/stats/fn/aggregates?dateFrom=2019-08-24T14:15:22Z%22%3B%20truncate%20table%20receipts_aggrs_fn_last%3B&dateto=2019-08-24T14:15:22Z&AuthToken=0ee3b982-f3a5-479e-ae7d-18a457fba20f"))
		                         .GET()
//		(HttpRequest.BodyPublishers.ofString(
//                                                                         """
//                                                                         {
//		                                                                       "Login": "fermatest1",
//		                                                                       "Password": "Hjsf3321klsadfAA"
//		                                                                 }
//		                                                                 """))
		                         .version(HttpClient.Version.HTTP_1_1)
		                         .header("Content-Type", "application/json")
		                         .header("Accept", "application/json")
		                         .build();
		var response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());

	}


}
