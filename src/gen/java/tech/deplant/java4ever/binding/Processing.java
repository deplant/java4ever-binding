package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.util.Map;

/**
 * <strong>Processing</strong>
 * Contains methods of "processing" module of EVER-SDK API
 *
 * Message processing module. This module incorporates functions related to complex message
 * processing scenarios.
 * @version 1.41.0
 */
public final class Processing {
  /**
   * Sends message to the network and returns the last generated shard block of the destination account
   * before the message was sent. It will be required later for message processing. Sends message to the network
   *
   * @param message  Message BOC.
   * @param abi If this parameter is specified and the message has the
   * `expire` header then expiration time will be checked against
   * the current time to prevent unnecessary sending of already expired message.
   *
   * The `message already expired` error will be returned in this
   * case.
   *
   * Note, that specifying `abi` for ABI compliant contracts is
   * strongly recommended, so that proper processing strategy can be
   * chosen. Optional message ABI.
   * @param sendEvents  Flag for requesting events sending
   */
  public static Processing.ResultOfSendMessage sendMessage(Context ctx, String message, Abi.ABI abi,
      Boolean sendEvents) throws EverSdkException {
    return ctx.call("processing.send_message", new Processing.ParamsOfSendMessage(message, abi, sendEvents), Processing.ResultOfSendMessage.class);
  }

  /**
   * `send_events` enables intermediate events, such as `WillFetchNextBlock`,
   * `FetchNextBlockFailed` that may be useful for logging of new shard blocks creation
   * during message processing.
   *
   * Note, that presence of the `abi` parameter is critical for ABI
   * compliant contracts. Message processing uses drastically
   * different strategy for processing message for contracts which
   * ABI includes "expire" header.
   *
   * When the ABI header `expire` is present, the processing uses
   * `message expiration` strategy:
   * - The maximum block gen time is set to
   *   `message_expiration_timeout + transaction_wait_timeout`.
   * - When maximum block gen time is reached, the processing will
   *   be finished with `MessageExpired` error.
   *
   * When the ABI header `expire` isn't present or `abi` parameter
   * isn't specified, the processing uses `transaction waiting`
   * strategy:
   * - The maximum block gen time is set to
   *   `now() + transaction_wait_timeout`.
   *
   * - If maximum block gen time is reached and no result transaction is found,
   * the processing will exit with an error. Performs monitoring of the network for the result transaction of the external inbound message processing.
   *
   * @param abi If it is specified, then the output messages' bodies will be
   * decoded according to this ABI.
   *
   * The `abi_decoded` result field will be filled out. Optional ABI for decoding the transaction result.
   * @param message Encoded with `base64`. Message BOC.
   * @param shardBlockId You must provide the same value as the `send_message` has returned. The last generated block id of the destination account shard before the message was sent.
   * @param sendEvents  Flag that enables/disables intermediate events
   * @param sendingEndpoints Use this field to get more informative errors.
   * Provide the same value as the `send_message` has returned.
   * If the message was not delivered (expired), SDK will log the endpoint URLs, used for its sending. The list of endpoints to which the message was sent.
   */
  public static Processing.ResultOfProcessMessage waitForTransaction(Context ctx, Abi.ABI abi,
      String message, String shardBlockId, Boolean sendEvents, String[] sendingEndpoints) throws
      EverSdkException {
    return ctx.call("processing.wait_for_transaction", new Processing.ParamsOfWaitForTransaction(abi, message, shardBlockId, sendEvents, sendingEndpoints), Processing.ResultOfProcessMessage.class);
  }

