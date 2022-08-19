package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.Optional;
import lombok.*;
import java.util.stream.*;
import java.util.Arrays;

/**
 *  <strong>crypto</strong>
 *  Contains methods of "crypto" module.

 *  Crypto functions.
 *  @version EVER-SDK 1.37.0
 */
public class Crypto {


    /**
    * 
    * @param hdpath Derivation path, for instance "m/44'/396'/0'/0/0"
    * @param algorithm Cryptographic algorithm, used by this encryption box
    * @param options Options, depends on algorithm and specific encryption box implementation
    * @param publicKey Public information, depends on algorithm
    */
    public record EncryptionBoxInfo(String hdpath, String algorithm, Map<String,Object> options, @JsonProperty("public") Map<String,Object> publicKey) {}
    public interface EncryptionAlgorithm {


    /**
    * 
    * @param mode 
    * @param key 
    * @param iv 
    */
    public record AES(@NonNull Map<String,Object> mode, @NonNull String key, String iv) implements EncryptionAlgorithm {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * 
    * @param key 256-bit key. Must be encoded with `hex`.
    * @param nonce 96-bit nonce. Must be encoded with `hex`.
    */
    public record ChaCha20(@NonNull String key, @NonNull String nonce) implements EncryptionAlgorithm {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * 
    * @param theirPublic 256-bit key. Must be encoded with `hex`.
    * @param secretKey 256-bit key. Must be encoded with `hex`.
    * @param nonce 96-bit nonce. Must be encoded with `hex`.
    */
    public record NaclBox(@NonNull String theirPublic, @NonNull @JsonProperty("secret") String secretKey, @NonNull String nonce) implements EncryptionAlgorithm {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * 
    * @param key Secret key - unprefixed 0-padded to 64 symbols hex string
    * @param nonce Nonce in `hex`
    */
    public record NaclSecretBox(@NonNull String key, @NonNull String nonce) implements EncryptionAlgorithm {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}
    public interface CryptoBoxSecret {


    /**
    * Creates Crypto Box from a random seed phrase. This option can be used if a developer doesn't want the seed phrase to leave the core library's memory, where it is stored encrypted. This type should be used upon the first wallet initialization, all further initializationsshould use `EncryptedSecret` type instead.<p>Get `encrypted_secret` with `get_crypto_box_info` function and store it on your side.
    * @param dictionary 
    * @param wordcount 
    */
    public record RandomSeedPhrase(@NonNull Number dictionary, @NonNull Number wordcount) implements CryptoBoxSecret {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Restores crypto box instance from an existing seed phrase. This type should be used when Crypto Box is initialized from a seed phrase, entered by a user. This type should be used only upon the first wallet initialization, all furtherinitializations should use `EncryptedSecret` type instead.<p>Get `encrypted_secret` with `get_crypto_box_info` function and store it on your side.
    * @param phrase 
    * @param dictionary 
    * @param wordcount 
    */
    public record PredefinedSeedPhrase(@NonNull String phrase, @NonNull Number dictionary, @NonNull Number wordcount) implements CryptoBoxSecret {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Use this type for wallet reinitializations, when you already have `encrypted_secret` on hands. To get `encrypted_secret`, use `get_crypto_box_info` function after you initialized your crypto box for the first time. It is an object, containing seed phrase or private key, encrypted with`secret_encryption_salt` and password from `password_provider`.<p>Note that if you want to change salt or password provider, then you need to reinitializethe wallet with `PredefinedSeedPhrase`, then get `EncryptedSecret` via `get_crypto_box_info`,store it somewhere, and only after that initialize the wallet with `EncryptedSecret` type.
    * @param encryptedSecret It is an object, containing encrypted seed phrase or private key (now we support only seed phrase).
    */
    public record EncryptedSecret(@NonNull String encryptedSecret) implements CryptoBoxSecret {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}
    public interface BoxEncryptionAlgorithm {


    /**
    * 
    * @param nonce 96-bit nonce. Must be encoded with `hex`.
    */
    public record ChaCha20(@NonNull String nonce) implements BoxEncryptionAlgorithm {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * 
    * @param theirPublic 256-bit key. Must be encoded with `hex`.
    * @param nonce 96-bit nonce. Must be encoded with `hex`.
    */
    public record NaclBox(@NonNull String theirPublic, @NonNull String nonce) implements BoxEncryptionAlgorithm {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * 
    * @param nonce Nonce in `hex`
    */
    public record NaclSecretBox(@NonNull String nonce) implements BoxEncryptionAlgorithm {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}

    /**
    * 
    * @param composite Hexadecimal representation of u64 composite number.
    */
    public record ParamsOfFactorize(@NonNull String composite) {}

    /**
    * 
    * @param factors Two factors of composite or empty if composite can't be factorized.
    */
    public record ResultOfFactorize(@NonNull String[] factors) {}

    /**
    * 
    * @param base `base` argument of calculation.
    * @param exponent `exponent` argument of calculation.
    * @param modulus `modulus` argument of calculation.
    */
    public record ParamsOfModularPower(@NonNull String base, @NonNull String exponent, @NonNull String modulus) {}

    /**
    * 
    * @param modularPower Result of modular exponentiation
    */
    public record ResultOfModularPower(@NonNull String modularPower) {}

    /**
    * 
    * @param data Input data for CRC calculation. Encoded with `base64`.
    */
    public record ParamsOfTonCrc16(@NonNull String data) {}

    /**
    * 
    * @param crc Calculated CRC for input data.
    */
    public record ResultOfTonCrc16(@NonNull Number crc) {}

    /**
    * 
    * @param length Size of random byte array.
    */
    public record ParamsOfGenerateRandomBytes(@NonNull Number length) {}

    /**
    * 
    * @param bytes Generated bytes encoded in `base64`.
    */
    public record ResultOfGenerateRandomBytes(@NonNull String bytes) {}

    /**
    * 
    * @param publicKey Public key - 64 symbols hex string
    */
    public record ParamsOfConvertPublicKeyToTonSafeFormat(@NonNull String publicKey) {}

    /**
    * 
    * @param tonPublicKey Public key represented in TON safe format.
    */
    public record ResultOfConvertPublicKeyToTonSafeFormat(@NonNull String tonPublicKey) {}

    /**
    * 
    * @param publicKey Public key - 64 symbols hex string
    * @param secretKey Private key - u64 symbols hex string
    */
    public record KeyPair(@NonNull @JsonProperty("public") String publicKey, @NonNull @JsonProperty("secret") String secretKey) {}

    /**
    * 
    * @param unsigned Data that must be signed encoded in `base64`.
    * @param keys Sign keys.
    */
    public record ParamsOfSign(@NonNull String unsigned, @NonNull KeyPair keys) {}

    /**
    * 
    * @param signed Signed data combined with signature encoded in `base64`.
    * @param signature Signature encoded in `hex`.
    */
    public record ResultOfSign(@NonNull String signed, @NonNull String signature) {}

    /**
    * 
    * @param signed Signed data that must be verified encoded in `base64`.
    * @param publicKey Signer's public key - 64 symbols hex string
    */
    public record ParamsOfVerifySignature(@NonNull String signed, @NonNull @JsonProperty("public") String publicKey) {}

