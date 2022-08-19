package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.Optional;
import lombok.*;
import java.util.stream.*;
import java.util.Arrays;

/**
 *  <h1>client</h1>
 *  Contains methods of "client" module.

 *  Provides information about library.
 *  @version EVER-SDK 1.37.0
 */
public class Client {


    /**
    * 
    * @param network 
    * @param crypto 
    * @param abi 
    * @param boc 
    * @param proofs 
    * @param localStoragePath For file based storage is a folder name where SDK will store its data. For browser based is a browser async storage key prefix. Default (recommended) value is "~/.tonclient" for native environments and ".tonclient" for web-browser.
    */
    public record ClientConfig(NetworkConfig network, CryptoConfig crypto, AbiConfig abi, BocConfig boc, ProofsConfig proofs, String localStoragePath) {}

    /**
    * 
    * @param serverAddress **This field is deprecated, but left for backward-compatibility.** DApp Server public address.
    * @param endpoints List of DApp Server addresses. Any correct URL format can be specified, including IP addresses. This parameter is prevailing over `server_address`.Check the full list of <a target="_blank" href="supported network endpoints">supported network endpoints</a>(../ton-os-api/networks.md).
    * @param networkRetriesCount Deprecated. You must use `network.max_reconnect_timeout` that allows to specify maximum network resolving timeout.
    * @param maxReconnectTimeout Maximum time for sequential reconnections. Must be specified in milliseconds. Default is 120000 (2 min).
    * @param reconnectTimeout Deprecated
    * @param messageRetriesCount The number of automatic message processing retries that SDK performs in case of `Message Expired (507)` error - but only for those messages which local emulation was successful or failed with replay protection error. Default is 5.
    * @param messageProcessingTimeout Timeout that is used to process message delivery for the contracts which ABI does not include "expire" header. If the message is not delivered within the specified timeout the appropriate error occurs. Must be specified in milliseconds. Default is 40000 (40 sec).
    * @param waitForTimeout Maximum timeout that is used for query response. Must be specified in milliseconds. Default is 40000 (40 sec).
    * @param outOfSyncThreshold Maximum time difference between server and client. If client's device time is out of sync and difference is more than the threshold then error will occur. Also an error will occur if the specified threshold is more than`message_processing_timeout/2`.<p>Must be specified in milliseconds. Default is 15000 (15 sec).
    * @param sendingEndpointCount Maximum number of randomly chosen endpoints the library uses to broadcast a message. Default is 1.
    * @param latencyDetectionInterval Frequency of sync latency detection. Library periodically checks the current endpoint for blockchain data syncronization latency.If the latency (time-lag) is less then `NetworkConfig.max_latency`then library selects another endpoint.<p>Must be specified in milliseconds. Default is 60000 (1 min).
    * @param maxLatency Maximum value for the endpoint's blockchain data syncronization latency (time-lag). Library periodically checks the current endpoint for blockchain data synchronization latency. If the latency (time-lag) is less then `NetworkConfig.max_latency` then library selects another endpoint. Must be specified in milliseconds. Default is 60000 (1 min).
    * @param queryTimeout Default timeout for http requests. Is is used when no timeout specified for the request to limit the answer waiting time. If no answer received during the timeout requests ends witherror.<p>Must be specified in milliseconds. Default is 60000 (1 min).
    * @param queriesProtocol Queries protocol. `HTTP` or `WS`. Default is `HTTP`.
    * @param firstRempStatusTimeout UNSTABLE. First REMP status awaiting timeout. If no status recieved during the timeout than fallback transaction scenario is activated.<p>Must be specified in milliseconds. Default is 1000 (1 sec).
    * @param nextRempStatusTimeout UNSTABLE. Subsequent REMP status awaiting timeout. If no status recieved during the timeout than fallback transaction scenario is activated.<p>Must be specified in milliseconds. Default is 5000 (5 sec).
    * @param accessKey Access key to GraphQL API. You can specify here Evercloud project secret ot serialized JWT.
    */
    public record NetworkConfig(@Deprecated String serverAddress, String[] endpoints, @Deprecated Number networkRetriesCount, Number maxReconnectTimeout, @Deprecated Number reconnectTimeout, Number messageRetriesCount, Number messageProcessingTimeout, Number waitForTimeout, Number outOfSyncThreshold, Number sendingEndpointCount, Number latencyDetectionInterval, Number maxLatency, Number queryTimeout, NetworkQueriesProtocol queriesProtocol, Number firstRempStatusTimeout, Number nextRempStatusTimeout, String accessKey) {}
    public enum NetworkQueriesProtocol {
        
        /**
        * Each GraphQL query uses separate HTTP request.
        */
        HTTP,

        /**
        * All GraphQL queries will be served using single web socket connection.
        */
        WS
    }

