package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.Optional;
import lombok.*;
import java.util.stream.*;
import java.util.Arrays;

/**
 *  <strong>abi</strong>
 *  Contains methods of "abi" module.

 *  Provides message encoding and decoding according to the ABI specification.
 *  @version EVER-SDK 1.37.0
 */
public class Abi {

    public interface ABI {


    /**
    * 
    * @param value 
    */
    public record Contract(@NonNull Map<String,Object> value) implements ABI {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * 
    * @param value 
    */
    public record Json(@NonNull String value) implements ABI {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * 
    * @param value 
    */
    public record Handle(@NonNull Integer value) implements ABI {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * 
    * @param value 
    */
    public record Serialized(@NonNull Map<String,Object> value) implements ABI {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}

    /**
    * 
    * @param expire Message expiration time in seconds. If not specified - calculated automatically from message_expiration_timeout(), try_index and message_expiration_timeout_grow_factor() (if ABI includes `expire` header).
    * @param time Message creation time in milliseconds. If not specified, `now` is used (if ABI includes `time` header).
    * @param pubkey Public key is used by the contract to check the signature. Encoded in `hex`. If not specified, method fails with exception (if ABI includes `pubkey` header)..
    */
    public record FunctionHeader(Number expire, Long time, String pubkey) {}

    /**
    * 
    * @param functionName Function name that is being called. Or function id encoded as string in hex (starting with 0x).
    * @param header Function header. If an application omits some header parameters required by thecontract's ABI, the library will set the default values forthem.
    * @param input Function input parameters according to ABI.
    */
    public record CallSet(@NonNull String functionName, FunctionHeader header, Map<String,Object> input) {}

    /**
    * 
    * @param tvc Content of TVC file encoded in `base64`.
    * @param workchainId Target workchain for destination address. Default is `0`.
    * @param initialData List of initial values for contract's public variables.
    * @param initialPubkey Optional public key that can be provided in deploy set in order to substitute one in TVM file or provided by Signer. Public key resolving priority:1. Public key from deploy set.2. Public key, specified in TVM file.3. Public key, provided by Signer.
    */
    public record DeploySet(@NonNull String tvc, Number workchainId, Map<String,Object> initialData, String initialPubkey) {}
    public interface Signer {

        public static final None NONE = new None();


    /**
    * No keys are provided. Creates an unsigned message.

    */
    public record None() implements Signer {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Only public key is provided in unprefixed hex string format to generate unsigned message and `data_to_sign` which can be signed later.
    * @param publicKey 
    */
    public record External(@NonNull String publicKey) implements Signer {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Key pair is provided for signing
    * @param keys 
    */
    public record Keys(@NonNull Crypto.KeyPair keys) implements Signer {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Signing Box interface is provided for signing, allows Dapps to sign messages using external APIs, such as HSM, cold wallet, etc.
    * @param handle 
    */
    public record SigningBox(@NonNull Integer handle) implements Signer {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}
    public enum MessageBodyType {
        
        /**
        * Message contains the input of the ABI function.
        */
        Input,

        /**
        * Message contains the output of the ABI function.
        */
        Output,

        /**
        * Message contains the input of the imported ABI function. Occurs when contract sends an internal message to othercontract.
        */
        InternalOutput,

        /**
        * Message contains the input of the ABI event.
        */
        Event
    }
    public interface StateInitSource {


    /**
    * Deploy message.
    * @param source 
    */
    public record Message(@NonNull Map<String,Object> source) implements StateInitSource {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * State init data.
    * @param code Code BOC. Encoded in `base64`.
    * @param data Data BOC. Encoded in `base64`.
    * @param library Library BOC. Encoded in `base64`.
    */
    public record StateInit(@NonNull String code, @NonNull String data, String library) implements StateInitSource {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Content of the TVC file. Encoded in `base64`.
    * @param tvc 
    * @param publicKey 
    * @param initParams 
    */
    public record Tvc(@NonNull String tvc, String publicKey, Map<String,Object> initParams) implements StateInitSource {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}

    /**
    * 
    * @param name 
    * @param type 
    * @param components 
    */
    public record AbiParam(@NonNull String name, @NonNull String type, AbiParam[] components) {}

