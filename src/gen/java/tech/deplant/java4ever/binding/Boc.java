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
public class Boc {

    /**
     * Parses message boc into a JSON
     *
     * @param boc BOC encoded as base64
     */
    public static CompletableFuture<ResultOfParse> parseMessage(@NonNull Context context, @NonNull String boc) {
        return context.future("boc.parse_message", new ParamsOfParse(boc), ResultOfParse.class);
    }

    /**
     * Parses transaction boc into a JSON
     *
     * @param boc BOC encoded as base64
     */
    public static CompletableFuture<ResultOfParse> parseTransaction(@NonNull Context context, @NonNull String boc) {
        return context.future("boc.parse_transaction", new ParamsOfParse(boc), ResultOfParse.class);
    }

    /**
     * Parses account boc into a JSON
     *
     * @param boc BOC encoded as base64
     */
    public static CompletableFuture<ResultOfParse> parseAccount(@NonNull Context context, @NonNull String boc) {
        return context.future("boc.parse_account", new ParamsOfParse(boc), ResultOfParse.class);
    }

    /**
     * Parses block boc into a JSON
     *
     * @param boc BOC encoded as base64
     */
    public static CompletableFuture<ResultOfParse> parseBlock(@NonNull Context context, @NonNull String boc) {
        return context.future("boc.parse_block", new ParamsOfParse(boc), ResultOfParse.class);
    }

    /**
     * Parses shardstate boc into a JSON
     *
     * @param boc         BOC encoded as base64
     * @param id          Shardstate identificator
     * @param workchainId Workchain shardstate belongs to
     */
    public static CompletableFuture<ResultOfParse> parseShardstate(@NonNull Context context, @NonNull String boc, @NonNull String id, @NonNull Number workchainId) {
        return context.future("boc.parse_shardstate", new ParamsOfParseShardstate(boc, id, workchainId), ResultOfParse.class);
    }

    /**
     * Extract blockchain configuration from key block and also from zerostate.
     *
     * @param blockBoc Key block BOC or zerostate BOC encoded as base64
     */
    public static CompletableFuture<ResultOfGetBlockchainConfig> getBlockchainConfig(@NonNull Context context, @NonNull String blockBoc) {
        return context.future("boc.get_blockchain_config", new ParamsOfGetBlockchainConfig(blockBoc), ResultOfGetBlockchainConfig.class);
    }

    /**
     * Calculates BOC root hash
     *
     * @param boc BOC encoded as base64 or BOC handle
     */
    public static CompletableFuture<ResultOfGetBocHash> getBocHash(@NonNull Context context, @NonNull String boc) {
        return context.future("boc.get_boc_hash", new ParamsOfGetBocHash(boc), ResultOfGetBocHash.class);
    }

    /**
     * Calculates BOC depth
     *
     * @param boc BOC encoded as base64 or BOC handle
     */
    public static CompletableFuture<ResultOfGetBocDepth> getBocDepth(@NonNull Context context, @NonNull String boc) {
        return context.future("boc.get_boc_depth", new ParamsOfGetBocDepth(boc), ResultOfGetBocDepth.class);
    }

    /**
     * Extracts code from TVC contract image
     *
     * @param tvc Contract TVC image or image BOC handle
     */
    public static CompletableFuture<ResultOfGetCodeFromTvc> getCodeFromTvc(@NonNull Context context, @NonNull String tvc) {
        return context.future("boc.get_code_from_tvc", new ParamsOfGetCodeFromTvc(tvc), ResultOfGetCodeFromTvc.class);
    }

    /**
     * Get BOC from cache
     *
     * @param bocRef Reference to the cached BOC
     */
    public static CompletableFuture<ResultOfBocCacheGet> cacheGet(@NonNull Context context, @NonNull String bocRef) {
        return context.future("boc.cache_get", new ParamsOfBocCacheGet(bocRef), ResultOfBocCacheGet.class);
    }

    /**
     * Save BOC into cache
     *
     * @param boc       BOC encoded as base64 or BOC reference
     * @param cacheType Cache type
     */
    public static CompletableFuture<ResultOfBocCacheSet> cacheSet(@NonNull Context context, @NonNull String boc, @NonNull BocCacheType cacheType) {
        return context.future("boc.cache_set", new ParamsOfBocCacheSet(boc, cacheType), ResultOfBocCacheSet.class);
    }