  /**
   * Creates ABI-compatible message,
   * sends it to the network and monitors for the result transaction.
   * Decodes the output messages' bodies.
   *
   * If contract's ABI includes "expire" header, then
   * SDK implements retries in case of unsuccessful message delivery within the expiration
   * timeout: SDK recreates the message, sends it and processes it again.
   *
   * The intermediate events, such as `WillFetchFirstBlock`, `WillSend`, `DidSend`,
   * `WillFetchNextBlock`, etc - are switched on/off by `send_events` flag
   * and logged into the supplied callback function.
   *
   * The retry configuration parameters are defined in the client's `NetworkConfig` and `AbiConfig`.
   *
   * If contract's ABI does not include "expire" header
   * then, if no transaction is found within the network timeout (see config parameter ), exits with error. Creates message, sends it to the network and monitors its processing.
   *
   * @param abi  Contract ABI.
   * @param address Must be specified in case of non-deploy message. Target address the message will be sent to.
   * @param deploySet Must be specified in case of deploy message. Deploy parameters.
   * @param callSet Must be specified in case of non-deploy message.
   *
   * In case of deploy message it is optional and contains parameters
   * of the functions that will to be called upon deploy transaction. Function call parameters.
   * @param signer  Signing parameters.
   * @param processingTryIndex Used in message processing with retries (if contract's ABI includes "expire" header).
   *
   * Encoder uses the provided try index to calculate message
   * expiration time. The 1st message expiration time is specified in
   * Client config.
   *
   * Expiration timeouts will grow with every retry.
   * Retry grow factor is set in Client config:
   * <.....add config parameter with default value here>
   *
   * Default value is 0. Processing try index.
   * @param signatureId  Signature ID to be used in data to sign preparing when CapSignatureWithId capability is enabled
   * @param sendEvents  Flag for requesting events sending
   */
  public static Processing.ResultOfProcessMessage processMessage(Context ctx, Abi.ABI abi,
      String address, Abi.DeploySet deploySet, Abi.CallSet callSet, Abi.Signer signer,
      Integer processingTryIndex, Integer signatureId, Boolean sendEvents) throws EverSdkException {
    return ctx.call("processing.process_message", new Processing.ParamsOfProcessMessage(new Abi.ParamsOfEncodeMessage(abi, address, deploySet, callSet, signer, processingTryIndex, signatureId), sendEvents), Processing.ResultOfProcessMessage.class);
  }

  /**
   * @param outMessages If the message can't be decoded, then `None` will be stored in
   * the appropriate position. Decoded bodies of the out messages.
   * @param output  Decoded body of the function output message.
   */
  public static final record DecodedOutput(Abi.DecodedMessageBody[] outMessages,
      Map<String, Object> output) {
  }

  /**
   * @param shardBlockId This block id must be used as a parameter of the
   * `wait_for_transaction`. The last generated shard block of the message destination account before the message was sent.
   * @param sendingEndpoints This list id must be used as a parameter of the
   * `wait_for_transaction`. The list of endpoints to which the message was sent.
   */
  public static final record ResultOfSendMessage(String shardBlockId, String[] sendingEndpoints) {
  }

  /**
   * @param transaction In addition to the regular transaction fields there is a
   * `boc` field encoded with `base64` which contains source
   * transaction BOC. Parsed transaction.
   * @param outMessages Encoded as `base64` List of output messages' BOCs.
   * @param decoded  Optional decoded message bodies according to the optional `abi` parameter.
   * @param fees  Transaction fees
   */
  public static final record ResultOfProcessMessage(Map<String, Object> transaction,
      String[] outMessages, Processing.DecodedOutput decoded, Tvm.TransactionFees fees) {
  }

  /**
   * @param abi If it is specified, then the output messages' bodies will be
   * decoded according to this ABI.
   *
   * The `abi_decoded` result field will be filled out. Optional ABI for decoding the transaction result.
   * @param message Encoded with `base64`. Message BOC.
   * @param shardBlockId You must provide the same value as the `send_message` has returned. The last generated block id of the destination account shard before the message was sent.
   * @param sendEvents  Flag that enables/disables intermediate events
   * @param sendingEndpoints Use this field to get more informative errors.
   * Provide the same value as the `send_message` has returned.
   * If the message was not delivered (expired), SDK will log the endpoint URLs, used for its sending. The list of endpoints to which the message was sent.
   */
  public static final record ParamsOfWaitForTransaction(Abi.ABI abi, String message,
      String shardBlockId, Boolean sendEvents, String[] sendingEndpoints) {
  }