    /**
    * 
    * @param abi Contract ABI.
    * @param callSet Function call parameters. Must be specified in non deploy message.<p>In case of deploy message contains parameters of constructor.
    * @param isInternal True if internal message body must be encoded.
    * @param signer Signing parameters.
    * @param processingTryIndex Processing try index. Used in message processing with retries.<p>Encoder uses the provided try index to calculate messageexpiration time.<p>Expiration timeouts will grow with every retry.<p>Default value is 0.
    * @param address Destination address of the message Since ABI version 2.3 destination address of external inbound message is used in messagebody signature calculation. Should be provided when signed external inbound message body iscreated. Otherwise can be omitted.
    */
    public record ParamsOfEncodeMessageBody(@NonNull ABI abi, @NonNull CallSet callSet, @NonNull Boolean isInternal, @NonNull Signer signer, Number processingTryIndex, String address) {}

    /**
    * 
    * @param body Message body BOC encoded with `base64`.
    * @param dataToSign Optional data to sign. Encoded with `base64`. Presents when `message` is unsigned. Can be used for externalmessage signing. Is this case you need to sing this data andproduce signed message using `abi.attach_signature`.
    */
    public record ResultOfEncodeMessageBody(@NonNull String body, String dataToSign) {}

    /**
    * 
    * @param abi Contract ABI
    * @param publicKey Public key. Must be encoded with `hex`.
    * @param message Unsigned message body BOC. Must be encoded with `base64`.
    * @param signature Signature. Must be encoded with `hex`.
    */
    public record ParamsOfAttachSignatureToMessageBody(@NonNull ABI abi, @NonNull String publicKey, @NonNull String message, @NonNull String signature) {}

    /**
    * 
    * @param body 
    */
    public record ResultOfAttachSignatureToMessageBody(@NonNull String body) {}

    /**
    * 
    * @param abi Contract ABI.
    * @param address Target address the message will be sent to. Must be specified in case of non-deploy message.
    * @param deploySet Deploy parameters. Must be specified in case of deploy message.
    * @param callSet Function call parameters. Must be specified in case of non-deploy message.<p>In case of deploy message it is optional and contains parametersof the functions that will to be called upon deploy transaction.
    * @param signer Signing parameters.
    * @param processingTryIndex Processing try index. Used in message processing with retries (if contract's ABI includes "expire" header).<p>Encoder uses the provided try index to calculate messageexpiration time. The 1st message expiration time is specified inClient config.<p>Expiration timeouts will grow with every retry.Retry grow factor is set in Client config:&lt;.....add config parameter with default value here&gt;<p>Default value is 0.
    */
    public record ParamsOfEncodeMessage(@NonNull ABI abi, String address, DeploySet deploySet, CallSet callSet, @NonNull Signer signer, Number processingTryIndex) {}

    /**
    * 
    * @param message Message BOC encoded with `base64`.
    * @param dataToSign Optional data to be signed encoded in `base64`. Returned in case of `Signer::External`. Can be used for externalmessage signing. Is this case you need to use this data to create signature andthen produce signed message using `abi.attach_signature`.
    * @param address Destination address.
    * @param messageId Message id.
    */
    public record ResultOfEncodeMessage(@NonNull String message, String dataToSign, @NonNull String address, @NonNull String messageId) {}

    /**
    * 
    * @param abi Contract ABI. Can be None if both deploy_set and call_set are None.
    * @param address Target address the message will be sent to. Must be specified in case of non-deploy message.
    * @param srcAddress Source address of the message.
    * @param deploySet Deploy parameters. Must be specified in case of deploy message.
    * @param callSet Function call parameters. Must be specified in case of non-deploy message.<p>In case of deploy message it is optional and contains parametersof the functions that will to be called upon deploy transaction.
    * @param value Value in nanotokens to be sent with message.
    * @param bounce Flag of bounceable message. Default is true.
    * @param enableIhr Enable Instant Hypercube Routing for the message. Default is false.
    */
    public record ParamsOfEncodeInternalMessage(ABI abi, String address, String srcAddress, DeploySet deploySet, CallSet callSet, @NonNull String value, Boolean bounce, Boolean enableIhr) {}

    /**
    * 
    * @param message Message BOC encoded with `base64`.
    * @param address Destination address.
    * @param messageId Message id.
    */
    public record ResultOfEncodeInternalMessage(@NonNull String message, @NonNull String address, @NonNull String messageId) {}

    /**
    * 
    * @param abi Contract ABI
    * @param publicKey Public key encoded in `hex`.
    * @param message Unsigned message BOC encoded in `base64`.
    * @param signature Signature encoded in `hex`.
    */
    public record ParamsOfAttachSignature(@NonNull ABI abi, @NonNull String publicKey, @NonNull String message, @NonNull String signature) {}