    /**
     * Unpin BOCs with specified pin.
     *
     * @param pin    Pinned name
     * @param bocRef Reference to the cached BOC. If it is provided then only referenced BOC is unpinned
     */
    public static CompletableFuture<Void> cacheUnpin(@NonNull Context context, @NonNull String pin, String bocRef) {
        return context.future("boc.cache_unpin", new ParamsOfBocCacheUnpin(pin, bocRef), Void.class);
    }

    /**
     * Encodes bag of cells (BOC) with builder operations. This method provides the same functionality as Solidity TvmBuilder. Resulting BOC of this method can be passed into Solidity and C++ contracts as TvmCell type
     *
     * @param builder  Cell builder operations.
     * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided.
     */
    public static CompletableFuture<ResultOfEncodeBoc> encodeBoc(@NonNull Context context, @NonNull BuilderOp[] builder, BocCacheType bocCache) {
        return context.future("boc.encode_boc", new ParamsOfEncodeBoc(builder, bocCache), ResultOfEncodeBoc.class);
    }

    /**
     * Returns the contract code's salt if it is present.
     *
     * @param code     Contract code BOC encoded as base64 or code BOC handle
     * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided.
     */
    public static CompletableFuture<ResultOfGetCodeSalt> getCodeSalt(@NonNull Context context, @NonNull String code, BocCacheType bocCache) {
        return context.future("boc.get_code_salt", new ParamsOfGetCodeSalt(code, bocCache), ResultOfGetCodeSalt.class);
    }

    /**
     * Sets new salt to contract code.
     *
     * @param code     Contract code BOC encoded as base64 or code BOC handle
     * @param salt     Code salt to set. BOC encoded as base64 or BOC handle
     * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided.
     */
    public static CompletableFuture<ResultOfSetCodeSalt> setCodeSalt(@NonNull Context context, @NonNull String code, @NonNull String salt, BocCacheType bocCache) {
        return context.future("boc.set_code_salt", new ParamsOfSetCodeSalt(code, salt, bocCache), ResultOfSetCodeSalt.class);
    }

    /**
     * Decodes tvc into code, data, libraries and special options.
     *
     * @param tvc      Contract TVC image BOC encoded as base64 or BOC handle
     * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided.
     */
    public static CompletableFuture<ResultOfDecodeTvc> decodeTvc(@NonNull Context context, @NonNull String tvc, BocCacheType bocCache) {
        return context.future("boc.decode_tvc", new ParamsOfDecodeTvc(tvc, bocCache), ResultOfDecodeTvc.class);
    }

    /**
     * Encodes tvc from code, data, libraries ans special options (see input params)
     *
     * @param code       Contract code BOC encoded as base64 or BOC handle
     * @param data       Contract data BOC encoded as base64 or BOC handle
     * @param library    Contract library BOC encoded as base64 or BOC handle
     * @param tick       `special.tick` field. Specifies the contract ability to handle tick transactions
     * @param tock       `special.tock` field. Specifies the contract ability to handle tock transactions
     * @param splitDepth Is present and non-zero only in instances of large smart contracts
     * @param bocCache   Cache type to put the result. The BOC itself returned if no cache type provided.
     */
    public static CompletableFuture<ResultOfEncodeTvc> encodeTvc(@NonNull Context context, String code, String data, String library, Boolean tick, Boolean tock, Number splitDepth, BocCacheType bocCache) {
        return context.future("boc.encode_tvc", new ParamsOfEncodeTvc(code, data, library, tick, tock, splitDepth, bocCache), ResultOfEncodeTvc.class);
    }

    /**
     * Returns the compiler version used to compile the code.
     *
     * @param code Contract code BOC encoded as base64 or code BOC handle
     */
    public static CompletableFuture<ResultOfGetCompilerVersion> getCompilerVersion(@NonNull Context context, @NonNull String code) {
        return context.future("boc.get_compiler_version", new ParamsOfGetCompilerVersion(code), ResultOfGetCompilerVersion.class);
    }

    public static abstract class BocCacheType {

        public static final Unpinned Unpinned = new Unpinned();

        /**
         * Such BOC will not be removed from cache until it is unpinned
         */
        @Value
        public static class Pinned extends BocCacheType {
            @SerializedName("pin")
            @NonNull String pin;

        }

        @Value
        public static class Unpinned extends BocCacheType {

        }
    }

    @Value
    public static class ParamsOfParse extends JsonData {

