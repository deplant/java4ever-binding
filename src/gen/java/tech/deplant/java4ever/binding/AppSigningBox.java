package tech.deplant.java4ever.binding;

import java.util.concurrent.CompletableFuture;
import java.util.Map;

public interface AppSigningBox {
    CompletableFuture<String> getPublicKey();
    CompletableFuture<String> sign(String unsigned);
}
