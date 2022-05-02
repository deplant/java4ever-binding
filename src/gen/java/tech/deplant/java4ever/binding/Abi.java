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
public class Abi {

    /**
     * Encodes message body according to ABI function call.
     *
     * @param abi                Contract ABI.
     * @param callSet            Function call parameters. Must be specified in non deploy message.<p>In case of deploy message contains parameters of constructor.
     * @param isInternal         True if internal message body must be encoded.
     * @param signer             Signing parameters.
     * @param processingTryIndex Processing try index. Used in message processing with retries.<p>Encoder uses the provided try index to calculate messageexpiration time.<p>Expiration timeouts will grow with every retry.<p>Default value is 0.
     */
    public static CompletableFuture<ResultOfEncodeMessageBody> encodeMessageBody(@NonNull Context context, @NonNull ABI abi, @NonNull CallSet callSet, @NonNull Boolean isInternal, @NonNull Signer signer, Number processingTryIndex) {
        return context.future("abi.encode_message_body", new ParamsOfEncodeMessageBody(abi, callSet, isInternal, signer, processingTryIndex), ResultOfEncodeMessageBody.class);
    }

    /**
     * @param abi       Contract ABI
     * @param publicKey Public key. Must be encoded with `hex`.
     * @param message   Unsigned message body BOC. Must be encoded with `base64`.
     * @param signature Signature. Must be encoded with `hex`.
     */
    public static CompletableFuture<ResultOfAttachSignatureToMessageBody> attachSignatureToMessageBody(@NonNull Context context, @NonNull ABI abi, @NonNull String publicKey, @NonNull String message, @NonNull String signature) {
        return context.future("abi.attach_signature_to_message_body", new ParamsOfAttachSignatureToMessageBody(abi, publicKey, message, signature), ResultOfAttachSignatureToMessageBody.class);
    }

    /**
     * Encodes an ABI-compatible message
     *
     * @param abi                Contract ABI.
     * @param address            Target address the message will be sent to. Must be specified in case of non-deploy message.
     * @param deploySet          Deploy parameters. Must be specified in case of deploy message.
     * @param callSet            Function call parameters. Must be specified in case of non-deploy message.<p>In case of deploy message it is optional and contains parametersof the functions that will to be called upon deploy transaction.
     * @param signer             Signing parameters.
     * @param processingTryIndex Processing try index. Used in message processing with retries (if contract's ABI includes "expire" header).<p>Encoder uses the provided try index to calculate messageexpiration time. The 1st message expiration time is specified inClient config.<p>Expiration timeouts will grow with every retry.Retry grow factor is set in Client config:&lt;.....add config parameter with default value here&gt;<p>Default value is 0.
     */
    public static CompletableFuture<ResultOfEncodeMessage> encodeMessage(@NonNull Context context, @NonNull ABI abi, String address, DeploySet deploySet, CallSet callSet, @NonNull Signer signer, Number processingTryIndex) {
        return context.future("abi.encode_message", new ParamsOfEncodeMessage(abi, address, deploySet, callSet, signer, processingTryIndex), ResultOfEncodeMessage.class);
    }

    /**
     * Encodes an internal ABI-compatible message
     *
     * @param abi        Contract ABI. Can be None if both deploy_set and call_set are None.
     * @param address    Target address the message will be sent to. Must be specified in case of non-deploy message.
     * @param srcAddress Source address of the message.
     * @param deploySet  Deploy parameters. Must be specified in case of deploy message.
     * @param callSet    Function call parameters. Must be specified in case of non-deploy message.<p>In case of deploy message it is optional and contains parametersof the functions that will to be called upon deploy transaction.
     * @param value      Value in nanotokens to be sent with message.
     * @param bounce     Flag of bounceable message. Default is true.
     * @param enableIhr  Enable Instant Hypercube Routing for the message. Default is false.
     */
    public static CompletableFuture<ResultOfEncodeInternalMessage> encodeInternalMessage(@NonNull Context context, ABI abi, String address, String srcAddress, DeploySet deploySet, CallSet callSet, @NonNull String value, Boolean bounce, Boolean enableIhr) {
        return context.future("abi.encode_internal_message", new ParamsOfEncodeInternalMessage(abi, address, srcAddress, deploySet, callSet, value, bounce, enableIhr), ResultOfEncodeInternalMessage.class);
    }

