package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.Optional;
import lombok.*;
import java.util.stream.*;
import java.util.Arrays;

/**
 *  <strong>tvm</strong>
 *  Contains methods of "tvm" module.

 *  
 *  @version EVER-SDK 1.37.0
 */
public class Tvm {


    /**
    * 
    * @param blockchainConfig boc with config
    * @param blockTime time that is used as transaction time
    * @param blockLt block logical time
    * @param transactionLt transaction logical time
    * @param chksigAlwaysSucceed Overrides standard TVM behaviour. If set to `true` then CHKSIG always will return `true`.
    */
    public record ExecutionOptions(String blockchainConfig, Number blockTime, Long blockLt, Long transactionLt, Boolean chksigAlwaysSucceed) {}
    public interface AccountForExecutor {

        public static final None NONE = new None();


    /**
    * Non-existing account to run a creation internal message. Should be used with `skip_transaction_check = true` if the message has no deploy data since transactions on the uninitialized account are always aborted

    */
    public record None() implements AccountForExecutor {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }

        public static final Uninit UNINIT = new Uninit();


    /**
    * Emulate uninitialized account to run deploy message

    */
    public record Uninit() implements AccountForExecutor {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Account state to run message
    * @param boc Account BOC. Encoded as base64.
    * @param unlimitedBalance Flag for running account with the unlimited balance. Can be used to calculate transaction fees without balance check
    */
    public record Account(@NonNull String boc, Boolean unlimitedBalance) implements AccountForExecutor {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}

    /**
    * 
    * @param inMsgFwdFee Deprecated. Left for backward compatibility. Does not participate in account transaction fees calculation.
    * @param storageFee Fee for account storage
    * @param gasFee Fee for processing
    * @param outMsgsFwdFee Deprecated. Contains the same data as total_fwd_fees field. Deprecated because of its confusing name, that is not the same with GraphQL API Transaction type's field.
    * @param totalAccountFees Deprecated. This is the field that is named as `total_fees` in GraphQL API Transaction type. `total_account_fees` name is misleading, because it does not mean account fees, instead it meansvalidators total fees received for the transaction execution. It does not include some forward fees that accountactually pays now, but validators will receive later during value delivery to another account (not even in the receivingtransaction).Because of all of this, this field is not interesting for those who wants to understandthe real account fees, this is why it is deprecated and left for backward compatibility.
    * @param totalOutput Deprecated because it means total value sent in the transaction, which does not relate to any fees.
    * @param extInMsgFee Fee for inbound external message import.
    * @param totalFwdFees Total fees the account pays for message forwarding
    * @param accountFees Total account fees for the transaction execution. Compounds of storage_fee + gas_fee + ext_in_msg_fee + total_fwd_fees
    */
    public record TransactionFees(@NonNull Long inMsgFwdFee, @NonNull Long storageFee, @NonNull Long gasFee, @NonNull Long outMsgsFwdFee, @NonNull Long totalAccountFees, @NonNull Long totalOutput, @NonNull Long extInMsgFee, @NonNull Long totalFwdFees, @NonNull Long accountFees) {}

    /**
    * 
    * @param message Input message BOC. Must be encoded as base64.
    * @param account Account to run on executor
    * @param executionOptions Execution options.
    * @param abi Contract ABI for decoding output messages
    * @param skipTransactionCheck Skip transaction check flag
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided
    * @param returnUpdatedAccount Return updated account flag. Empty string is returned if the flag is `false`
    */
    public record ParamsOfRunExecutor(@NonNull String message, @NonNull AccountForExecutor account, ExecutionOptions executionOptions, Abi.ABI abi, Boolean skipTransactionCheck, Boc.BocCacheType bocCache, Boolean returnUpdatedAccount) {}

    /**
    * 
    * @param transaction Parsed transaction. In addition to the regular transaction fields there is a`boc` field encoded with `base64` which contains sourcetransaction BOC.
    * @param outMessages List of output messages' BOCs. Encoded as `base64`
    * @param decoded Optional decoded message bodies according to the optional `abi` parameter.
    * @param account Updated account state BOC. Encoded as `base64`
    * @param fees Transaction fees
    */
    public record ResultOfRunExecutor(@NonNull Map<String,Object> transaction, @NonNull String[] outMessages, Processing.DecodedOutput decoded, @NonNull String account, @NonNull TransactionFees fees) {}

    /**
    * 
    * @param message Input message BOC. Must be encoded as base64.
    * @param account Account BOC. Must be encoded as base64.
    * @param executionOptions Execution options.
    * @param abi Contract ABI for decoding output messages
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided
    * @param returnUpdatedAccount Return updated account flag. Empty string is returned if the flag is `false`
    */
    public record ParamsOfRunTvm(@NonNull String message, @NonNull String account, ExecutionOptions executionOptions, Abi.ABI abi, Boc.BocCacheType bocCache, Boolean returnUpdatedAccount) {}

    /**
    * 
    * @param outMessages List of output messages' BOCs. Encoded as `base64`
    * @param decoded Optional decoded message bodies according to the optional `abi` parameter.
    * @param account Updated account state BOC. Encoded as `base64`. Attention! Only `account_state.storage.state.data` part of the BOC is updated.
    */
    public record ResultOfRunTvm(@NonNull String[] outMessages, Processing.DecodedOutput decoded, @NonNull String account) {}

    /**
    * 
    * @param account Account BOC in `base64`
    * @param functionName Function name
    * @param input Input parameters
    * @param executionOptions Execution options
    * @param tupleListAsArray Convert lists based on nested tuples in the **result** into plain arrays. Default is `false`. Input parameters may use any of lists representationsIf you receive this error on Web: "Runtime error. Unreachable code should not be executed...",set this flag to true.This may happen, for example, when elector contract contains too many participants
    */
    public record ParamsOfRunGet(@NonNull String account, @NonNull String functionName, Map<String,Object> input, ExecutionOptions executionOptions, Boolean tupleListAsArray) {}

