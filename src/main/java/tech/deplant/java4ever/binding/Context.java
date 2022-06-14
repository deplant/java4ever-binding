package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import jdk.incubator.foreign.MemorySegment;
import jdk.incubator.foreign.ResourceScope;
import jdk.incubator.foreign.SegmentAllocator;
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

//TODO Define rules for responses and exceptions (Timeout & so on)
//TODO Methods for map of contexts
//TODO Making sure that methods check SDK version
//TODO Move context creation here
//TODO In ConfigContext remove all constructors except AllArgs
@Log4j2
public final class Context {

    public int requestCount = 0;
    @Getter
    int id;
    @Getter
    @Setter
    long timeout;
    @Getter
    Double version = 1.28;
    //HashMap<Integer, CompletableFuture<String>> responses = new HashMap<>();
    //HashMap<Integer, Callback<?>> callbacks = new HashMap<>();
    //HashMap<Integer, Object> appObjects = new HashMap<>();
    //private ResourceScope resourceScopeOfConfig = ResourceScope.newSharedScope();

    public Context(LibraryLoader loader, String config) {
        loader.load();
        this.id = tcCreateContext(ResourceScope.newSharedScope(), config);
        this.timeout = 30;
    }

    private Context(LibraryLoader loader, Config config) {
        this(loader, config.toJson());
    }

    public <T, P> CompletableFuture<T> futureCallback(String functionName, P params, Class<T> clazz) throws JsonProcessingException {
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

    private int tcCreateContext(ResourceScope scope, String configJson) {
        log.info("FUNC:sdk.create_context CTXID:0 REQID:0 SEND:{}", () -> configJson);
        MemorySegment out = ton_client.tc_read_string(SegmentAllocator.ofScope(scope), ton_client.tc_create_context(tc_string_data_t.ofString(configJson, scope)));
        String result = tc_string_data_t.toString(out, scope);
        log.info("FUNC:sdk.create_context CTXID:0 REQID:0 RESP:{}", () -> result);
        ResultOfCreateContext context = null;
        try {
            context = JsonContext.MAPPER.readValue(result, ResultOfCreateContext.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return context.result() == null ? -1 : context.result();
    }

    @Value
    public static class Config extends JsonData {
        Context.NetworkConfig network;
        Context.CryptoConfig crypto;
        Context.AbiConfig abi;
    }

    @Value
    public static class NetworkConfig extends JsonData {
        String[] endpoints;
        @Deprecated
        String server_address; // deprecated, use endpoints
        Integer network_retries_count; // default = 5
        Integer message_retries_count; // default = 5
        Integer message_processing_timeout; // default = 40000 ms
        Integer wait_for_timeout; // default = 40000 ms
        Integer out_of_sync_threshold; // default = 15000 ms
        Integer reconnect_timeout; // default = 12000 ms
        String access_key;
    }

    @Value
    public static class CryptoConfig extends JsonData {
        Integer mnemonic_dictionary; // default = 1
        Integer mnemonic_word_count; // default = 12
        String hdkey_derivation_path; // default = "m/44'/396'/0'/0/0"
    }

    @Value
    public static class AbiConfig extends JsonData {
        Integer workchain; // default = 0
        Integer message_expiration_timeout; // default = 40000 ms
        Integer message_expiration_timeout_grow_factor; // default = 1.5

    }

    public record ResultOfCreateContext(Integer result,String error) {}

    private class Callback<T> {
        BiConsumer<T, Integer> consumer;
        Class<T> clazz;

        Callback(BiConsumer<T, Integer> consumer, Class<T> clazz) {
            this.consumer = consumer;
            this.clazz = clazz;
        }

    }

}