    /**
     * Combines `hex`-encoded `signature` with `base64`-encoded `unsigned_message`. Returns signed message encoded in `base64`.
     *
     * @param abi       Contract ABI
     * @param publicKey Public key encoded in `hex`.
     * @param message   Unsigned message BOC encoded in `base64`.
     * @param signature Signature encoded in `hex`.
     */
    public static CompletableFuture<ResultOfAttachSignature> attachSignature(@NonNull Context context, @NonNull ABI abi, @NonNull String publicKey, @NonNull String message, @NonNull String signature) {
        return context.future("abi.attach_signature", new ParamsOfAttachSignature(abi, publicKey, message, signature), ResultOfAttachSignature.class);
    }

    /**
     * Decodes message body using provided message BOC and ABI.
     *
     * @param abi     contract ABI
     * @param message Message BOC
     */
    public static CompletableFuture<DecodedMessageBody> decodeMessage(@NonNull Context context, @NonNull ABI abi, @NonNull String message) {
        return context.future("abi.decode_message", new ParamsOfDecodeMessage(abi, message), DecodedMessageBody.class);
    }

    /**
     * Decodes message body using provided body BOC and ABI.
     *
     * @param abi        Contract ABI used to decode.
     * @param body       Message body BOC encoded in `base64`.
     * @param isInternal True if the body belongs to the internal message.
     */
    public static CompletableFuture<DecodedMessageBody> decodeMessageBody(@NonNull Context context, @NonNull ABI abi, @NonNull String body, @NonNull Boolean isInternal) {
        return context.future("abi.decode_message_body", new ParamsOfDecodeMessageBody(abi, body, isInternal), DecodedMessageBody.class);
    }

    /**
     * Creates account state BOC
     *
     * @param stateInit   Source of the account state init.
     * @param balance     Initial balance.
     * @param lastTransLt Initial value for the `last_trans_lt`.
     * @param lastPaid    Initial value for the `last_paid`.
     * @param bocCache    Cache type to put the result. The BOC itself returned if no cache type provided
     */
    public static CompletableFuture<ResultOfEncodeAccount> encodeAccount(@NonNull Context context, @NonNull StateInitSource stateInit, Long balance, Long lastTransLt, Number lastPaid, Boc.BocCacheType bocCache) {
        return context.future("abi.encode_account", new ParamsOfEncodeAccount(stateInit, balance, lastTransLt, lastPaid, bocCache), ResultOfEncodeAccount.class);
    }

    /**
     * Decodes account data using provided data BOC and ABI.
     *
     * @param abi  Contract ABI
     * @param data Data BOC or BOC handle
     */
    public static CompletableFuture<ResultOfDecodeAccountData> decodeAccountData(@NonNull Context context, @NonNull ABI abi, @NonNull String data) {
        return context.future("abi.decode_account_data", new ParamsOfDecodeAccountData(abi, data), ResultOfDecodeAccountData.class);
    }

    /**
     * Updates initial account data with initial values for the contract's static variables and owner's public key. This operation is applicable only for initial account data (before deploy). If the contract is already deployed, its data doesn't contain this data section any more.
     *
     * @param abi           Contract ABI
     * @param data          Data BOC or BOC handle
     * @param initialData   List of initial values for contract's static variables. `abi` parameter should be provided to set initial data
     * @param initialPubkey Initial account owner's public key to set into account data
     * @param bocCache      Cache type to put the result. The BOC itself returned if no cache type provided.
     */
    public static CompletableFuture<ResultOfUpdateInitialData> updateInitialData(@NonNull Context context, ABI abi, @NonNull String data, Map<String, Object> initialData, String initialPubkey, Boc.BocCacheType bocCache) {
        return context.future("abi.update_initial_data", new ParamsOfUpdateInitialData(abi, data, initialData, initialPubkey, bocCache), ResultOfUpdateInitialData.class);
    }

