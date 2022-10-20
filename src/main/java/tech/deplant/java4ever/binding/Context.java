package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.deplant.java4ever.binding.ffi.EverSdkBridge;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Class that represents the established environment inside EVER-SDK and
 * is identified by id number. Holds last request id and functions to
 * call SDK methods.
 */
public record Context(int id, ObjectMapper mapper, long timeout, AtomicInteger requestCount) {

	private final static System.Logger logger = System.getLogger(Context.class.getName());

	/**
	 * Constructor of EVER-SDK context
	 *
	 * @param id           number of context received from EVER-SDK, it's provided in contextBuilder.buildNew(loader). You can call it manually from EverSdkBridge.tcCreateContext(), but it's recommended to use ContextBuilder.
	 * @param requestCount it's 0 for new contexts or last request id for 'loaded' ones
	 * @param timeout      timeout for operations in milliseconds
	 * @param mapper       Jackson's ObjectMapper, ContextBuilder have correctly preconfigured one.
	 */
	public Context(int id,
	               int requestCount,
	               long timeout,
	               ObjectMapper mapper) {
		this(id, mapper, timeout, new AtomicInteger(requestCount));
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
	 * @param <T>
	 * @param <P>
	 * @param <E>
	 * @return
	 * @throws EverSdkException
	 */
	public <T, P, E extends ExternalEvent> T callEvent(String functionName,
	                                                   P params,
	                                                   Consumer<E> consumer,
	                                                   Class<T> clazz) throws EverSdkException {
		return call(functionName, params, clazz);
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
	public <T, P> T call(String functionName,
	                     P params,
	                     Class<T> clazz) throws EverSdkException {
		try {
			return this.mapper.readValue(processRequest(functionName, processParams(params)), clazz);
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
	public <P> void callVoid(String functionName,
	                         P params) throws EverSdkException {
		processRequest(functionName, processParams(params));
	}

	private <P> String processParams(P params) throws EverSdkException {
		try {
			return (null == params) ? "" : this.mapper.writeValueAsString(params);
		} catch (JsonProcessingException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "Parameters serialization failed!" + e.getMessage() + e.getCause());
			throw new EverSdkException(new EverSdkException.ErrorResult(-501,
			                                                            "Parameters serialization failed!"), e);
		}
	}

	private String processRequest(String functionName,
	                              String params) throws EverSdkException {
		final int requestId = requestCount().incrementAndGet();
		try {
			logger.log(System.Logger.Level.TRACE,
			           () -> "FUNC:" + functionName + " CTXID:" + id() + " REQID:" + requestId + " SEND:" + params);
			final String result = EverSdkBridge
					.tcRequest(id(), requestId, functionName, params)
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
				final var responseCopy = sdkResponse;
				if (responseCopy.data().localError() != null &&
				    responseCopy.data().localError().data().exitCode() > 0) {
					logger.log(System.Logger.Level.WARNING,
					           () -> "Error from SDK. Code: " + responseCopy.data().localError().code() +
					                 ", Message: " +
					                 responseCopy.data().localError().message());
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
			logger.log(System.Logger.Level.ERROR, () -> "EVER-SDK call interrupted!" + e.getCause());
			throw new EverSdkException(new EverSdkException.ErrorResult(-400, "EVER-SDK call interrupted!"), e);
		} catch (TimeoutException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "EVER-SDK Execution expired on Timeout! Current timeout: " + timeout() + " Message: " +
			                 e.getMessage());
			throw new EverSdkException(new EverSdkException.ErrorResult(-402,
			                                                            "EVER-SDK Execution expired on Timeout! Current timeout: " +
			                                                            timeout()), e);

		}

	}
}
