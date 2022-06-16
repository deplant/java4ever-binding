package tech.deplant.java4ever.binding;

import lombok.NonNull;
import lombok.Value;

import java.util.Map;

public record SubscribeCollectionEvent(@NonNull Map<String, Object> result) implements ExternalEvent {
}