    /**
     * Encodes initial account data with initial values for the contract's static variables and owner's public key into a data BOC that can be passed to `encode_tvc` function afterwards.
     *
     * @param abi           Contract ABI
     * @param initialData   List of initial values for contract's static variables. `abi` parameter should be provided to set initial data
     * @param initialPubkey Initial account owner's public key to set into account data
     * @param bocCache      Cache type to put the result. The BOC itself returned if no cache type provided.
     */
    public static CompletableFuture<ResultOfEncodeInitialData> encodeInitialData(@NonNull Context context, ABI abi, Map<String, Object> initialData, String initialPubkey, Boc.BocCacheType bocCache) {
        return context.future("abi.encode_initial_data", new ParamsOfEncodeInitialData(abi, initialData, initialPubkey, bocCache), ResultOfEncodeInitialData.class);
    }

    /**
     * Decodes initial values of a contract's static variables and owner's public key from account initial data This operation is applicable only for initial account data (before deploy). If the contract is already deployed, its data doesn't contain this data section any more.
     *
     * @param abi  Contract ABI. Initial data is decoded if this parameter is provided
     * @param data Data BOC or BOC handle
     */
    public static CompletableFuture<ResultOfDecodeInitialData> decodeInitialData(@NonNull Context context, ABI abi, @NonNull String data) {
        return context.future("abi.decode_initial_data", new ParamsOfDecodeInitialData(abi, data), ResultOfDecodeInitialData.class);
    }

    /**
     * Decodes BOC into JSON as a set of provided parameters.
     *
     * @param params       Parameters to decode from BOC
     * @param boc          Data BOC or BOC handle
     * @param allowPartial
     */
    public static CompletableFuture<ResultOfDecodeBoc> decodeBoc(@NonNull Context context, @NonNull AbiParam[] params, @NonNull String boc, @NonNull Boolean allowPartial) {
        return context.future("abi.decode_boc", new ParamsOfDecodeBoc(params, boc, allowPartial), ResultOfDecodeBoc.class);
    }

    /**
     *
     */
    public enum MessageBodyType {

        /**
         *
         */
        Input,

        /**
         *
         */
        Output,

        /**
         * Occurs when contract sends an internal message to othercontract.
         */
        InternalOutput,

        /**
         *
         */
        Event
    }

    public static abstract class ABI {


        @Value
        public static class Contract extends ABI {

            @SerializedName("type")
            @NonNull String type = "Contract";

            @SerializedName("value")
            @NonNull Object value;

        }


        @Value
        public static class Json extends ABI {

            @SerializedName("type")
            @NonNull String type = "Json";

            @SerializedName("value")
            @NonNull String value;

        }


        @Value
        public static class Handle extends ABI {

            @SerializedName("type")
            @NonNull String type = "Handle";

            @SerializedName("value")
            @NonNull Integer value;

        }


        @Value
        public static class Serialized extends ABI {

            @SerializedName("type")
            @NonNull String type = "Serialized";

            @SerializedName("value")
            @NonNull Map<String, Object> value;

        }
    }

    @Value
    public static class FunctionHeader extends JsonData {
        @SerializedName("expire")
        @Getter(AccessLevel.NONE)
        Number expire;
        @SerializedName("time")
        @Getter(AccessLevel.NONE)
        Long time;
        @SerializedName("pubkey")
        @Getter(AccessLevel.NONE)
        String pubkey;

        /**
         * Message expiration time in seconds. If not specified - calculated automatically from message_expiration_timeout(), try_index and message_expiration_timeout_grow_factor() (if ABI includes `expire` header).
         */
        public Optional<Number> expire() {
            return Optional.ofNullable(this.expire);
        }

