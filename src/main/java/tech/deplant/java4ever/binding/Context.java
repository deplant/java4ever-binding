package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.deplant.java4ever.binding.ffi.EverSdkBridge;

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
	                                 Class<T> clazz) throws ExecutionException, JsonProcessingException {
		return call(functionName, params, clazz);
	}

	public <T, P, E extends ExternalEvent> T callEvent(String functionName,
	                                                   P params,
	                                                   Consumer<E> consumer,
	                                                   Class<T> clazz) throws ExecutionException, JsonProcessingException {
		return call(functionName, params, clazz);
	}

	public <T, P> T call(String functionName,
	                     P params,
	                     Class<T> clazz) throws ExecutionException, JsonProcessingException {
		final var paramsChecked = (null == params) ? "" : this.mapper.writeValueAsString(params);
		return this.mapper.readValue(processRequest(functionName, paramsChecked), clazz);
	}

	public <P> void callVoid(String functionName,
	                         P params) throws ExecutionException, JsonProcessingException {
		var paramsChecked = (null == params) ? "" : this.mapper.writeValueAsString(params);
		processRequest(functionName, paramsChecked);
	}

	private String processRequest(String functionName,
	                              String params) throws ExecutionException {
		this.requestCount++;
		try {
			log.info("FUNC:" + functionName + " CTXID:" + id() + " REQID:" + requestCount() + " SEND:" + params);
			final String result = EverSdkBridge
					.tcRequest(id(), requestCount(), functionName, params)
					.result()
					.get(this.timeout, TimeUnit.MILLISECONDS);
			log.info("FUNC: " + functionName + " CTXID:" + id() + " REQID:" + requestCount() + " RESP:" + result);
			return result;
		} catch (InterruptedException e) {
			throw new ExecutionException("Interrupted!", e);
		} catch (TimeoutException e) {
			throw new ExecutionException("Timeouted! Current timeout: " + this.timeout, e);
		}
	}

	public int id() {
		return this.id;
	}

	public int requestCount() {
		return this.requestCount;
	}
}