    /**
    * 
    * @param message Signed message BOC
    * @param messageId Message ID
    */
    public record ResultOfAttachSignature(@NonNull String message, @NonNull String messageId) {}

    /**
    * 
    * @param abi contract ABI
    * @param message Message BOC
    * @param allowPartial Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default)
    */
    public record ParamsOfDecodeMessage(@NonNull ABI abi, @NonNull String message, Boolean allowPartial) {}

    /**
    * 
    * @param bodyType Type of the message body content.
    * @param name Function or event name.
    * @param value Parameters or result value.
    * @param header Function header.
    */
    public record DecodedMessageBody(@NonNull MessageBodyType bodyType, @NonNull String name, Map<String,Object> value, FunctionHeader header) {}

    /**
    * 
    * @param abi Contract ABI used to decode.
    * @param body Message body BOC encoded in `base64`.
    * @param isInternal True if the body belongs to the internal message.
    * @param allowPartial Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default)
    */
    public record ParamsOfDecodeMessageBody(@NonNull ABI abi, @NonNull String body, @NonNull Boolean isInternal, Boolean allowPartial) {}

    /**
    * 
    * @param stateInit Source of the account state init.
    * @param balance Initial balance.
    * @param lastTransLt Initial value for the `last_trans_lt`.
    * @param lastPaid Initial value for the `last_paid`.
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided
    */
    public record ParamsOfEncodeAccount(@NonNull StateInitSource stateInit, Long balance, Long lastTransLt, Number lastPaid, Boc.BocCacheType bocCache) {}

    /**
    * 
    * @param account Account BOC encoded in `base64`.
    * @param id Account ID  encoded in `hex`.
    */
    public record ResultOfEncodeAccount(@NonNull String account, @NonNull String id) {}

    /**
    * 
    * @param abi Contract ABI
    * @param data Data BOC or BOC handle
    * @param allowPartial Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default)
    */
    public record ParamsOfDecodeAccountData(@NonNull ABI abi, @NonNull String data, Boolean allowPartial) {}

    /**
    * 
    * @param data Decoded data as a JSON structure.
    */
    public record ResultOfDecodeAccountData(@NonNull Map<String,Object> data) {}

    /**
    * 
    * @param abi Contract ABI
    * @param data Data BOC or BOC handle
    * @param initialData List of initial values for contract's static variables. `abi` parameter should be provided to set initial data
    * @param initialPubkey Initial account owner's public key to set into account data
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided.
    */
    public record ParamsOfUpdateInitialData(ABI abi, @NonNull String data, Map<String,Object> initialData, String initialPubkey, Boc.BocCacheType bocCache) {}

    /**
    * 
    * @param data Updated data BOC or BOC handle
    */
    public record ResultOfUpdateInitialData(@NonNull String data) {}

    /**
    * 
    * @param abi Contract ABI
    * @param initialData List of initial values for contract's static variables. `abi` parameter should be provided to set initial data
    * @param initialPubkey Initial account owner's public key to set into account data
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided.
    */
    public record ParamsOfEncodeInitialData(ABI abi, Map<String,Object> initialData, String initialPubkey, Boc.BocCacheType bocCache) {}

    /**
    * 
    * @param data Updated data BOC or BOC handle
    */
    public record ResultOfEncodeInitialData(@NonNull String data) {}

    /**
    * 
    * @param abi Contract ABI. Initial data is decoded if this parameter is provided
    * @param data Data BOC or BOC handle
    * @param allowPartial Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default)
    */
    public record ParamsOfDecodeInitialData(ABI abi, @NonNull String data, Boolean allowPartial) {}

    /**
    * 
    * @param initialData List of initial values of contract's public variables. Initial data is decoded if `abi` input parameter is provided
    * @param initialPubkey Initial account owner's public key
    */
    public record ResultOfDecodeInitialData(Map<String,Object> initialData, @NonNull String initialPubkey) {}

    /**
    * 
    * @param params Parameters to decode from BOC
    * @param boc Data BOC or BOC handle
    * @param allowPartial 
    */
    public record ParamsOfDecodeBoc(@NonNull AbiParam[] params, @NonNull String boc, @NonNull Boolean allowPartial) {}

    /**
    * 
    * @param data Decoded data as a JSON structure.
    */
    public record ResultOfDecodeBoc(@NonNull Map<String,Object> data) {}