    /**
    * 
    * @param unsigned Unsigned data encoded in `base64`.
    */
    public record ResultOfVerifySignature(@NonNull String unsigned) {}

    /**
    * 
    * @param data Input data for hash calculation. Encoded with `base64`.
    */
    public record ParamsOfHash(@NonNull String data) {}

    /**
    * 
    * @param hash Hash of input `data`. Encoded with 'hex'.
    */
    public record ResultOfHash(@NonNull String hash) {}

    /**
    * 
    * @param password The password bytes to be hashed. Must be encoded with `base64`.
    * @param salt Salt bytes that modify the hash to protect against Rainbow table attacks. Must be encoded with `base64`.
    * @param logN CPU/memory cost parameter
    * @param r The block size parameter, which fine-tunes sequential memory read size and performance.
    * @param p Parallelization parameter.
    * @param dkLen Intended output length in octets of the derived key.
    */
    public record ParamsOfScrypt(@NonNull String password, @NonNull String salt, @NonNull Number logN, @NonNull Number r, @NonNull Number p, @NonNull Number dkLen) {}

    /**
    * 
    * @param key Derived key. Encoded with `hex`.
    */
    public record ResultOfScrypt(@NonNull String key) {}

    /**
    * 
    * @param secretKey Secret key - unprefixed 0-padded to 64 symbols hex string
    */
    public record ParamsOfNaclSignKeyPairFromSecret(@NonNull @JsonProperty("secret") String secretKey) {}

    /**
    * 
    * @param unsigned Data that must be signed encoded in `base64`.
    * @param secretKey Signer's secret key - unprefixed 0-padded to 128 symbols hex string (concatenation of 64 symbols secret and 64 symbols public keys). See `nacl_sign_keypair_from_secret_key`.
    */
    public record ParamsOfNaclSign(@NonNull String unsigned, @NonNull @JsonProperty("secret") String secretKey) {}

    /**
    * 
    * @param signed Signed data, encoded in `base64`.
    */
    public record ResultOfNaclSign(@NonNull String signed) {}

    /**
    * 
    * @param signed Signed data that must be unsigned. Encoded with `base64`.
    * @param publicKey Signer's public key - unprefixed 0-padded to 64 symbols hex string
    */
    public record ParamsOfNaclSignOpen(@NonNull String signed, @NonNull @JsonProperty("public") String publicKey) {}

    /**
    * 
    * @param unsigned Unsigned data, encoded in `base64`.
    */
    public record ResultOfNaclSignOpen(@NonNull String unsigned) {}

    /**
    * 
    * @param signature Signature encoded in `hex`.
    */
    public record ResultOfNaclSignDetached(@NonNull String signature) {}

    /**
    * 
    * @param unsigned Unsigned data that must be verified. Encoded with `base64`.
    * @param signature Signature that must be verified. Encoded with `hex`.
    * @param publicKey Signer's public key - unprefixed 0-padded to 64 symbols hex string.
    */
    public record ParamsOfNaclSignDetachedVerify(@NonNull String unsigned, @NonNull String signature, @NonNull @JsonProperty("public") String publicKey) {}

    /**
    * 
    * @param succeeded `true` if verification succeeded or `false` if it failed
    */
    public record ResultOfNaclSignDetachedVerify(@NonNull Boolean succeeded) {}

    /**
    * 
    * @param secretKey Secret key - unprefixed 0-padded to 64 symbols hex string
    */
    public record ParamsOfNaclBoxKeyPairFromSecret(@NonNull @JsonProperty("secret") String secretKey) {}

    /**
    * 
    * @param decrypted Data that must be encrypted encoded in `base64`.
    * @param nonce Nonce, encoded in `hex`
    * @param theirPublic Receiver's public key - unprefixed 0-padded to 64 symbols hex string
    * @param secretKey Sender's private key - unprefixed 0-padded to 64 symbols hex string
    */
    public record ParamsOfNaclBox(@NonNull String decrypted, @NonNull String nonce, @NonNull String theirPublic, @NonNull @JsonProperty("secret") String secretKey) {}

    /**
    * 
    * @param encrypted Encrypted data encoded in `base64`.
    */
    public record ResultOfNaclBox(@NonNull String encrypted) {}

    /**
    * 
    * @param encrypted Data that must be decrypted. Encoded with `base64`.
    * @param nonce Nonce
    * @param theirPublic Sender's public key - unprefixed 0-padded to 64 symbols hex string
    * @param secretKey Receiver's private key - unprefixed 0-padded to 64 symbols hex string
    */
    public record ParamsOfNaclBoxOpen(@NonNull String encrypted, @NonNull String nonce, @NonNull String theirPublic, @NonNull @JsonProperty("secret") String secretKey) {}

    /**
    * 
    * @param decrypted Decrypted data encoded in `base64`.
    */
    public record ResultOfNaclBoxOpen(@NonNull String decrypted) {}

    /**
    * 
    * @param decrypted Data that must be encrypted. Encoded with `base64`.
    * @param nonce Nonce in `hex`
    * @param key Secret key - unprefixed 0-padded to 64 symbols hex string
    */
    public record ParamsOfNaclSecretBox(@NonNull String decrypted, @NonNull String nonce, @NonNull String key) {}

    /**
    * 
    * @param encrypted Data that must be decrypted. Encoded with `base64`.
    * @param nonce Nonce in `hex`
    * @param key Secret key - unprefixed 0-padded to 64 symbols hex string
    */
    public record ParamsOfNaclSecretBoxOpen(@NonNull String encrypted, @NonNull String nonce, @NonNull String key) {}

    /**
    * 
    * @param dictionary Dictionary identifier
    */
    public record ParamsOfMnemonicWords(Number dictionary) {}

    /**
    * 
    * @param words The list of mnemonic words
    */
    public record ResultOfMnemonicWords(@NonNull String words) {}

    /**
    * 
    * @param dictionary Dictionary identifier
    * @param wordCount Mnemonic word count
    */
    public record ParamsOfMnemonicFromRandom(Number dictionary, Number wordCount) {}

    /**
    * 
    * @param phrase String of mnemonic words
    */
    public record ResultOfMnemonicFromRandom(@NonNull String phrase) {}

    /**
    * 
    * @param entropy Entropy bytes. Hex encoded.
    * @param dictionary Dictionary identifier
    * @param wordCount Mnemonic word count
    */
    public record ParamsOfMnemonicFromEntropy(@NonNull String entropy, Number dictionary, Number wordCount) {}

    /**
    * 
    * @param phrase Phrase
    */
    public record ResultOfMnemonicFromEntropy(@NonNull String phrase) {}

    /**
    * 
    * @param phrase Phrase
    * @param dictionary Dictionary identifier
    * @param wordCount Word count
    */
    public record ParamsOfMnemonicVerify(@NonNull String phrase, Number dictionary, Number wordCount) {}

    /**
    * 
    * @param valid Flag indicating if the mnemonic is valid or not
    */
    public record ResultOfMnemonicVerify(@NonNull Boolean valid) {}

    /**
    * 
    * @param phrase Phrase
    * @param path Derivation path, for instance "m/44'/396'/0'/0/0"
    * @param dictionary Dictionary identifier
    * @param wordCount Word count
    */
    public record ParamsOfMnemonicDeriveSignKeys(@NonNull String phrase, String path, Number dictionary, Number wordCount) {}

