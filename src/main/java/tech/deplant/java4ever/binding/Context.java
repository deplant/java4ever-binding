package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import tech.deplant.java4ever.binding.ffi.EverSdkBridge;
import tech.deplant.java4ever.binding.json.JsonContext;
import tech.deplant.java4ever.binding.loader.LibraryLoader;

import java.lang.foreign.MemorySession;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

//TODO Define rules for responses and exceptions (Timeout & so on)
//TODO Methods for map of contexts
//TODO Making sure that methods check SDK version
//TODO Move context creation here
//TODO In ConfigContext remove all constructors except AllArgs
@Slf4j
@AllArgsConstructor
public final class Context {
    @Getter
    private int id;
    @Getter
    private int requestCount = 0;

    //HashMap<Integer, CompletableFuture<String>> responses = new HashMap<>();
    //HashMap<Integer, Callback<?>> callbacks = new HashMap<>();
    //HashMap<Integer, Object> appObjects = new HashMap<>();
    //private ResourceScope resourceScopeOfConfig = ResourceScope.newSharedScope();

    public static Context create(LibraryLoader loader, String configJson) throws JsonProcessingException {
        loader.load();
        String responseJson = null;
        try (MemorySession scope = MemorySession.openShared()) {
            responseJson = tcCreateContext(scope, configJson);
        }
        var createContextResponse = JsonContext.MAPPER.readValue(responseJson, ResultOfCreateContext.class);
        if (createContextResponse.result() == null || createContextResponse.result() < 1) {
            throw new RuntimeException("sdk.create_context failed!");
        }
        return new Context(createContextResponse.result(), 0);
    }

    public static Context create(LibraryLoader loader, Client.ClientConfig config) throws JsonProcessingException {
        return create(loader, JsonContext.MAPPER.writeValueAsString(config));
    }

    private static String tcCreateContext(MemorySession scope, String configJson) {
        log.info("FUNC:sdk.create_context CTXID:0 REQID:0 SEND:" + configJson);
        String s = EverSdkBridge.tcCreateContext(scope, configJson);
        log.info("FUNC:sdk.create_context CTXID:0 REQID:0 RESP:" + s);
        return s;
    }

    public <T, P, A> T callAppObject(String functionName, P params, A appObject, Class<T> clazz) throws JsonProcessingException {
        return call(functionName, params, clazz);
    }

    public <T, P, E extends ExternalEvent> T callEvent(String functionName, P params, Consumer<E> consumer, Class<T> clazz) throws JsonProcessingException {
        return call(functionName, params, clazz);
    }

    public <T, P> T call(String functionName, P params, Class<T> clazz) throws JsonProcessingException {
        this.requestCount++;
        var paramsChecked = (null == params) ? "" : JsonContext.MAPPER.writeValueAsString(params);
        log.info("FUNC:" + functionName + " CTXID:" + id() + " REQID:" + requestCount() + " SEND:" + paramsChecked);
        String s = null;
        try {
            s = EverSdkBridge.tcRequest(id(), requestCount(), functionName, paramsChecked).result().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        log.info("FUNC: " + functionName + " CTXID:" + id() + " REQID:" + requestCount() + " RESP:" + s);
        return JsonContext.MAPPER.readValue(s, clazz);
    }

    public <P> void callVoid(String functionName, P params) throws JsonProcessingException {
        this.requestCount++;
        var paramsChecked = (null == params) ? "" : JsonContext.MAPPER.writeValueAsString(params);
        log.info("FUNC:" + functionName + " CTXID:" + id() + " REQID:" + requestCount() + " SEND:" + paramsChecked);
        String s = null;
        try {
            s = EverSdkBridge.tcRequest(id(), requestCount(), functionName, paramsChecked).result().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        log.info("FUNC: " + functionName + " CTXID:" + id() + " REQID:" + requestCount() + " RESP:" + s);
    }

    public record ResultOfCreateContext(Integer result, String error) {
    }

//    private class Callback<T> {
//        BiConsumer<T, Integer> consumer;
//        Class<T> clazz;
//
//        Callback(BiConsumer<T, Integer> consumer, Class<T> clazz) {
//            this.consumer = consumer;
//            this.clazz = clazz;
//        }
//
//    }

}