  /**
   * @param message  Message BOC.
   * @param abi If this parameter is specified and the message has the
   * `expire` header then expiration time will be checked against
   * the current time to prevent unnecessary sending of already expired message.
   *
   * The `message already expired` error will be returned in this
   * case.
   *
   * Note, that specifying `abi` for ABI compliant contracts is
   * strongly recommended, so that proper processing strategy can be
   * chosen. Optional message ABI.
   * @param sendEvents  Flag for requesting events sending
   */
  public static final record ParamsOfSendMessage(String message, Abi.ABI abi, Boolean sendEvents) {
  }

  public sealed interface ProcessingEvent {
    /**
     * Fetched block will be used later in waiting phase. Notifies the application that the account's current shard block will be fetched from the network. This step is performed before the message sending so that sdk knows starting from which block it will search for the transaction.
     */
    final record WillFetchFirstBlock(String messageId,
        String messageDst) implements ProcessingEvent {
      @JsonProperty("type")
      public String type() {
        return "WillFetchFirstBlock";
      }
    }

    /**
     * This may happen due to the network issues. Receiving this event means that message processing will not proceed -
     * message was not sent, and Developer can try to run `process_message` again,
     * in the hope that the connection is restored. Notifies the app that the client has failed to fetch the account's current shard block.
     */
    final record FetchFirstBlockFailed(Client.ClientError error, String messageId,
        String messageDst) implements ProcessingEvent {
      @JsonProperty("type")
      public String type() {
        return "FetchFirstBlockFailed";
      }
    }

    /**
     *  Notifies the app that the message will be sent to the network. This event means that the account's current shard block was successfully fetched and the message was successfully created (`abi.encode_message` function was executed successfully).
     */
    final record WillSend(String shardBlockId, String messageId, String messageDst,
        String message) implements ProcessingEvent {
      @JsonProperty("type")
      public String type() {
        return "WillSend";
      }
    }

    /**
     * Do not forget to specify abi of your contract as well, it is crucial for processing. See `processing.wait_for_transaction` documentation. Notifies the app that the message was sent to the network, i.e `processing.send_message` was successfully executed. Now, the message is in the blockchain. If Application exits at this phase, Developer needs to proceed with processing after the application is restored with `wait_for_transaction` function, passing shard_block_id and message from this event.
     */
    final record DidSend(String shardBlockId, String messageId, String messageDst,
        String message) implements ProcessingEvent {
      @JsonProperty("type")
      public String type() {
        return "DidSend";
      }
    }

    /**
     * Nevertheless the processing will be continued at the waiting
     * phase because the message possibly has been delivered to the
     * node.
     * If Application exits at this phase, Developer needs to proceed with processing
     * after the application is restored with `wait_for_transaction` function, passing
     * shard_block_id and message from this event. Do not forget to specify abi of your contract
     * as well, it is crucial for processing. See `processing.wait_for_transaction` documentation. Notifies the app that the sending operation was failed with network error.
     */
    final record SendFailed(String shardBlockId, String messageId, String messageDst,
        String message, Client.ClientError error) implements ProcessingEvent {
      @JsonProperty("type")
      public String type() {
        return "SendFailed";
      }
    }

    /**
     * Event can occurs more than one time due to block walking
     * procedure.
     * If Application exits at this phase, Developer needs to proceed with processing
     * after the application is restored with `wait_for_transaction` function, passing
     * shard_block_id and message from this event. Do not forget to specify abi of your contract
     * as well, it is crucial for processing. See `processing.wait_for_transaction` documentation. Notifies the app that the next shard block will be fetched from the network.
     */
    final record WillFetchNextBlock(String shardBlockId, String messageId, String messageDst,
        String message) implements ProcessingEvent {
      @JsonProperty("type")
      public String type() {
        return "WillFetchNextBlock";
      }
    }