    /**
    * 
    * @param params Parameters to encode into BOC
    * @param data Parameters and values as a JSON structure
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided
    */
    public record ParamsOfAbiEncodeBoc(@NonNull AbiParam[] params, @NonNull Map<String,Object> data, Boc.BocCacheType bocCache) {}

    /**
    * 
    * @param boc BOC encoded as base64
    */
    public record ResultOfAbiEncodeBoc(@NonNull String boc) {}

    /**
    * 
    * @param abi Contract ABI.
    * @param functionName Contract function name
    * @param output If set to `true` output function ID will be returned which is used in contract response. Default is `false`
    */
    public record ParamsOfCalcFunctionId(@NonNull ABI abi, @NonNull String functionName, Boolean output) {}

    /**
    * 
    * @param functionId Contract function ID
    */
    public record ResultOfCalcFunctionId(@NonNull Number functionId) {}
    /**
    * <strong>abi.encode_message_body</strong>
    * Encodes message body according to ABI function call.
    * @param abi Contract ABI. 
    * @param callSet Function call parameters. Must be specified in non deploy message.<p>In case of deploy message contains parameters of constructor.
    * @param isInternal True if internal message body must be encoded. 
    * @param signer Signing parameters. 
    * @param processingTryIndex Processing try index. Used in message processing with retries.<p>Encoder uses the provided try index to calculate messageexpiration time.<p>Expiration timeouts will grow with every retry.<p>Default value is 0.
    * @param address Destination address of the message Since ABI version 2.3 destination address of external inbound message is used in messagebody signature calculation. Should be provided when signed external inbound message body iscreated. Otherwise can be omitted.
    * @return {@link tech.deplant.java4ever.binding.Abi.ResultOfEncodeMessageBody}
    */
    public static ResultOfEncodeMessageBody encodeMessageBody(@NonNull Context ctx, @NonNull ABI abi, @NonNull CallSet callSet, @NonNull Boolean isInternal, @NonNull Signer signer,  Number processingTryIndex,  String address)  throws JsonProcessingException {
        return  ctx.call("abi.encode_message_body", new ParamsOfEncodeMessageBody(abi, callSet, isInternal, signer, processingTryIndex, address), ResultOfEncodeMessageBody.class);
    }

    /**
    * <strong>abi.attach_signature_to_message_body</strong>
    * 
    * @param abi Contract ABI 
    * @param publicKey Public key. Must be encoded with `hex`.
    * @param message Unsigned message body BOC. Must be encoded with `base64`.
    * @param signature Signature. Must be encoded with `hex`.
    * @return {@link tech.deplant.java4ever.binding.Abi.ResultOfAttachSignatureToMessageBody}
    */
    public static ResultOfAttachSignatureToMessageBody attachSignatureToMessageBody(@NonNull Context ctx, @NonNull ABI abi, @NonNull String publicKey, @NonNull String message, @NonNull String signature)  throws JsonProcessingException {
        return  ctx.call("abi.attach_signature_to_message_body", new ParamsOfAttachSignatureToMessageBody(abi, publicKey, message, signature), ResultOfAttachSignatureToMessageBody.class);
    }

