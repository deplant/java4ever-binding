package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import tech.deplant.java4ever.binding.ffi.EverSdkBridge;
import tech.deplant.java4ever.binding.loader.LibraryLoader;

import java.lang.foreign.MemorySession;

/**
 * Builder to correctly request and create Context object
 */
public class ContextBuilder {

	public static final ObjectMapper DEFAULT_MAPPER = JsonMapper.builder() // or different mapper for other format
	                                                            .addModule(new ParameterNamesModule())
	                                                            .addModule(new Jdk8Module())
	                                                            .addModule(new JavaTimeModule())
	                                                            // and possibly other configuration, modules, then:
	                                                            .build()
	                                                            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
	                                                            .registerModule(new RecordNamingStrategyPatchModule());

	private long timeout = 60_000L;
	private String configJson = "{}";

	private ObjectMapper jsonMapper = DEFAULT_MAPPER;

	public ContextBuilder() {
	}

	public ContextBuilder setConfigJson(String configJson) {
		this.configJson = configJson;
		return this;
	}

	/**
	 * Sets timeout for EVER-SDK responses
	 *
	 * @param timeout Response completion waiting timeout (in milliseconds)
	 * @return instance of builder
	 */
	public ContextBuilder setTimeout(long timeout) {
		this.timeout = timeout;
		return this;
	}

	public ContextBuilder setMapper(ObjectMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
		return this;
	}

	/**
	 * If you, for some reason, can't directly access already created Context object, but you're sure that
	 * it is created, loaded and you know it's id and last request count, you should use this method to
	 * load Context object out of this data.
	 * It's up to you if id and request count are correct, it will not be checked until you start calls with Context.
	 *
	 * @param existingContextId           id of existing, previously created Context in EVER-SDK
	 * @param existingContextRequestCount last request number of previously created Context in EVER-SDK
	 * @return Context object made of provided data
	 */
	public Context buildFromExisting(int existingContextId, int existingContextRequestCount) {
		return new Context(existingContextId, existingContextRequestCount, this.timeout, this.jsonMapper);
	}

	/**
	 * Terminal builder method to create new Context
	 *
	 * @param loader Specify how exactly ton_client library will be found and loaded
	 * @return Context object that can be used in calls to EVER-SDK, ton_client library is loaded and called in the process
	 * @throws JsonProcessingException
	 */
	public Context buildNew(LibraryLoader loader) throws JsonProcessingException {
		loader.load();
		try (MemorySession scope = MemorySession.openShared()) {
			final String response = EverSdkBridge.tcCreateContext(scope, this.configJson);
			final var createContextResponse = this.jsonMapper.readValue(response,
			                                                            ContextBuilder.ResultOfCreateContext.class);
			if (createContextResponse.result() == null || createContextResponse.result() < 1) {
				throw new RuntimeException("sdk.create_context failed!");
			}
			return new Context(createContextResponse.result(), 0, this.timeout, this.jsonMapper);
		}

	}

	public record ResultOfCreateContext(Integer result, String error) {
	}
}
