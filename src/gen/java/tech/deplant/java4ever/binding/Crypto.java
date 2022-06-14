package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.Optional;
import lombok.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.*;
import com.google.gson.annotations.SerializedName;
import java.util.Arrays;

/**
 *  <h1>Module "crypto"</h1>
 *  Crypto functions.
 *  @version EVER-SDK 1.34.2
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
    public record AES(@NonNull Map<String,Object> mode, @NonNull String key, String iv) implements EncryptionAlgorithm {}


    /**
    * 
    * @param key 256-bit key. Must be encoded with `hex`.
    * @param nonce 96-bit nonce. Must be encoded with `hex`.
    */
    public record ChaCha20(@NonNull String key, @NonNull String nonce) implements EncryptionAlgorithm {}


    /**
    * 
    * @param theirPublic 256-bit key. Must be encoded with `hex`.
    * @param secretKey 256-bit key. Must be encoded with `hex`.
    * @param nonce 96-bit nonce. Must be encoded with `hex`.
    */
    public record NaclBox(@NonNull String theirPublic, @NonNull @JsonProperty("secret") String secretKey, @NonNull String nonce) implements EncryptionAlgorithm {}


    /**
    * 
    * @param key Secret key - unprefixed 0-padded to 64 symbols hex string
    * @param nonce Nonce in `hex`
    */
    public record NaclSecretBox(@NonNull String key, @NonNull String nonce) implements EncryptionAlgorithm {}
}
    public interface CryptoBoxSecret {


    /**
    * Creates Crypto Box from a random seed phrase. This option can be used if a developer doesn't want the seed phrase to leave the core library's memory, where it is stored encrypted. This type should be used upon the first wallet initialization, all further initializationsshould use `EncryptedSecret` type instead.<p>Get `encrypted_secret` with `get_crypto_box_info` function and store it on your side.
    * @param dictionary 
    * @param wordcount 
    */
    public record RandomSeedPhrase(@NonNull Number dictionary, @NonNull Number wordcount) implements CryptoBoxSecret {}


    /**
    * Restores crypto box instance from an existing seed phrase. This type should be used when Crypto Box is initialized from a seed phrase, entered by a user. This type should be used only upon the first wallet initialization, all furtherinitializations should use `EncryptedSecret` type instead.<p>Get `encrypted_secret` with `get_crypto_box_info` function and store it on your side.
    * @param phrase 
    * @param dictionary 
    * @param wordcount 
    */
    public record PredefinedSeedPhrase(@NonNull String phrase, @NonNull Number dictionary, @NonNull Number wordcount) implements CryptoBoxSecret {}


    /**
    * Use this type for wallet reinitializations, when you already have `encrypted_secret` on hands. To get `encrypted_secret`, use `get_crypto_box_info` function after you initialized your crypto box for the first time. It is an object, containing seed phrase or private key, encrypted with`secret_encryption_salt` and password from `password_provider`.<p>Note that if you want to change salt or password provider, then you need to reinitializethe wallet with `PredefinedSeedPhrase`, then get `EncryptedSecret` via `get_crypto_box_info`,store it somewhere, and only after that initialize the wallet with `EncryptedSecret` type.
    * @param encryptedSecret It is an object, containing encrypted seed phrase or private key (now we support only seed phrase).
    */
    public record EncryptedSecret(@NonNull String encryptedSecret) implements CryptoBoxSecret {}
}
    public interface BoxEncryptionAlgorithm {


    /**
    * 
    * @param nonce 96-bit nonce. Must be encoded with `hex`.
    */
    public record ChaCha20(@NonNull String nonce) implements BoxEncryptionAlgorithm {}


    /**
    * 
    * @param theirPublic 256-bit key. Must be encoded with `hex`.
    * @param nonce 96-bit nonce. Must be encoded with `hex`.
    */
    public record NaclBox(@NonNull String theirPublic, @NonNull String nonce) implements BoxEncryptionAlgorithm {}


    /**
    * 
    * @param nonce Nonce in `hex`
    */
    public record NaclSecretBox(@NonNull String nonce) implements BoxEncryptionAlgorithm {}
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

        public static final GetPublicKey GetPublicKey = new GetPublicKey();


    /**
    * Get signing box public key

    */
    public record GetPublicKey() implements ParamsOfAppSigningBox {}


    /**
    * Sign data
    * @param unsigned Data to sign encoded as base64
    */
    public record Sign(@NonNull String unsigned) implements ParamsOfAppSigningBox {}
}
    public interface ResultOfAppSigningBox {


    /**
    * Result of getting public key
    * @param publicKey Signing box public key
    */
    public record GetPublicKey(@NonNull String publicKey) implements ResultOfAppSigningBox {}


    /**
    * Result of signing data
    * @param signature Data signature encoded as hex
    */
    public record Sign(@NonNull String signature) implements ResultOfAppSigningBox {}
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

        public static final GetInfo GetInfo = new GetInfo();


    /**
    * Get encryption box info

    */
    public record GetInfo() implements ParamsOfAppEncryptionBox {}


    /**
    * Encrypt data
    * @param data Data, encoded in Base64
    */
    public record Encrypt(@NonNull String data) implements ParamsOfAppEncryptionBox {}


    /**
    * Decrypt data
    * @param data Data, encoded in Base64
    */
    public record Decrypt(@NonNull String data) implements ParamsOfAppEncryptionBox {}
}
    public interface ResultOfAppEncryptionBox {


    /**
    * Result of getting encryption box info
    * @param info 
    */
    public record GetInfo(@NonNull EncryptionBoxInfo info) implements ResultOfAppEncryptionBox {}


    /**
    * Result of encrypting data
    * @param data Encrypted data, encoded in Base64
    */
    public record Encrypt(@NonNull String data) implements ResultOfAppEncryptionBox {}


    /**
    * Result of decrypting data
    * @param data Decrypted data, encoded in Base64
    */
    public record Decrypt(@NonNull String data) implements ResultOfAppEncryptionBox {}
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
    * <h2>crypto.factorize</h2>
    * Integer factorization Performs prime factorization – decomposition of a composite numberinto a product of smaller prime integers (factors).See <a target="_blank" href="https://en.wikipedia.org/wiki/Integer_factorization">https://en.wikipedia.org/wiki/Integer_factorization</a>
    * @param composite Hexadecimal representation of u64 composite number. 
    */
    public static CompletableFuture<ResultOfFactorize> factorize(@NonNull Context context, @NonNull String composite)  throws JsonProcessingException {
        return context.future("crypto.factorize", new ParamsOfFactorize(composite), ResultOfFactorize.class);
    }

    /**
    * <h2>crypto.modular_power</h2>
    * Modular exponentiation Performs modular exponentiation for big integers (`base`^`exponent` mod `modulus`).See <a target="_blank" href="https://en.wikipedia.org/wiki/Modular_exponentiation">https://en.wikipedia.org/wiki/Modular_exponentiation</a>
    * @param base `base` argument of calculation. 
    * @param exponent `exponent` argument of calculation. 
    * @param modulus `modulus` argument of calculation. 
    */
    public static CompletableFuture<ResultOfModularPower> modularPower(@NonNull Context context, @NonNull String base, @NonNull String exponent, @NonNull String modulus)  throws JsonProcessingException {
        return context.future("crypto.modular_power", new ParamsOfModularPower(base, exponent, modulus), ResultOfModularPower.class);
    }

    /**
    * <h2>crypto.ton_crc16</h2>
    * Calculates CRC16 using TON algorithm.
    * @param data Input data for CRC calculation. Encoded with `base64`.
    */
    public static CompletableFuture<ResultOfTonCrc16> tonCrc16(@NonNull Context context, @NonNull String data)  throws JsonProcessingException {
        return context.future("crypto.ton_crc16", new ParamsOfTonCrc16(data), ResultOfTonCrc16.class);
    }

    /**
    * <h2>crypto.generate_random_bytes</h2>
    * Generates random byte array of the specified length and returns it in `base64` format
    * @param length Size of random byte array. 
    */
    public static CompletableFuture<ResultOfGenerateRandomBytes> generateRandomBytes(@NonNull Context context, @NonNull Number length)  throws JsonProcessingException {
        return context.future("crypto.generate_random_bytes", new ParamsOfGenerateRandomBytes(length), ResultOfGenerateRandomBytes.class);
    }

    /**
    * <h2>crypto.convert_public_key_to_ton_safe_format</h2>
    * Converts public key to ton safe_format
    * @param publicKey Public key - 64 symbols hex string 
    */
    public static CompletableFuture<ResultOfConvertPublicKeyToTonSafeFormat> convertPublicKeyToTonSafeFormat(@NonNull Context context, @NonNull String publicKey)  throws JsonProcessingException {
        return context.future("crypto.convert_public_key_to_ton_safe_format", new ParamsOfConvertPublicKeyToTonSafeFormat(publicKey), ResultOfConvertPublicKeyToTonSafeFormat.class);
    }

    /**
    * <h2>crypto.generate_random_sign_keys</h2>
    * Generates random ed25519 key pair.
    */
    public static CompletableFuture<KeyPair> generateRandomSignKeys(@NonNull Context context)  throws JsonProcessingException {
        return context.future("crypto.generate_random_sign_keys", null, KeyPair.class);
    }

    /**
    * <h2>crypto.sign</h2>
    * Signs a data using the provided keys.
    * @param unsigned Data that must be signed encoded in `base64`. 
    * @param keys Sign keys. 
    */
    public static CompletableFuture<ResultOfSign> sign(@NonNull Context context, @NonNull String unsigned, @NonNull KeyPair keys)  throws JsonProcessingException {
        return context.future("crypto.sign", new ParamsOfSign(unsigned, keys), ResultOfSign.class);
    }

    /**
    * <h2>crypto.verify_signature</h2>
    * Verifies signed data using the provided public key. Raises error if verification is failed.
    * @param signed Signed data that must be verified encoded in `base64`. 
    * @param publicKey Signer's public key - 64 symbols hex string 
    */
    public static CompletableFuture<ResultOfVerifySignature> verifySignature(@NonNull Context context, @NonNull String signed, @NonNull String publicKey)  throws JsonProcessingException {
        return context.future("crypto.verify_signature", new ParamsOfVerifySignature(signed, publicKey), ResultOfVerifySignature.class);
    }

    /**
    * <h2>crypto.sha256</h2>
    * Calculates SHA256 hash of the specified data.
    * @param data Input data for hash calculation. Encoded with `base64`.
    */
    public static CompletableFuture<ResultOfHash> sha256(@NonNull Context context, @NonNull String data)  throws JsonProcessingException {
        return context.future("crypto.sha256", new ParamsOfHash(data), ResultOfHash.class);
    }

    /**
    * <h2>crypto.sha512</h2>
    * Calculates SHA512 hash of the specified data.
    * @param data Input data for hash calculation. Encoded with `base64`.
    */
    public static CompletableFuture<ResultOfHash> sha512(@NonNull Context context, @NonNull String data)  throws JsonProcessingException {
        return context.future("crypto.sha512", new ParamsOfHash(data), ResultOfHash.class);
    }

    /**
    * <h2>crypto.scrypt</h2>
    * Perform `scrypt` encryption Derives key from `password` and `key` using `scrypt` algorithm.See <a target="_blank" href="https://en.wikipedia.org/wiki/Scrypt">https://en.wikipedia.org/wiki/Scrypt</a>.<p># Arguments- `log_n` - The log2 of the Scrypt parameter `N`- `r` - The Scrypt parameter `r`- `p` - The Scrypt parameter `p`# Conditions- `log_n` must be less than `64`- `r` must be greater than `0` and less than or equal to `4294967295`- `p` must be greater than `0` and less than `4294967295`# Recommended values sufficient for most use-cases- `log_n = 15` (`n = 32768`)- `r = 8`- `p = 1`
    * @param password The password bytes to be hashed. Must be encoded with `base64`. 
    * @param salt Salt bytes that modify the hash to protect against Rainbow table attacks. Must be encoded with `base64`. 
    * @param logN CPU/memory cost parameter 
    * @param r The block size parameter, which fine-tunes sequential memory read size and performance. 
    * @param p Parallelization parameter. 
    * @param dkLen Intended output length in octets of the derived key. 
    */
    public static CompletableFuture<ResultOfScrypt> scrypt(@NonNull Context context, @NonNull String password, @NonNull String salt, @NonNull Number logN, @NonNull Number r, @NonNull Number p, @NonNull Number dkLen)  throws JsonProcessingException {
        return context.future("crypto.scrypt", new ParamsOfScrypt(password, salt, logN, r, p, dkLen), ResultOfScrypt.class);
    }

    /**
    * <h2>crypto.nacl_sign_keypair_from_secret_key</h2>
    * Generates a key pair for signing from the secret key **NOTE:** In the result the secret key is actually the concatenationof secret and public keys (128 symbols hex string) by design of <a target="_blank" href="NaCL](http://nacl.cr.yp.to/sign.html).See also [the stackexchange question">NaCL](http://nacl.cr.yp.to/sign.html).See also [the stackexchange question</a>(https://crypto.stackexchange.com/questions/54353/).
    * @param secretKey Secret key - unprefixed 0-padded to 64 symbols hex string 
    */
    public static CompletableFuture<KeyPair> naclSignKeypairFromSecretKey(@NonNull Context context, @NonNull String secretKey)  throws JsonProcessingException {
        return context.future("crypto.nacl_sign_keypair_from_secret_key", new ParamsOfNaclSignKeyPairFromSecret(secretKey), KeyPair.class);
    }

    /**
    * <h2>crypto.nacl_sign</h2>
    * Signs data using the signer's secret key.
    * @param unsigned Data that must be signed encoded in `base64`. 
    * @param secretKey Signer's secret key - unprefixed 0-padded to 128 symbols hex string (concatenation of 64 symbols secret and 64 symbols public keys). See `nacl_sign_keypair_from_secret_key`. 
    */
    public static CompletableFuture<ResultOfNaclSign> naclSign(@NonNull Context context, @NonNull String unsigned, @NonNull String secretKey)  throws JsonProcessingException {
        return context.future("crypto.nacl_sign", new ParamsOfNaclSign(unsigned, secretKey), ResultOfNaclSign.class);
    }

    /**
    * <h2>crypto.nacl_sign_open</h2>
    * Verifies the signature and returns the unsigned message Verifies the signature in `signed` using the signer's public key `public`and returns the message `unsigned`.<p>If the signature fails verification, crypto_sign_open raises an exception.
    * @param signed Signed data that must be unsigned. Encoded with `base64`.
    * @param publicKey Signer's public key - unprefixed 0-padded to 64 symbols hex string 
    */
    public static CompletableFuture<ResultOfNaclSignOpen> naclSignOpen(@NonNull Context context, @NonNull String signed, @NonNull String publicKey)  throws JsonProcessingException {
        return context.future("crypto.nacl_sign_open", new ParamsOfNaclSignOpen(signed, publicKey), ResultOfNaclSignOpen.class);
    }

    /**
    * <h2>crypto.nacl_sign_detached</h2>
    * Signs the message using the secret key and returns a signature. Signs the message `unsigned` using the secret key `secret`and returns a signature `signature`.
    * @param unsigned Data that must be signed encoded in `base64`. 
    * @param secretKey Signer's secret key - unprefixed 0-padded to 128 symbols hex string (concatenation of 64 symbols secret and 64 symbols public keys). See `nacl_sign_keypair_from_secret_key`. 
    */
    public static CompletableFuture<ResultOfNaclSignDetached> naclSignDetached(@NonNull Context context, @NonNull String unsigned, @NonNull String secretKey)  throws JsonProcessingException {
        return context.future("crypto.nacl_sign_detached", new ParamsOfNaclSign(unsigned, secretKey), ResultOfNaclSignDetached.class);
    }

    /**
    * <h2>crypto.nacl_sign_detached_verify</h2>
    * Verifies the signature with public key and `unsigned` data.
    * @param unsigned Unsigned data that must be verified. Encoded with `base64`.
    * @param signature Signature that must be verified. Encoded with `hex`.
    * @param publicKey Signer's public key - unprefixed 0-padded to 64 symbols hex string. 
    */
    public static CompletableFuture<ResultOfNaclSignDetachedVerify> naclSignDetachedVerify(@NonNull Context context, @NonNull String unsigned, @NonNull String signature, @NonNull String publicKey)  throws JsonProcessingException {
        return context.future("crypto.nacl_sign_detached_verify", new ParamsOfNaclSignDetachedVerify(unsigned, signature, publicKey), ResultOfNaclSignDetachedVerify.class);
    }

    /**
    * <h2>crypto.nacl_box_keypair</h2>
    * Generates a random NaCl key pair
    */
    public static CompletableFuture<KeyPair> naclBoxKeypair(@NonNull Context context)  throws JsonProcessingException {
        return context.future("crypto.nacl_box_keypair", null, KeyPair.class);
    }

    /**
    * <h2>crypto.nacl_box_keypair_from_secret_key</h2>
    * Generates key pair from a secret key
    * @param secretKey Secret key - unprefixed 0-padded to 64 symbols hex string 
    */
    public static CompletableFuture<KeyPair> naclBoxKeypairFromSecretKey(@NonNull Context context, @NonNull String secretKey)  throws JsonProcessingException {
        return context.future("crypto.nacl_box_keypair_from_secret_key", new ParamsOfNaclBoxKeyPairFromSecret(secretKey), KeyPair.class);
    }

    /**
    * <h2>crypto.nacl_box</h2>
    * Public key authenticated encryption Encrypt and authenticate a message using the senders secret key, the receivers publickey, and a nonce.
    * @param decrypted Data that must be encrypted encoded in `base64`. 
    * @param nonce Nonce, encoded in `hex` 
    * @param theirPublic Receiver's public key - unprefixed 0-padded to 64 symbols hex string 
    * @param secretKey Sender's private key - unprefixed 0-padded to 64 symbols hex string 
    */
    public static CompletableFuture<ResultOfNaclBox> naclBox(@NonNull Context context, @NonNull String decrypted, @NonNull String nonce, @NonNull String theirPublic, @NonNull String secretKey)  throws JsonProcessingException {
        return context.future("crypto.nacl_box", new ParamsOfNaclBox(decrypted, nonce, theirPublic, secretKey), ResultOfNaclBox.class);
    }

    /**
    * <h2>crypto.nacl_box_open</h2>
    * Decrypt and verify the cipher text using the receivers secret key, the senders public key, and the nonce.
    * @param encrypted Data that must be decrypted. Encoded with `base64`.
    * @param nonce Nonce 
    * @param theirPublic Sender's public key - unprefixed 0-padded to 64 symbols hex string 
    * @param secretKey Receiver's private key - unprefixed 0-padded to 64 symbols hex string 
    */
    public static CompletableFuture<ResultOfNaclBoxOpen> naclBoxOpen(@NonNull Context context, @NonNull String encrypted, @NonNull String nonce, @NonNull String theirPublic, @NonNull String secretKey)  throws JsonProcessingException {
        return context.future("crypto.nacl_box_open", new ParamsOfNaclBoxOpen(encrypted, nonce, theirPublic, secretKey), ResultOfNaclBoxOpen.class);
    }

    /**
    * <h2>crypto.nacl_secret_box</h2>
    * Encrypt and authenticate message using nonce and secret key.
    * @param decrypted Data that must be encrypted. Encoded with `base64`.
    * @param nonce Nonce in `hex` 
    * @param key Secret key - unprefixed 0-padded to 64 symbols hex string 
    */
    public static CompletableFuture<ResultOfNaclBox> naclSecretBox(@NonNull Context context, @NonNull String decrypted, @NonNull String nonce, @NonNull String key)  throws JsonProcessingException {
        return context.future("crypto.nacl_secret_box", new ParamsOfNaclSecretBox(decrypted, nonce, key), ResultOfNaclBox.class);
    }

    /**
    * <h2>crypto.nacl_secret_box_open</h2>
    * Decrypts and verifies cipher text using `nonce` and secret `key`.
    * @param encrypted Data that must be decrypted. Encoded with `base64`.
    * @param nonce Nonce in `hex` 
    * @param key Secret key - unprefixed 0-padded to 64 symbols hex string 
    */
    public static CompletableFuture<ResultOfNaclBoxOpen> naclSecretBoxOpen(@NonNull Context context, @NonNull String encrypted, @NonNull String nonce, @NonNull String key)  throws JsonProcessingException {
        return context.future("crypto.nacl_secret_box_open", new ParamsOfNaclSecretBoxOpen(encrypted, nonce, key), ResultOfNaclBoxOpen.class);
    }

    /**
    * <h2>crypto.mnemonic_words</h2>
    * Prints the list of words from the specified dictionary
    * @param dictionary Dictionary identifier 
    */
    public static CompletableFuture<ResultOfMnemonicWords> mnemonicWords(@NonNull Context context,  Number dictionary)  throws JsonProcessingException {
        return context.future("crypto.mnemonic_words", new ParamsOfMnemonicWords(dictionary), ResultOfMnemonicWords.class);
    }

    /**
    * <h2>crypto.mnemonic_from_random</h2>
    * Generates a random mnemonic Generates a random mnemonic from the specified dictionary and word count
    * @param dictionary Dictionary identifier 
    * @param wordCount Mnemonic word count 
    */
    public static CompletableFuture<ResultOfMnemonicFromRandom> mnemonicFromRandom(@NonNull Context context,  Number dictionary,  Number wordCount)  throws JsonProcessingException {
        return context.future("crypto.mnemonic_from_random", new ParamsOfMnemonicFromRandom(dictionary, wordCount), ResultOfMnemonicFromRandom.class);
    }

    /**
    * <h2>crypto.mnemonic_from_entropy</h2>
    * Generates mnemonic from pre-generated entropy
    * @param entropy Entropy bytes. Hex encoded.
    * @param dictionary Dictionary identifier 
    * @param wordCount Mnemonic word count 
    */
    public static CompletableFuture<ResultOfMnemonicFromEntropy> mnemonicFromEntropy(@NonNull Context context, @NonNull String entropy,  Number dictionary,  Number wordCount)  throws JsonProcessingException {
        return context.future("crypto.mnemonic_from_entropy", new ParamsOfMnemonicFromEntropy(entropy, dictionary, wordCount), ResultOfMnemonicFromEntropy.class);
    }

    /**
    * <h2>crypto.mnemonic_verify</h2>
    * Validates a mnemonic phrase The phrase supplied will be checked for word length and validated according to the checksumspecified in BIP0039.
    * @param phrase Phrase 
    * @param dictionary Dictionary identifier 
    * @param wordCount Word count 
    */
    public static CompletableFuture<ResultOfMnemonicVerify> mnemonicVerify(@NonNull Context context, @NonNull String phrase,  Number dictionary,  Number wordCount)  throws JsonProcessingException {
        return context.future("crypto.mnemonic_verify", new ParamsOfMnemonicVerify(phrase, dictionary, wordCount), ResultOfMnemonicVerify.class);
    }

    /**
    * <h2>crypto.mnemonic_derive_sign_keys</h2>
    * Derives a key pair for signing from the seed phrase Validates the seed phrase, generates master key and then derivesthe key pair from the master key and the specified path
    * @param phrase Phrase 
    * @param path Derivation path, for instance "m/44'/396'/0'/0/0" 
    * @param dictionary Dictionary identifier 
    * @param wordCount Word count 
    */
    public static CompletableFuture<KeyPair> mnemonicDeriveSignKeys(@NonNull Context context, @NonNull String phrase,  String path,  Number dictionary,  Number wordCount)  throws JsonProcessingException {
        return context.future("crypto.mnemonic_derive_sign_keys", new ParamsOfMnemonicDeriveSignKeys(phrase, path, dictionary, wordCount), KeyPair.class);
    }

    /**
    * <h2>crypto.hdkey_xprv_from_mnemonic</h2>
    * Generates an extended master private key that will be the root for all the derived keys
    * @param phrase String with seed phrase 
    * @param dictionary Dictionary identifier 
    * @param wordCount Mnemonic word count 
    */
    public static CompletableFuture<ResultOfHDKeyXPrvFromMnemonic> hdkeyXprvFromMnemonic(@NonNull Context context, @NonNull String phrase,  Number dictionary,  Number wordCount)  throws JsonProcessingException {
        return context.future("crypto.hdkey_xprv_from_mnemonic", new ParamsOfHDKeyXPrvFromMnemonic(phrase, dictionary, wordCount), ResultOfHDKeyXPrvFromMnemonic.class);
    }

    /**
    * <h2>crypto.hdkey_derive_from_xprv</h2>
    * Returns extended private key derived from the specified extended private key and child index
    * @param xprv Serialized extended private key 
    * @param childIndex Child index (see BIP-0032) 
    * @param hardened Indicates the derivation of hardened/not-hardened key (see BIP-0032) 
    */
    public static CompletableFuture<ResultOfHDKeyDeriveFromXPrv> hdkeyDeriveFromXprv(@NonNull Context context, @NonNull String xprv, @NonNull Number childIndex, @NonNull Boolean hardened)  throws JsonProcessingException {
        return context.future("crypto.hdkey_derive_from_xprv", new ParamsOfHDKeyDeriveFromXPrv(xprv, childIndex, hardened), ResultOfHDKeyDeriveFromXPrv.class);
    }

    /**
    * <h2>crypto.hdkey_derive_from_xprv_path</h2>
    * Derives the extended private key from the specified key and path
    * @param xprv Serialized extended private key 
    * @param path Derivation path, for instance "m/44'/396'/0'/0/0" 
    */
    public static CompletableFuture<ResultOfHDKeyDeriveFromXPrvPath> hdkeyDeriveFromXprvPath(@NonNull Context context, @NonNull String xprv, @NonNull String path)  throws JsonProcessingException {
        return context.future("crypto.hdkey_derive_from_xprv_path", new ParamsOfHDKeyDeriveFromXPrvPath(xprv, path), ResultOfHDKeyDeriveFromXPrvPath.class);
    }

    /**
    * <h2>crypto.hdkey_secret_from_xprv</h2>
    * Extracts the private key from the serialized extended private key
    * @param xprv Serialized extended private key 
    */
    public static CompletableFuture<ResultOfHDKeySecretFromXPrv> hdkeySecretFromXprv(@NonNull Context context, @NonNull String xprv)  throws JsonProcessingException {
        return context.future("crypto.hdkey_secret_from_xprv", new ParamsOfHDKeySecretFromXPrv(xprv), ResultOfHDKeySecretFromXPrv.class);
    }

    /**
    * <h2>crypto.hdkey_public_from_xprv</h2>
    * Extracts the public key from the serialized extended private key
    * @param xprv Serialized extended private key 
    */
    public static CompletableFuture<ResultOfHDKeyPublicFromXPrv> hdkeyPublicFromXprv(@NonNull Context context, @NonNull String xprv)  throws JsonProcessingException {
        return context.future("crypto.hdkey_public_from_xprv", new ParamsOfHDKeyPublicFromXPrv(xprv), ResultOfHDKeyPublicFromXPrv.class);
    }

    /**
    * <h2>crypto.chacha20</h2>
    * Performs symmetric `chacha20` encryption.
    * @param data Source data to be encrypted or decrypted. Must be encoded with `base64`.
    * @param key 256-bit key. Must be encoded with `hex`.
    * @param nonce 96-bit nonce. Must be encoded with `hex`.
    */
    public static CompletableFuture<ResultOfChaCha20> chacha20(@NonNull Context context, @NonNull String data, @NonNull String key, @NonNull String nonce)  throws JsonProcessingException {
        return context.future("crypto.chacha20", new ParamsOfChaCha20(data, key, nonce), ResultOfChaCha20.class);
    }

    /**
    * <h2>crypto.create_crypto_box</h2>
    * Creates a Crypto Box instance. Crypto Box is a root crypto object, that encapsulates some secret (seed phrase usually)in encrypted form and acts as a factory for all crypto primitives used in SDK:keys for signing and encryption, derived from this secret.<p>Crypto Box encrypts original Seed Phrase with salt and password that is retrievedfrom `password_provider` callback, implemented on Application side.<p>When used, decrypted secret shows up in core library's memory for a very short periodof time and then is immediately overwritten with zeroes.
    * @param secretEncryptionSalt Salt used for secret encryption. For example, a mobile device can use device ID as salt. 
    * @param secretKey Cryptobox secret 
    */
    public static CompletableFuture<RegisteredCryptoBox> createCryptoBox(@NonNull Context context, @NonNull String secretEncryptionSalt, @NonNull CryptoBoxSecret secretKey)  throws JsonProcessingException {
        return context.future("crypto.create_crypto_box", new ParamsOfCreateCryptoBox(secretEncryptionSalt, secretKey), RegisteredCryptoBox.class);
    }

    /**
    * <h2>crypto.remove_crypto_box</h2>
    * Removes Crypto Box. Clears all secret data.
    * @param handle  
    */
    public static CompletableFuture<Void> removeCryptoBox(@NonNull Context context, @NonNull Integer handle)  throws JsonProcessingException {
        return context.future("crypto.remove_crypto_box", new RegisteredCryptoBox(handle), Void.class);
    }

    /**
    * <h2>crypto.get_crypto_box_info</h2>
    * Get Crypto Box Info. Used to get `encrypted_secret` that should be used for all the cryptobox initializations except the first one.
    * @param handle  
    */
    public static CompletableFuture<ResultOfGetCryptoBoxInfo> getCryptoBoxInfo(@NonNull Context context, @NonNull Integer handle)  throws JsonProcessingException {
        return context.future("crypto.get_crypto_box_info", new RegisteredCryptoBox(handle), ResultOfGetCryptoBoxInfo.class);
    }

    /**
    * <h2>crypto.get_crypto_box_seed_phrase</h2>
    * Get Crypto Box Seed Phrase. Attention! Store this data in your application for a very short period of time and overwrite it with zeroes ASAP.
    * @param handle  
    */
    public static CompletableFuture<ResultOfGetCryptoBoxSeedPhrase> getCryptoBoxSeedPhrase(@NonNull Context context, @NonNull Integer handle)  throws JsonProcessingException {
        return context.future("crypto.get_crypto_box_seed_phrase", new RegisteredCryptoBox(handle), ResultOfGetCryptoBoxSeedPhrase.class);
    }

    /**
    * <h2>crypto.get_signing_box_from_crypto_box</h2>
    * Get handle of Signing Box derived from Crypto Box.
    * @param handle Crypto Box Handle. 
    * @param hdpath HD key derivation path. By default, Everscale HD path is used.
    * @param secretLifetime Store derived secret for this lifetime (in ms). The timer starts after each signing box operation. Secrets will be deleted immediately after each signing box operation, if this value is not set. 
    */
    public static CompletableFuture<RegisteredSigningBox> getSigningBoxFromCryptoBox(@NonNull Context context, @NonNull Number handle,  String hdpath,  Number secretLifetime)  throws JsonProcessingException {
        return context.future("crypto.get_signing_box_from_crypto_box", new ParamsOfGetSigningBoxFromCryptoBox(handle, hdpath, secretLifetime), RegisteredSigningBox.class);
    }

    /**
    * <h2>crypto.get_encryption_box_from_crypto_box</h2>
    * Gets Encryption Box from Crypto Box. Derives encryption keypair from cryptobox secret and hdpath andstores it in cache for `secret_lifetime`or until explicitly cleared by `clear_crypto_box_secret_cache` method.If `secret_lifetime` is not specified - overwrites encryption secret with zeroes immediately afterencryption operation.
    * @param handle Crypto Box Handle. 
    * @param hdpath HD key derivation path. By default, Everscale HD path is used.
    * @param algorithm Encryption algorithm. 
    * @param secretLifetime Store derived secret for encryption algorithm for this lifetime (in ms). The timer starts after each encryption box operation. Secrets will be deleted (overwritten with zeroes) after each encryption operation, if this value is not set. 
    */
    public static CompletableFuture<RegisteredEncryptionBox> getEncryptionBoxFromCryptoBox(@NonNull Context context, @NonNull Number handle,  String hdpath, @NonNull BoxEncryptionAlgorithm algorithm,  Number secretLifetime)  throws JsonProcessingException {
        return context.future("crypto.get_encryption_box_from_crypto_box", new ParamsOfGetEncryptionBoxFromCryptoBox(handle, hdpath, algorithm, secretLifetime), RegisteredEncryptionBox.class);
    }

    /**
    * <h2>crypto.clear_crypto_box_secret_cache</h2>
    * Removes cached secrets (overwrites with zeroes) from all signing and encryption boxes, derived from crypto box.
    * @param handle  
    */
    public static CompletableFuture<Void> clearCryptoBoxSecretCache(@NonNull Context context, @NonNull Integer handle)  throws JsonProcessingException {
        return context.future("crypto.clear_crypto_box_secret_cache", new RegisteredCryptoBox(handle), Void.class);
    }

    /**
    * <h2>crypto.register_signing_box</h2>
    * Register an application implemented signing box.
    * @param appObject  
    */
    public static CompletableFuture<RegisteredSigningBox> registerSigningBox(@NonNull Context context,  AppSigningBox appObject)  throws JsonProcessingException {
        return context.futureCallback("crypto.register_signing_box", appObjectnull, RegisteredSigningBox.class);
    }

    /**
    * <h2>crypto.get_signing_box</h2>
    * Creates a default signing box implementation.
    * @param publicKey Public key - 64 symbols hex string 
    * @param secretKey Private key - u64 symbols hex string 
    */
    public static CompletableFuture<RegisteredSigningBox> getSigningBox(@NonNull Context context, @NonNull String publicKey, @NonNull String secretKey)  throws JsonProcessingException {
        return context.future("crypto.get_signing_box", new KeyPair(publicKey, secretKey), RegisteredSigningBox.class);
    }

    /**
    * <h2>crypto.signing_box_get_public_key</h2>
    * Returns public key of signing key pair.
    * @param handle Handle of the signing box. 
    */
    public static CompletableFuture<ResultOfSigningBoxGetPublicKey> signingBoxGetPublicKey(@NonNull Context context, @NonNull Integer handle)  throws JsonProcessingException {
        return context.future("crypto.signing_box_get_public_key", new RegisteredSigningBox(handle), ResultOfSigningBoxGetPublicKey.class);
    }

    /**
    * <h2>crypto.signing_box_sign</h2>
    * Returns signed user data.
    * @param signingBox Signing Box handle. 
    * @param unsigned Unsigned user data. Must be encoded with `base64`.
    */
    public static CompletableFuture<ResultOfSigningBoxSign> signingBoxSign(@NonNull Context context, @NonNull Integer signingBox, @NonNull String unsigned)  throws JsonProcessingException {
        return context.future("crypto.signing_box_sign", new ParamsOfSigningBoxSign(signingBox, unsigned), ResultOfSigningBoxSign.class);
    }

    /**
    * <h2>crypto.remove_signing_box</h2>
    * Removes signing box from SDK.
    * @param handle Handle of the signing box. 
    */
    public static CompletableFuture<Void> removeSigningBox(@NonNull Context context, @NonNull Integer handle)  throws JsonProcessingException {
        return context.future("crypto.remove_signing_box", new RegisteredSigningBox(handle), Void.class);
    }

    /**
    * <h2>crypto.register_encryption_box</h2>
    * Register an application implemented encryption box.
    * @param appObject  
    */
    public static CompletableFuture<RegisteredEncryptionBox> registerEncryptionBox(@NonNull Context context,  AppEncryptionBox appObject)  throws JsonProcessingException {
        return context.futureCallback("crypto.register_encryption_box", appObjectnull, RegisteredEncryptionBox.class);
    }

    /**
    * <h2>crypto.remove_encryption_box</h2>
    * Removes encryption box from SDK
    * @param handle Handle of the encryption box. 
    */
    public static CompletableFuture<Void> removeEncryptionBox(@NonNull Context context, @NonNull Integer handle)  throws JsonProcessingException {
        return context.future("crypto.remove_encryption_box", new RegisteredEncryptionBox(handle), Void.class);
    }

    /**
    * <h2>crypto.encryption_box_get_info</h2>
    * Queries info from the given encryption box
    * @param encryptionBox Encryption box handle 
    */
    public static CompletableFuture<ResultOfEncryptionBoxGetInfo> encryptionBoxGetInfo(@NonNull Context context, @NonNull Integer encryptionBox)  throws JsonProcessingException {
        return context.future("crypto.encryption_box_get_info", new ParamsOfEncryptionBoxGetInfo(encryptionBox), ResultOfEncryptionBoxGetInfo.class);
    }

    /**
    * <h2>crypto.encryption_box_encrypt</h2>
    * Encrypts data using given encryption box Note. Block cipher algorithms pad data to cipher block size so encrypted data can be longer then original data. Client should store the original data size after encryption and use it afterdecryption to retrieve the original data from decrypted data.
    * @param encryptionBox Encryption box handle 
    * @param data Data to be encrypted, encoded in Base64 
    */
    public static CompletableFuture<ResultOfEncryptionBoxEncrypt> encryptionBoxEncrypt(@NonNull Context context, @NonNull Integer encryptionBox, @NonNull String data)  throws JsonProcessingException {
        return context.future("crypto.encryption_box_encrypt", new ParamsOfEncryptionBoxEncrypt(encryptionBox, data), ResultOfEncryptionBoxEncrypt.class);
    }

    /**
    * <h2>crypto.encryption_box_decrypt</h2>
    * Decrypts data using given encryption box Note. Block cipher algorithms pad data to cipher block size so encrypted data can be longer then original data. Client should store the original data size after encryption and use it afterdecryption to retrieve the original data from decrypted data.
    * @param encryptionBox Encryption box handle 
    * @param data Data to be decrypted, encoded in Base64 
    */
    public static CompletableFuture<ResultOfEncryptionBoxDecrypt> encryptionBoxDecrypt(@NonNull Context context, @NonNull Integer encryptionBox, @NonNull String data)  throws JsonProcessingException {
        return context.future("crypto.encryption_box_decrypt", new ParamsOfEncryptionBoxDecrypt(encryptionBox, data), ResultOfEncryptionBoxDecrypt.class);
    }

    /**
    * <h2>crypto.create_encryption_box</h2>
    * Creates encryption box with specified algorithm
    * @param algorithm Encryption algorithm specifier including cipher parameters (key, IV, etc) 
    */
    public static CompletableFuture<RegisteredEncryptionBox> createEncryptionBox(@NonNull Context context, @NonNull EncryptionAlgorithm algorithm)  throws JsonProcessingException {
        return context.future("crypto.create_encryption_box", new ParamsOfCreateEncryptionBox(algorithm), RegisteredEncryptionBox.class);
    }

}