        /**
         * BOC encoded as base64
         */
        @SerializedName("boc")
        @NonNull String boc;

    }

    @Value
    public static class ResultOfParse extends JsonData {

        /**
         * JSON containing parsed BOC
         */
        @SerializedName("parsed")
        @NonNull Map<String, Object> parsed;

    }

    @Value
    public static class ParamsOfParseShardstate extends JsonData {

        /**
         * BOC encoded as base64
         */
        @SerializedName("boc")
        @NonNull String boc;

        /**
         * Shardstate identificator
         */
        @SerializedName("id")
        @NonNull String id;

        /**
         * Workchain shardstate belongs to
         */
        @SerializedName("workchain_id")
        @NonNull Number workchainId;

    }

    @Value
    public static class ParamsOfGetBlockchainConfig extends JsonData {

        /**
         * Key block BOC or zerostate BOC encoded as base64
         */
        @SerializedName("block_boc")
        @NonNull String blockBoc;

    }

    @Value
    public static class ResultOfGetBlockchainConfig extends JsonData {

        /**
         * Blockchain config BOC encoded as base64
         */
        @SerializedName("config_boc")
        @NonNull String configBoc;

    }

    @Value
    public static class ParamsOfGetBocHash extends JsonData {

        /**
         * BOC encoded as base64 or BOC handle
         */
        @SerializedName("boc")
        @NonNull String boc;

    }

    @Value
    public static class ResultOfGetBocHash extends JsonData {

        /**
         * BOC root hash encoded with hex
         */
        @SerializedName("hash")
        @NonNull String hash;

    }

    @Value
    public static class ParamsOfGetBocDepth extends JsonData {

        /**
         * BOC encoded as base64 or BOC handle
         */
        @SerializedName("boc")
        @NonNull String boc;

    }

    @Value
    public static class ResultOfGetBocDepth extends JsonData {

        /**
         * BOC root cell depth
         */
        @SerializedName("depth")
        @NonNull Number depth;

    }

    @Value
    public static class ParamsOfGetCodeFromTvc extends JsonData {

        /**
         * Contract TVC image or image BOC handle
         */
        @SerializedName("tvc")
        @NonNull String tvc;

    }

    @Value
    public static class ResultOfGetCodeFromTvc extends JsonData {

        /**
         * Contract code encoded as base64
         */
        @SerializedName("code")
        @NonNull String code;

    }

    @Value
    public static class ParamsOfBocCacheGet extends JsonData {

        /**
         * Reference to the cached BOC
         */
        @SerializedName("boc_ref")
        @NonNull String bocRef;

    }

    @Value
    public static class ResultOfBocCacheGet extends JsonData {
        @SerializedName("boc")
        @Getter(AccessLevel.NONE)
        String boc;

        /**
         * BOC encoded as base64.
         */
        public Optional<String> boc() {
            return Optional.ofNullable(this.boc);
        }

    }

    @Value
    public static class ParamsOfBocCacheSet extends JsonData {

        /**
         * BOC encoded as base64 or BOC reference
         */
        @SerializedName("boc")
        @NonNull String boc;

        /**
         * Cache type
         */
        @SerializedName("cache_type")
        @NonNull BocCacheType cacheType;

    }

    @Value
    public static class ResultOfBocCacheSet extends JsonData {

        /**
         * Reference to the cached BOC
         */
        @SerializedName("boc_ref")
        @NonNull String bocRef;

    }

    @Value
    public static class ParamsOfBocCacheUnpin extends JsonData {

        /**
         * Pinned name
         */
        @SerializedName("pin")
        @NonNull String pin;
        @SerializedName("boc_ref")
        @Getter(AccessLevel.NONE)
        String bocRef;

        /**
         * Reference to the cached BOC.
         */
        public Optional<String> bocRef() {
            return Optional.ofNullable(this.bocRef);
        }

    }

    public static abstract class BuilderOp {


        @Value
        public static class Integer extends BuilderOp {

            /**
             * Bit size of the value.
             */
            @SerializedName("size")
            @NonNull Number size;

            /**
             * Value: - `Number` containing integer number.
             */
            @SerializedName("value")
            @NonNull Map<String, Object> value;

        }


        @Value
        public static class BitString extends BuilderOp {

            /**
             * Bit string content using bitstring notation. See `TON VM specification` 1.0.
             */
            @SerializedName("value")
            @NonNull String value;

        }


