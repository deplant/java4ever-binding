package tech.deplant.java4ever.binding;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import tech.deplant.java4ever.binding.json.JsonData;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class Client {


    /**
     * Returns Core Library API reference
     */
    public static CompletableFuture<ResultOfGetApiReference> getApiReference(@NonNull Context context) {
        return context.future("client.get_api_reference", null, ResultOfGetApiReference.class);
    }

    /**
     * Returns Core Library version
     */
    public static CompletableFuture<ResultOfVersion> version(@NonNull Context context) {
        return context.future("client.version", null, ResultOfVersion.class);
    }

    /**
     * Returns detailed information about this build.
     */
    public static CompletableFuture<ResultOfBuildInfo> buildInfo(@NonNull Context context) {
        return context.future("client.build_info", null, ResultOfBuildInfo.class);
    }

    /**
     * Resolves application request processing result
     *
     * @param appRequestId Request ID received from SDK
     * @param result       Result of request processing
     */
    public static CompletableFuture<Void> resolveAppRequest(@NonNull Context context, @NonNull Number appRequestId, @NonNull AppRequestResult result) {
        return context.future("client.resolve_app_request", new ParamsOfResolveAppRequest(appRequestId, result), Void.class);
    }

    @Value
    public static class ClientConfig extends JsonData {
        @SerializedName("network")
        @Getter(AccessLevel.NONE)
        NetworkConfig network;
        @SerializedName("crypto")
        @Getter(AccessLevel.NONE)
        CryptoConfig crypto;
        @SerializedName("abi")
        @Getter(AccessLevel.NONE)
        AbiConfig abi;
        @SerializedName("boc")
        @Getter(AccessLevel.NONE)
        BocConfig boc;
        @SerializedName("proofs")
        @Getter(AccessLevel.NONE)
        ProofsConfig proofs;
        @SerializedName("local_storage_path")
        @Getter(AccessLevel.NONE)
        String localStoragePath;

        public Optional<NetworkConfig> network() {
            return Optional.ofNullable(this.network);
        }

        public Optional<CryptoConfig> crypto() {
            return Optional.ofNullable(this.crypto);
        }

        public Optional<AbiConfig> abi() {
            return Optional.ofNullable(this.abi);
        }

        public Optional<BocConfig> boc() {
            return Optional.ofNullable(this.boc);
        }

        public Optional<ProofsConfig> proofs() {
            return Optional.ofNullable(this.proofs);
        }

        /**
         * For file based storage is a folder name where SDK will store its data. For browser based is a browser async storage key prefix. Default (recommended) value is "~/.tonclient" for native environments and ".tonclient" for web-browser.
         */
        public Optional<String> localStoragePath() {
            return Optional.ofNullable(this.localStoragePath);
        }

    }

    @Value
    public static class NetworkConfig extends JsonData {
        @SerializedName("server_address")
        @Getter(AccessLevel.NONE)
        String serverAddress;
        @SerializedName("endpoints")
        @Getter(AccessLevel.NONE)
        String[] endpoints;
        @SerializedName("network_retries_count")
        @Getter(AccessLevel.NONE)
        Number networkRetriesCount;
        @SerializedName("max_reconnect_timeout")
        @Getter(AccessLevel.NONE)
        Number maxReconnectTimeout;
        @SerializedName("reconnect_timeout")
        @Getter(AccessLevel.NONE)
        Number reconnectTimeout;
        @SerializedName("message_retries_count")
        @Getter(AccessLevel.NONE)
        Number messageRetriesCount;
        @SerializedName("message_processing_timeout")
        @Getter(AccessLevel.NONE)
        Number messageProcessingTimeout;
        @SerializedName("wait_for_timeout")
        @Getter(AccessLevel.NONE)
        Number waitForTimeout;
        @SerializedName("out_of_sync_threshold")
        @Getter(AccessLevel.NONE)
        Number outOfSyncThreshold;
        @SerializedName("sending_endpoint_count")
        @Getter(AccessLevel.NONE)
        Number sendingEndpointCount;
        @SerializedName("latency_detection_interval")
        @Getter(AccessLevel.NONE)
        Number latencyDetectionInterval;
        @SerializedName("max_latency")
        @Getter(AccessLevel.NONE)
        Number maxLatency;
        @SerializedName("query_timeout")
        @Getter(AccessLevel.NONE)
        Number queryTimeout;
        @SerializedName("access_key")
        @Getter(AccessLevel.NONE)
        String accessKey;

        /**
         * DApp Server public address. For instance, for `net.ton.dev/graphql` GraphQL endpoint the server address will be net.ton.dev
         */
        public Optional<String> serverAddress() {
            return Optional.ofNullable(this.serverAddress);
        }

        /**
         * List of DApp Server addresses.
         */
        public Optional<String[]> endpoints() {
            return Optional.ofNullable(this.endpoints);
        }

        /**
         * Deprecated.
         */
        @Deprecated
        public Optional<Number> networkRetriesCount() {
            return Optional.ofNullable(this.networkRetriesCount);
        }

        /**
         * Maximum time for sequential reconnections.
         */
        public Optional<Number> maxReconnectTimeout() {
            return Optional.ofNullable(this.maxReconnectTimeout);
        }

        /**
         * Deprecated
         */
        @Deprecated
        public Optional<Number> reconnectTimeout() {
            return Optional.ofNullable(this.reconnectTimeout);
        }

        /**
         * The number of automatic message processing retries that SDK performs in case of `Message Expired (507)` error - but only for those messages which local emulation was successful or failed with replay protection error.
         */
        public Optional<Number> messageRetriesCount() {
            return Optional.ofNullable(this.messageRetriesCount);
        }

        /**
         * Timeout that is used to process message delivery for the contracts which ABI does not include "expire" header. If the message is not delivered within the specified timeout the appropriate error occurs.
         */
        public Optional<Number> messageProcessingTimeout() {
            return Optional.ofNullable(this.messageProcessingTimeout);
        }

        /**
         * Maximum timeout that is used for query response.
         */
        public Optional<Number> waitForTimeout() {
            return Optional.ofNullable(this.waitForTimeout);
        }

        /**
         * Maximum time difference between server and client.
         */
        public Optional<Number> outOfSyncThreshold() {
            return Optional.ofNullable(this.outOfSyncThreshold);
        }

        /**
         * Maximum number of randomly chosen endpoints the library uses to broadcast a message.
         */
        public Optional<Number> sendingEndpointCount() {
            return Optional.ofNullable(this.sendingEndpointCount);
        }

        /**
         * Frequency of sync latency detection.
         */
        public Optional<Number> latencyDetectionInterval() {
            return Optional.ofNullable(this.latencyDetectionInterval);
        }

        /**
         * Maximum value for the endpoint's blockchain data syncronization latency (time-lag). Library periodically checks the current endpoint for blockchain data synchronization latency. If the latency (time-lag) is less then `NetworkConfig.max_latency` then library selects another endpoint.
         */
        public Optional<Number> maxLatency() {
            return Optional.ofNullable(this.maxLatency);
        }

        /**
         * Default timeout for http requests.
         */
        public Optional<Number> queryTimeout() {
            return Optional.ofNullable(this.queryTimeout);
        }

        /**
         * Access key to GraphQL API.
         */
        public Optional<String> accessKey() {
            return Optional.ofNullable(this.accessKey);
        }

    }

    @Value
    public static class CryptoConfig extends JsonData {
        @SerializedName("mnemonic_dictionary")
        @Getter(AccessLevel.NONE)
        Number mnemonicDictionary;
        @SerializedName("mnemonic_word_count")
        @Getter(AccessLevel.NONE)
        Number mnemonicWordCount;
        @SerializedName("hdkey_derivation_path")
        @Getter(AccessLevel.NONE)
        String hdkeyDerivationPath;

        /**
         * Mnemonic dictionary that will be used by default in crypto functions. If not specified, 1 dictionary will be used.
         */
        public Optional<Number> mnemonicDictionary() {
            return Optional.ofNullable(this.mnemonicDictionary);
        }

        /**
         * Mnemonic word count that will be used by default in crypto functions. If not specified the default value will be 12.
         */
        public Optional<Number> mnemonicWordCount() {
            return Optional.ofNullable(this.mnemonicWordCount);
        }

        /**
         * Derivation path that will be used by default in crypto functions. If not specified `m/44'/396'/0'/0/0` will be used.
         */
        public Optional<String> hdkeyDerivationPath() {
            return Optional.ofNullable(this.hdkeyDerivationPath);
        }

    }

    @Value
    public static class AbiConfig extends JsonData {
        @SerializedName("workchain")
        @Getter(AccessLevel.NONE)
        Number workchain;
        @SerializedName("message_expiration_timeout")
        @Getter(AccessLevel.NONE)
        Number messageExpirationTimeout;
        @SerializedName("message_expiration_timeout_grow_factor")
        @Getter(AccessLevel.NONE)
        Number messageExpirationTimeoutGrowFactor;

        /**
         * Workchain id that is used by default in DeploySet
         */
        public Optional<Number> workchain() {
            return Optional.ofNullable(this.workchain);
        }

        /**
         * Message lifetime for contracts which ABI includes "expire" header. The default value is 40 sec.
         */
        public Optional<Number> messageExpirationTimeout() {
            return Optional.ofNullable(this.messageExpirationTimeout);
        }

        /**
         * Factor that increases the expiration timeout for each retry The default value is 1.5
         */
        public Optional<Number> messageExpirationTimeoutGrowFactor() {
            return Optional.ofNullable(this.messageExpirationTimeoutGrowFactor);
        }

    }

    @Value
    public static class BocConfig extends JsonData {
        @SerializedName("cache_max_size")
        @Getter(AccessLevel.NONE)
        Number cacheMaxSize;

        /**
         * Maximum BOC cache size in kilobytes.
         */
        public Optional<Number> cacheMaxSize() {
            return Optional.ofNullable(this.cacheMaxSize);
        }

    }

    @Value
    public static class ProofsConfig extends JsonData {
        @SerializedName("cache_in_local_storage")
        @Getter(AccessLevel.NONE)
        Boolean cacheInLocalStorage;

        /**
         * Cache proofs in the local storage.
         */
        public Optional<Boolean> cacheInLocalStorage() {
            return Optional.ofNullable(this.cacheInLocalStorage);
        }

    }

    @Value
    public static class BuildInfoDependency extends JsonData {

        /**
         * Dependency name.
         */
        @SerializedName("name")
        @NonNull String name;

        /**
         * Git commit hash of the related repository.
         */
        @SerializedName("git_commit")
        @NonNull String gitCommit;

    }

    public static abstract class AppRequestResult {


        @Value
        public static class Error extends AppRequestResult {

            /**
             * Error description
             */
            @SerializedName("text")
            @NonNull String text;

        }


        @Value
        public static class Ok extends AppRequestResult {

            /**
             * Request processing result
             */
            @SerializedName("result")
            @NonNull Map<String, Object> result;

        }
    }

    @Value
    public static class ResultOfGetApiReference extends JsonData {
        @SerializedName("api")
        @NonNull Map<String, Object> api;

    }

    @Value
    public static class ResultOfVersion extends JsonData {

        /**
         * Core Library version
         */
        @SerializedName("version")
        @NonNull String version;

    }

    @Value
    public static class ResultOfBuildInfo extends JsonData {

        /**
         * Build number assigned to this build by the CI.
         */
        @SerializedName("build_number")
        @NonNull Number buildNumber;

        /**
         * Fingerprint of the most important dependencies.
         */
        @SerializedName("dependencies")
        @NonNull BuildInfoDependency[] dependencies;

    }

    @Value
    public static class ParamsOfResolveAppRequest extends JsonData {

        /**
         * Request ID received from SDK
         */
        @SerializedName("app_request_id")
        @NonNull Number appRequestId;

        /**
         * Result of request processing
         */
        @SerializedName("result")
        @NonNull AppRequestResult result;

    }

}