    /**
     * If no block was fetched within `NetworkConfig.wait_for_timeout` then processing stops.
     * This may happen when the shard stops, or there are other network issues.
     * In this case Developer should resume message processing with `wait_for_transaction`, passing shard_block_id,
     * message and contract abi to it. Note that passing ABI is crucial, because it will influence the processing strategy.
     *
     * Another way to tune this is to specify long timeout in `NetworkConfig.wait_for_timeout` Notifies the app that the next block can't be fetched.
     */
    final record FetchNextBlockFailed(String shardBlockId, String messageId, String messageDst,
        String message, Client.ClientError error) implements ProcessingEvent {
      @JsonProperty("type")
      public String type() {
        return "FetchNextBlockFailed";
      }
    }

    /**
     * This event occurs only for the contracts which ABI includes "expire" header.
     *
     * If Application specifies `NetworkConfig.message_retries_count` > 0, then `process_message`
     * will perform retries: will create a new message and send it again and repeat it until it reaches
     * the maximum retries count or receives a successful result.  All the processing
     * events will be repeated. Notifies the app that the message was not executed within expire timeout on-chain and will never be because it is already expired. The expiration timeout can be configured with `AbiConfig` parameters.
     */
    final record MessageExpired(String messageId, String messageDst, String message,
        Client.ClientError error) implements ProcessingEvent {
      @JsonProperty("type")
      public String type() {
        return "MessageExpired";
      }
    }

    /**
     *  Notifies the app that the message has been delivered to the thread's validators
     */
    final record RempSentToValidators(String messageId, String messageDst, Long timestamp,
        Map<String, Object> json) implements ProcessingEvent {
      @JsonProperty("type")
      public String type() {
        return "RempSentToValidators";
      }
    }

    /**
     *  Notifies the app that the message has been successfully included into a block candidate by the thread's collator
     */
    final record RempIncludedIntoBlock(String messageId, String messageDst, Long timestamp,
        Map<String, Object> json) implements ProcessingEvent {
      @JsonProperty("type")
      public String type() {
        return "RempIncludedIntoBlock";
      }
    }

    /**
     *  Notifies the app that the block candidate with the message has been accepted by the thread's validators
     */
    final record RempIncludedIntoAcceptedBlock(String messageId, String messageDst, Long timestamp,
        Map<String, Object> json) implements ProcessingEvent {
      @JsonProperty("type")
      public String type() {
        return "RempIncludedIntoAcceptedBlock";
      }
    }

    /**
     *  Notifies the app about some other minor REMP statuses occurring during message processing
     */
    final record RempOther(String messageId, String messageDst, Long timestamp,
        Map<String, Object> json) implements ProcessingEvent {
      @JsonProperty("type")
      public String type() {
        return "RempOther";
      }
    }

    /**
     *  Notifies the app about any problem that has occurred in REMP processing - in this case library switches to the fallback transaction awaiting scenario (sequential block reading).
     */
    final record RempError(String messageId, String messageDst,
        Client.ClientError error) implements ProcessingEvent {
      @JsonProperty("type")
      public String type() {
        return "RempError";
      }
    }
  }

  /**
   * @param messageEncodeParams  Message encode parameters.
   * @param sendEvents  Flag for requesting events sending
   */
  public static final record ParamsOfProcessMessage(Abi.ParamsOfEncodeMessage messageEncodeParams,
      Boolean sendEvents) {
  }

  public enum ProcessingErrorCode {
    MessageAlreadyExpired(501),

    MessageHasNotDestinationAddress(502),

    CanNotBuildMessageCell(503),

    FetchBlockFailed(504),

    SendMessageFailed(505),

    InvalidMessageBoc(506),

    MessageExpired(507),

    TransactionWaitTimeout(508),

    InvalidBlockReceived(509),

    CanNotCheckBlockShard(510),

    BlockNotFound(511),

    InvalidData(512),

    ExternalSignerMustNotBeUsed(513),

    MessageRejected(514),

    InvalidRempStatus(515),

    NextRempStatusTimeout(516);

    private final Integer value;

    ProcessingErrorCode(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer value() {
      return this.value;
    }
  }
}