        /**
         * Message creation time in milliseconds.
         */
        public Optional<Long> time() {
            return Optional.ofNullable(this.time);
        }

        /**
         * Public key is used by the contract to check the signature.
         */
        public Optional<String> pubkey() {
            return Optional.ofNullable(this.pubkey);
        }

    }

    @Value
    public static class CallSet extends JsonData {

        /**
         * Function name that is being called. Or function id encoded as string in hex (starting with 0x).
         */
        @SerializedName("function_name")
        @NonNull String functionName;
        @SerializedName("header")
        @Getter(AccessLevel.NONE)
        FunctionHeader header;
        @SerializedName("input")
        @Getter(AccessLevel.NONE)
        Map<String, Object> input;

        /**
         * Function header.
         */
        public Optional<FunctionHeader> header() {
            return Optional.ofNullable(this.header);
        }

        /**
         * Function input parameters according to ABI.
         */
        public Optional<Map<String, Object>> input() {
            return Optional.ofNullable(this.input);
        }

    }

    @Value
    public static class DeploySet extends JsonData {

        /**
         * Content of TVC file encoded in `base64`.
         */
        @SerializedName("tvc")
        @NonNull String tvc;
        @SerializedName("workchain_id")
        @Getter(AccessLevel.NONE)
        Number workchainId;
        @SerializedName("initial_data")
        @Getter(AccessLevel.NONE)
        Map<String, Object> initialData;
        @SerializedName("initial_pubkey")
        @Getter(AccessLevel.NONE)
        String initialPubkey;

        /**
         * Target workchain for destination address.
         */
        public Optional<Number> workchainId() {
            return Optional.ofNullable(this.workchainId);
        }

        /**
         * List of initial values for contract's public variables.
         */
        public Optional<Map<String, Object>> initialData() {
            return Optional.ofNullable(this.initialData);
        }

        /**
         * Optional public key that can be provided in deploy set in order to substitute one in TVM file or provided by Signer.
         */
        public Optional<String> initialPubkey() {
            return Optional.ofNullable(this.initialPubkey);
        }

    }

    public static abstract class Signer {

        public static final None None = new None();

        /**
         * Creates an unsigned message.
         */
        @Value
        public static class None extends Signer {

            @SerializedName("type")
            @NonNull String type = "None";

        }


        @Value
        public static class External extends Signer {

            @SerializedName("type")
            @NonNull String type = "External";

            @SerializedName("public_key")
            @NonNull String publicKey;

        }


        @Value
        public static class Keys extends Signer {

            @SerializedName("type")
            @NonNull String type = "Keys";

            @SerializedName("keys")
            @NonNull Crypto.KeyPair keys;

        }


        @Value
        public static class SigningBox extends Signer {

            @SerializedName("type")
            @NonNull String type = "SigningBox";

            @SerializedName("handle")
            @NonNull Integer handle;

        }
    }

    public static abstract class StateInitSource {


        @Value
        public static class Message extends StateInitSource {
            @SerializedName("source")
            @NonNull Map<String, Object> source;

        }


        @Value
        public static class StateInit extends StateInitSource {

            /**
             * Code BOC.
             */
            @SerializedName("code")
            @NonNull String code;

            /**
             * Data BOC.
             */
            @SerializedName("data")
            @NonNull String data;
            @SerializedName("library")
            @Getter(AccessLevel.NONE)
            String library;

            /**
             * Library BOC.
             */
            public Optional<String> library() {
                return Optional.ofNullable(this.library);
            }

        }

        /**
         * Encoded in `base64`.
         */
        @Value
        public static class Tvc extends StateInitSource {
            @SerializedName("tvc")
            @NonNull String tvc;
            @SerializedName("public_key")
            @Getter(AccessLevel.NONE)
            String publicKey;
            @SerializedName("init_params")
            @Getter(AccessLevel.NONE)
            Map<String, Object> initParams;

