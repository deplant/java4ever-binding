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
public class Tvm {


    /**
     * Emulates all the phases of contract execution locally
     *
     * @param message              Input message BOC. Must be encoded as base64.
     * @param account              Account to run on executor
     * @param executionOptions     Execution options.
     * @param abi                  Contract ABI for decoding output messages
     * @param skipTransactionCheck Skip transaction check flag
     * @param bocCache             Cache type to put the result. The BOC itself returned if no cache type provided
     * @param returnUpdatedAccount Return updated account flag. Empty string is returned if the flag is `false`
     */
    public static CompletableFuture<ResultOfRunExecutor> runExecutor(@NonNull Context context, @NonNull String message, @NonNull AccountForExecutor account, ExecutionOptions executionOptions, Abi.ABI abi, Boolean skipTransactionCheck, Boc.BocCacheType bocCache, Boolean returnUpdatedAccount) {
        return context.future("tvm.run_executor", new ParamsOfRunExecutor(message, account, executionOptions, abi, skipTransactionCheck, bocCache, returnUpdatedAccount), ResultOfRunExecutor.class);
    }

    /**
     * Executes get-methods of ABI-compatible contracts
     *
     * @param message              Input message BOC. Must be encoded as base64.
     * @param account              Account BOC. Must be encoded as base64.
     * @param executionOptions     Execution options.
     * @param abi                  Contract ABI for decoding output messages
     * @param bocCache             Cache type to put the result. The BOC itself returned if no cache type provided
     * @param returnUpdatedAccount Return updated account flag. Empty string is returned if the flag is `false`
     */
    public static CompletableFuture<ResultOfRunTvm> runTvm(@NonNull Context context, @NonNull String message, @NonNull String account, ExecutionOptions executionOptions, Abi.ABI abi, Boc.BocCacheType bocCache, Boolean returnUpdatedAccount) {
        return context.future("tvm.run_tvm", new ParamsOfRunTvm(message, account, executionOptions, abi, bocCache, returnUpdatedAccount), ResultOfRunTvm.class);
    }

    /**
     * Executes a get-method of FIFT contract
     *
     * @param account          Account BOC in `base64`
     * @param functionName     Function name
     * @param input            Input parameters
     * @param executionOptions Execution options
     * @param tupleListAsArray Convert lists based on nested tuples in the **result** into plain arrays. Default is `false`. Input parameters may use any of lists representationsIf you receive this error on Web: "Runtime error. Unreachable code should not be executed...",set this flag to true.This may happen, for example, when elector contract contains too many participants
     */
    public static CompletableFuture<ResultOfRunGet> runGet(@NonNull Context context, @NonNull String account, @NonNull String functionName, Map<String, Object> input, ExecutionOptions executionOptions, Boolean tupleListAsArray) {
        return context.future("tvm.run_get", new ParamsOfRunGet(account, functionName, input, executionOptions, tupleListAsArray), ResultOfRunGet.class);
    }

    @Value
    public static class ExecutionOptions extends JsonData {
        @SerializedName("blockchain_config")
        @Getter(AccessLevel.NONE)
        String blockchainConfig;
        @SerializedName("block_time")
        @Getter(AccessLevel.NONE)
        Number blockTime;
        @SerializedName("block_lt")
        @Getter(AccessLevel.NONE)
        Long blockLt;
        @SerializedName("transaction_lt")
        @Getter(AccessLevel.NONE)
        Long transactionLt;

        /**
         * boc with config
         */
        public Optional<String> blockchainConfig() {
            return Optional.ofNullable(this.blockchainConfig);
        }

        /**
         * time that is used as transaction time
         */
        public Optional<Number> blockTime() {
            return Optional.ofNullable(this.blockTime);
        }

        /**
         * block logical time
         */
        public Optional<Long> blockLt() {
            return Optional.ofNullable(this.blockLt);
        }

        /**
         * transaction logical time
         */
        public Optional<Long> transactionLt() {
            return Optional.ofNullable(this.transactionLt);
        }

    }

    public static abstract class AccountForExecutor {

        public static final None None = new None();
        public static final Uninit Uninit = new Uninit();

        @Value
        public static class None extends AccountForExecutor {

        }

        @Value
        public static class Uninit extends AccountForExecutor {

        }


        @Value
        public static class Account extends AccountForExecutor {

            /**
             * Account BOC.
             */
            @SerializedName("boc")
            @NonNull String boc;
            @SerializedName("unlimited_balance")
            @Getter(AccessLevel.NONE)
            Boolean unlimitedBalance;

            /**
             * Flag for running account with the unlimited balance.
             */
            public Optional<Boolean> unlimitedBalance() {
                return Optional.ofNullable(this.unlimitedBalance);
            }

        }
    }

    @Value
    public static class TransactionFees extends JsonData {
        @SerializedName("in_msg_fwd_fee")
        @NonNull Long inMsgFwdFee;
        @SerializedName("storage_fee")
        @NonNull Long storageFee;
        @SerializedName("gas_fee")
        @NonNull Long gasFee;
        @SerializedName("out_msgs_fwd_fee")
        @NonNull Long outMsgsFwdFee;
        @SerializedName("total_account_fees")
        @NonNull Long totalAccountFees;
        @SerializedName("total_output")
        @NonNull Long totalOutput;

    }

    @Value
    public static class ParamsOfRunExecutor extends JsonData {

        /**
         * Input message BOC.
         */
        @SerializedName("message")
        @NonNull String message;

