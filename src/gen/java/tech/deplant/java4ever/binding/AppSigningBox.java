package tech.deplant.java4ever.binding;

import java.util.Map;

public interface AppSigningBox {
    String getPublicKey();
    String sign(String unsigned);
}