    /**
    * 
    * @param output Values returned by get-method on stack
    */
    public record ResultOfRunGet(@NonNull Map<String,Object> output) {}
    /**
    * <strong>tvm.run_executor</strong>
    * Emulates all the phases of contract execution locally Performs all the phases of contract execution on Transaction Executor -the same component that is used on Validator Nodes.<p>Can be used for contract debugging, to find out the reason why a message was not delivered successfully.Validators throw away the failed external inbound messages (if they failed bedore `ACCEPT`) in the real network.This is why these messages are impossible to debug in the real network.With the help of run_executor you can do that. In fact, `process_message` functionperforms local check with `run_executor` if there was no transaction as a result of processingand returns the error, if there is one.<p>Another use case to use `run_executor` is to estimate fees for message execution.Set  `AccountForExecutor::Account.unlimited_balance`to `true` so that emulation will not depend on the actual balance.This may be needed to calculate deploy fees for an account that does not exist yet.JSON with fees is in `fees` field of the result.<p>One more use case - you can produce the sequence of operations,thus emulating the sequential contract calls locally.And so on.<p>Transaction executor requires account BOC (bag of cells) as a parameter.To get the account BOC - use `net.query` method to download it from GraphQL API(field `boc` of `account`) or generate it with `abi.encode_account` method.<p>Also it requires message BOC. To get the message BOC - use `abi.encode_message` or `abi.encode_internal_message`.<p>If you need this emulation to be as precise as possible (for instance - emulate transactionwith particular lt in particular block or use particular blockchain config,downloaded from a particular key block - then specify `execution_options` parameter.<p>If you need to see the aborted transaction as a result, not as an error, set `skip_transaction_check` to `true`.
    * @param message Input message BOC. Must be encoded as base64.
    * @param account Account to run on executor 
    * @param executionOptions Execution options. 
    * @param abi Contract ABI for decoding output messages 
    * @param skipTransactionCheck Skip transaction check flag 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided
    * @param returnUpdatedAccount Return updated account flag. Empty string is returned if the flag is `false`
    * @return {@link tech.deplant.java4ever.binding.Tvm.ResultOfRunExecutor}
    */
    public static ResultOfRunExecutor runExecutor(@NonNull Context ctx, @NonNull String message, @NonNull AccountForExecutor account,  ExecutionOptions executionOptions,  Abi.ABI abi,  Boolean skipTransactionCheck,  Boc.BocCacheType bocCache,  Boolean returnUpdatedAccount)  throws JsonProcessingException {
        return  ctx.call("tvm.run_executor", new ParamsOfRunExecutor(message, account, executionOptions, abi, skipTransactionCheck, bocCache, returnUpdatedAccount), ResultOfRunExecutor.class);
    }

    /**
    * <strong>tvm.run_tvm</strong>
    * Executes get-methods of ABI-compatible contracts Performs only a part of compute phase of transaction executionthat is used to run get-methods of ABI-compatible contracts.<p>If you try to run get-methods with `run_executor` you will get an error, because it checks ACCEPT and exitsif there is none, which is actually true for get-methods.<p> To get the account BOC (bag of cells) - use `net.query` method to download it from GraphQL API(field `boc` of `account`) or generate it with `abi.encode_account method`.To get the message BOC - use `abi.encode_message` or prepare it any other way, for instance, with FIFT script.<p>Attention! Updated account state is produces as well, but only`account_state.storage.state.data`  part of the BOC is updated.
    * @param message Input message BOC. Must be encoded as base64.
    * @param account Account BOC. Must be encoded as base64.
    * @param executionOptions Execution options. 
    * @param abi Contract ABI for decoding output messages 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided
    * @param returnUpdatedAccount Return updated account flag. Empty string is returned if the flag is `false`
    * @return {@link tech.deplant.java4ever.binding.Tvm.ResultOfRunTvm}
    */
    public static ResultOfRunTvm runTvm(@NonNull Context ctx, @NonNull String message, @NonNull String account,  ExecutionOptions executionOptions,  Abi.ABI abi,  Boc.BocCacheType bocCache,  Boolean returnUpdatedAccount)  throws JsonProcessingException {
        return  ctx.call("tvm.run_tvm", new ParamsOfRunTvm(message, account, executionOptions, abi, bocCache, returnUpdatedAccount), ResultOfRunTvm.class);
    }

    /**
    * <strong>tvm.run_get</strong>
    * Executes a get-method of FIFT contract Executes a get-method of FIFT contract that fulfills the smc-guidelines https://test.ton.org/smc-guidelines.txtand returns the result data from TVM's stack
    * @param account Account BOC in `base64` 
    * @param functionName Function name 
    * @param input Input parameters 
    * @param executionOptions Execution options 
    * @param tupleListAsArray Convert lists based on nested tuples in the **result** into plain arrays. Default is `false`. Input parameters may use any of lists representationsIf you receive this error on Web: "Runtime error. Unreachable code should not be executed...",set this flag to true.This may happen, for example, when elector contract contains too many participants
    * @return {@link tech.deplant.java4ever.binding.Tvm.ResultOfRunGet}
    */
    public static ResultOfRunGet runGet(@NonNull Context ctx, @NonNull String account, @NonNull String functionName,  Map<String,Object> input,  ExecutionOptions executionOptions,  Boolean tupleListAsArray)  throws JsonProcessingException {
        return  ctx.call("tvm.run_get", new ParamsOfRunGet(account, functionName, input, executionOptions, tupleListAsArray), ResultOfRunGet.class);
    }

}