    /**
    * 
    * @param phrase String with seed phrase
    * @param dictionary Dictionary identifier
    * @param wordCount Mnemonic word count
    */
    public record ParamsOfHDKeyXPrvFromMnemonic(@NonNull String phrase, Number dictionary, Number wordCount) {}

    /**
    * 
    * @param xprv Serialized extended master private key
    */
    public record ResultOfHDKeyXPrvFromMnemonic(@NonNull String xprv) {}

    /**
    * 
    * @param xprv Serialized extended private key
    * @param childIndex Child index (see BIP-0032)
    * @param hardened Indicates the derivation of hardened/not-hardened key (see BIP-0032)
    */
    public record ParamsOfHDKeyDeriveFromXPrv(@NonNull String xprv, @NonNull Number childIndex, @NonNull Boolean hardened) {}

    /**
    * 
    * @param xprv Serialized extended private key
    */
    public record ResultOfHDKeyDeriveFromXPrv(@NonNull String xprv) {}

    /**
    * 
    * @param xprv Serialized extended private key
    * @param path Derivation path, for instance "m/44'/396'/0'/0/0"
    */
    public record ParamsOfHDKeyDeriveFromXPrvPath(@NonNull String xprv, @NonNull String path) {}

    /**
    * 
    * @param xprv Derived serialized extended private key
    */
    public record ResultOfHDKeyDeriveFromXPrvPath(@NonNull String xprv) {}

    /**
    * 
    * @param xprv Serialized extended private key
    */
    public record ParamsOfHDKeySecretFromXPrv(@NonNull String xprv) {}

    /**
    * 
    * @param secretKey Private key - 64 symbols hex string
    */
    public record ResultOfHDKeySecretFromXPrv(@NonNull @JsonProperty("secret") String secretKey) {}

    /**
    * 
    * @param xprv Serialized extended private key
    */
    public record ParamsOfHDKeyPublicFromXPrv(@NonNull String xprv) {}

    /**
    * 
    * @param publicKey Public key - 64 symbols hex string
    */
    public record ResultOfHDKeyPublicFromXPrv(@NonNull @JsonProperty("public") String publicKey) {}

    /**
    * 
    * @param data Source data to be encrypted or decrypted. Must be encoded with `base64`.
    * @param key 256-bit key. Must be encoded with `hex`.
    * @param nonce 96-bit nonce. Must be encoded with `hex`.
    */
    public record ParamsOfChaCha20(@NonNull String data, @NonNull String key, @NonNull String nonce) {}

    /**
    * 
    * @param data Encrypted/decrypted data. Encoded with `base64`.
    */
    public record ResultOfChaCha20(@NonNull String data) {}

    /**
    * 
    * @param secretEncryptionSalt Salt used for secret encryption. For example, a mobile device can use device ID as salt.
    * @param secretKey Cryptobox secret
    */
    public record ParamsOfCreateCryptoBox(@NonNull String secretEncryptionSalt, @NonNull @JsonProperty("secret") CryptoBoxSecret secretKey) {}

    /**
    * 
    * @param handle 
    */
    public record RegisteredCryptoBox(@NonNull Integer handle) {}

    /**
    * 
    * @param encryptedSecret Secret (seed phrase) encrypted with salt and password.
    */
    public record ResultOfGetCryptoBoxInfo(@NonNull String encryptedSecret) {}

    /**
    * 
    * @param phrase 
    * @param dictionary 
    * @param wordcount 
    */
    public record ResultOfGetCryptoBoxSeedPhrase(@NonNull String phrase, @NonNull Number dictionary, @NonNull Number wordcount) {}

    /**
    * 
    * @param handle Crypto Box Handle.
    * @param hdpath HD key derivation path. By default, Everscale HD path is used.
    * @param secretLifetime Store derived secret for this lifetime (in ms). The timer starts after each signing box operation. Secrets will be deleted immediately after each signing box operation, if this value is not set.
    */
    public record ParamsOfGetSigningBoxFromCryptoBox(@NonNull Number handle, String hdpath, Number secretLifetime) {}

    /**
    * 
    * @param handle Handle of the signing box.
    */
    public record RegisteredSigningBox(@NonNull Integer handle) {}

    /**
    * 
    * @param handle Crypto Box Handle.
    * @param hdpath HD key derivation path. By default, Everscale HD path is used.
    * @param algorithm Encryption algorithm.
    * @param secretLifetime Store derived secret for encryption algorithm for this lifetime (in ms). The timer starts after each encryption box operation. Secrets will be deleted (overwritten with zeroes) after each encryption operation, if this value is not set.
    */
    public record ParamsOfGetEncryptionBoxFromCryptoBox(@NonNull Number handle, String hdpath, @NonNull BoxEncryptionAlgorithm algorithm, Number secretLifetime) {}

    /**
    * 
    * @param handle Handle of the encryption box.
    */
    public record RegisteredEncryptionBox(@NonNull Integer handle) {}
    public interface ParamsOfAppSigningBox {

        public static final GetPublicKey GETPUBLICKEY = new GetPublicKey();


    /**
    * Get signing box public key

    */
    public record GetPublicKey() implements ParamsOfAppSigningBox {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Sign data
    * @param unsigned Data to sign encoded as base64
    */
    public record Sign(@NonNull String unsigned) implements ParamsOfAppSigningBox {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}
    public interface ResultOfAppSigningBox {


    /**
    * Result of getting public key
    * @param publicKey Signing box public key
    */
    public record GetPublicKey(@NonNull String publicKey) implements ResultOfAppSigningBox {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Result of signing data
    * @param signature Data signature encoded as hex
    */
    public record Sign(@NonNull String signature) implements ResultOfAppSigningBox {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}

    /**
    * 
    * @param pubkey Public key of signing box. Encoded with hex
    */
    public record ResultOfSigningBoxGetPublicKey(@NonNull String pubkey) {}

    /**
    * 
    * @param signingBox Signing Box handle.
    * @param unsigned Unsigned user data. Must be encoded with `base64`.
    */
    public record ParamsOfSigningBoxSign(@NonNull Integer signingBox, @NonNull String unsigned) {}

    /**
    * 
    * @param signature Data signature. Encoded with `hex`.
    */
    public record ResultOfSigningBoxSign(@NonNull String signature) {}
    public interface ParamsOfAppEncryptionBox {

        public static final GetInfo GETINFO = new GetInfo();


    /**
    * Get encryption box info

    */
    public record GetInfo() implements ParamsOfAppEncryptionBox {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Encrypt data
    * @param data Data, encoded in Base64
    */
    public record Encrypt(@NonNull String data) implements ParamsOfAppEncryptionBox {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Decrypt data
    * @param data Data, encoded in Base64
    */
    public record Decrypt(@NonNull String data) implements ParamsOfAppEncryptionBox {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}
    public interface ResultOfAppEncryptionBox {


    /**
    * Result of getting encryption box info
    * @param info 
    */
    public record GetInfo(@NonNull EncryptionBoxInfo info) implements ResultOfAppEncryptionBox {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Result of encrypting data
    * @param data Encrypted data, encoded in Base64
    */
    public record Encrypt(@NonNull String data) implements ResultOfAppEncryptionBox {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Result of decrypting data
    * @param data Decrypted data, encoded in Base64
    */
    public record Decrypt(@NonNull String data) implements ResultOfAppEncryptionBox {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}

