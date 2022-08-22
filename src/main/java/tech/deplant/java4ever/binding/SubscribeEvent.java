package tech.deplant.java4ever.binding;

import java.util.Map;

public record SubscribeEvent(Map<String, Object> result) implements ExternalEvent {
}