    /**
    * <strong>abi.encode_message</strong>
    * Encodes an ABI-compatible message Allows to encode deploy and function call messages,both signed and unsigned.<p>Use cases include messages of any possible type:- deploy with initial function call (i.e. `constructor` or any other function that is used for some kindof initialization);- deploy without initial function call;- signed/unsigned + data for signing.<p>`Signer` defines how the message should or shouldn't be signed:<p>`Signer::None` creates an unsigned message. This may be needed in case of some public methods,that do not require authorization by pubkey.<p>`Signer::External` takes public key and returns `data_to_sign` for later signing.Use `attach_signature` method with the result signature to get the signed message.<p>`Signer::Keys` creates a signed message with provided key pair.
     * Soon `Signer::SigningBox` Allows using a special interface to implement signingwithout private key disclosure to SDK. For instance, in case of using a cold wallet or HSM,when application calls some API to sign data.
     * There is an optional public key can be provided in deploy set in order to substitute onein TVM file.<p>Public key resolving priority:1. Public key from deploy set.2. Public key, specified in TVM file.3. Public key, provided by signer.
    * @param abi Contract ABI. 
    * @param address Target address the message will be sent to. Must be specified in case of non-deploy message.
    * @param deploySet Deploy parameters. Must be specified in case of deploy message.
    * @param callSet Function call parameters. Must be specified in case of non-deploy message.<p>In case of deploy message it is optional and contains parametersof the functions that will to be called upon deploy transaction.
    * @param signer Signing parameters. 
    * @param processingTryIndex Processing try index. Used in message processing with retries (if contract's ABI includes "expire" header).<p>Encoder uses the provided try index to calculate messageexpiration time. The 1st message expiration time is specified inClient config.<p>Expiration timeouts will grow with every retry.Retry grow factor is set in Client config:&lt;.....add config parameter with default value here&gt;<p>Default value is 0.
    * @return {@link tech.deplant.java4ever.binding.Abi.ResultOfEncodeMessage}
    */
    public static ResultOfEncodeMessage encodeMessage(@NonNull Context ctx, @NonNull ABI abi,  String address,  DeploySet deploySet,  CallSet callSet, @NonNull Signer signer,  Number processingTryIndex)  throws JsonProcessingException {
        return  ctx.call("abi.encode_message", new ParamsOfEncodeMessage(abi, address, deploySet, callSet, signer, processingTryIndex), ResultOfEncodeMessage.class);
    }

    /**
    * <strong>abi.encode_internal_message</strong>
    * Encodes an internal ABI-compatible message Allows to encode deploy and function call messages.<p>Use cases include messages of any possible type:- deploy with initial function call (i.e. `constructor` or any other function that is used for some kindof initialization);- deploy without initial function call;- simple function call<p>There is an optional public key can be provided in deploy set in order to substitute onein TVM file.<p>Public key resolving priority:1. Public key from deploy set.2. Public key, specified in TVM file.
    * @param abi Contract ABI. Can be None if both deploy_set and call_set are None.
    * @param address Target address the message will be sent to. Must be specified in case of non-deploy message.
    * @param srcAddress Source address of the message. 
    * @param deploySet Deploy parameters. Must be specified in case of deploy message.
    * @param callSet Function call parameters. Must be specified in case of non-deploy message.<p>In case of deploy message it is optional and contains parametersof the functions that will to be called upon deploy transaction.
    * @param value Value in nanotokens to be sent with message. 
    * @param bounce Flag of bounceable message. Default is true.
    * @param enableIhr Enable Instant Hypercube Routing for the message. Default is false.
    * @return {@link tech.deplant.java4ever.binding.Abi.ResultOfEncodeInternalMessage}
    */
    public static ResultOfEncodeInternalMessage encodeInternalMessage(@NonNull Context ctx,  ABI abi,  String address,  String srcAddress,  DeploySet deploySet,  CallSet callSet, @NonNull String value,  Boolean bounce,  Boolean enableIhr)  throws JsonProcessingException {
        return  ctx.call("abi.encode_internal_message", new ParamsOfEncodeInternalMessage(abi, address, srcAddress, deploySet, callSet, value, bounce, enableIhr), ResultOfEncodeInternalMessage.class);
    }

    /**
    * <strong>abi.attach_signature</strong>
    * Combines `hex`-encoded `signature` with `base64`-encoded `unsigned_message`. Returns signed message encoded in `base64`.
    * @param abi Contract ABI 
    * @param publicKey Public key encoded in `hex`. 
    * @param message Unsigned message BOC encoded in `base64`. 
    * @param signature Signature encoded in `hex`. 
    * @return {@link tech.deplant.java4ever.binding.Abi.ResultOfAttachSignature}
    */
    public static ResultOfAttachSignature attachSignature(@NonNull Context ctx, @NonNull ABI abi, @NonNull String publicKey, @NonNull String message, @NonNull String signature)  throws JsonProcessingException {
        return  ctx.call("abi.attach_signature", new ParamsOfAttachSignature(abi, publicKey, message, signature), ResultOfAttachSignature.class);
    }

    /**
    * <strong>abi.decode_message</strong>
    * Decodes message body using provided message BOC and ABI.
    * @param abi contract ABI 
    * @param message Message BOC 
    * @param allowPartial Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default) 
    * @return {@link tech.deplant.java4ever.binding.Abi.DecodedMessageBody}
    */
    public static DecodedMessageBody decodeMessage(@NonNull Context ctx, @NonNull ABI abi, @NonNull String message,  Boolean allowPartial)  throws JsonProcessingException {
        return  ctx.call("abi.decode_message", new ParamsOfDecodeMessage(abi, message, allowPartial), DecodedMessageBody.class);
    }

