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
import java.util.function.Consumer;

/**
 * This module incorporates functions related to complex messageprocessing scenarios.
 */
public class Processing {


    /**
     * Sends message to the network
     *
     * @param message    Message BOC.
     * @param abi        Optional message ABI. If this parameter is specified and the message has the`expire` header then expiration time will be checked againstthe current time to prevent unnecessary sending of already expired message.<p>The `message already expired` error will be returned in thiscase.<p>Note, that specifying `abi` for ABI compliant contracts isstrongly recommended, so that proper processing strategy can bechosen.
     * @param sendEvents Flag for requesting events sending
     */
    public static CompletableFuture<ResultOfSendMessage> sendMessage(@NonNull Context context, @NonNull String message, Abi.ABI abi, @NonNull Boolean sendEvents, Consumer<SendMessageEvent> consumer) {
        return Context.futureCallback("processing.send_message", context, new ParamsOfSendMessage(message, abi, sendEvents), ResultOfSendMessage.class);
    }

    /**
     * Performs monitoring of the network for the result transaction of the external inbound message processing.
     *
     * @param abi              Optional ABI for decoding the transaction result. If it is specified, then the output messages' bodies will bedecoded according to this ABI.<p>The `abi_decoded` result field will be filled out.
     * @param message          Message BOC. Encoded with `base64`.
     * @param shardBlockId     The last generated block id of the destination account shard before the message was sent. You must provide the same value as the `send_message` has returned.
     * @param sendEvents       Flag that enables/disables intermediate events
     * @param sendingEndpoints The list of endpoints to which the message was sent. Use this field to get more informative errors.Provide the same value as the `send_message` has returned.If the message was not delivered (expired), SDK will log the endpoint URLs, used for its sending.
     */
    public static CompletableFuture<ResultOfProcessMessage> waitForTransaction(@NonNull Context context, Abi.ABI abi, @NonNull String message, @NonNull String shardBlockId, @NonNull Boolean sendEvents, String[] sendingEndpoints, Consumer<WaitForTransactionEvent> consumer) {
        return Context.futureCallback("processing.wait_for_transaction", context, new ParamsOfWaitForTransaction(abi, message, shardBlockId, sendEvents, sendingEndpoints), ResultOfProcessMessage.class);
    }

    /**
     * Creates message, sends it to the network and monitors its processing.
     *
     * @param abi                Contract ABI.
     * @param address            Target address the message will be sent to. Must be specified in case of non-deploy message.
     * @param deploySet          Deploy parameters. Must be specified in case of deploy message.
     * @param callSet            Function call parameters. Must be specified in case of non-deploy message.<p>In case of deploy message it is optional and contains parametersof the functions that will to be called upon deploy transaction.
     * @param signer             Signing parameters.
     * @param processingTryIndex Processing try index. Used in message processing with retries (if contract's ABI includes "expire" header).<p>Encoder uses the provided try index to calculate messageexpiration time. The 1st message expiration time is specified inClient config.<p>Expiration timeouts will grow with every retry.Retry grow factor is set in Client config:&lt;.....add config parameter with default value here&gt;<p>Default value is 0.
     * @param sendEvents         Flag for requesting events sending
     */
    public static CompletableFuture<ResultOfProcessMessage> processMessage(@NonNull Context context, @NonNull Abi.ABI abi, String address, Abi.DeploySet deploySet, Abi.CallSet callSet, @NonNull Abi.Signer signer, Number processingTryIndex, @NonNull Boolean sendEvents, Consumer<ProcessMessageEvent> consumer) {
        return Context.futureCallback("processing.process_message", context, new ParamsOfProcessMessage(new Abi.ParamsOfEncodeMessage(abi, address, deploySet, callSet, signer, processingTryIndex), sendEvents), ResultOfProcessMessage.class);
    }

    @Value
    public static class ResultOfProcessMessage extends JsonData {

        /**
         * Parsed transaction.
         */
        @SerializedName("transaction")
        @NonNull Map<String, Object> transaction;

        /**
         * List of output messages' BOCs.
         */
        @SerializedName("out_messages")
        @NonNull String[] outMessages;
        @SerializedName("decoded")
        @Getter(AccessLevel.NONE)
        DecodedOutput decoded;
        /**
         * Transaction fees
         */
        @SerializedName("fees")
        @NonNull Tvm.TransactionFees fees;

        /**
         * Optional decoded message bodies according to the optional `abi` parameter.
         */
        public Optional<DecodedOutput> decoded() {
            return Optional.ofNullable(this.decoded);
        }

    }

    @Value
    public static class DecodedOutput extends JsonData {
        @SerializedName("out_messages")
        @Getter(AccessLevel.NONE)
        Abi.DecodedMessageBody[] outMessages;
        @SerializedName("output")
        @Getter(AccessLevel.NONE)
        Map<String, Object> output;

        /**
         * Decoded bodies of the out messages.
         */
        public Optional<Abi.DecodedMessageBody[]> outMessages() {
            return Optional.ofNullable(this.outMessages);
        }

        /**
         * Decoded body of the function output message.
         */
        public Optional<Map<String, Object>> output() {
            return Optional.ofNullable(this.output);
        }

    }

    @Value
    public static class ParamsOfSendMessage extends JsonData {

        /**
         * Message BOC.
         */
        @SerializedName("message")
        @NonNull String message;
        @SerializedName("abi")
        @Getter(AccessLevel.NONE)
        Abi.ABI abi;
        /**
         * Flag for requesting events sending
         */
        @SerializedName("send_events")
        @NonNull Boolean sendEvents;

        /**
         * Optional message ABI.
         */
        public Optional<Abi.ABI> abi() {
            return Optional.ofNullable(this.abi);
        }

    }

    @Value
    public static class ResultOfSendMessage extends JsonData {

        /**
         * The last generated shard block of the message destination account before the message was sent.
         */
        @SerializedName("shard_block_id")
        @NonNull String shardBlockId;

        /**
         * The list of endpoints to which the message was sent.
         */
        @SerializedName("sending_endpoints")
        @NonNull String[] sendingEndpoints;

    }

    @Value
    public static class ParamsOfWaitForTransaction extends JsonData {
        @SerializedName("abi")
        @Getter(AccessLevel.NONE)
        Abi.ABI abi;
        /**
         * Message BOC.
         */
        @SerializedName("message")
        @NonNull String message;
        /**
         * The last generated block id of the destination account shard before the message was sent.
         */
        @SerializedName("shard_block_id")
        @NonNull String shardBlockId;
        /**
         * Flag that enables/disables intermediate events
         */
        @SerializedName("send_events")
        @NonNull Boolean sendEvents;
        @SerializedName("sending_endpoints")
        @Getter(AccessLevel.NONE)
        String[] sendingEndpoints;

        /**
         * Optional ABI for decoding the transaction result.
         */
        public Optional<Abi.ABI> abi() {
            return Optional.ofNullable(this.abi);
        }

        /**
         * The list of endpoints to which the message was sent.
         */
        public Optional<String[]> sendingEndpoints() {
            return Optional.ofNullable(this.sendingEndpoints);
        }

    }

    @Value
    public static class ParamsOfProcessMessage extends JsonData {

        /**
         * Message encode parameters.
         */
        @SerializedName("message_encode_params")
        @NonNull Abi.ParamsOfEncodeMessage messageEncodeParams;

        /**
         * Flag for requesting events sending
         */
        @SerializedName("send_events")
        @NonNull Boolean sendEvents;

    }

}