    /**
    * 
    * @param encryptionBox Encryption box handle
    */
    public record ParamsOfEncryptionBoxGetInfo(@NonNull Integer encryptionBox) {}

    /**
    * 
    * @param info Encryption box information
    */
    public record ResultOfEncryptionBoxGetInfo(@NonNull EncryptionBoxInfo info) {}

    /**
    * 
    * @param encryptionBox Encryption box handle
    * @param data Data to be encrypted, encoded in Base64
    */
    public record ParamsOfEncryptionBoxEncrypt(@NonNull Integer encryptionBox, @NonNull String data) {}

    /**
    * 
    * @param data Encrypted data, encoded in Base64. Padded to cipher block size
    */
    public record ResultOfEncryptionBoxEncrypt(@NonNull String data) {}

    /**
    * 
    * @param encryptionBox Encryption box handle
    * @param data Data to be decrypted, encoded in Base64
    */
    public record ParamsOfEncryptionBoxDecrypt(@NonNull Integer encryptionBox, @NonNull String data) {}

    /**
    * 
    * @param data Decrypted data, encoded in Base64.
    */
    public record ResultOfEncryptionBoxDecrypt(@NonNull String data) {}

    /**
    * 
    * @param algorithm Encryption algorithm specifier including cipher parameters (key, IV, etc)
    */
    public record ParamsOfCreateEncryptionBox(@NonNull EncryptionAlgorithm algorithm) {}
    /**
    * <strong>crypto.factorize</strong>
    * Integer factorization Performs prime factorization – decomposition of a composite numberinto a product of smaller prime integers (factors).
    * @param composite Hexadecimal representation of u64 composite number. 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfFactorize}
    */
    public static ResultOfFactorize factorize(@NonNull Context ctx, @NonNull String composite)  throws JsonProcessingException {
        return  ctx.call("crypto.factorize", new ParamsOfFactorize(composite), ResultOfFactorize.class);
    }

    /**
    * <strong>crypto.modular_power</strong>
    * Modular exponentiation Performs modular exponentiation for big integers (`base`^`exponent` mod `modulus`).
    * @param base `base` argument of calculation. 
    * @param exponent `exponent` argument of calculation. 
    * @param modulus `modulus` argument of calculation. 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfModularPower}
    */
    public static ResultOfModularPower modularPower(@NonNull Context ctx, @NonNull String base, @NonNull String exponent, @NonNull String modulus)  throws JsonProcessingException {
        return  ctx.call("crypto.modular_power", new ParamsOfModularPower(base, exponent, modulus), ResultOfModularPower.class);
    }

    /**
    * <strong>crypto.ton_crc16</strong>
    * Calculates CRC16 using TON algorithm.
    * @param data Input data for CRC calculation. Encoded with `base64`.
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfTonCrc16}
    */
    public static ResultOfTonCrc16 tonCrc16(@NonNull Context ctx, @NonNull String data)  throws JsonProcessingException {
        return  ctx.call("crypto.ton_crc16", new ParamsOfTonCrc16(data), ResultOfTonCrc16.class);
    }

    /**
    * <strong>crypto.generate_random_bytes</strong>
    * Generates random byte array of the specified length and returns it in `base64` format
    * @param length Size of random byte array. 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfGenerateRandomBytes}
    */
    public static ResultOfGenerateRandomBytes generateRandomBytes(@NonNull Context ctx, @NonNull Number length)  throws JsonProcessingException {
        return  ctx.call("crypto.generate_random_bytes", new ParamsOfGenerateRandomBytes(length), ResultOfGenerateRandomBytes.class);
    }

    /**
    * <strong>crypto.convert_public_key_to_ton_safe_format</strong>
    * Converts public key to ton safe_format
    * @param publicKey Public key - 64 symbols hex string 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfConvertPublicKeyToTonSafeFormat}
    */
    public static ResultOfConvertPublicKeyToTonSafeFormat convertPublicKeyToTonSafeFormat(@NonNull Context ctx, @NonNull String publicKey)  throws JsonProcessingException {
        return  ctx.call("crypto.convert_public_key_to_ton_safe_format", new ParamsOfConvertPublicKeyToTonSafeFormat(publicKey), ResultOfConvertPublicKeyToTonSafeFormat.class);
    }

    /**
    * <strong>crypto.generate_random_sign_keys</strong>
    * Generates random ed25519 key pair.
    * @return {@link tech.deplant.java4ever.binding.Crypto.KeyPair}
    */
    public static KeyPair generateRandomSignKeys(@NonNull Context ctx)  throws JsonProcessingException {
        return  ctx.call("crypto.generate_random_sign_keys", null, KeyPair.class);
    }

    /**
    * <strong>crypto.sign</strong>
    * Signs a data using the provided keys.
    * @param unsigned Data that must be signed encoded in `base64`. 
    * @param keys Sign keys. 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfSign}
    */
    public static ResultOfSign sign(@NonNull Context ctx, @NonNull String unsigned, @NonNull KeyPair keys)  throws JsonProcessingException {
        return  ctx.call("crypto.sign", new ParamsOfSign(unsigned, keys), ResultOfSign.class);
    }

    /**
    * <strong>crypto.verify_signature</strong>
    * Verifies signed data using the provided public key. Raises error if verification is failed.
    * @param signed Signed data that must be verified encoded in `base64`. 
    * @param publicKey Signer's public key - 64 symbols hex string 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfVerifySignature}
    */
    public static ResultOfVerifySignature verifySignature(@NonNull Context ctx, @NonNull String signed, @NonNull String publicKey)  throws JsonProcessingException {
        return  ctx.call("crypto.verify_signature", new ParamsOfVerifySignature(signed, publicKey), ResultOfVerifySignature.class);
    }

    /**
    * <strong>crypto.sha256</strong>
    * Calculates SHA256 hash of the specified data.
    * @param data Input data for hash calculation. Encoded with `base64`.
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfHash}
    */
    public static ResultOfHash sha256(@NonNull Context ctx, @NonNull String data)  throws JsonProcessingException {
        return  ctx.call("crypto.sha256", new ParamsOfHash(data), ResultOfHash.class);
    }

    /**
    * <strong>crypto.sha512</strong>
    * Calculates SHA512 hash of the specified data.
    * @param data Input data for hash calculation. Encoded with `base64`.
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfHash}
    */
    public static ResultOfHash sha512(@NonNull Context ctx, @NonNull String data)  throws JsonProcessingException {
        return  ctx.call("crypto.sha512", new ParamsOfHash(data), ResultOfHash.class);
    }

