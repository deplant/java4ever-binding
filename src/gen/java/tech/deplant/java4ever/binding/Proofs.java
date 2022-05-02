package tech.deplant.java4ever.binding;

import com.google.gson.annotations.SerializedName;
import lombok.NonNull;
import lombok.Value;
import tech.deplant.java4ever.binding.json.JsonData;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class Proofs {


    /**
     * Proves that a given block's data, which is queried from TONOS API, can be trusted.
     *
     * @param block Single block's data, retrieved from TONOS API, that needs proof. Required fields are `id` and/or top-level `boc` (for block identification), others are optional.
     */
    public static CompletableFuture<Void> proofBlockData(@NonNull Context context, @NonNull Map<String, Object> block) {
        return context.future("proofs.proof_block_data", new ParamsOfProofBlockData(block), Void.class);
    }

    /**
     * Proves that a given transaction's data, which is queried from TONOS API, can be trusted.
     *
     * @param transaction Single transaction's data as queried from DApp server, without modifications. The required fields are `id` and/or top-level `boc`, others are optional. In order to reduce network requests count, it is recommended to provide `block_id` and `boc` of transaction.
     */
    public static CompletableFuture<Void> proofTransactionData(@NonNull Context context, @NonNull Map<String, Object> transaction) {
        return context.future("proofs.proof_transaction_data", new ParamsOfProofTransactionData(transaction), Void.class);
    }

    /**
     * Proves that a given message's data, which is queried from TONOS API, can be trusted.
     *
     * @param message Single message's data as queried from DApp server, without modifications. The required fields are `id` and/or top-level `boc`, others are optional. In order to reduce network requests count, it is recommended to provide at least `boc` of message and non-null `src_transaction.id` or `dst_transaction.id`.
     */
    public static CompletableFuture<Void> proofMessageData(@NonNull Context context, @NonNull Map<String, Object> message) {
        return context.future("proofs.proof_message_data", new ParamsOfProofMessageData(message), Void.class);
    }

    @Value
    public static class ParamsOfProofBlockData extends JsonData {

        /**
         * Single block's data, retrieved from TONOS API, that needs proof. Required fields are `id` and/or top-level `boc` (for block identification), others are optional.
         */
        @SerializedName("block")
        @NonNull Map<String, Object> block;

    }

    @Value
    public static class ParamsOfProofTransactionData extends JsonData {

        /**
         * Single transaction's data as queried from DApp server, without modifications. The required fields are `id` and/or top-level `boc`, others are optional. In order to reduce network requests count, it is recommended to provide `block_id` and `boc` of transaction.
         */
        @SerializedName("transaction")
        @NonNull Map<String, Object> transaction;

    }

    @Value
    public static class ParamsOfProofMessageData extends JsonData {

        /**
         * Single message's data as queried from DApp server, without modifications. The required fields are `id` and/or top-level `boc`, others are optional. In order to reduce network requests count, it is recommended to provide at least `boc` of message and non-null `src_transaction.id` or `dst_transaction.id`.
         */
        @SerializedName("message")
        @NonNull Map<String, Object> message;

    }

}
