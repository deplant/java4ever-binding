package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.ffi.EverSdkBridge;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * Class that represents the established environment inside EVER-SDK and
 * is identified by id number. Holds last request id and functions to
 * call SDK methods.
 */
public final class Context {


	private final static Logger log = LoggerFactory.getLogger(Context.class);
	private final int id;
	private final ObjectMapper mapper;

	private final long timeout;

	private int requestCount;

	/**
	 * Constructo of EVER-SDK context
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
		this.id = id;
		this.requestCount = requestCount;
		this.timeout = timeout;
		this.mapper = mapper;
	}

	public ObjectMapper mapper() {
		return this.mapper;
	}

	public long timeout() {
		return this.timeout;
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
			log.error("Successful response deserialization failed!" + e.getMessage() + e.getCause());
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
			log.error("Parameters serialization failed!" + e.getMessage() + e.getCause());
			throw new EverSdkException(new EverSdkException.ErrorResult(-501,
			                                                            "Parameters serialization failed!"), e);
		}
	}

	private String processRequest(String functionName,
	                              String params) throws EverSdkException {
		this.requestCount++;
		try {
			log.trace("FUNC:" + functionName + " CTXID:" + id() + " REQID:" + requestCount() + " SEND:" + params);
			final String result = EverSdkBridge
					.tcRequest(id(), requestCount(), functionName, params)
					.result()
					.get(this.timeout, TimeUnit.MILLISECONDS);
			log.trace("FUNC: " + functionName + " CTXID:" + id() + " REQID:" + requestCount() + " RESP:" + result);
			return result;
		} catch (CompletionException | ExecutionException e) {
			// These errors are sent by SDK, response_type=1
			EverSdkException.ErrorResult sdkResponse = null;
			try {
				sdkResponse = this.mapper.readValue(e.getCause().getMessage(), EverSdkException.ErrorResult.class);
				log.warn("Error from SDK. Code: " + sdkResponse.code() + ", Message: " + sdkResponse.message());
				throw new EverSdkException(sdkResponse, e);
			} catch (JsonProcessingException ex) {
				log.error("SDK Error Response deserialization failed! Response: " + e.getCause().getMessage() +
				          ex.getMessage());
				throw new EverSdkException(new EverSdkException.ErrorResult(-500,
				                                                            "SDK Error Response deserialization failed! Check getCause() for actual response."),
				                           ex);
			}
		} catch (InterruptedException e) {
			log.error("EVER-SDK call interrupted!" + e.getCause());
			throw new EverSdkException(new EverSdkException.ErrorResult(-400, "EVER-SDK call interrupted!"), e);
		} catch (TimeoutException e) {
			log.error("EVER-SDK Execution expired on Timeout! Current timeout: " + this.timeout + " Message: " +
			          e.getMessage());
			throw new EverSdkException(new EverSdkException.ErrorResult(-402,
			                                                            "EVER-SDK Execution expired on Timeout! Current timeout: " +
			                                                            this.timeout), e);

		}

	}

	public int id() {
		return this.id;
	}

	public int requestCount() {
		return this.requestCount;
	}
}