        @Value
        public static class Cell extends BuilderOp {

            /**
             * Nested cell builder
             */
            @SerializedName("builder")
            @NonNull BuilderOp[] builder;

        }


        @Value
        public static class CellBoc extends BuilderOp {

            /**
             * Nested cell BOC encoded with `base64` or BOC cache key.
             */
            @SerializedName("boc")
            @NonNull String boc;

        }
    }

    @Value
    public static class ParamsOfEncodeBoc extends JsonData {

        /**
         * Cell builder operations.
         */
        @SerializedName("builder")
        @NonNull BuilderOp[] builder;
        @SerializedName("boc_cache")
        @Getter(AccessLevel.NONE)
        BocCacheType bocCache;

        /**
         * Cache type to put the result. The BOC itself returned if no cache type provided.
         */
        public Optional<BocCacheType> bocCache() {
            return Optional.ofNullable(this.bocCache);
        }

    }

    @Value
    public static class ResultOfEncodeBoc extends JsonData {

        /**
         * Encoded cell BOC or BOC cache key.
         */
        @SerializedName("boc")
        @NonNull String boc;

    }

    @Value
    public static class ParamsOfGetCodeSalt extends JsonData {

        /**
         * Contract code BOC encoded as base64 or code BOC handle
         */
        @SerializedName("code")
        @NonNull String code;
        @SerializedName("boc_cache")
        @Getter(AccessLevel.NONE)
        BocCacheType bocCache;

        /**
         * Cache type to put the result. The BOC itself returned if no cache type provided.
         */
        public Optional<BocCacheType> bocCache() {
            return Optional.ofNullable(this.bocCache);
        }

    }

    @Value
    public static class ResultOfGetCodeSalt extends JsonData {
        @SerializedName("salt")
        @Getter(AccessLevel.NONE)
        String salt;

        /**
         * Contract code salt if present.
         */
        public Optional<String> salt() {
            return Optional.ofNullable(this.salt);
        }

    }

    @Value
    public static class ParamsOfSetCodeSalt extends JsonData {

        /**
         * Contract code BOC encoded as base64 or code BOC handle
         */
        @SerializedName("code")
        @NonNull String code;

        /**
         * Code salt to set.
         */
        @SerializedName("salt")
        @NonNull String salt;
        @SerializedName("boc_cache")
        @Getter(AccessLevel.NONE)
        BocCacheType bocCache;

        /**
         * Cache type to put the result. The BOC itself returned if no cache type provided.
         */
        public Optional<BocCacheType> bocCache() {
            return Optional.ofNullable(this.bocCache);
        }

    }

    @Value
    public static class ResultOfSetCodeSalt extends JsonData {

        /**
         * Contract code with salt set.
         */
        @SerializedName("code")
        @NonNull String code;

    }

    @Value
    public static class ParamsOfDecodeTvc extends JsonData {

        /**
         * Contract TVC image BOC encoded as base64 or BOC handle
         */
        @SerializedName("tvc")
        @NonNull String tvc;
        @SerializedName("boc_cache")
        @Getter(AccessLevel.NONE)
        BocCacheType bocCache;

        /**
         * Cache type to put the result. The BOC itself returned if no cache type provided.
         */
        public Optional<BocCacheType> bocCache() {
            return Optional.ofNullable(this.bocCache);
        }

    }

    @Value
    public static class ResultOfDecodeTvc extends JsonData {
        @SerializedName("code")
        @Getter(AccessLevel.NONE)
        String code;
        @SerializedName("code_hash")
        @Getter(AccessLevel.NONE)
        String codeHash;
        @SerializedName("code_depth")
        @Getter(AccessLevel.NONE)
        Number codeDepth;
        @SerializedName("data")
        @Getter(AccessLevel.NONE)
        String data;
        @SerializedName("data_hash")
        @Getter(AccessLevel.NONE)
        String dataHash;
        @SerializedName("data_depth")
        @Getter(AccessLevel.NONE)
        Number dataDepth;
        @SerializedName("library")
        @Getter(AccessLevel.NONE)
        String library;
        @SerializedName("tick")
        @Getter(AccessLevel.NONE)
        Boolean tick;
        @SerializedName("tock")
        @Getter(AccessLevel.NONE)
        Boolean tock;
        @SerializedName("split_depth")
        @Getter(AccessLevel.NONE)
        Number splitDepth;
        @SerializedName("compiler_version")
        @Getter(AccessLevel.NONE)
        String compilerVersion;

