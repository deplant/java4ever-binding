package tech.deplant.java4ever.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.Context;
import tech.deplant.java4ever.binding.loader.JavaLibraryPathLoader;


@Log4j2
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

}
