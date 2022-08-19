package tech.deplant.java4ever.binding;

import java.util.Map;

public interface AppEncryptionBox {
    Crypto.EncryptionBoxInfo getInfo();
    String encrypt(String data);
    String decrypt(String data);
}