    /**
    * <strong>crypto.scrypt</strong>
    * Perform `scrypt` encryption Derives key from `password` and `key` using `scrypt` algorithm.<p># Arguments- `log_n` - The log2 of the Scrypt parameter `N`- `r` - The Scrypt parameter `r`- `p` - The Scrypt parameter `p`# Conditions- `log_n` must be less than `64`- `r` must be greater than `0` and less than or equal to `4294967295`- `p` must be greater than `0` and less than `4294967295`# Recommended values sufficient for most use-cases- `log_n = 15` (`n = 32768`)- `r = 8`- `p = 1`
    * @param password The password bytes to be hashed. Must be encoded with `base64`. 
    * @param salt Salt bytes that modify the hash to protect against Rainbow table attacks. Must be encoded with `base64`. 
    * @param logN CPU/memory cost parameter 
    * @param r The block size parameter, which fine-tunes sequential memory read size and performance. 
    * @param p Parallelization parameter. 
    * @param dkLen Intended output length in octets of the derived key. 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfScrypt}
    */
    public static ResultOfScrypt scrypt(@NonNull Context ctx, @NonNull String password, @NonNull String salt, @NonNull Number logN, @NonNull Number r, @NonNull Number p, @NonNull Number dkLen)  throws JsonProcessingException {
        return  ctx.call("crypto.scrypt", new ParamsOfScrypt(password, salt, logN, r, p, dkLen), ResultOfScrypt.class);
    }

    /**
    * <strong>crypto.nacl_sign_keypair_from_secret_key</strong>
    * Generates a key pair for signing from the secret key **NOTE:** In the result the secret key is actually the concatenationof secret and public keys (128 symbols hex string) by design of NaCL
    * @param secretKey Secret key - unprefixed 0-padded to 64 symbols hex string 
    * @return {@link tech.deplant.java4ever.binding.Crypto.KeyPair}
    */
    public static KeyPair naclSignKeypairFromSecretKey(@NonNull Context ctx, @NonNull String secretKey)  throws JsonProcessingException {
        return  ctx.call("crypto.nacl_sign_keypair_from_secret_key", new ParamsOfNaclSignKeyPairFromSecret(secretKey), KeyPair.class);
    }

    /**
    * <strong>crypto.nacl_sign</strong>
    * Signs data using the signer's secret key.
    * @param unsigned Data that must be signed encoded in `base64`. 
    * @param secretKey Signer's secret key - unprefixed 0-padded to 128 symbols hex string (concatenation of 64 symbols secret and 64 symbols public keys). See `nacl_sign_keypair_from_secret_key`. 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfNaclSign}
    */
    public static ResultOfNaclSign naclSign(@NonNull Context ctx, @NonNull String unsigned, @NonNull String secretKey)  throws JsonProcessingException {
        return  ctx.call("crypto.nacl_sign", new ParamsOfNaclSign(unsigned, secretKey), ResultOfNaclSign.class);
    }

    /**
    * <strong>crypto.nacl_sign_open</strong>
    * Verifies the signature and returns the unsigned message Verifies the signature in `signed` using the signer's public key `public`and returns the message `unsigned`.<p>If the signature fails verification, crypto_sign_open raises an exception.
    * @param signed Signed data that must be unsigned. Encoded with `base64`.
    * @param publicKey Signer's public key - unprefixed 0-padded to 64 symbols hex string 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfNaclSignOpen}
    */
    public static ResultOfNaclSignOpen naclSignOpen(@NonNull Context ctx, @NonNull String signed, @NonNull String publicKey)  throws JsonProcessingException {
        return  ctx.call("crypto.nacl_sign_open", new ParamsOfNaclSignOpen(signed, publicKey), ResultOfNaclSignOpen.class);
    }

    /**
    * <strong>crypto.nacl_sign_detached</strong>
    * Signs the message using the secret key and returns a signature. Signs the message `unsigned` using the secret key `secret`and returns a signature `signature`.
    * @param unsigned Data that must be signed encoded in `base64`. 
    * @param secretKey Signer's secret key - unprefixed 0-padded to 128 symbols hex string (concatenation of 64 symbols secret and 64 symbols public keys). See `nacl_sign_keypair_from_secret_key`. 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfNaclSignDetached}
    */
    public static ResultOfNaclSignDetached naclSignDetached(@NonNull Context ctx, @NonNull String unsigned, @NonNull String secretKey)  throws JsonProcessingException {
        return  ctx.call("crypto.nacl_sign_detached", new ParamsOfNaclSign(unsigned, secretKey), ResultOfNaclSignDetached.class);
    }

    /**
    * <strong>crypto.nacl_sign_detached_verify</strong>
    * Verifies the signature with public key and `unsigned` data.
    * @param unsigned Unsigned data that must be verified. Encoded with `base64`.
    * @param signature Signature that must be verified. Encoded with `hex`.
    * @param publicKey Signer's public key - unprefixed 0-padded to 64 symbols hex string. 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfNaclSignDetachedVerify}
    */
    public static ResultOfNaclSignDetachedVerify naclSignDetachedVerify(@NonNull Context ctx, @NonNull String unsigned, @NonNull String signature, @NonNull String publicKey)  throws JsonProcessingException {
        return  ctx.call("crypto.nacl_sign_detached_verify", new ParamsOfNaclSignDetachedVerify(unsigned, signature, publicKey), ResultOfNaclSignDetachedVerify.class);
    }

    /**
    * <strong>crypto.nacl_box_keypair</strong>
    * Generates a random NaCl key pair
    * @return {@link tech.deplant.java4ever.binding.Crypto.KeyPair}
    */
    public static KeyPair naclBoxKeypair(@NonNull Context ctx)  throws JsonProcessingException {
        return  ctx.call("crypto.nacl_box_keypair", null, KeyPair.class);
    }

    /**
    * <strong>crypto.nacl_box_keypair_from_secret_key</strong>
    * Generates key pair from a secret key
    * @param secretKey Secret key - unprefixed 0-padded to 64 symbols hex string 
    * @return {@link tech.deplant.java4ever.binding.Crypto.KeyPair}
    */
    public static KeyPair naclBoxKeypairFromSecretKey(@NonNull Context ctx, @NonNull String secretKey)  throws JsonProcessingException {
        return  ctx.call("crypto.nacl_box_keypair_from_secret_key", new ParamsOfNaclBoxKeyPairFromSecret(secretKey), KeyPair.class);
    }

    /**
    * <strong>crypto.nacl_box</strong>
    * Public key authenticated encryption Encrypt and authenticate a message using the senders secret key, the receivers publickey, and a nonce.
    * @param decrypted Data that must be encrypted encoded in `base64`. 
    * @param nonce Nonce, encoded in `hex` 
    * @param theirPublic Receiver's public key - unprefixed 0-padded to 64 symbols hex string 
    * @param secretKey Sender's private key - unprefixed 0-padded to 64 symbols hex string 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfNaclBox}
    */
    public static ResultOfNaclBox naclBox(@NonNull Context ctx, @NonNull String decrypted, @NonNull String nonce, @NonNull String theirPublic, @NonNull String secretKey)  throws JsonProcessingException {
        return  ctx.call("crypto.nacl_box", new ParamsOfNaclBox(decrypted, nonce, theirPublic, secretKey), ResultOfNaclBox.class);
    }