            public Optional<String> publicKey() {
                return Optional.ofNullable(this.publicKey);
            }

            public Optional<Map<String, Object>> initParams() {
                return Optional.ofNullable(this.initParams);
            }

        }
    }

    @Value
    public static class AbiParam extends JsonData {
        @SerializedName("name")
        @NonNull String name;
        @SerializedName("type")
        @NonNull String type;
        @SerializedName("components")
        @Getter(AccessLevel.NONE)
        AbiParam[] components;

        public Optional<AbiParam[]> components() {
            return Optional.ofNullable(this.components);
        }

    }

    @Value
    public static class ParamsOfEncodeMessageBody extends JsonData {

        /**
         * Contract ABI.
         */
        @SerializedName("abi")
        @NonNull ABI abi;

        /**
         * Function call parameters.
         */
        @SerializedName("call_set")
        @NonNull CallSet callSet;

        /**
         * True if internal message body must be encoded.
         */
        @SerializedName("is_internal")
        @NonNull Boolean isInternal;

        /**
         * Signing parameters.
         */
        @SerializedName("signer")
        @NonNull Signer signer;
        @SerializedName("processing_try_index")
        @Getter(AccessLevel.NONE)
        Number processingTryIndex;

        /**
         * Processing try index.
         */
        public Optional<Number> processingTryIndex() {
            return Optional.ofNullable(this.processingTryIndex);
        }

    }

    @Value
    public static class ResultOfEncodeMessageBody extends JsonData {

        /**
         * Message body BOC encoded with `base64`.
         */
        @SerializedName("body")
        @NonNull String body;
        @SerializedName("data_to_sign")
        @Getter(AccessLevel.NONE)
        String dataToSign;

        /**
         * Optional data to sign.
         */
        public Optional<String> dataToSign() {
            return Optional.ofNullable(this.dataToSign);
        }

    }

    @Value
    public static class ParamsOfAttachSignatureToMessageBody extends JsonData {

        /**
         * Contract ABI
         */
        @SerializedName("abi")
        @NonNull ABI abi;

        /**
         * Public key.
         */
        @SerializedName("public_key")
        @NonNull String publicKey;

        /**
         * Unsigned message body BOC.
         */
        @SerializedName("message")
        @NonNull String message;

        /**
         * Signature.
         */
        @SerializedName("signature")
        @NonNull String signature;

    }

    @Value
    public static class ResultOfAttachSignatureToMessageBody extends JsonData {
        @SerializedName("body")
        @NonNull String body;

    }

    @Value
    public static class ParamsOfEncodeMessage extends JsonData {

        /**
         * Contract ABI.
         */
        @SerializedName("abi")
        @NonNull ABI abi;
        @SerializedName("address")
        @Getter(AccessLevel.NONE)
        String address;
        @SerializedName("deploy_set")
        @Getter(AccessLevel.NONE)
        DeploySet deploySet;
        @SerializedName("call_set")
        @Getter(AccessLevel.NONE)
        CallSet callSet;
        /**
         * Signing parameters.
         */
        @SerializedName("signer")
        @NonNull Signer signer;
        @SerializedName("processing_try_index")
        @Getter(AccessLevel.NONE)
        Number processingTryIndex;

        /**
         * Target address the message will be sent to.
         */
        public Optional<String> address() {
            return Optional.ofNullable(this.address);
        }

        /**
         * Deploy parameters.
         */
        public Optional<DeploySet> deploySet() {
            return Optional.ofNullable(this.deploySet);
        }

        /**
         * Function call parameters.
         */
        public Optional<CallSet> callSet() {
            return Optional.ofNullable(this.callSet);
        }

        /**
         * Processing try index.
         */
        public Optional<Number> processingTryIndex() {
            return Optional.ofNullable(this.processingTryIndex);
        }

    }

    @Value
    public static class ResultOfEncodeMessage extends JsonData {

