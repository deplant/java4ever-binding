package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import tech.deplant.java4ever.binding.ffi.SdkBridge;
import tech.deplant.java4ever.binding.loader.DefaultLoader;
import tech.deplant.java4ever.binding.loader.DefaultLoaderContext;
import tech.deplant.java4ever.binding.loader.LibraryLoader;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Class that represents the established environment inside EVER-SDK and
 * is identified by id number. Holds last request id and functions to
 * call SDK methods.
 */
public record EverSdkContext(int id, @JsonIgnore ObjectMapper mapper, long timeout, AtomicInteger requestCount) {

	private final static System.Logger logger = System.getLogger(EverSdkContext.class.getName());


	/**
	 * Constructor of EVER-SDK context
	 *
	 * @param id           number of context received from EVER-SDK, it's provided in contextBuilder.buildNew(loader). You can call it manually from EverSdkBridge.tcCreateContext(), but it's recommended to use ContextBuilder.
	 * @param requestCount it's 0 for new contexts or last request id for 'loaded' ones
	 * @param timeout      timeout for operations in milliseconds
	 * @param mapper       Jackson's ObjectMapper, ContextBuilder have correctly preconfigured one.
	 */
	public EverSdkContext(int id, int requestCount, long timeout, ObjectMapper mapper) {
		this(id, mapper, timeout, new AtomicInteger(requestCount));
	}

	public static EverSdkContext.Builder builder() {
		return new EverSdkContext.Builder();
	}

	/**
	 * Call for methods that use app_object as one of params
	 *
	 * @param functionName
	 * @param params
	 * @param appObject
	 * @param clazz
	 * @param <T>
	 * @param <P>
	 * @param <A>
	 * @return
	 * @throws EverSdkException
	 */
	public <T, P, A> T callAppObject(String functionName,
	                                 P params,
	                                 A appObject,
	                                 Class<T> clazz) throws EverSdkException {
		return call(functionName, params, clazz);
	}