    /**
    * <strong>crypto.nacl_box_open</strong>
    * Decrypt and verify the cipher text using the receivers secret key, the senders public key, and the nonce.
    * @param encrypted Data that must be decrypted. Encoded with `base64`.
    * @param nonce Nonce 
    * @param theirPublic Sender's public key - unprefixed 0-padded to 64 symbols hex string 
    * @param secretKey Receiver's private key - unprefixed 0-padded to 64 symbols hex string 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfNaclBoxOpen}
    */
    public static ResultOfNaclBoxOpen naclBoxOpen(@NonNull Context ctx, @NonNull String encrypted, @NonNull String nonce, @NonNull String theirPublic, @NonNull String secretKey)  throws JsonProcessingException {
        return  ctx.call("crypto.nacl_box_open", new ParamsOfNaclBoxOpen(encrypted, nonce, theirPublic, secretKey), ResultOfNaclBoxOpen.class);
    }

    /**
    * <strong>crypto.nacl_secret_box</strong>
    * Encrypt and authenticate message using nonce and secret key.
    * @param decrypted Data that must be encrypted. Encoded with `base64`.
    * @param nonce Nonce in `hex` 
    * @param key Secret key - unprefixed 0-padded to 64 symbols hex string 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfNaclBox}
    */
    public static ResultOfNaclBox naclSecretBox(@NonNull Context ctx, @NonNull String decrypted, @NonNull String nonce, @NonNull String key)  throws JsonProcessingException {
        return  ctx.call("crypto.nacl_secret_box", new ParamsOfNaclSecretBox(decrypted, nonce, key), ResultOfNaclBox.class);
    }

    /**
    * <strong>crypto.nacl_secret_box_open</strong>
    * Decrypts and verifies cipher text using `nonce` and secret `key`.
    * @param encrypted Data that must be decrypted. Encoded with `base64`.
    * @param nonce Nonce in `hex` 
    * @param key Secret key - unprefixed 0-padded to 64 symbols hex string 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfNaclBoxOpen}
    */
    public static ResultOfNaclBoxOpen naclSecretBoxOpen(@NonNull Context ctx, @NonNull String encrypted, @NonNull String nonce, @NonNull String key)  throws JsonProcessingException {
        return  ctx.call("crypto.nacl_secret_box_open", new ParamsOfNaclSecretBoxOpen(encrypted, nonce, key), ResultOfNaclBoxOpen.class);
    }

    /**
    * <strong>crypto.mnemonic_words</strong>
    * Prints the list of words from the specified dictionary
    * @param dictionary Dictionary identifier 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfMnemonicWords}
    */
    public static ResultOfMnemonicWords mnemonicWords(@NonNull Context ctx,  Number dictionary)  throws JsonProcessingException {
        return  ctx.call("crypto.mnemonic_words", new ParamsOfMnemonicWords(dictionary), ResultOfMnemonicWords.class);
    }

    /**
    * <strong>crypto.mnemonic_from_random</strong>
    * Generates a random mnemonic Generates a random mnemonic from the specified dictionary and word count
    * @param dictionary Dictionary identifier 
    * @param wordCount Mnemonic word count 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfMnemonicFromRandom}
    */
    public static ResultOfMnemonicFromRandom mnemonicFromRandom(@NonNull Context ctx,  Number dictionary,  Number wordCount)  throws JsonProcessingException {
        return  ctx.call("crypto.mnemonic_from_random", new ParamsOfMnemonicFromRandom(dictionary, wordCount), ResultOfMnemonicFromRandom.class);
    }

    /**
    * <strong>crypto.mnemonic_from_entropy</strong>
    * Generates mnemonic from pre-generated entropy
    * @param entropy Entropy bytes. Hex encoded.
    * @param dictionary Dictionary identifier 
    * @param wordCount Mnemonic word count 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfMnemonicFromEntropy}
    */
    public static ResultOfMnemonicFromEntropy mnemonicFromEntropy(@NonNull Context ctx, @NonNull String entropy,  Number dictionary,  Number wordCount)  throws JsonProcessingException {
        return  ctx.call("crypto.mnemonic_from_entropy", new ParamsOfMnemonicFromEntropy(entropy, dictionary, wordCount), ResultOfMnemonicFromEntropy.class);
    }

    /**
    * <strong>crypto.mnemonic_verify</strong>
    * Validates a mnemonic phrase The phrase supplied will be checked for word length and validated according to the checksumspecified in BIP0039.
    * @param phrase Phrase 
    * @param dictionary Dictionary identifier 
    * @param wordCount Word count 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfMnemonicVerify}
    */
    public static ResultOfMnemonicVerify mnemonicVerify(@NonNull Context ctx, @NonNull String phrase,  Number dictionary,  Number wordCount)  throws JsonProcessingException {
        return  ctx.call("crypto.mnemonic_verify", new ParamsOfMnemonicVerify(phrase, dictionary, wordCount), ResultOfMnemonicVerify.class);
    }

    /**
    * <strong>crypto.mnemonic_derive_sign_keys</strong>
    * Derives a key pair for signing from the seed phrase Validates the seed phrase, generates master key and then derivesthe key pair from the master key and the specified path
    * @param phrase Phrase 
    * @param path Derivation path, for instance "m/44'/396'/0'/0/0" 
    * @param dictionary Dictionary identifier 
    * @param wordCount Word count 
    * @return {@link tech.deplant.java4ever.binding.Crypto.KeyPair}
    */
    public static KeyPair mnemonicDeriveSignKeys(@NonNull Context ctx, @NonNull String phrase,  String path,  Number dictionary,  Number wordCount)  throws JsonProcessingException {
        return  ctx.call("crypto.mnemonic_derive_sign_keys", new ParamsOfMnemonicDeriveSignKeys(phrase, path, dictionary, wordCount), KeyPair.class);
    }

    /**
    * <strong>crypto.hdkey_xprv_from_mnemonic</strong>
    * Generates an extended master private key that will be the root for all the derived keys
    * @param phrase String with seed phrase 
    * @param dictionary Dictionary identifier 
    * @param wordCount Mnemonic word count 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfHDKeyXPrvFromMnemonic}
    */
    public static ResultOfHDKeyXPrvFromMnemonic hdkeyXprvFromMnemonic(@NonNull Context ctx, @NonNull String phrase,  Number dictionary,  Number wordCount)  throws JsonProcessingException {
        return  ctx.call("crypto.hdkey_xprv_from_mnemonic", new ParamsOfHDKeyXPrvFromMnemonic(phrase, dictionary, wordCount), ResultOfHDKeyXPrvFromMnemonic.class);
    }

    /**
    * <strong>crypto.hdkey_derive_from_xprv</strong>
    * Returns extended private key derived from the specified extended private key and child index
    * @param xprv Serialized extended private key 
    * @param childIndex Child index (see BIP-0032) 
    * @param hardened Indicates the derivation of hardened/not-hardened key (see BIP-0032) 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfHDKeyDeriveFromXPrv}
    */
    public static ResultOfHDKeyDeriveFromXPrv hdkeyDeriveFromXprv(@NonNull Context ctx, @NonNull String xprv, @NonNull Number childIndex, @NonNull Boolean hardened)  throws JsonProcessingException {
        return  ctx.call("crypto.hdkey_derive_from_xprv", new ParamsOfHDKeyDeriveFromXPrv(xprv, childIndex, hardened), ResultOfHDKeyDeriveFromXPrv.class);
    }