        /**
         * Message BOC encoded with `base64`.
         */
        @SerializedName("message")
        @NonNull String message;
        @SerializedName("data_to_sign")
        @Getter(AccessLevel.NONE)
        String dataToSign;
        /**
         * Destination address.
         */
        @SerializedName("address")
        @NonNull String address;
        /**
         * Message id.
         */
        @SerializedName("message_id")
        @NonNull String messageId;

        /**
         * Optional data to be signed encoded in `base64`.
         */
        public Optional<String> dataToSign() {
            return Optional.ofNullable(this.dataToSign);
        }

    }

    @Value
    public static class ParamsOfEncodeInternalMessage extends JsonData {
        @SerializedName("abi")
        @Getter(AccessLevel.NONE)
        ABI abi;
        @SerializedName("address")
        @Getter(AccessLevel.NONE)
        String address;
        @SerializedName("src_address")
        @Getter(AccessLevel.NONE)
        String srcAddress;
        @SerializedName("deploy_set")
        @Getter(AccessLevel.NONE)
        DeploySet deploySet;
        @SerializedName("call_set")
        @Getter(AccessLevel.NONE)
        CallSet callSet;
        /**
         * Value in nanotokens to be sent with message.
         */
        @SerializedName("value")
        @NonNull String value;
        @SerializedName("bounce")
        @Getter(AccessLevel.NONE)
        Boolean bounce;
        @SerializedName("enable_ihr")
        @Getter(AccessLevel.NONE)
        Boolean enableIhr;

        /**
         * Contract ABI.
         */
        public Optional<ABI> abi() {
            return Optional.ofNullable(this.abi);
        }

        /**
         * Target address the message will be sent to.
         */
        public Optional<String> address() {
            return Optional.ofNullable(this.address);
        }

        /**
         * Source address of the message.
         */
        public Optional<String> srcAddress() {
            return Optional.ofNullable(this.srcAddress);
        }

        /**
         * Deploy parameters.
         */
        public Optional<DeploySet> deploySet() {
            return Optional.ofNullable(this.deploySet);
        }

        /**
         * Function call parameters.
         */
        public Optional<CallSet> callSet() {
            return Optional.ofNullable(this.callSet);
        }

        /**
         * Flag of bounceable message.
         */
        public Optional<Boolean> bounce() {
            return Optional.ofNullable(this.bounce);
        }

        /**
         * Enable Instant Hypercube Routing for the message.
         */
        public Optional<Boolean> enableIhr() {
            return Optional.ofNullable(this.enableIhr);
        }

    }

    @Value
    public static class ResultOfEncodeInternalMessage extends JsonData {

        /**
         * Message BOC encoded with `base64`.
         */
        @SerializedName("message")
        @NonNull String message;

        /**
         * Destination address.
         */
        @SerializedName("address")
        @NonNull String address;

        /**
         * Message id.
         */
        @SerializedName("message_id")
        @NonNull String messageId;

    }

    @Value
    public static class ParamsOfAttachSignature extends JsonData {

        /**
         * Contract ABI
         */
        @SerializedName("abi")
        @NonNull ABI abi;

        /**
         * Public key encoded in `hex`.
         */
        @SerializedName("public_key")
        @NonNull String publicKey;

        /**
         * Unsigned message BOC encoded in `base64`.
         */
        @SerializedName("message")
        @NonNull String message;

        /**
         * Signature encoded in `hex`.
         */
        @SerializedName("signature")
        @NonNull String signature;

    }

    @Value
    public static class ResultOfAttachSignature extends JsonData {

        /**
         * Signed message BOC
         */
        @SerializedName("message")
        @NonNull String message;

        /**
         * Message ID
         */
        @SerializedName("message_id")
        @NonNull String messageId;

    }

    @Value
    public static class ParamsOfDecodeMessage extends JsonData {

        /**
         * contract ABI
         */
        @SerializedName("abi")
        @NonNull ABI abi;