	/**
	 * Call that uses event as consumer param
	 *
	 * @param functionName
	 * @param params
	 * @param consumer
	 * @param clazz
	 * @param <T> type of function result
	 * @param <P> type of function params
	 * @return
	 * @throws EverSdkException
	 */
	public <T, P> T callEvent(String functionName,
	                          P params,
	                          Consumer<CallbackHandler> consumer,
	                          Class<T> clazz) throws EverSdkException {
		try {
			return this.mapper.readValue(processRequest(functionName, processParams(params), consumer), clazz);
		} catch (JsonProcessingException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "Successful response deserialization failed!" + e.getMessage() + e.getCause());
			throw new EverSdkException(new EverSdkException.ErrorResult(-500,
			                                                            "Successful response deserialization failed! Check getCause() for actual response."),
			                           e);
		}
	}

	/**
	 * Most used call to EVER-SDK with some output object
	 *
	 * @param functionName
	 * @param params       record of input type, usually ParamsOf...
	 * @param clazz        class of output type record, usually ResultOf...class
	 * @param <T>
	 * @param <P>
	 * @return output type record, usually ResultOf...
	 * @throws EverSdkException
	 */
	public <T, P> T call(String functionName, P params, Class<T> clazz) throws EverSdkException {
		try {
			return this.mapper.readValue(processRequest(functionName, processParams(params), null), clazz);
		} catch (JsonProcessingException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "Successful response deserialization failed!" + e.getMessage() + e.getCause());
			throw new EverSdkException(new EverSdkException.ErrorResult(-500,
			                                                            "Successful response deserialization failed! Check getCause() for actual response."),
			                           e);
		}
	}

	/**
	 * Calls to EVER-SDK without outputs
	 *
	 * @param functionName
	 * @param params
	 * @param <P>
	 * @throws EverSdkException
	 */
	public <P> void callVoid(String functionName, P params) throws EverSdkException {
		processRequest(functionName, processParams(params), null);
	}

	private <P> String processParams(P params) throws EverSdkException {
		try {
			return (null == params) ? "" : this.mapper.writeValueAsString(params);
		} catch (JsonProcessingException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "Parameters serialization failed!" + e.getMessage() + e.getCause());
			throw new EverSdkException(new EverSdkException.ErrorResult(-501, "Parameters serialization failed!"), e);
		}
	}

	private String processRequest(String functionName, String params, Consumer<CallbackHandler> consumer) throws EverSdkException {
		final int requestId = requestCount().incrementAndGet();
		try {
			logger.log(System.Logger.Level.TRACE,
			           () -> "FUNC:" + functionName + " CTXID:" + id() + " REQID:" + requestId + " SEND:" + params);
			final String result = SdkBridge.tcRequest(id(), requestId, functionName, params, consumer)
			                               .result()
			                               .get(timeout(), TimeUnit.MILLISECONDS);
			logger.log(System.Logger.Level.TRACE,
			           () -> "FUNC: " + functionName + " CTXID:" + id() + " REQID:" + requestId + " RESP:" + result);
			return result;
		} catch (CompletionException | ExecutionException e) {
			// These errors are sent by SDK, response_type=1
			EverSdkException.ErrorResult sdkResponse = null;
			// let's try to parse error response
			try {
				sdkResponse = mapper().readValue(e.getCause().getMessage(), EverSdkException.ErrorResult.class);
				// in case of contract custom exit code ("require" error)
				final EverSdkException.ErrorResult responseCopy = sdkResponse;
				if (responseCopy.data().localError() != null &&
				    responseCopy.data().localError().data().exitCode() > 0) {
					logger.log(System.Logger.Level.WARNING,
					           () -> "Error from SDK. Code: " + responseCopy.data().localError().code() +
					                 ", Message: " + responseCopy.data().localError().message());
					throw new EverSdkException(new EverSdkException.ErrorResult(responseCopy.data()
					                                                                        .localError()
					                                                                        .data()
					                                                                        .exitCode(),
					                                                            "Contract did not accept message. For more information about exit code check the contract source code or ask the contract developer",
					                                                            responseCopy.data()
					                                                                        .localError()
					                                                                        .data()), e);
				} else { // on other errors, we just re-throw
					logger.log(System.Logger.Level.WARNING,
					           () -> "Error from SDK. Code: " + responseCopy.code() + ", Message: " +
					                 responseCopy.message());
					throw new EverSdkException(responseCopy, e);
				}

			} catch (JsonProcessingException ex) {
				// if error response parsing failed
				logger.log(System.Logger.Level.ERROR,
				           () -> "SDK Error Response deserialization failed! Response: " + e.getCause().getMessage() +
				                 ex.getMessage());
				throw new EverSdkException(new EverSdkException.ErrorResult(-500,
				                                                            "SDK Error Response deserialization failed! Check getCause() for actual response."),
				                           ex);
			}
		} catch (InterruptedException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "FUNC:" + functionName + " CTXID:" + id() + " REQID:" + requestId + " ERR: INTERRUPTED! " +
			                 params + e.getCause() + " " + e.getMessage() + " " + e);
			throw new EverSdkException(new EverSdkException.ErrorResult(-400, "EVER-SDK call interrupted!"), e);
		} catch (TimeoutException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "FUNC:" + functionName + " CTXID:" + id() + " REQID:" + requestId +
			                 " ERR: TIMEOUT! LIMIT: " + timeout() + " Message: " + e.getMessage());
			throw new EverSdkException(new EverSdkException.ErrorResult(-402,
			                                                            "EVER-SDK Execution expired on Timeout! Current timeout: " +
			                                                            timeout()), e);

		}

	}

	/**
	 * Builder to correctly request and create Context object
	 */
	public static class Builder {

		//.registerModule(new RecordNamingStrategyPatchModule());
		private long timeout = 60_000L;
		private String configJson = "{}";
		private ObjectMapper jsonMapper;

		public Builder() {
		}

		public Builder setConfigJson(String configJson) {
			this.configJson = configJson;
			return this;
		}

		/**
		 * Sets timeout for EVER-SDK responses
		 *
		 * @param timeout Response completion waiting timeout (in milliseconds)
		 * @return instance of builder
		 */
		public Builder setTimeout(long timeout) {
			this.timeout = timeout;
			return this;
		}

		public Builder setMapper(ObjectMapper jsonMapper) {
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
		public EverSdkContext buildFromExisting(int existingContextId, int existingContextRequestCount) {
			return new EverSdkContext(existingContextId, existingContextRequestCount, this.timeout, this.jsonMapper);
		}

		/**
		 * Terminal builder method to create new Context
		 *
		 * @param loader Specify how exactly ton_client library will be found and loaded
		 * @return Context object that can be used in calls to EVER-SDK, ton_client library is loaded and called in the process
		 * @throws JsonProcessingException
		 */
		public EverSdkContext buildNew(LibraryLoader loader) throws JsonProcessingException {
			if (this.jsonMapper == null) {
				this.jsonMapper = JsonContext.SDK_JSON_MAPPER();
			}
			var defaults = this.jsonMapper.readValue(this.configJson, Client.ClientConfig.class);
			var mergedConfig = new Client.ClientConfig(new Client.BindingConfig(DefaultLoader.BINDING_LIBRARY_NAME, DefaultLoader.BINDING_LIBRARY_VERSION),
			                                           defaults.network(),
			                                           defaults.crypto(),
			                                           defaults.abi(),
			                                           defaults.boc(),
			                                           defaults.proofs(),
			                                           defaults.localStoragePath());
			var mergedJson = this.jsonMapper.writeValueAsString(mergedConfig);
			final var createContextResponse = this.jsonMapper.readValue(SdkBridge.tcCreateContext(loader, mergedJson),
			                                                            ResultOfCreateContext.class);
			if (createContextResponse.result() == null || createContextResponse.result() < 1) {
				throw new RuntimeException("sdk.create_context failed!");
			}
			return new EverSdkContext(createContextResponse.result(), 0, this.timeout, this.jsonMapper);
		}

		/**
		 * Terminal builder method to create new Context. Uses default loader with EVER-SDK for autodetected
		 * OS & processor arch.
		 *
		 * @return Context object that can be used in calls to EVER-SDK, ton_client library is loaded and called in the process
		 * @throws JsonProcessingException
		 */
		public EverSdkContext buildNew() throws JsonProcessingException {
			return buildNew(DefaultLoaderContext.SINGLETON(this.getClass().getClassLoader()));
		}

		public record ResultOfCreateContext(Integer result, String error) {
		}
	}
}
