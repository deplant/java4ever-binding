package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.ffi.NativeMethods;
import tech.deplant.java4ever.binding.ffi.EverSdkContext;
import tech.deplant.java4ever.binding.loader.DefaultLoader;
import tech.deplant.java4ever.binding.loader.DefaultLoaderContext;
import tech.deplant.java4ever.binding.loader.LibraryLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EverSdk {

	public final static ScopedValue<EverSdkContext> CONTEXT = ScopedValue.newInstance();
	public final static String LOG_FORMAT = "CTX:%d REQ:%d FUNC:%s %s:%s";
	private final static System.Logger logger = System.getLogger(EverSdk.class.getName());
	private final static Map<Integer, EverSdkContext> contexts = new HashMap<>();

	public record ResultOfCreateContext(Integer result, String error) {
	}

	public static EverSdkContext getContext(int contextId) {
		return contexts.get(contextId);
	}

	public static void load() {
		load(DefaultLoaderContext.SINGLETON(ClassLoader.getSystemClassLoader()));
	}

	public static void load(LibraryLoader loader) {
		loader.load();
	}

	public static <T, P> T call(int ctxId,
	                            String functionName,
	                            P functionInputs,
	                            Class<T> outputClass) throws EverSdkException {
		return EverSdk.getContext(ctxId).call(functionName, functionInputs, outputClass);
	}

	public static <T, P> T callEvent(int ctxId, String functionName,
	                                 P params,
	                                 Consumer<JsonNode> consumer,
	                                 Class<T> resultClass) throws EverSdkException {
		return EverSdk.getContext(ctxId).callEvent(functionName, params, consumer, resultClass);
	}

	public static <P> void callVoid(int ctxId, String functionName, P params) throws EverSdkException {
		EverSdk.getContext(ctxId).callVoid(functionName, params);
	}

	public static <T, P, A> T callAppObject(int ctxId,
	                                        String functionName,
	                                 P params,
	                                 A appObject,
	                                 Class<T> clazz) throws EverSdkException {
		return EverSdk.getContext(ctxId).callAppObject(functionName, params, appObject, clazz);
	}

	public static Optional<Integer> createDefault() throws JsonProcessingException {
		return createWithConfig("{}");
	}

	public static Optional<Integer> createWithEndpoint(String endpoint) throws JsonProcessingException {
		return createWithConfig("{ \"network\":{ \"endpoints\": [\"%s\"] } }".formatted(endpoint));
	}

	public static Optional<Integer> createWithConfig(Client.ClientConfig config) throws JsonProcessingException {
		var mergedConfig = new Client.ClientConfig(new Client.BindingConfig(DefaultLoader.BINDING_LIBRARY_NAME,
		                                                                    DefaultLoader.BINDING_LIBRARY_VERSION),
		                                           config.network(),
		                                           config.crypto(),
		                                           config.abi(),
		                                           config.boc(),
		                                           config.proofs(),
		                                           config.localStoragePath());
		var mergedJson = JsonContext.SDK_JSON_MAPPER().writeValueAsString(mergedConfig);
		//logger.log(System.Logger.Level.TRACE,
		//           () -> "FUNC:tc_create_context JSON:%s".formatted(configJson));
		final var createContextResponse = JsonContext.SDK_JSON_MAPPER()
		                                             .readValue(NativeMethods.tcCreateContext(mergedJson),
		                                                        ResultOfCreateContext.class);
		Optional<Integer> contextId = Optional.ofNullable(createContextResponse.result());
		if (contextId.isEmpty() || contextId.get() < 1) {
			logger.log(System.Logger.Level.ERROR, "sdk.create_context failed!");
		} else {
			int ctxId = contextId.get();
			contexts.put(ctxId, new EverSdkContext(ctxId, mergedConfig));
			logger.log(System.Logger.Level.TRACE,
			           () -> "FUNC:tc_create_context CTX:%d".formatted(ctxId));
		}
		return contextId;
	}

	public static Optional<Integer> createWithConfig(String configJson) throws JsonProcessingException {
		return createWithConfig(JsonContext.SDK_JSON_MAPPER().readValue(configJson, Client.ClientConfig.class));
	}
}