    /**
    * <strong>abi.decode_message_body</strong>
    * Decodes message body using provided body BOC and ABI.
    * @param abi Contract ABI used to decode. 
    * @param body Message body BOC encoded in `base64`. 
    * @param isInternal True if the body belongs to the internal message. 
    * @param allowPartial Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default) 
    * @return {@link tech.deplant.java4ever.binding.Abi.DecodedMessageBody}
    */
    public static DecodedMessageBody decodeMessageBody(@NonNull Context ctx, @NonNull ABI abi, @NonNull String body, @NonNull Boolean isInternal,  Boolean allowPartial)  throws JsonProcessingException {
        return  ctx.call("abi.decode_message_body", new ParamsOfDecodeMessageBody(abi, body, isInternal, allowPartial), DecodedMessageBody.class);
    }

    /**
    * <strong>abi.encode_account</strong>
    * Creates account state BOC Creates account state provided with one of these sets of data :1. BOC of code, BOC of data, BOC of library2. TVC (string in `base64`), keys, init params
    * @param stateInit Source of the account state init. 
    * @param balance Initial balance. 
    * @param lastTransLt Initial value for the `last_trans_lt`. 
    * @param lastPaid Initial value for the `last_paid`. 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided
    * @return {@link tech.deplant.java4ever.binding.Abi.ResultOfEncodeAccount}
    */
    public static ResultOfEncodeAccount encodeAccount(@NonNull Context ctx, @NonNull StateInitSource stateInit,  Long balance,  Long lastTransLt,  Number lastPaid,  Boc.BocCacheType bocCache)  throws JsonProcessingException {
        return  ctx.call("abi.encode_account", new ParamsOfEncodeAccount(stateInit, balance, lastTransLt, lastPaid, bocCache), ResultOfEncodeAccount.class);
    }

    /**
    * <strong>abi.decode_account_data</strong>
    * Decodes account data using provided data BOC and ABI. Note: this feature requires ABI 2.1 or higher.
    * @param abi Contract ABI 
    * @param data Data BOC or BOC handle 
    * @param allowPartial Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default) 
    * @return {@link tech.deplant.java4ever.binding.Abi.ResultOfDecodeAccountData}
    */
    public static ResultOfDecodeAccountData decodeAccountData(@NonNull Context ctx, @NonNull ABI abi, @NonNull String data,  Boolean allowPartial)  throws JsonProcessingException {
        return  ctx.call("abi.decode_account_data", new ParamsOfDecodeAccountData(abi, data, allowPartial), ResultOfDecodeAccountData.class);
    }

    /**
    * <strong>abi.update_initial_data</strong>
    * Updates initial account data with initial values for the contract's static variables and owner's public key. This operation is applicable only for initial account data (before deploy). If the contract is already deployed, its data doesn't contain this data section any more.
    * @param abi Contract ABI 
    * @param data Data BOC or BOC handle 
    * @param initialData List of initial values for contract's static variables. `abi` parameter should be provided to set initial data
    * @param initialPubkey Initial account owner's public key to set into account data 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided. 
    * @return {@link tech.deplant.java4ever.binding.Abi.ResultOfUpdateInitialData}
    */
    public static ResultOfUpdateInitialData updateInitialData(@NonNull Context ctx,  ABI abi, @NonNull String data,  Map<String,Object> initialData,  String initialPubkey,  Boc.BocCacheType bocCache)  throws JsonProcessingException {
        return  ctx.call("abi.update_initial_data", new ParamsOfUpdateInitialData(abi, data, initialData, initialPubkey, bocCache), ResultOfUpdateInitialData.class);
    }

    /**
    * <strong>abi.encode_initial_data</strong>
    * Encodes initial account data with initial values for the contract's static variables and owner's public key into a data BOC that can be passed to `encode_tvc` function afterwards. This function is analogue of `tvm.buildDataInit` function in Solidity.
    * @param abi Contract ABI 
    * @param initialData List of initial values for contract's static variables. `abi` parameter should be provided to set initial data
    * @param initialPubkey Initial account owner's public key to set into account data 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided. 
    * @return {@link tech.deplant.java4ever.binding.Abi.ResultOfEncodeInitialData}
    */
    public static ResultOfEncodeInitialData encodeInitialData(@NonNull Context ctx,  ABI abi,  Map<String,Object> initialData,  String initialPubkey,  Boc.BocCacheType bocCache)  throws JsonProcessingException {
        return  ctx.call("abi.encode_initial_data", new ParamsOfEncodeInitialData(abi, initialData, initialPubkey, bocCache), ResultOfEncodeInitialData.class);
    }

