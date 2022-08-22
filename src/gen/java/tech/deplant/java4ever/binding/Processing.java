package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.*;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 *  <strong>processing</strong>
 *  Contains methods of "processing" module.

 *  Message processing module. This module incorporates functions related to complex messageprocessing scenarios.
 *  @version EVER-SDK 1.37.0
 */
public class Processing {


    /**
    * 
    * @param transaction Parsed transaction. In addition to the regular transaction fields there is a`boc` field encoded with `base64` which contains sourcetransaction BOC.
    * @param outMessages List of output messages' BOCs. Encoded as `base64`
    * @param decoded Optional decoded message bodies according to the optional `abi` parameter.
    * @param fees Transaction fees
    */
    public record ResultOfProcessMessage(Map<String,Object> transaction, String[] outMessages, DecodedOutput decoded, Tvm.TransactionFees fees) {}

    /**
    * 
    * @param outMessages Decoded bodies of the out messages. If the message can't be decoded, then `None` will be stored inthe appropriate position.
    * @param output Decoded body of the function output message.
    */
    public record DecodedOutput(Abi.DecodedMessageBody[] outMessages, Map<String,Object> output) {}

    /**
    * 
    * @param message Message BOC.
    * @param abi Optional message ABI. If this parameter is specified and the message has the`expire` header then expiration time will be checked againstthe current time to prevent unnecessary sending of already expired message.<p>The `message already expired` error will be returned in thiscase.<p>Note, that specifying `abi` for ABI compliant contracts isstrongly recommended, so that proper processing strategy can bechosen.
    * @param sendEvents Flag for requesting events sending
    */
    public record ParamsOfSendMessage(String message, Abi.ABI abi, Boolean sendEvents) {}

    /**
    * 
    * @param shardBlockId The last generated shard block of the message destination account before the message was sent. This block id must be used as a parameter of the`wait_for_transaction`.
    * @param sendingEndpoints The list of endpoints to which the message was sent. This list id must be used as a parameter of the`wait_for_transaction`.
    */
    public record ResultOfSendMessage(String shardBlockId, String[] sendingEndpoints) {}

    /**
    * 
    * @param abi Optional ABI for decoding the transaction result. If it is specified, then the output messages' bodies will bedecoded according to this ABI.<p>The `abi_decoded` result field will be filled out.
    * @param message Message BOC. Encoded with `base64`.
    * @param shardBlockId The last generated block id of the destination account shard before the message was sent. You must provide the same value as the `send_message` has returned.
    * @param sendEvents Flag that enables/disables intermediate events
    * @param sendingEndpoints The list of endpoints to which the message was sent. Use this field to get more informative errors.Provide the same value as the `send_message` has returned.If the message was not delivered (expired), SDK will log the endpoint URLs, used for its sending.
    */
    public record ParamsOfWaitForTransaction(Abi.ABI abi, String message, String shardBlockId, Boolean sendEvents, String[] sendingEndpoints) {}

    /**
    * 
    * @param messageEncodeParams Message encode parameters.
    * @param sendEvents Flag for requesting events sending
    */
    public record ParamsOfProcessMessage(Abi.ParamsOfEncodeMessage messageEncodeParams, Boolean sendEvents) {}
    /**
    * <strong>processing.send_message</strong>
    * Sends message to the network Sends message to the network and returns the last generated shard block of the destination accountbefore the message was sent. It will be required later for message processing.
    * @param message Message BOC. 
    * @param abi Optional message ABI. If this parameter is specified and the message has the`expire` header then expiration time will be checked againstthe current time to prevent unnecessary sending of already expired message.<p>The `message already expired` error will be returned in thiscase.<p>Note, that specifying `abi` for ABI compliant contracts isstrongly recommended, so that proper processing strategy can bechosen.
    * @param sendEvents Flag for requesting events sending 
    * @return {@link tech.deplant.java4ever.binding.Processing.ResultOfSendMessage}
    */
    public static ResultOfSendMessage sendMessage(Context ctx, String message,  Abi.ABI abi, Boolean sendEvents, Consumer<SendMessageEvent> consumer) {
        return  ctx.callEvent("processing.send_message", new ParamsOfSendMessage(message, abi, sendEvents), consumer, ResultOfSendMessage.class);
    }

