package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.ffi.EverSdkContext;
import tech.deplant.java4ever.binding.ffi.NativeMethods;
import tech.deplant.java4ever.binding.loader.DefaultLoader;
import tech.deplant.java4ever.binding.loader.DefaultLoaderContext;
import tech.deplant.java4ever.binding.loader.LibraryLoader;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class EverSdk {
	public final static String LOG_FORMAT = "CTX:%d REQ:%d FUNC:%s %s:%s";
	private final static System.Logger logger = System.getLogger(EverSdk.class.getName());
	private final static Map<Integer, EverSdkContext> contexts = new ConcurrentHashMap<>();
	private final static long timeout = 600_000L;

	public static Client.ClientConfig contextConfig(int contextId) {
		return contexts.get(contextId).config();
	}

	public static long getDefaultWorkchainId(int contextId) {
		return switch (contextConfig(contextId).abi()) {
			case Client.AbiConfig abiConfig -> Objects.requireNonNullElse(abiConfig.workchain(), 0L);
			case null -> 0L;
		};
	}

	public static void load() {
		load(DefaultLoaderContext.SINGLETON(ClassLoader.getSystemClassLoader()));
	}

	public static void load(LibraryLoader loader) {
		loader.load();
	}

	public static <P> CompletableFuture<Void> asyncVoid(final int contextId,
	                                                    final String functionName,
	                                                    final P functionInputs) throws EverSdkException {
		return contexts.get(contextId).callAsync(functionName, functionInputs, Void.class, null, null);
	}


	public static <T, P> CompletableFuture<T> async(final int contextId,
	                                                final String functionName,
	                                                final P functionInputs,
	                                                final Class<T> outputClass) throws EverSdkException {
		return contexts.get(contextId).callAsync(functionName, functionInputs, outputClass, null, null);
	}

	public static <T, P> CompletableFuture<T> asyncCallback(final int contextId,
	                                                        final String functionName,
	                                                        final P functionInputs,
	                                                        final Class<T> outputClass,
	                                                        Consumer<JsonNode> eventConsumer) throws EverSdkException {
		return contexts.get(contextId).callAsync(functionName, functionInputs, outputClass, eventConsumer, null);
	}

	public static <T, P> CompletableFuture<T> asyncAppObject(final int contextId,
	                                                         final String functionName,
	                                                         final P functionInputs,
	                                                         final Class<T> outputClass,
	                                                         AppObject appObject) throws EverSdkException {
		return contexts.get(contextId).callAsync(functionName, functionInputs, outputClass, null, appObject);
	}

	public static void destroy(int contextId) {
		NativeMethods.tcDestroyContext(contextId);
	}

	public static int createDefault() throws EverSdkException {
		return createWithJson("{}");
	}

	public static Builder builder() {
		return new Builder();
	}

	public static int createWithEndpoint(String endpoint) throws EverSdkException {
		return createWithJson("{ \"network\":{ \"endpoints\": [\"%s\"] } }".formatted(endpoint));
	}

	public static int createWithConfig(Client.ClientConfig config) throws EverSdkException {
		var mergedConfig = new Client.ClientConfig(new Client.BindingConfig(DefaultLoader.BINDING_LIBRARY_NAME,
		                                                                    DefaultLoader.BINDING_LIBRARY_VERSION),
		                                           config.network(),
		                                           config.crypto(),
		                                           config.abi(),
		                                           config.boc(),
		                                           config.proofs(),
		                                           config.localStoragePath());
		String resultString = "";
		ResultOfCreateContext createContextResponse;
		try {
			String mergedJson = JsonContext.SDK_JSON_MAPPER().writeValueAsString(mergedConfig);
			try {
				resultString = NativeMethods.tcCreateContext(mergedJson);
				createContextResponse = JsonContext.SDK_JSON_MAPPER()
				                                   .readValue(resultString,
				                                              ResultOfCreateContext.class);
				Optional<Integer> contextId = Optional.ofNullable(createContextResponse.result());
				if (contextId.isEmpty() || contextId.get() < 1) {
					logger.log(System.Logger.Level.ERROR, () -> "FUNC:sdk.tc_create_context result is empty!");
					throw new EverSdkException(new EverSdkException.ErrorResult(-502,
					                                                            "FUNC:sdk.tc_create_context result is empty!"));
				}
				int ctxId = contextId.get();
				contexts.put(ctxId, new EverSdkContext(ctxId, mergedConfig));
				logger.log(System.Logger.Level.TRACE,
				           () -> "FUNC:sdk.tc_create_context CTX:%d JSON:%s".formatted(ctxId, mergedJson));
				return ctxId;
			} catch (JsonProcessingException e) {
				final String finalResultString = resultString;
				logger.log(System.Logger.Level.ERROR,
				           () -> "FUNC:sdk.tc_create_context request deserialization failed! Exception: %s Result: %s".formatted(
						           e,
						           finalResultString));
				throw new EverSdkException(new EverSdkException.ErrorResult(-501,
				                                                            "FUNC:sdk.tc_create_context request deserialization failed!"),
				                           e.getCause());
			}
		} catch (JsonProcessingException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "EVER-SDK tc_create_context request serialization failed! Exception: %s Config: %s".formatted(
					           e,
					           mergedConfig));
			throw new EverSdkException(new EverSdkException.ErrorResult(-502,
			                                                            "EVER-SDK tc_create_context request serialization failed!"),
			                           e.getCause());
		}
	}

	public static int createWithJson(String configJson) throws EverSdkException {
		try {
		return createWithConfig(JsonContext.SDK_JSON_MAPPER().readValue(configJson, Client.ClientConfig.class));
		} catch (JsonProcessingException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "EVER-SDK tc_create_context request serialization failed! Exception: %s Config: %s".formatted(
					           e,
					           configJson));
			throw new EverSdkException(new EverSdkException.ErrorResult(-502,
			                                                            "EVER-SDK tc_create_context request serialization failed!"),
			                           e.getCause());
		}
	}

	public static <T> T await(CompletableFuture<T> functionOutputs) throws EverSdkException {
		try {
			return functionOutputs.get(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException ex3) {
			logger.log(System.Logger.Level.ERROR, () -> "EVER-SDK Call interrupted! %s".formatted(ex3.toString()));
			throw new EverSdkException(new EverSdkException.ErrorResult(-400, "EVER-SDK call interrupted!"),
			                           ex3.getCause());
		} catch (TimeoutException ex4) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "EVER-SDK Call expired on Timeout! %s".formatted(ex4.toString()));
			throw new EverSdkException(new EverSdkException.ErrorResult(-408, "EVER-SDK call expired on Timeout!"),
			                           ex4.getCause());
		} catch (ExecutionException e) {
			if (e.getCause() instanceof EverSdkException everEx) {
				throw everEx;
			} else {
				logger.log(System.Logger.Level.ERROR,
				           () -> "EVER-SDK Call unknown execution exception! %s".formatted(e.toString()));
				throw new EverSdkException(new EverSdkException.ErrorResult(-500, "EVER-SDK call expired on Timeout!"),
				                           e.getCause());
			}
		}
	}

	public record ResultOfCreateContext(Integer result, String error) {
	}

	public static class Builder {

		private Boolean cacheInLocalStorage = null; // true
		private String localStoragePath = null; // "~/.tonclient"

		//Context.NetworkConfig
		private String[] endpoints = null; // "https://localhost";
		@Deprecated private String serverAddress; // deprecated, use endpoints
		private Integer networkRetriesCount = null; // 5;
		private Integer messageRetriesCount = null; // 5;
		private Long messageProcessingTimeout = null; // 40000L;
		private Long waitForTimeout = null; // 40000L;
		private Long outOfSyncThreshold = null; // 15000L;
		private Long reconnectTimeout = null; // 12000L;
		private String accessKey = null; //
		//Context.CryptoConfig
		private Crypto.MnemonicDictionary mnemonicDictionary = null; // Crypto.MnemonicDictionary.English;
		private Integer mnemonicWordCount = null; // 12;
		private String hdkeyDerivationPath = null; // "m/44'/396'/0'/0/0";
		//Context.AbiConfig;
		private Long workchain = null; // 0L;
		private Long messageExpirationTimeout = null; // 40000L;
		private Long messageExpirationTimeoutGrowFactor = null; // null;
		private Long cacheMaxSize = null; // 10240L;
		private Long maxReconnectTimeout = null; // 120000L;
		private Integer sendingEndpointCount = null; // 1;
		private Long latencyDetectionInterval = null; // 60000L;
		private Long maxLatency = null; // 60000L;
		private Long queryTimeout = null; // 60000L;
		private Client.NetworkQueriesProtocol queriesProtocol = null; // Client.NetworkQueriesProtocol.HTTP;
		private Long firstRempStatusTimeout = null; // 1000L;
		private Long nextRempStatusTimeout = null; // 5000L;
		private Long signatureId = null;

		public Builder() {
		}

		public Builder proofsCacheInLocalStorage(boolean cacheInLocalStorage) {
			this.cacheInLocalStorage = cacheInLocalStorage;
			return this;
		}

		public Builder networkMaxReconnectTimeout(Long maxReconnectTimeout) {
			this.maxReconnectTimeout = maxReconnectTimeout;
			return this;
		}

		public Builder networkSendingEndpointCount(Integer sendingEndpointCount) {
			this.sendingEndpointCount = sendingEndpointCount;
			return this;
		}

		public Builder networkLatencyDetectionInterval(Long latencyDetectionInterval) {
			this.latencyDetectionInterval = latencyDetectionInterval;
			return this;
		}

		public Builder networkMaxLatency(Long maxLatency) {
			this.maxLatency = maxLatency;
			return this;
		}

		public Builder networkQueryTimeout(Long queryTimeout) {
			this.queryTimeout = queryTimeout;
			return this;
		}

		public Builder networkQueriesProtocol(Client.NetworkQueriesProtocol queriesProtocol) {
			this.queriesProtocol = queriesProtocol;
			return this;
		}

		public Builder networkFirstRempStatusTimeout(Long firstRempStatusTimeout) {
			this.firstRempStatusTimeout = firstRempStatusTimeout;
			return this;
		}

		public Builder networkNextRempStatusTimeout(Long nextRempStatusTimeout) {
			this.nextRempStatusTimeout = nextRempStatusTimeout;
			return this;
		}

		public Builder networkEndpoints(String... endpoints) {
			this.endpoints = endpoints;
			return this;
		}

		public Builder networkServerAddress(String server_address) {
			this.serverAddress = server_address;
			return this;
		}

		public Builder networkRetriesCount(Integer network_retries_count) {
			this.networkRetriesCount = network_retries_count;
			return this;
		}

		public Builder networkMessageRetriesCount(Integer message_retries_count) {
			this.messageRetriesCount = message_retries_count;
			return this;
		}

		public Builder networkMessageProcessingTimeout(Long message_processing_timeout) {
			this.messageProcessingTimeout = message_processing_timeout;
			return this;
		}

		public Builder networkWaitForTimeout(Long wait_for_timeout) {
			this.waitForTimeout = wait_for_timeout;
			return this;
		}

		public Builder networkOutOfSyncThreshold(Long out_of_sync_threshold) {
			this.outOfSyncThreshold = out_of_sync_threshold;
			return this;
		}

		public Builder networkReconnectTimeout(Long reconnect_timeout) {
			this.reconnectTimeout = reconnect_timeout;
			return this;
		}

		public Builder networkSignatureId(Long signatureId) {
			this.signatureId = signatureId;
			return this;
		}

		public Builder networkAccessKey(String access_key) {
			this.accessKey = access_key;
			return this;
		}

		//cripto
		public Builder cryptoMnemonicDictionary(Crypto.MnemonicDictionary mnemonic_dictionary) {
			this.mnemonicDictionary = mnemonic_dictionary;
			return this;
		}

		public Builder cryptoMnemonicWordCount(Integer mnemonic_word_count) {
			this.mnemonicWordCount = mnemonic_word_count;
			return this;
		}

		public Builder cryptoHdkeyDerivationPath(String hdkey_derivation_path) {
			this.hdkeyDerivationPath = hdkey_derivation_path;
			return this;
		}

		//abi
		public Builder abiWorkchain(Long workchain) {
			this.workchain = workchain;
			return this;
		}

		public Builder abiMessageExpirationTimeout(Long message_expiration_timeout) {
			this.messageExpirationTimeout = message_expiration_timeout;
			return this;
		}

		public Builder abiMessageExpirationTimeoutGrowFactor(Long message_expiration_timeout_grow_factor) {
			this.messageExpirationTimeoutGrowFactor = message_expiration_timeout_grow_factor;
			return this;
		}

		public Builder bocCacheMaxSize(Long cacheMaxSize) {
			this.cacheMaxSize = cacheMaxSize;
			return this;
		}

		private Client.NetworkConfig buildNetworkConfig() {
			if (this.serverAddress == null && this.endpoints == null && this.networkRetriesCount == null &&
			    this.maxReconnectTimeout == null && this.reconnectTimeout == null && this.messageRetriesCount == null &&
			    this.messageProcessingTimeout == null && this.waitForTimeout == null &&
			    this.outOfSyncThreshold == null && this.sendingEndpointCount == null &&
			    this.latencyDetectionInterval == null && this.maxLatency == null && this.queryTimeout == null &&
			    this.queriesProtocol == null && this.firstRempStatusTimeout == null &&
			    this.nextRempStatusTimeout == null && this.signatureId == null && this.accessKey == null) {
				return null;
			} else {
				return new Client.NetworkConfig(this.serverAddress,
				                                this.endpoints,
				                                this.networkRetriesCount,
				                                this.maxReconnectTimeout,
				                                this.reconnectTimeout,
				                                this.messageRetriesCount,
				                                this.messageProcessingTimeout,
				                                this.waitForTimeout,
				                                this.outOfSyncThreshold,
				                                this.sendingEndpointCount,
				                                this.latencyDetectionInterval,
				                                this.maxLatency,
				                                this.queryTimeout,
				                                this.queriesProtocol,
				                                this.firstRempStatusTimeout,
				                                this.nextRempStatusTimeout,
				                                this.signatureId,
				                                this.accessKey);
			}
		}

		private Client.CryptoConfig buildCryptoConfig() {
			if (this.mnemonicDictionary == null && this.mnemonicWordCount == null && this.hdkeyDerivationPath == null) {
				return null;
			} else {
				return new Client.CryptoConfig(this.mnemonicDictionary,
				                               this.mnemonicWordCount,
				                               this.hdkeyDerivationPath);
			}
		}

		private Client.AbiConfig buildAbiConfig() {
			if (this.workchain == null && this.messageExpirationTimeout == null &&
			    this.messageExpirationTimeoutGrowFactor == null) {
				return null;
			} else {
				return new Client.AbiConfig(this.workchain,
				                            this.messageExpirationTimeout,
				                            this.messageExpirationTimeoutGrowFactor);
			}
		}

		private Client.BocConfig buildBocConfig() {
			if (this.cacheMaxSize == null) {
				return null;
			} else {
				return new Client.BocConfig(this.cacheMaxSize);
			}
		}

		private Client.ProofsConfig buildProofsConfig() {
			if (this.cacheInLocalStorage == null) {
				return null;
			} else {
				return new Client.ProofsConfig(this.cacheInLocalStorage);
			}
		}

		public int build() throws EverSdkException {
			var config = new Client.ClientConfig(new Client.BindingConfig("java4ever", "3.0.0"),
			                                     buildNetworkConfig(),
			                                     buildCryptoConfig(),
			                                     buildAbiConfig(),
			                                     buildBocConfig(),
			                                     buildProofsConfig(),
			                                     this.localStoragePath);
			return createWithConfig(config);
		}

	}

}
