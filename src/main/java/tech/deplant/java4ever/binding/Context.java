package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jdk.incubator.foreign.MemorySegment;
import jdk.incubator.foreign.ResourceScope;
import jdk.incubator.foreign.SegmentAllocator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.binding.json.JsonData;
import tech.deplant.java4ever.binding.loader.LibraryLoader;
import tech.deplant.java4ever.binding.response.EverResponse;
import tech.deplant.java4ever.ffi.tc_response_handler_t;
import tech.deplant.java4ever.ffi.tc_string_data_t;
import tech.deplant.java4ever.ffi.ton_client;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

//TODO Define rules for responses and exceptions (Timeout & so on)
//TODO Methods for map of contexts
//TODO Making sure that methods check SDK version
//TODO Move context creation here
//TODO In ConfigContext remove all constructors except AllArgs
@Log4j2
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

    public CompletableFuture<String> tcRequest(String functionName, String params) {
        int requestId = this.requestCount++;
        EverResponse response = new EverResponse();
        log.trace("FUNC:{} CTXID:{} REQID:{} SEND:{}", () -> functionName, this::id, () -> requestId, () -> params);
        ton_client.tc_request(this.id(), tc_string_data_t.ofString(functionName, ResourceScope.newSharedScope()), tc_string_data_t.ofString(params, ResourceScope.newSharedScope()),
                requestId, tc_response_handler_t.allocate(response, ResourceScope.newSharedScope()));
        response.result().thenApply(res -> {
                    log.trace("FUNC: " + functionName + " CTXID:" + this.id() + " REQID:" + requestId + " RESP:" + res);
                    return res;
                }
        );
        return response.result();
        //}
    }

    public static Context create(LibraryLoader loader, String configJson) throws JsonProcessingException {
        loader.load();
        String responseJson = null;
        try (ResourceScope scope = ResourceScope.newSharedScope()) {
            responseJson = tcCreateContext(scope, configJson);
        }
        var createContextResponse = JsonContext.MAPPER.readValue(responseJson, ResultOfCreateContext.class);
        if (createContextResponse.result() == null || createContextResponse.result() < 1) {
            throw new RuntimeException("sdk.create_context failed!");
        }
        return new Context(0,createContextResponse.result());
    }

    public static Context create(LibraryLoader loader, Client.ClientConfig config) throws JsonProcessingException  {
        return create(loader, JsonContext.MAPPER.writeValueAsString(config));
    }

    private static String tcCreateContext(ResourceScope scope, String configJson) {
        log.info("FUNC:sdk.create_context CTXID:0 REQID:0 SEND:{}", () -> configJson);
        MemorySegment out = ton_client.tc_read_string(SegmentAllocator.ofScope(scope), ton_client.tc_create_context(tc_string_data_t.ofString(configJson, scope)));
        final String result = tc_string_data_t.toString(out, scope);
        log.info("FUNC:sdk.create_context CTXID:0 REQID:0 RESP:{}", () -> result);
        return result;
    }

    public record ResultOfCreateContext(Integer result,String error) {}

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