        /**
         * Account to run on executor
         */
        @SerializedName("account")
        @NonNull AccountForExecutor account;
        @SerializedName("execution_options")
        @Getter(AccessLevel.NONE)
        ExecutionOptions executionOptions;
        @SerializedName("abi")
        @Getter(AccessLevel.NONE)
        Abi.ABI abi;
        @SerializedName("skip_transaction_check")
        @Getter(AccessLevel.NONE)
        Boolean skipTransactionCheck;
        @SerializedName("boc_cache")
        @Getter(AccessLevel.NONE)
        Boc.BocCacheType bocCache;
        @SerializedName("return_updated_account")
        @Getter(AccessLevel.NONE)
        Boolean returnUpdatedAccount;

        /**
         * Execution options.
         */
        public Optional<ExecutionOptions> executionOptions() {
            return Optional.ofNullable(this.executionOptions);
        }

        /**
         * Contract ABI for decoding output messages
         */
        public Optional<Abi.ABI> abi() {
            return Optional.ofNullable(this.abi);
        }

        /**
         * Skip transaction check flag
         */
        public Optional<Boolean> skipTransactionCheck() {
            return Optional.ofNullable(this.skipTransactionCheck);
        }

        /**
         * Cache type to put the result.
         */
        public Optional<Boc.BocCacheType> bocCache() {
            return Optional.ofNullable(this.bocCache);
        }

        /**
         * Return updated account flag.
         */
        public Optional<Boolean> returnUpdatedAccount() {
            return Optional.ofNullable(this.returnUpdatedAccount);
        }

    }

    @Value
    public static class ResultOfRunExecutor extends JsonData {

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
        Processing.DecodedOutput decoded;
        /**
         * Updated account state BOC.
         */
        @SerializedName("account")
        @NonNull String account;
        /**
         * Transaction fees
         */
        @SerializedName("fees")
        @NonNull TransactionFees fees;

        /**
         * Optional decoded message bodies according to the optional `abi` parameter.
         */
        public Optional<Processing.DecodedOutput> decoded() {
            return Optional.ofNullable(this.decoded);
        }

    }

    @Value
    public static class ParamsOfRunTvm extends JsonData {

        /**
         * Input message BOC.
         */
        @SerializedName("message")
        @NonNull String message;

        /**
         * Account BOC.
         */
        @SerializedName("account")
        @NonNull String account;
        @SerializedName("execution_options")
        @Getter(AccessLevel.NONE)
        ExecutionOptions executionOptions;
        @SerializedName("abi")
        @Getter(AccessLevel.NONE)
        Abi.ABI abi;
        @SerializedName("boc_cache")
        @Getter(AccessLevel.NONE)
        Boc.BocCacheType bocCache;
        @SerializedName("return_updated_account")
        @Getter(AccessLevel.NONE)
        Boolean returnUpdatedAccount;

        /**
         * Execution options.
         */
        public Optional<ExecutionOptions> executionOptions() {
            return Optional.ofNullable(this.executionOptions);
        }

        /**
         * Contract ABI for decoding output messages
         */
        public Optional<Abi.ABI> abi() {
            return Optional.ofNullable(this.abi);
        }

        /**
         * Cache type to put the result.
         */
        public Optional<Boc.BocCacheType> bocCache() {
            return Optional.ofNullable(this.bocCache);
        }

        /**
         * Return updated account flag.
         */
        public Optional<Boolean> returnUpdatedAccount() {
            return Optional.ofNullable(this.returnUpdatedAccount);
        }

    }

    @Value
    public static class ResultOfRunTvm extends JsonData {

        /**
         * List of output messages' BOCs.
         */
        @SerializedName("out_messages")
        @NonNull String[] outMessages;
        @SerializedName("decoded")
        @Getter(AccessLevel.NONE)
        Processing.DecodedOutput decoded;
        /**
         * Updated account state BOC.
         */
        @SerializedName("account")
        @NonNull String account;

        /**
         * Optional decoded message bodies according to the optional `abi` parameter.
         */
        public Optional<Processing.DecodedOutput> decoded() {
            return Optional.ofNullable(this.decoded);
        }

    }

    @Value
    public static class ParamsOfRunGet extends JsonData {

        /**
         * Account BOC in `base64`
         */
        @SerializedName("account")
        @NonNull String account;

        /**
         * Function name
         */
        @SerializedName("function_name")
        @NonNull String functionName;
        @SerializedName("input")
        @Getter(AccessLevel.NONE)
        Map<String, Object> input;
        @SerializedName("execution_options")
        @Getter(AccessLevel.NONE)
        ExecutionOptions executionOptions;
        @SerializedName("tuple_list_as_array")
        @Getter(AccessLevel.NONE)
        Boolean tupleListAsArray;

        /**
         * Input parameters
         */
        public Optional<Map<String, Object>> input() {
            return Optional.ofNullable(this.input);
        }

        /**
         * Execution options
         */
        public Optional<ExecutionOptions> executionOptions() {
            return Optional.ofNullable(this.executionOptions);
        }

        /**
         * Convert lists based on nested tuples in the **result** into plain arrays.
         */
        public Optional<Boolean> tupleListAsArray() {
            return Optional.ofNullable(this.tupleListAsArray);
        }

    }

    @Value
    public static class ResultOfRunGet extends JsonData {

        /**
         * Values returned by get-method on stack
         */
        @SerializedName("output")
        @NonNull Map<String, Object> output;

    }

}