    /**
    * <strong>abi.decode_initial_data</strong>
    * Decodes initial values of a contract's static variables and owner's public key from account initial data This operation is applicable only for initial account data (before deploy). If the contract is already deployed, its data doesn't contain this data section any more.
    * @param abi Contract ABI. Initial data is decoded if this parameter is provided
    * @param data Data BOC or BOC handle 
    * @param allowPartial Flag allowing partial BOC decoding when ABI doesn't describe the full body BOC. Controls decoder behaviour when after decoding all described in ABI params there are some data left in BOC: `true` - return decoded values `false` - return error of incomplete BOC deserialization (default) 
    * @return {@link tech.deplant.java4ever.binding.Abi.ResultOfDecodeInitialData}
    */
    public static ResultOfDecodeInitialData decodeInitialData(@NonNull Context ctx,  ABI abi, @NonNull String data,  Boolean allowPartial)  throws JsonProcessingException {
        return  ctx.call("abi.decode_initial_data", new ParamsOfDecodeInitialData(abi, data, allowPartial), ResultOfDecodeInitialData.class);
    }

    /**
    * <strong>abi.decode_boc</strong>
    * Decodes BOC into JSON as a set of provided parameters. Solidity functions use ABI types for builder encoding.The simplest way to decode such a BOC is to use ABI decoding.ABI has it own rules for fields layout in cells so manually encodedBOC can not be described in terms of ABI rules.<p>To solve this problem we introduce a new ABI type `Ref(&lt;ParamType&gt;)`which allows to store `ParamType` ABI parameter in cell reference and, thus,decode manually encoded BOCs. This type is available only in `decode_boc` functionand will not be available in ABI messages encoding until it is included into some ABI revision.<p>Such BOC descriptions covers most users needs. If someone wants to decode some BOC whichcan not be described by these rules (i.e. BOC with TLB containing constructors of flagsdefining some parsing conditions) then they can decode the fields up to fork condition,check the parsed data manually, expand the parsing schema and then decode the whole BOCwith the full schema.
    * @param params Parameters to decode from BOC 
    * @param boc Data BOC or BOC handle 
    * @param allowPartial  
    * @return {@link tech.deplant.java4ever.binding.Abi.ResultOfDecodeBoc}
    */
    public static ResultOfDecodeBoc decodeBoc(@NonNull Context ctx, @NonNull AbiParam[] params, @NonNull String boc, @NonNull Boolean allowPartial)  throws JsonProcessingException {
        return  ctx.call("abi.decode_boc", new ParamsOfDecodeBoc(params, boc, allowPartial), ResultOfDecodeBoc.class);
    }

    /**
    * <strong>abi.encode_boc</strong>
    * Encodes given parameters in JSON into a BOC using param types from ABI.
    * @param params Parameters to encode into BOC 
    * @param data Parameters and values as a JSON structure 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided
    * @return {@link tech.deplant.java4ever.binding.Abi.ResultOfAbiEncodeBoc}
    */
    public static ResultOfAbiEncodeBoc encodeBoc(@NonNull Context ctx, @NonNull AbiParam[] params, @NonNull Map<String,Object> data,  Boc.BocCacheType bocCache)  throws JsonProcessingException {
        return  ctx.call("abi.encode_boc", new ParamsOfAbiEncodeBoc(params, data, bocCache), ResultOfAbiEncodeBoc.class);
    }

    /**
    * <strong>abi.calc_function_id</strong>
    * Calculates contract function ID by contract ABI
    * @param abi Contract ABI. 
    * @param functionName Contract function name 
    * @param output If set to `true` output function ID will be returned which is used in contract response. Default is `false` 
    * @return {@link tech.deplant.java4ever.binding.Abi.ResultOfCalcFunctionId}
    */
    public static ResultOfCalcFunctionId calcFunctionId(@NonNull Context ctx, @NonNull ABI abi, @NonNull String functionName,  Boolean output)  throws JsonProcessingException {
        return  ctx.call("abi.calc_function_id", new ParamsOfCalcFunctionId(abi, functionName, output), ResultOfCalcFunctionId.class);
    }

}