        /**
         * Message BOC
         */
        @SerializedName("message")
        @NonNull String message;

    }

    @Value
    public static class DecodedMessageBody extends JsonData {

        /**
         * Type of the message body content.
         */
        @SerializedName("body_type")
        @NonNull MessageBodyType bodyType;

        /**
         * Function or event name.
         */
        @SerializedName("name")
        @NonNull String name;
        @SerializedName("value")
        @Getter(AccessLevel.NONE)
        Map<String, Object> value;
        @SerializedName("header")
        @Getter(AccessLevel.NONE)
        FunctionHeader header;

        /**
         * Parameters or result value.
         */
        public Optional<Map<String, Object>> value() {
            return Optional.ofNullable(this.value);
        }

        /**
         * Function header.
         */
        public Optional<FunctionHeader> header() {
            return Optional.ofNullable(this.header);
        }

    }

    @Value
    public static class ParamsOfDecodeMessageBody extends JsonData {

        /**
         * Contract ABI used to decode.
         */
        @SerializedName("abi")
        @NonNull ABI abi;

        /**
         * Message body BOC encoded in `base64`.
         */
        @SerializedName("body")
        @NonNull String body;

        /**
         * True if the body belongs to the internal message.
         */
        @SerializedName("is_internal")
        @NonNull Boolean isInternal;

    }

    @Value
    public static class ParamsOfEncodeAccount extends JsonData {

        /**
         * Source of the account state init.
         */
        @SerializedName("state_init")
        @NonNull StateInitSource stateInit;
        @SerializedName("balance")
        @Getter(AccessLevel.NONE)
        Long balance;
        @SerializedName("last_trans_lt")
        @Getter(AccessLevel.NONE)
        Long lastTransLt;
        @SerializedName("last_paid")
        @Getter(AccessLevel.NONE)
        Number lastPaid;
        @SerializedName("boc_cache")
        @Getter(AccessLevel.NONE)
        Boc.BocCacheType bocCache;

        /**
         * Initial balance.
         */
        public Optional<Long> balance() {
            return Optional.ofNullable(this.balance);
        }

        /**
         * Initial value for the `last_trans_lt`.
         */
        public Optional<Long> lastTransLt() {
            return Optional.ofNullable(this.lastTransLt);
        }

        /**
         * Initial value for the `last_paid`.
         */
        public Optional<Number> lastPaid() {
            return Optional.ofNullable(this.lastPaid);
        }

        /**
         * Cache type to put the result.
         */
        public Optional<Boc.BocCacheType> bocCache() {
            return Optional.ofNullable(this.bocCache);
        }

    }

    @Value
    public static class ResultOfEncodeAccount extends JsonData {

        /**
         * Account BOC encoded in `base64`.
         */
        @SerializedName("account")
        @NonNull String account;

        /**
         * Account ID  encoded in `hex`.
         */
        @SerializedName("id")
        @NonNull String id;

    }

    @Value
    public static class ParamsOfDecodeAccountData extends JsonData {

        /**
         * Contract ABI
         */
        @SerializedName("abi")
        @NonNull ABI abi;

        /**
         * Data BOC or BOC handle
         */
        @SerializedName("data")
        @NonNull String data;

    }

    @Value
    public static class ResultOfDecodeAccountData extends JsonData {

        /**
         * Decoded data as a JSON structure.
         */
        @SerializedName("data")
        @NonNull Map<String, Object> data;

    }

    @Value
    public static class ParamsOfUpdateInitialData extends JsonData {
        @SerializedName("abi")
        @Getter(AccessLevel.NONE)
        ABI abi;
        /**
         * Data BOC or BOC handle
         */
        @SerializedName("data")
        @NonNull String data;
        @SerializedName("initial_data")
        @Getter(AccessLevel.NONE)
        Map<String, Object> initialData;
        @SerializedName("initial_pubkey")
        @Getter(AccessLevel.NONE)
        String initialPubkey;
        @SerializedName("boc_cache")
        @Getter(AccessLevel.NONE)
        Boc.BocCacheType bocCache;