        /**
         * Contract code BOC encoded as base64 or BOC handle
         */
        public Optional<String> code() {
            return Optional.ofNullable(this.code);
        }

        /**
         * Contract code hash
         */
        public Optional<String> codeHash() {
            return Optional.ofNullable(this.codeHash);
        }

        /**
         * Contract code depth
         */
        public Optional<Number> codeDepth() {
            return Optional.ofNullable(this.codeDepth);
        }

        /**
         * Contract data BOC encoded as base64 or BOC handle
         */
        public Optional<String> data() {
            return Optional.ofNullable(this.data);
        }

        /**
         * Contract data hash
         */
        public Optional<String> dataHash() {
            return Optional.ofNullable(this.dataHash);
        }

        /**
         * Contract data depth
         */
        public Optional<Number> dataDepth() {
            return Optional.ofNullable(this.dataDepth);
        }

        /**
         * Contract library BOC encoded as base64 or BOC handle
         */
        public Optional<String> library() {
            return Optional.ofNullable(this.library);
        }

        /**
         * `special.tick` field.
         */
        public Optional<Boolean> tick() {
            return Optional.ofNullable(this.tick);
        }

        /**
         * `special.tock` field.
         */
        public Optional<Boolean> tock() {
            return Optional.ofNullable(this.tock);
        }

        /**
         * Is present and non-zero only in instances of large smart contracts
         */
        public Optional<Number> splitDepth() {
            return Optional.ofNullable(this.splitDepth);
        }

        /**
         * Compiler version, for example 'sol 0.49.0'
         */
        public Optional<String> compilerVersion() {
            return Optional.ofNullable(this.compilerVersion);
        }

    }

    @Value
    public static class ParamsOfEncodeTvc extends JsonData {
        @SerializedName("code")
        @Getter(AccessLevel.NONE)
        String code;
        @SerializedName("data")
        @Getter(AccessLevel.NONE)
        String data;
        @SerializedName("library")
        @Getter(AccessLevel.NONE)
        String library;
        @SerializedName("tick")
        @Getter(AccessLevel.NONE)
        Boolean tick;
        @SerializedName("tock")
        @Getter(AccessLevel.NONE)
        Boolean tock;
        @SerializedName("split_depth")
        @Getter(AccessLevel.NONE)
        Number splitDepth;
        @SerializedName("boc_cache")
        @Getter(AccessLevel.NONE)
        BocCacheType bocCache;

        /**
         * Contract code BOC encoded as base64 or BOC handle
         */
        public Optional<String> code() {
            return Optional.ofNullable(this.code);
        }

        /**
         * Contract data BOC encoded as base64 or BOC handle
         */
        public Optional<String> data() {
            return Optional.ofNullable(this.data);
        }

        /**
         * Contract library BOC encoded as base64 or BOC handle
         */
        public Optional<String> library() {
            return Optional.ofNullable(this.library);
        }

        /**
         * `special.tick` field.
         */
        public Optional<Boolean> tick() {
            return Optional.ofNullable(this.tick);
        }

        /**
         * `special.tock` field.
         */
        public Optional<Boolean> tock() {
            return Optional.ofNullable(this.tock);
        }

        /**
         * Is present and non-zero only in instances of large smart contracts
         */
        public Optional<Number> splitDepth() {
            return Optional.ofNullable(this.splitDepth);
        }

        /**
         * Cache type to put the result. The BOC itself returned if no cache type provided.
         */
        public Optional<BocCacheType> bocCache() {
            return Optional.ofNullable(this.bocCache);
        }

    }

    @Value
    public static class ResultOfEncodeTvc extends JsonData {

        /**
         * Contract TVC image BOC encoded as base64 or BOC handle of boc_cache parameter was specified
         */
        @SerializedName("tvc")
        @NonNull String tvc;

    }

    @Value
    public static class ParamsOfGetCompilerVersion extends JsonData {

        /**
         * Contract code BOC encoded as base64 or code BOC handle
         */
        @SerializedName("code")
        @NonNull String code;

    }

    @Value
    public static class ResultOfGetCompilerVersion extends JsonData {
        @SerializedName("version")
        @Getter(AccessLevel.NONE)
        String version;

        /**
         * Compiler version, for example 'sol 0.49.0'
         */
        public Optional<String> version() {
            return Optional.ofNullable(this.version);
        }

    }

}
