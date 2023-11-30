package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tech.deplant.java4ever.binding.ffi.NativeMethods;
import tech.deplant.java4ever.binding.ffi.NativeUpcallHandler;
import tech.deplant.java4ever.binding.ffi.SdkResponseHandler;

import java.lang.foreign.Arena;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public record SdkContext(Sdk sdk, int id, AtomicInteger requestCount, @JsonIgnore Map<Integer, NativeUpcallHandler> requests, @JsonIgnore Map<Integer, CompletableFuture<String>> responses, @JsonIgnore Map<Integer, Queue<String>> subscriptions) {

	public SdkContext(Sdk sdk) {
		this(sdk, 0, new AtomicInteger(), new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
	}

	public static SdkContext ofExisting(Sdk sdk, int id, int requestCount) {
		return new SdkContext(sdk, id, new AtomicInteger(requestCount), new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
	}

	public int requestCountNextVal() {
		return requestCount().incrementAndGet();
	}

	public <R> R call(R calleeFunction) {
		return ScopedValue.where(Sdk.CONTEXT, this)
		                  .get(() -> calleeFunction);
	}


	public void addResponse(int request_id, String responseString) {

	}

	public void addError(int request_id, String responseString) {

	}

	public void addEvent(int request_id, String responseString) {

	}

	public void finishRequest(int request_id) {

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
	public <T, P> T addRequest(String functionName, P params, Class<T> clazz) throws EverSdkException {

		final int requestId = requestCountNextVal();
		final NativeUpcallHandler handler = new NativeUpcallHandler(this);
		String result = NativeMethods.tcRequest(id(), functionName, params, Arena.ofAuto(), requestId, handler);
		return sdk().mapper.readValue(handler.(this.mapper, timeout(), TimeUnit.MILLISECONDS), clazz);
		logger.log(System.Logger.Level.TRACE,
		           () -> "Removing request by result acception: " + requestId);
		return result;
	}
}