    /**
    * <strong>processing.wait_for_transaction</strong>
    * Performs monitoring of the network for the result transaction of the external inbound message processing. `send_events` enables intermediate events, such as `WillFetchNextBlock`,`FetchNextBlockFailed` that may be useful for logging of new shard blocks creationduring message processing.<p>Note, that presence of the `abi` parameter is critical for ABIcompliant contracts. Message processing uses drasticallydifferent strategy for processing message for contracts whichABI includes "expire" header.<p>When the ABI header `expire` is present, the processing uses`message expiration` strategy:- The maximum block gen time is set to  `message_expiration_timeout + transaction_wait_timeout`.- When maximum block gen time is reached, the processing will  be finished with `MessageExpired` error.<p>When the ABI header `expire` isn't present or `abi` parameterisn't specified, the processing uses `transaction waiting`strategy:- The maximum block gen time is set to  `now() + transaction_wait_timeout`.<p>- If maximum block gen time is reached and no result transaction is found,the processing will exit with an error.
    * @param abi Optional ABI for decoding the transaction result. If it is specified, then the output messages' bodies will bedecoded according to this ABI.<p>The `abi_decoded` result field will be filled out.
    * @param message Message BOC. Encoded with `base64`.
    * @param shardBlockId The last generated block id of the destination account shard before the message was sent. You must provide the same value as the `send_message` has returned.
    * @param sendEvents Flag that enables/disables intermediate events 
    * @param sendingEndpoints The list of endpoints to which the message was sent. Use this field to get more informative errors.Provide the same value as the `send_message` has returned.If the message was not delivered (expired), SDK will log the endpoint URLs, used for its sending.
    * @return {@link tech.deplant.java4ever.binding.Processing.ResultOfProcessMessage}
    */
    public static ResultOfProcessMessage waitForTransaction(Context ctx,  Abi.ABI abi, String message, String shardBlockId, Boolean sendEvents,  String[] sendingEndpoints, Consumer<WaitForTransactionEvent> consumer) {
        return  ctx.callEvent("processing.wait_for_transaction", new ParamsOfWaitForTransaction(abi, message, shardBlockId, sendEvents, sendingEndpoints), consumer, ResultOfProcessMessage.class);
    }

    /**
    * <strong>processing.process_message</strong>
    * Creates message, sends it to the network and monitors its processing. Creates ABI-compatible message,sends it to the network and monitors for the result transaction.Decodes the output messages' bodies.<p>If contract's ABI includes "expire" header, thenSDK implements retries in case of unsuccessful message delivery within the expirationtimeout: SDK recreates the message, sends it and processes it again.<p>The intermediate events, such as `WillFetchFirstBlock`, `WillSend`, `DidSend`,`WillFetchNextBlock`, etc - are switched on/off by `send_events` flagand logged into the supplied callback function.<p>The retry configuration parameters are defined in the client's `NetworkConfig` and `AbiConfig`.<p>If contract's ABI does not include "expire" headerthen, if no transaction is found within the network timeout (see config parameter ), exits with error.
    * @param abi Contract ABI. 
    * @param address Target address the message will be sent to. Must be specified in case of non-deploy message.
    * @param deploySet Deploy parameters. Must be specified in case of deploy message.
    * @param callSet Function call parameters. Must be specified in case of non-deploy message.<p>In case of deploy message it is optional and contains parametersof the functions that will to be called upon deploy transaction.
    * @param signer Signing parameters. 
    * @param processingTryIndex Processing try index. Used in message processing with retries (if contract's ABI includes "expire" header).<p>Encoder uses the provided try index to calculate messageexpiration time. The 1st message expiration time is specified inClient config.<p>Expiration timeouts will grow with every retry.Retry grow factor is set in Client config:&lt;.....add config parameter with default value here&gt;<p>Default value is 0.
    * @param sendEvents Flag for requesting events sending 
    * @return {@link tech.deplant.java4ever.binding.Processing.ResultOfProcessMessage}
    */
    public static ResultOfProcessMessage processMessage(Context ctx, Abi.ABI abi,  String address,  Abi.DeploySet deploySet,  Abi.CallSet callSet, Abi.Signer signer,  Number processingTryIndex, Boolean sendEvents, Consumer<ProcessMessageEvent> consumer) {
        return  ctx.callEvent("processing.process_message", new ParamsOfProcessMessage(new Abi.ParamsOfEncodeMessage(abi, address, deploySet, callSet, signer, processingTryIndex), sendEvents), consumer, ResultOfProcessMessage.class);
    }

}