    /**
    * <strong>crypto.hdkey_derive_from_xprv_path</strong>
    * Derives the extended private key from the specified key and path
    * @param xprv Serialized extended private key 
    * @param path Derivation path, for instance "m/44'/396'/0'/0/0" 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfHDKeyDeriveFromXPrvPath}
    */
    public static ResultOfHDKeyDeriveFromXPrvPath hdkeyDeriveFromXprvPath(@NonNull Context ctx, @NonNull String xprv, @NonNull String path)  throws JsonProcessingException {
        return  ctx.call("crypto.hdkey_derive_from_xprv_path", new ParamsOfHDKeyDeriveFromXPrvPath(xprv, path), ResultOfHDKeyDeriveFromXPrvPath.class);
    }

    /**
    * <strong>crypto.hdkey_secret_from_xprv</strong>
    * Extracts the private key from the serialized extended private key
    * @param xprv Serialized extended private key 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfHDKeySecretFromXPrv}
    */
    public static ResultOfHDKeySecretFromXPrv hdkeySecretFromXprv(@NonNull Context ctx, @NonNull String xprv)  throws JsonProcessingException {
        return  ctx.call("crypto.hdkey_secret_from_xprv", new ParamsOfHDKeySecretFromXPrv(xprv), ResultOfHDKeySecretFromXPrv.class);
    }

    /**
    * <strong>crypto.hdkey_public_from_xprv</strong>
    * Extracts the public key from the serialized extended private key
    * @param xprv Serialized extended private key 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfHDKeyPublicFromXPrv}
    */
    public static ResultOfHDKeyPublicFromXPrv hdkeyPublicFromXprv(@NonNull Context ctx, @NonNull String xprv)  throws JsonProcessingException {
        return  ctx.call("crypto.hdkey_public_from_xprv", new ParamsOfHDKeyPublicFromXPrv(xprv), ResultOfHDKeyPublicFromXPrv.class);
    }

    /**
    * <strong>crypto.chacha20</strong>
    * Performs symmetric `chacha20` encryption.
    * @param data Source data to be encrypted or decrypted. Must be encoded with `base64`.
    * @param key 256-bit key. Must be encoded with `hex`.
    * @param nonce 96-bit nonce. Must be encoded with `hex`.
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfChaCha20}
    */
    public static ResultOfChaCha20 chacha20(@NonNull Context ctx, @NonNull String data, @NonNull String key, @NonNull String nonce)  throws JsonProcessingException {
        return  ctx.call("crypto.chacha20", new ParamsOfChaCha20(data, key, nonce), ResultOfChaCha20.class);
    }

    /**
    * <strong>crypto.create_crypto_box</strong>
    * Creates a Crypto Box instance. Crypto Box is a root crypto object, that encapsulates some secret (seed phrase usually)in encrypted form and acts as a factory for all crypto primitives used in SDK:keys for signing and encryption, derived from this secret.<p>Crypto Box encrypts original Seed Phrase with salt and password that is retrievedfrom `password_provider` callback, implemented on Application side.<p>When used, decrypted secret shows up in core library's memory for a very short periodof time and then is immediately overwritten with zeroes.
    * @param secretEncryptionSalt Salt used for secret encryption. For example, a mobile device can use device ID as salt. 
    * @param secretKey Cryptobox secret 
    * @return {@link tech.deplant.java4ever.binding.Crypto.RegisteredCryptoBox}
    */
    public static RegisteredCryptoBox createCryptoBox(@NonNull Context ctx, @NonNull String secretEncryptionSalt, @NonNull CryptoBoxSecret secretKey)  throws JsonProcessingException {
        return  ctx.call("crypto.create_crypto_box", new ParamsOfCreateCryptoBox(secretEncryptionSalt, secretKey), RegisteredCryptoBox.class);
    }

    /**
    * <strong>crypto.remove_crypto_box</strong>
    * Removes Crypto Box. Clears all secret data.
    * @param handle  
    */
    public static void removeCryptoBox(@NonNull Context ctx, @NonNull Integer handle)  throws JsonProcessingException {
         ctx.callVoid("crypto.remove_crypto_box", new RegisteredCryptoBox(handle));
    }

    /**
    * <strong>crypto.get_crypto_box_info</strong>
    * Get Crypto Box Info. Used to get `encrypted_secret` that should be used for all the cryptobox initializations except the first one.
    * @param handle  
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfGetCryptoBoxInfo}
    */
    public static ResultOfGetCryptoBoxInfo getCryptoBoxInfo(@NonNull Context ctx, @NonNull Integer handle)  throws JsonProcessingException {
        return  ctx.call("crypto.get_crypto_box_info", new RegisteredCryptoBox(handle), ResultOfGetCryptoBoxInfo.class);
    }

    /**
    * <strong>crypto.get_crypto_box_seed_phrase</strong>
    * Get Crypto Box Seed Phrase. Attention! Store this data in your application for a very short period of time and overwrite it with zeroes ASAP.
    * @param handle  
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfGetCryptoBoxSeedPhrase}
    */
    public static ResultOfGetCryptoBoxSeedPhrase getCryptoBoxSeedPhrase(@NonNull Context ctx, @NonNull Integer handle)  throws JsonProcessingException {
        return  ctx.call("crypto.get_crypto_box_seed_phrase", new RegisteredCryptoBox(handle), ResultOfGetCryptoBoxSeedPhrase.class);
    }

    /**
    * <strong>crypto.get_signing_box_from_crypto_box</strong>
    * Get handle of Signing Box derived from Crypto Box.
    * @param handle Crypto Box Handle. 
    * @param hdpath HD key derivation path. By default, Everscale HD path is used.
    * @param secretLifetime Store derived secret for this lifetime (in ms). The timer starts after each signing box operation. Secrets will be deleted immediately after each signing box operation, if this value is not set. 
    * @return {@link tech.deplant.java4ever.binding.Crypto.RegisteredSigningBox}
    */
    public static RegisteredSigningBox getSigningBoxFromCryptoBox(@NonNull Context ctx, @NonNull Number handle,  String hdpath,  Number secretLifetime)  throws JsonProcessingException {
        return  ctx.call("crypto.get_signing_box_from_crypto_box", new ParamsOfGetSigningBoxFromCryptoBox(handle, hdpath, secretLifetime), RegisteredSigningBox.class);
    }

    /**
    * <strong>crypto.get_encryption_box_from_crypto_box</strong>
    * Gets Encryption Box from Crypto Box. Derives encryption keypair from cryptobox secret and hdpath andstores it in cache for `secret_lifetime`or until explicitly cleared by `clear_crypto_box_secret_cache` method.If `secret_lifetime` is not specified - overwrites encryption secret with zeroes immediately afterencryption operation.
    * @param handle Crypto Box Handle. 
    * @param hdpath HD key derivation path. By default, Everscale HD path is used.
    * @param algorithm Encryption algorithm. 
    * @param secretLifetime Store derived secret for encryption algorithm for this lifetime (in ms). The timer starts after each encryption box operation. Secrets will be deleted (overwritten with zeroes) after each encryption operation, if this value is not set. 
    * @return {@link tech.deplant.java4ever.binding.Crypto.RegisteredEncryptionBox}
    */
    public static RegisteredEncryptionBox getEncryptionBoxFromCryptoBox(@NonNull Context ctx, @NonNull Number handle,  String hdpath, @NonNull BoxEncryptionAlgorithm algorithm,  Number secretLifetime)  throws JsonProcessingException {
        return  ctx.call("crypto.get_encryption_box_from_crypto_box", new ParamsOfGetEncryptionBoxFromCryptoBox(handle, hdpath, algorithm, secretLifetime), RegisteredEncryptionBox.class);
    }

