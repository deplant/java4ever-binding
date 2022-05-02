package tech.deplant.java4ever.binding;

import java.util.concurrent.CompletableFuture;

public interface AppSigningBox {
    CompletableFuture<String> getPublicKey();

    CompletableFuture<String> sign(String unsigned);
}
