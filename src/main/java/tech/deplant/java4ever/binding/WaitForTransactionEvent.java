package tech.deplant.java4ever.binding;

import com.google.gson.annotations.SerializedName;
import lombok.NonNull;
import lombok.Value;
import tech.deplant.java4ever.binding.json.JsonData;

@Value
public class WaitForTransactionEvent extends JsonData {
    @NonNull String type, message;
    @NonNull Object error;
    @SerializedName("shard_block_id")
    @NonNull String shardBlockId;
    @SerializedName("message_id")
    @NonNull String messageId;
}
