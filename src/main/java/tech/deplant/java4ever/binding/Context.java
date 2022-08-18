package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import tech.deplant.java4ever.binding.ffi.EverSdkBridge;
import tech.deplant.java4ever.binding.loader.LibraryLoader;

import java.lang.foreign.MemorySession;
import java.util.concurrent.CompletableFuture;
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
    public int requestCount = 0;
    @Getter
    int id;

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
        return new Context(0, createContextResponse.result());
    }

    public static Context create(LibraryLoader loader, Client.ClientConfig config) throws JsonProcessingException {
        return create(loader, JsonContext.MAPPER.writeValueAsString(config));
    }

    private static String tcCreateContext(MemorySession scope, String configJson) {
        //log.info("FUNC:sdk.create_context CTXID:0 REQID:0 SEND:{}", () -> configJson);
        String s = EverSdkBridge.tcCreateContext(scope, configJson);
        //log.info("FUNC:sdk.create_context CTXID:0 REQID:0 RESP:{}", () -> result);
        return s;
    }

    public <T, P, A> CompletableFuture<T> futureAppObject(String functionName, P params, A appObject, Class<T> clazz) throws JsonProcessingException {
        return future(functionName, params, clazz);
    }

    public <T, P, E extends ExternalEvent> CompletableFuture<T> futureEvent(String functionName, P params, Consumer<E> consumer, Class<T> clazz) throws JsonProcessingException {
        return future(functionName, params, clazz);
    }

    public <T, P> CompletableFuture<T> future(String functionName, P params, Class<T> clazz) throws JsonProcessingException {
        return tcRequest(functionName, params == null ? "" : JsonContext.MAPPER.writeValueAsString(params)).thenApply(json -> {
            try {
                return JsonContext.MAPPER.readValue(json, clazz);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public String tcRequest(String functionName, String params) {
        //log.trace("FUNC:{} CTXID:{} REQID:{} SEND:{}", () -> functionName, this::id, () -> requestId, () -> params);
        String s = EverSdkBridge.tcRequest(this.id(), this.requestCount++, functionName, params);
        //log.trace("FUNC: " + functionName + " CTXID:" + this.id() + " REQID:" + requestId + " RESP:" + res);    }
        return s;
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
