package tech.deplant.java4ever.binding;

import java.util.concurrent.CompletableFuture;

public interface AppEncryptionBox {
    CompletableFuture<Crypto.EncryptionBoxInfo> getInfo();

    CompletableFuture<String> encrypt(String data);

    CompletableFuture<String> decrypt(String data);
}
