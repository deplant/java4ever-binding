package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;

public record WaitForTransactionEvent(@NonNull String type, @NonNull String message, @NonNull Object error,
                                      @JsonProperty("shard_block_id") @NonNull String shardBlockId,
                                      @JsonProperty("message_id") @NonNull String messageId) implements ExternalEvent {
}