        /**
         * Contract ABI
         */
        public Optional<ABI> abi() {
            return Optional.ofNullable(this.abi);
        }

        /**
         * List of initial values for contract's static variables.
         */
        public Optional<Map<String, Object>> initialData() {
            return Optional.ofNullable(this.initialData);
        }

        /**
         * Initial account owner's public key to set into account data
         */
        public Optional<String> initialPubkey() {
            return Optional.ofNullable(this.initialPubkey);
        }

        /**
         * Cache type to put the result. The BOC itself returned if no cache type provided.
         */
        public Optional<Boc.BocCacheType> bocCache() {
            return Optional.ofNullable(this.bocCache);
        }

    }

    @Value
    public static class ResultOfUpdateInitialData extends JsonData {

        /**
         * Updated data BOC or BOC handle
         */
        @SerializedName("data")
        @NonNull String data;

    }

    @Value
    public static class ParamsOfEncodeInitialData extends JsonData {
        @SerializedName("abi")
        @Getter(AccessLevel.NONE)
        ABI abi;
        @SerializedName("initial_data")
        @Getter(AccessLevel.NONE)
        Map<String, Object> initialData;
        @SerializedName("initial_pubkey")
        @Getter(AccessLevel.NONE)
        String initialPubkey;
        @SerializedName("boc_cache")
        @Getter(AccessLevel.NONE)
        Boc.BocCacheType bocCache;

        /**
         * Contract ABI
         */
        public Optional<ABI> abi() {
            return Optional.ofNullable(this.abi);
        }

        /**
         * List of initial values for contract's static variables.
         */
        public Optional<Map<String, Object>> initialData() {
            return Optional.ofNullable(this.initialData);
        }

        /**
         * Initial account owner's public key to set into account data
         */
        public Optional<String> initialPubkey() {
            return Optional.ofNullable(this.initialPubkey);
        }

        /**
         * Cache type to put the result. The BOC itself returned if no cache type provided.
         */
        public Optional<Boc.BocCacheType> bocCache() {
            return Optional.ofNullable(this.bocCache);
        }

    }

    @Value
    public static class ResultOfEncodeInitialData extends JsonData {

        /**
         * Updated data BOC or BOC handle
         */
        @SerializedName("data")
        @NonNull String data;

    }

    @Value
    public static class ParamsOfDecodeInitialData extends JsonData {
        @SerializedName("abi")
        @Getter(AccessLevel.NONE)
        ABI abi;
        /**
         * Data BOC or BOC handle
         */
        @SerializedName("data")
        @NonNull String data;

        /**
         * Contract ABI.
         */
        public Optional<ABI> abi() {
            return Optional.ofNullable(this.abi);
        }

    }

    @Value
    public static class ResultOfDecodeInitialData extends JsonData {
        @SerializedName("initial_data")
        @Getter(AccessLevel.NONE)
        Map<String, Object> initialData;
        /**
         * Initial account owner's public key
         */
        @SerializedName("initial_pubkey")
        @NonNull String initialPubkey;

        /**
         * List of initial values of contract's public variables.
         */
        public Optional<Map<String, Object>> initialData() {
            return Optional.ofNullable(this.initialData);
        }

    }

    @Value
    public static class ParamsOfDecodeBoc extends JsonData {

        /**
         * Parameters to decode from BOC
         */
        @SerializedName("params")
        @NonNull AbiParam[] params;

        /**
         * Data BOC or BOC handle
         */
        @SerializedName("boc")
        @NonNull String boc;
        @SerializedName("allow_partial")
        @NonNull Boolean allowPartial;

    }

    @Value
    public static class ResultOfDecodeBoc extends JsonData {

        /**
         * Decoded data as a JSON structure.
         */
        @SerializedName("data")
        @NonNull Map<String, Object> data;

    }

}
