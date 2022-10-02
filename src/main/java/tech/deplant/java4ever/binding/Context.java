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

//TODO Define rules for responses and exceptions (Timeout & so on)
//TODO Methods for map of contexts
//TODO Making sure that methods check SDK version
//TODO Move context creation here
//TODO In ConfigContext remove all constructors except AllArgs
public final class Context {


	private final static Logger log = LoggerFactory.getLogger(Context.class);
	private final int id;
	private final ObjectMapper mapper;

	private final long timeout;

	private int requestCount;

	public Context(int id,
	               int requestCount,
	               long timeout,
	               ObjectMapper mapper) {
		this.id = id;
		this.requestCount = requestCount;
		this.timeout = timeout;
		this.mapper = mapper;
	}

	public <T, P, A> T callAppObject(String functionName,
	                                 P params,
	                                 A appObject,
	                                 Class<T> clazz) throws EverSdkException {
		return call(functionName, params, clazz);
	}

	public <T, P, E extends ExternalEvent> T callEvent(String functionName,
	                                                   P params,
	                                                   Consumer<E> consumer,
	                                                   Class<T> clazz) throws EverSdkException {
		return call(functionName, params, clazz);
	}

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
			log.info("FUNC:" + functionName + " CTXID:" + id() + " REQID:" + requestCount() + " SEND:" + params);
			final String result = EverSdkBridge
					.tcRequest(id(), requestCount(), functionName, params)
					.result()
					.get(this.timeout, TimeUnit.MILLISECONDS);
			log.info("FUNC: " + functionName + " CTXID:" + id() + " REQID:" + requestCount() + " RESP:" + result);
			return result;
		} catch (CompletionException e) {
			// These errors are sent by SDK, response_type=1
			EverSdkException.ErrorResult sdkResponse = null;
			try {
				sdkResponse = this.mapper.readValue(e.getMessage(), EverSdkException.ErrorResult.class);
				log.warn("Error from SDK. Code: " + sdkResponse.code() + ", Message: " + sdkResponse.message());
				throw new EverSdkException(sdkResponse, e);
			} catch (JsonProcessingException ex) {
				log.error("SDK Error Response deserialization failed!");
				throw new EverSdkException(new EverSdkException.ErrorResult(-500,
				                                                            "SDK Error Response deserialization failed! Check getCause() for actual response."),
				                           e);
			}
		} catch (InterruptedException e) {
			log.error("EVER-SDK call interrupted!" + e.getCause());
			throw new EverSdkException(new EverSdkException.ErrorResult(-400, "EVER-SDK call interrupted!"), e);
		} catch (ExecutionException e) {
			log.error("EVER-SDK execution exception!" + e.getCause());
			throw new EverSdkException(new EverSdkException.ErrorResult(-401, "EVER-SDK execution exception!"), e);
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
