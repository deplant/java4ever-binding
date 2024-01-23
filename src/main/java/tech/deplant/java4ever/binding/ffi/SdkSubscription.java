package tech.deplant.java4ever.binding.ffi;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Predicate;

public record SdkSubscription(Queue<String> events, Consumer<JsonNode> eventConsumer) {
	public SdkSubscription(Consumer<JsonNode> eventConsumer) {
		this(new ConcurrentLinkedQueue<>(), eventConsumer);
	}
}