    /**
    * <strong>crypto.clear_crypto_box_secret_cache</strong>
    * Removes cached secrets (overwrites with zeroes) from all signing and encryption boxes, derived from crypto box.
    * @param handle  
    */
    public static void clearCryptoBoxSecretCache(@NonNull Context ctx, @NonNull Integer handle)  throws JsonProcessingException {
         ctx.callVoid("crypto.clear_crypto_box_secret_cache", new RegisteredCryptoBox(handle));
    }

    /**
    * <strong>crypto.register_signing_box</strong>
    * Register an application implemented signing box.
    * @param appObject  
    * @return {@link tech.deplant.java4ever.binding.Crypto.RegisteredSigningBox}
    */
    public static RegisteredSigningBox registerSigningBox(@NonNull Context ctx,  AppSigningBox appObject)  throws JsonProcessingException {
        return  ctx.callAppObject("crypto.register_signing_box", null, appObject, RegisteredSigningBox.class);
    }

    /**
    * <strong>crypto.get_signing_box</strong>
    * Creates a default signing box implementation.
    * @param publicKey Public key - 64 symbols hex string 
    * @param secretKey Private key - u64 symbols hex string 
    * @return {@link tech.deplant.java4ever.binding.Crypto.RegisteredSigningBox}
    */
    public static RegisteredSigningBox getSigningBox(@NonNull Context ctx, @NonNull String publicKey, @NonNull String secretKey)  throws JsonProcessingException {
        return  ctx.call("crypto.get_signing_box", new KeyPair(publicKey, secretKey), RegisteredSigningBox.class);
    }

    /**
    * <strong>crypto.signing_box_get_public_key</strong>
    * Returns public key of signing key pair.
    * @param handle Handle of the signing box. 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfSigningBoxGetPublicKey}
    */
    public static ResultOfSigningBoxGetPublicKey signingBoxGetPublicKey(@NonNull Context ctx, @NonNull Integer handle)  throws JsonProcessingException {
        return  ctx.call("crypto.signing_box_get_public_key", new RegisteredSigningBox(handle), ResultOfSigningBoxGetPublicKey.class);
    }

    /**
    * <strong>crypto.signing_box_sign</strong>
    * Returns signed user data.
    * @param signingBox Signing Box handle. 
    * @param unsigned Unsigned user data. Must be encoded with `base64`.
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfSigningBoxSign}
    */
    public static ResultOfSigningBoxSign signingBoxSign(@NonNull Context ctx, @NonNull Integer signingBox, @NonNull String unsigned)  throws JsonProcessingException {
        return  ctx.call("crypto.signing_box_sign", new ParamsOfSigningBoxSign(signingBox, unsigned), ResultOfSigningBoxSign.class);
    }

    /**
    * <strong>crypto.remove_signing_box</strong>
    * Removes signing box from SDK.
    * @param handle Handle of the signing box. 
    */
    public static void removeSigningBox(@NonNull Context ctx, @NonNull Integer handle)  throws JsonProcessingException {
         ctx.callVoid("crypto.remove_signing_box", new RegisteredSigningBox(handle));
    }

    /**
    * <strong>crypto.register_encryption_box</strong>
    * Register an application implemented encryption box.
    * @param appObject  
    * @return {@link tech.deplant.java4ever.binding.Crypto.RegisteredEncryptionBox}
    */
    public static RegisteredEncryptionBox registerEncryptionBox(@NonNull Context ctx,  AppEncryptionBox appObject)  throws JsonProcessingException {
        return  ctx.callAppObject("crypto.register_encryption_box", null, appObject, RegisteredEncryptionBox.class);
    }

    /**
    * <strong>crypto.remove_encryption_box</strong>
    * Removes encryption box from SDK
    * @param handle Handle of the encryption box. 
    */
    public static void removeEncryptionBox(@NonNull Context ctx, @NonNull Integer handle)  throws JsonProcessingException {
         ctx.callVoid("crypto.remove_encryption_box", new RegisteredEncryptionBox(handle));
    }

    /**
    * <strong>crypto.encryption_box_get_info</strong>
    * Queries info from the given encryption box
    * @param encryptionBox Encryption box handle 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfEncryptionBoxGetInfo}
    */
    public static ResultOfEncryptionBoxGetInfo encryptionBoxGetInfo(@NonNull Context ctx, @NonNull Integer encryptionBox)  throws JsonProcessingException {
        return  ctx.call("crypto.encryption_box_get_info", new ParamsOfEncryptionBoxGetInfo(encryptionBox), ResultOfEncryptionBoxGetInfo.class);
    }

    /**
    * <strong>crypto.encryption_box_encrypt</strong>
    * Encrypts data using given encryption box Note. Block cipher algorithms pad data to cipher block size so encrypted data can be longer then original data. Client should store the original data size after encryption and use it afterdecryption to retrieve the original data from decrypted data.
    * @param encryptionBox Encryption box handle 
    * @param data Data to be encrypted, encoded in Base64 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfEncryptionBoxEncrypt}
    */
    public static ResultOfEncryptionBoxEncrypt encryptionBoxEncrypt(@NonNull Context ctx, @NonNull Integer encryptionBox, @NonNull String data)  throws JsonProcessingException {
        return  ctx.call("crypto.encryption_box_encrypt", new ParamsOfEncryptionBoxEncrypt(encryptionBox, data), ResultOfEncryptionBoxEncrypt.class);
    }

    /**
    * <strong>crypto.encryption_box_decrypt</strong>
    * Decrypts data using given encryption box Note. Block cipher algorithms pad data to cipher block size so encrypted data can be longer then original data. Client should store the original data size after encryption and use it afterdecryption to retrieve the original data from decrypted data.
    * @param encryptionBox Encryption box handle 
    * @param data Data to be decrypted, encoded in Base64 
    * @return {@link tech.deplant.java4ever.binding.Crypto.ResultOfEncryptionBoxDecrypt}
    */
    public static ResultOfEncryptionBoxDecrypt encryptionBoxDecrypt(@NonNull Context ctx, @NonNull Integer encryptionBox, @NonNull String data)  throws JsonProcessingException {
        return  ctx.call("crypto.encryption_box_decrypt", new ParamsOfEncryptionBoxDecrypt(encryptionBox, data), ResultOfEncryptionBoxDecrypt.class);
    }

    /**
    * <strong>crypto.create_encryption_box</strong>
    * Creates encryption box with specified algorithm
    * @param algorithm Encryption algorithm specifier including cipher parameters (key, IV, etc) 
    * @return {@link tech.deplant.java4ever.binding.Crypto.RegisteredEncryptionBox}
    */
    public static RegisteredEncryptionBox createEncryptionBox(@NonNull Context ctx, @NonNull EncryptionAlgorithm algorithm)  throws JsonProcessingException {
        return  ctx.call("crypto.create_encryption_box", new ParamsOfCreateEncryptionBox(algorithm), RegisteredEncryptionBox.class);
    }

}