    /**
    * 
    * @param mnemonicDictionary Mnemonic dictionary that will be used by default in crypto functions. If not specified, 1 dictionary will be used.
    * @param mnemonicWordCount Mnemonic word count that will be used by default in crypto functions. If not specified the default value will be 12.
    * @param hdkeyDerivationPath Derivation path that will be used by default in crypto functions. If not specified `m/44'/396'/0'/0/0` will be used.
    */
    public record CryptoConfig(Number mnemonicDictionary, Number mnemonicWordCount, String hdkeyDerivationPath) {}

    /**
    * 
    * @param workchain Workchain id that is used by default in DeploySet
    * @param messageExpirationTimeout Message lifetime for contracts which ABI includes "expire" header. The default value is 40 sec.
    * @param messageExpirationTimeoutGrowFactor Factor that increases the expiration timeout for each retry The default value is 1.5
    */
    public record AbiConfig(Number workchain, Number messageExpirationTimeout, Number messageExpirationTimeoutGrowFactor) {}

    /**
    * 
    * @param cacheMaxSize Maximum BOC cache size in kilobytes. Default is 10 MB
    */
    public record BocConfig(Number cacheMaxSize) {}

    /**
    * 
    * @param cacheInLocalStorage Cache proofs in the local storage. Default is `true`. If this value is set to `true`, downloaded proofs and master-chain BOCs are saved into thepersistent local storage (e.g. file system for native environments or browser's IndexedDBfor the web); otherwise all the data is cached only in memory in current client's contextand will be lost after destruction of the client.
    */
    public record ProofsConfig(Boolean cacheInLocalStorage) {}

    /**
    * 
    * @param name Dependency name. Usually it is a crate name.
    * @param gitCommit Git commit hash of the related repository.
    */
    public record BuildInfoDependency(@NonNull String name, @NonNull String gitCommit) {}
    public interface AppRequestResult {


    /**
    * Error occurred during request processing
    * @param text Error description
    */
    public record Error(@NonNull String text) implements AppRequestResult {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Request processed successfully
    * @param result Request processing result
    */
    public record Ok(@NonNull Map<String,Object> result) implements AppRequestResult {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}

    /**
    * 
    * @param api 
    */
    public record ResultOfGetApiReference(@NonNull Map<String,Object> api) {}

    /**
    * 
    * @param version Core Library version
    */
    public record ResultOfVersion(@NonNull String version) {}

    /**
    * 
    * @param buildNumber Build number assigned to this build by the CI.
    * @param dependencies Fingerprint of the most important dependencies.
    */
    public record ResultOfBuildInfo(@NonNull Number buildNumber, @NonNull BuildInfoDependency[] dependencies) {}

    /**
    * 
    * @param appRequestId Request ID received from SDK
    * @param result Result of request processing
    */
    public record ParamsOfResolveAppRequest(@NonNull Number appRequestId, @NonNull AppRequestResult result) {}
    /**
    * <h2>client.get_api_reference</h2>
    * Returns Core Library API reference
    * @return {@link tech.deplant.java4ever.binding.Client.ResultOfGetApiReference}
    */
    public static ResultOfGetApiReference getApiReference(@NonNull Context ctx)  throws JsonProcessingException {
        return ctx.call("client.get_api_reference", null, ResultOfGetApiReference.class);
    }

    /**
    * <h2>client.version</h2>
    * Returns Core Library version
    * @return {@link tech.deplant.java4ever.binding.Client.ResultOfVersion}
    */
    public static ResultOfVersion version(@NonNull Context ctx)  throws JsonProcessingException {
        return ctx.call("client.version", null, ResultOfVersion.class);
    }

    /**
    * <h2>client.config</h2>
    * Returns Core Library API reference
    * @return {@link tech.deplant.java4ever.binding.Client.ClientConfig}
    */
    public static ClientConfig config(@NonNull Context ctx)  throws JsonProcessingException {
        return ctx.call("client.config", null, ClientConfig.class);
    }

    /**
    * <h2>client.build_info</h2>
    * Returns detailed information about this build.
    * @return {@link tech.deplant.java4ever.binding.Client.ResultOfBuildInfo}
    */
    public static ResultOfBuildInfo buildInfo(@NonNull Context ctx)  throws JsonProcessingException {
        return ctx.call("client.build_info", null, ResultOfBuildInfo.class);
    }

    /**
    * <h2>client.resolve_app_request</h2>
    * Resolves application request processing result
    * @param appRequestId Request ID received from SDK 
    * @param result Result of request processing 
    * @return {@link tech.deplant.java4ever.binding.Client.Void}
    */
    public static Void resolveAppRequest(@NonNull Context ctx, @NonNull Number appRequestId, @NonNull AppRequestResult result)  throws JsonProcessingException {
        return ctx.call("client.resolve_app_request", new ParamsOfResolveAppRequest(appRequestId, result), Void.class);
    }

}
