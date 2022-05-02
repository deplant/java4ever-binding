package tech.deplant.java4ever.binding;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import tech.deplant.java4ever.binding.json.JsonData;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class Crypto {


    /**
     * Integer factorization
     *
     * @param composite Hexadecimal representation of u64 composite number.
     */
    public static CompletableFuture<ResultOfFactorize> factorize(@NonNull Context context, @NonNull String composite) {
        return context.future("crypto.factorize", new ParamsOfFactorize(composite), ResultOfFactorize.class);
    }

    /**
     * Modular exponentiation
     *
     * @param base     `base` argument of calculation.
     * @param exponent `exponent` argument of calculation.
     * @param modulus  `modulus` argument of calculation.
     */
    public static CompletableFuture<ResultOfModularPower> modularPower(@NonNull Context context, @NonNull String base, @NonNull String exponent, @NonNull String modulus) {
        return context.future("crypto.modular_power", new ParamsOfModularPower(base, exponent, modulus), ResultOfModularPower.class);
    }

    /**
     * Calculates CRC16 using TON algorithm.
     *
     * @param data Input data for CRC calculation. Encoded with `base64`.
     */
    public static CompletableFuture<ResultOfTonCrc16> tonCrc16(@NonNull Context context, @NonNull String data) {
        return context.future("crypto.ton_crc16", new ParamsOfTonCrc16(data), ResultOfTonCrc16.class);
    }

    /**
     * Generates random byte array of the specified length and returns it in `base64` format
     *
     * @param length Size of random byte array.
     */
    public static CompletableFuture<ResultOfGenerateRandomBytes> generateRandomBytes(@NonNull Context context, @NonNull Number length) {
        return context.future("crypto.generate_random_bytes", new ParamsOfGenerateRandomBytes(length), ResultOfGenerateRandomBytes.class);
    }

    /**
     * Converts public key to ton safe_format
     *
     * @param publicKey Public key - 64 symbols hex string
     */
    public static CompletableFuture<ResultOfConvertPublicKeyToTonSafeFormat> convertPublicKeyToTonSafeFormat(@NonNull Context context, @NonNull String publicKey) {
        return context.future("crypto.convert_public_key_to_ton_safe_format", new ParamsOfConvertPublicKeyToTonSafeFormat(publicKey), ResultOfConvertPublicKeyToTonSafeFormat.class);
    }

    /**
     * Generates random ed25519 key pair.
     */
    public static CompletableFuture<KeyPair> generateRandomSignKeys(@NonNull Context context) {
        return context.future("crypto.generate_random_sign_keys", null, KeyPair.class);
    }

    /**
     * Signs a data using the provided keys.
     *
     * @param unsigned Data that must be signed encoded in `base64`.
     * @param keys     Sign keys.
     */
    public static CompletableFuture<ResultOfSign> sign(@NonNull Context context, @NonNull String unsigned, @NonNull KeyPair keys) {
        return context.future("crypto.sign", new ParamsOfSign(unsigned, keys), ResultOfSign.class);
    }

    /**
     * Verifies signed data using the provided public key. Raises error if verification is failed.
     *
     * @param signed    Signed data that must be verified encoded in `base64`.
     * @param publicKey Signer's public key - 64 symbols hex string
     */
    public static CompletableFuture<ResultOfVerifySignature> verifySignature(@NonNull Context context, @NonNull String signed, @NonNull String publicKey) {
        return context.future("crypto.verify_signature", new ParamsOfVerifySignature(signed, publicKey), ResultOfVerifySignature.class);
    }

    /**
     * Calculates SHA256 hash of the specified data.
     *
     * @param data Input data for hash calculation. Encoded with `base64`.
     */
    public static CompletableFuture<ResultOfHash> sha256(@NonNull Context context, @NonNull String data) {
        return context.future("crypto.sha256", new ParamsOfHash(data), ResultOfHash.class);
    }

    /**
     * Calculates SHA512 hash of the specified data.
     *
     * @param data Input data for hash calculation. Encoded with `base64`.
     */
    public static CompletableFuture<ResultOfHash> sha512(@NonNull Context context, @NonNull String data) {
        return context.future("crypto.sha512", new ParamsOfHash(data), ResultOfHash.class);
    }

    /**
     * Perform `scrypt` encryption
     *
     * @param password The password bytes to be hashed. Must be encoded with `base64`.
     * @param salt     Salt bytes that modify the hash to protect against Rainbow table attacks. Must be encoded with `base64`.
     * @param logN     CPU/memory cost parameter
     * @param r        The block size parameter, which fine-tunes sequential memory read size and performance.
     * @param p        Parallelization parameter.
     * @param dkLen    Intended output length in octets of the derived key.
     */
    public static CompletableFuture<ResultOfScrypt> scrypt(@NonNull Context context, @NonNull String password, @NonNull String salt, @NonNull Number logN, @NonNull Number r, @NonNull Number p, @NonNull Number dkLen) {
        return context.future("crypto.scrypt", new ParamsOfScrypt(password, salt, logN, r, p, dkLen), ResultOfScrypt.class);
    }

    /**
     * Generates a key pair for signing from the secret key
     *
     * @param secretKey Secret key - unprefixed 0-padded to 64 symbols hex string
     */
    public static CompletableFuture<KeyPair> naclSignKeypairFromSecretKey(@NonNull Context context, @NonNull String secretKey) {
        return context.future("crypto.nacl_sign_keypair_from_secret_key", new ParamsOfNaclSignKeyPairFromSecret(secretKey), KeyPair.class);
    }

    /**
     * Signs data using the signer's secret key.
     *
     * @param unsigned  Data that must be signed encoded in `base64`.
     * @param secretKey Signer's secret key - unprefixed 0-padded to 128 symbols hex string (concatenation of 64 symbols secret and 64 symbols public keys). See `nacl_sign_keypair_from_secret_key`.
     */
    public static CompletableFuture<ResultOfNaclSign> naclSign(@NonNull Context context, @NonNull String unsigned, @NonNull String secretKey) {
        return context.future("crypto.nacl_sign", new ParamsOfNaclSign(unsigned, secretKey), ResultOfNaclSign.class);
    }

    /**
     * Verifies the signature and returns the unsigned message
     *
     * @param signed    Signed data that must be unsigned. Encoded with `base64`.
     * @param publicKey Signer's public key - unprefixed 0-padded to 64 symbols hex string
     */
    public static CompletableFuture<ResultOfNaclSignOpen> naclSignOpen(@NonNull Context context, @NonNull String signed, @NonNull String publicKey) {
        return context.future("crypto.nacl_sign_open", new ParamsOfNaclSignOpen(signed, publicKey), ResultOfNaclSignOpen.class);
    }

    /**
     * Signs the message using the secret key and returns a signature.
     *
     * @param unsigned  Data that must be signed encoded in `base64`.
     * @param secretKey Signer's secret key - unprefixed 0-padded to 128 symbols hex string (concatenation of 64 symbols secret and 64 symbols public keys). See `nacl_sign_keypair_from_secret_key`.
     */
    public static CompletableFuture<ResultOfNaclSignDetached> naclSignDetached(@NonNull Context context, @NonNull String unsigned, @NonNull String secretKey) {
        return context.future("crypto.nacl_sign_detached", new ParamsOfNaclSign(unsigned, secretKey), ResultOfNaclSignDetached.class);
    }

    /**
     * Verifies the signature with public key and `unsigned` data.
     *
     * @param unsigned  Unsigned data that must be verified. Encoded with `base64`.
     * @param signature Signature that must be verified. Encoded with `hex`.
     * @param publicKey Signer's public key - unprefixed 0-padded to 64 symbols hex string.
     */
    public static CompletableFuture<ResultOfNaclSignDetachedVerify> naclSignDetachedVerify(@NonNull Context context, @NonNull String unsigned, @NonNull String signature, @NonNull String publicKey) {
        return context.future("crypto.nacl_sign_detached_verify", new ParamsOfNaclSignDetachedVerify(unsigned, signature, publicKey), ResultOfNaclSignDetachedVerify.class);
    }

    /**
     * Generates a random NaCl key pair
     */
    public static CompletableFuture<KeyPair> naclBoxKeypair(@NonNull Context context) {
        return context.future("crypto.nacl_box_keypair", null, KeyPair.class);
    }

    /**
     * Generates key pair from a secret key
     *
     * @param secretKey Secret key - unprefixed 0-padded to 64 symbols hex string
     */
    public static CompletableFuture<KeyPair> naclBoxKeypairFromSecretKey(@NonNull Context context, @NonNull String secretKey) {
        return context.future("crypto.nacl_box_keypair_from_secret_key", new ParamsOfNaclBoxKeyPairFromSecret(secretKey), KeyPair.class);
    }

    /**
     * Public key authenticated encryption
     *
     * @param decrypted   Data that must be encrypted encoded in `base64`.
     * @param nonce       Nonce, encoded in `hex`
     * @param theirPublic Receiver's public key - unprefixed 0-padded to 64 symbols hex string
     * @param secretKey   Sender's private key - unprefixed 0-padded to 64 symbols hex string
     */
    public static CompletableFuture<ResultOfNaclBox> naclBox(@NonNull Context context, @NonNull String decrypted, @NonNull String nonce, @NonNull String theirPublic, @NonNull String secretKey) {
        return context.future("crypto.nacl_box", new ParamsOfNaclBox(decrypted, nonce, theirPublic, secretKey), ResultOfNaclBox.class);
    }

    /**
     * Decrypt and verify the cipher text using the receivers secret key, the senders public key, and the nonce.
     *
     * @param encrypted   Data that must be decrypted. Encoded with `base64`.
     * @param nonce
     * @param theirPublic Sender's public key - unprefixed 0-padded to 64 symbols hex string
     * @param secretKey   Receiver's private key - unprefixed 0-padded to 64 symbols hex string
     */
    public static CompletableFuture<ResultOfNaclBoxOpen> naclBoxOpen(@NonNull Context context, @NonNull String encrypted, @NonNull String nonce, @NonNull String theirPublic, @NonNull String secretKey) {
        return context.future("crypto.nacl_box_open", new ParamsOfNaclBoxOpen(encrypted, nonce, theirPublic, secretKey), ResultOfNaclBoxOpen.class);
    }

    /**
     * Encrypt and authenticate message using nonce and secret key.
     *
     * @param decrypted Data that must be encrypted. Encoded with `base64`.
     * @param nonce     Nonce in `hex`
     * @param key       Secret key - unprefixed 0-padded to 64 symbols hex string
     */
    public static CompletableFuture<ResultOfNaclBox> naclSecretBox(@NonNull Context context, @NonNull String decrypted, @NonNull String nonce, @NonNull String key) {
        return context.future("crypto.nacl_secret_box", new ParamsOfNaclSecretBox(decrypted, nonce, key), ResultOfNaclBox.class);
    }

    /**
     * Decrypts and verifies cipher text using `nonce` and secret `key`.
     *
     * @param encrypted Data that must be decrypted. Encoded with `base64`.
     * @param nonce     Nonce in `hex`
     * @param key       Public key - unprefixed 0-padded to 64 symbols hex string
     */
    public static CompletableFuture<ResultOfNaclBoxOpen> naclSecretBoxOpen(@NonNull Context context, @NonNull String encrypted, @NonNull String nonce, @NonNull String key) {
        return context.future("crypto.nacl_secret_box_open", new ParamsOfNaclSecretBoxOpen(encrypted, nonce, key), ResultOfNaclBoxOpen.class);
    }

    /**
     * Prints the list of words from the specified dictionary
     *
     * @param dictionary Dictionary identifier
     */
    public static CompletableFuture<ResultOfMnemonicWords> mnemonicWords(@NonNull Context context, Number dictionary) {
        return context.future("crypto.mnemonic_words", new ParamsOfMnemonicWords(dictionary), ResultOfMnemonicWords.class);
    }

    /**
     * Generates a random mnemonic
     *
     * @param dictionary Dictionary identifier
     * @param wordCount  Mnemonic word count
     */
    public static CompletableFuture<ResultOfMnemonicFromRandom> mnemonicFromRandom(@NonNull Context context, Number dictionary, Number wordCount) {
        return context.future("crypto.mnemonic_from_random", new ParamsOfMnemonicFromRandom(dictionary, wordCount), ResultOfMnemonicFromRandom.class);
    }

    /**
     * Generates mnemonic from pre-generated entropy
     *
     * @param entropy    Entropy bytes. Hex encoded.
     * @param dictionary Dictionary identifier
     * @param wordCount  Mnemonic word count
     */
    public static CompletableFuture<ResultOfMnemonicFromEntropy> mnemonicFromEntropy(@NonNull Context context, @NonNull String entropy, Number dictionary, Number wordCount) {
        return context.future("crypto.mnemonic_from_entropy", new ParamsOfMnemonicFromEntropy(entropy, dictionary, wordCount), ResultOfMnemonicFromEntropy.class);
    }

    /**
     * Validates a mnemonic phrase
     *
     * @param phrase     Phrase
     * @param dictionary Dictionary identifier
     * @param wordCount  Word count
     */
    public static CompletableFuture<ResultOfMnemonicVerify> mnemonicVerify(@NonNull Context context, @NonNull String phrase, Number dictionary, Number wordCount) {
        return context.future("crypto.mnemonic_verify", new ParamsOfMnemonicVerify(phrase, dictionary, wordCount), ResultOfMnemonicVerify.class);
    }

    /**
     * Derives a key pair for signing from the seed phrase
     *
     * @param phrase     Phrase
     * @param path       Derivation path, for instance "m/44'/396'/0'/0/0"
     * @param dictionary Dictionary identifier
     * @param wordCount  Word count
     */
    public static CompletableFuture<KeyPair> mnemonicDeriveSignKeys(@NonNull Context context, @NonNull String phrase, String path, Number dictionary, Number wordCount) {
        return context.future("crypto.mnemonic_derive_sign_keys", new ParamsOfMnemonicDeriveSignKeys(phrase, path, dictionary, wordCount), KeyPair.class);
    }

    /**
     * Generates an extended master private key that will be the root for all the derived keys
     *
     * @param phrase     String with seed phrase
     * @param dictionary Dictionary identifier
     * @param wordCount  Mnemonic word count
     */
    public static CompletableFuture<ResultOfHDKeyXPrvFromMnemonic> hdkeyXprvFromMnemonic(@NonNull Context context, @NonNull String phrase, Number dictionary, Number wordCount) {
        return context.future("crypto.hdkey_xprv_from_mnemonic", new ParamsOfHDKeyXPrvFromMnemonic(phrase, dictionary, wordCount), ResultOfHDKeyXPrvFromMnemonic.class);
    }

    /**
     * Returns extended private key derived from the specified extended private key and child index
     *
     * @param xprv       Serialized extended private key
     * @param childIndex Child index (see BIP-0032)
     * @param hardened   Indicates the derivation of hardened/not-hardened key (see BIP-0032)
     */
    public static CompletableFuture<ResultOfHDKeyDeriveFromXPrv> hdkeyDeriveFromXprv(@NonNull Context context, @NonNull String xprv, @NonNull Number childIndex, @NonNull Boolean hardened) {
        return context.future("crypto.hdkey_derive_from_xprv", new ParamsOfHDKeyDeriveFromXPrv(xprv, childIndex, hardened), ResultOfHDKeyDeriveFromXPrv.class);
    }

    /**
     * Derives the extended private key from the specified key and path
     *
     * @param xprv Serialized extended private key
     * @param path Derivation path, for instance "m/44'/396'/0'/0/0"
     */
    public static CompletableFuture<ResultOfHDKeyDeriveFromXPrvPath> hdkeyDeriveFromXprvPath(@NonNull Context context, @NonNull String xprv, @NonNull String path) {
        return context.future("crypto.hdkey_derive_from_xprv_path", new ParamsOfHDKeyDeriveFromXPrvPath(xprv, path), ResultOfHDKeyDeriveFromXPrvPath.class);
    }

    /**
     * Extracts the private key from the serialized extended private key
     *
     * @param xprv Serialized extended private key
     */
    public static CompletableFuture<ResultOfHDKeySecretFromXPrv> hdkeySecretFromXprv(@NonNull Context context, @NonNull String xprv) {
        return context.future("crypto.hdkey_secret_from_xprv", new ParamsOfHDKeySecretFromXPrv(xprv), ResultOfHDKeySecretFromXPrv.class);
    }

    /**
     * Extracts the public key from the serialized extended private key
     *
     * @param xprv Serialized extended private key
     */
    public static CompletableFuture<ResultOfHDKeyPublicFromXPrv> hdkeyPublicFromXprv(@NonNull Context context, @NonNull String xprv) {
        return context.future("crypto.hdkey_public_from_xprv", new ParamsOfHDKeyPublicFromXPrv(xprv), ResultOfHDKeyPublicFromXPrv.class);
    }

    /**
     * Performs symmetric `chacha20` encryption.
     *
     * @param data  Source data to be encrypted or decrypted. Must be encoded with `base64`.
     * @param key   256-bit key. Must be encoded with `hex`.
     * @param nonce 96-bit nonce. Must be encoded with `hex`.
     */
    public static CompletableFuture<ResultOfChaCha20> chacha20(@NonNull Context context, @NonNull String data, @NonNull String key, @NonNull String nonce) {
        return context.future("crypto.chacha20", new ParamsOfChaCha20(data, key, nonce), ResultOfChaCha20.class);
    }

    /**
     * Creates a default signing box implementation.
     *
     * @param publicKey Public key - 64 symbols hex string
     * @param secretKey Private key - u64 symbols hex string
     */
    public static CompletableFuture<RegisteredSigningBox> getSigningBox(@NonNull Context context, @NonNull String publicKey, @NonNull String secretKey) {
        return context.future("crypto.get_signing_box", new KeyPair(publicKey, secretKey), RegisteredSigningBox.class);
    }

    /**
     * Returns public key of signing key pair.
     *
     * @param handle Handle of the signing box.
     */
    public static CompletableFuture<ResultOfSigningBoxGetPublicKey> signingBoxGetPublicKey(@NonNull Context context, @NonNull Integer handle) {
        return context.future("crypto.signing_box_get_public_key", new RegisteredSigningBox(handle), ResultOfSigningBoxGetPublicKey.class);
    }

    /**
     * Returns signed user data.
     *
     * @param signingBox Signing Box handle.
     * @param unsigned   Unsigned user data. Must be encoded with `base64`.
     */
    public static CompletableFuture<ResultOfSigningBoxSign> signingBoxSign(@NonNull Context context, @NonNull Integer signingBox, @NonNull String unsigned) {
        return context.future("crypto.signing_box_sign", new ParamsOfSigningBoxSign(signingBox, unsigned), ResultOfSigningBoxSign.class);
    }

    /**
     * Removes signing box from SDK.
     *
     * @param handle Handle of the signing box.
     */
    public static CompletableFuture<Void> removeSigningBox(@NonNull Context context, @NonNull Integer handle) {
        return context.future("crypto.remove_signing_box", new RegisteredSigningBox(handle), Void.class);
    }

    /**
     * Removes encryption box from SDK
     *
     * @param handle Handle of the encryption box
     */
    public static CompletableFuture<Void> removeEncryptionBox(@NonNull Context context, @NonNull Integer handle) {
        return context.future("crypto.remove_encryption_box", new RegisteredEncryptionBox(handle), Void.class);
    }

    /**
     * Queries info from the given encryption box
     *
     * @param encryptionBox Encryption box handle
     */
    public static CompletableFuture<ResultOfEncryptionBoxGetInfo> encryptionBoxGetInfo(@NonNull Context context, @NonNull Integer encryptionBox) {
        return context.future("crypto.encryption_box_get_info", new ParamsOfEncryptionBoxGetInfo(encryptionBox), ResultOfEncryptionBoxGetInfo.class);
    }

    /**
     * Encrypts data using given encryption box Note.
     *
     * @param encryptionBox Encryption box handle
     * @param data          Data to be encrypted, encoded in Base64
     */
    public static CompletableFuture<ResultOfEncryptionBoxEncrypt> encryptionBoxEncrypt(@NonNull Context context, @NonNull Integer encryptionBox, @NonNull String data) {
        return context.future("crypto.encryption_box_encrypt", new ParamsOfEncryptionBoxEncrypt(encryptionBox, data), ResultOfEncryptionBoxEncrypt.class);
    }

    /**
     * Decrypts data using given encryption box Note.
     *
     * @param encryptionBox Encryption box handle
     * @param data          Data to be decrypted, encoded in Base64
     */
    public static CompletableFuture<ResultOfEncryptionBoxDecrypt> encryptionBoxDecrypt(@NonNull Context context, @NonNull Integer encryptionBox, @NonNull String data) {
        return context.future("crypto.encryption_box_decrypt", new ParamsOfEncryptionBoxDecrypt(encryptionBox, data), ResultOfEncryptionBoxDecrypt.class);
    }

    /**
     * Creates encryption box with specified algorithm
     *
     * @param algorithm Encryption algorithm specifier including cipher parameters (key, IV, etc)
     */
    public static CompletableFuture<RegisteredEncryptionBox> createEncryptionBox(@NonNull Context context, @NonNull EncryptionAlgorithm algorithm) {
        return context.future("crypto.create_encryption_box", new ParamsOfCreateEncryptionBox(algorithm), RegisteredEncryptionBox.class);
    }

    @Value
    public static class EncryptionBoxInfo extends JsonData {
        @SerializedName("hdpath")
        @Getter(AccessLevel.NONE)
        String hdpath;
        @SerializedName("algorithm")
        @Getter(AccessLevel.NONE)
        String algorithm;
        @SerializedName("options")
        @Getter(AccessLevel.NONE)
        Map<String, Object> options;
        @SerializedName("public")
        @Getter(AccessLevel.NONE)
        Map<String, Object> publicKey;

        /**
         * Derivation path, for instance "m/44'/396'/0'/0/0"
         */
        public Optional<String> hdpath() {
            return Optional.ofNullable(this.hdpath);
        }

        /**
         * Cryptographic algorithm, used by this encryption box
         */
        public Optional<String> algorithm() {
            return Optional.ofNullable(this.algorithm);
        }

        /**
         * Options, depends on algorithm and specific encryption box implementation
         */
        public Optional<Map<String, Object>> options() {
            return Optional.ofNullable(this.options);
        }

        /**
         * Public information, depends on algorithm
         */
        public Optional<Map<String, Object>> publicKey() {
            return Optional.ofNullable(this.publicKey);
        }

    }

    public static abstract class EncryptionAlgorithm {


        @Value
        public static class AES extends EncryptionAlgorithm {
            @SerializedName("mode")
            @NonNull Map<String, Object> mode;
            @SerializedName("key")
            @NonNull String key;
            @SerializedName("iv")
            @Getter(AccessLevel.NONE)
            String iv;

            public Optional<String> iv() {
                return Optional.ofNullable(this.iv);
            }

        }
    }

    @Value
    public static class ParamsOfFactorize extends JsonData {

        /**
         * Hexadecimal representation of u64 composite number.
         */
        @SerializedName("composite")
        @NonNull String composite;

    }

    @Value
    public static class ResultOfFactorize extends JsonData {

        /**
         * Two factors of composite or empty if composite can't be factorized.
         */
        @SerializedName("factors")
        @NonNull String[] factors;

    }

    @Value
    public static class ParamsOfModularPower extends JsonData {

        /**
         * `base` argument of calculation.
         */
        @SerializedName("base")
        @NonNull String base;

        /**
         * `exponent` argument of calculation.
         */
        @SerializedName("exponent")
        @NonNull String exponent;

        /**
         * `modulus` argument of calculation.
         */
        @SerializedName("modulus")
        @NonNull String modulus;

    }

    @Value
    public static class ResultOfModularPower extends JsonData {

        /**
         * Result of modular exponentiation
         */
        @SerializedName("modular_power")
        @NonNull String modularPower;

    }

    @Value
    public static class ParamsOfTonCrc16 extends JsonData {

        /**
         * Input data for CRC calculation.
         */
        @SerializedName("data")
        @NonNull String data;

    }

    @Value
    public static class ResultOfTonCrc16 extends JsonData {

        /**
         * Calculated CRC for input data.
         */
        @SerializedName("crc")
        @NonNull Number crc;

    }

    @Value
    public static class ParamsOfGenerateRandomBytes extends JsonData {

        /**
         * Size of random byte array.
         */
        @SerializedName("length")
        @NonNull Number length;

    }

    @Value
    public static class ResultOfGenerateRandomBytes extends JsonData {

        /**
         * Generated bytes encoded in `base64`.
         */
        @SerializedName("bytes")
        @NonNull String bytes;

    }

    @Value
    public static class ParamsOfConvertPublicKeyToTonSafeFormat extends JsonData {

        /**
         * Public key - 64 symbols hex string
         */
        @SerializedName("public_key")
        @NonNull String publicKey;

    }

    @Value
    public static class ResultOfConvertPublicKeyToTonSafeFormat extends JsonData {

        /**
         * Public key represented in TON safe format.
         */
        @SerializedName("ton_public_key")
        @NonNull String tonPublicKey;

    }

    @Value
    public static class KeyPair extends JsonData {

        /**
         * Public key - 64 symbols hex string
         */
        @SerializedName("public")
        @NonNull String publicKey;

        /**
         * Private key - u64 symbols hex string
         */
        @SerializedName("secret")
        @NonNull String secretKey;

    }

    @Value
    public static class ParamsOfSign extends JsonData {

        /**
         * Data that must be signed encoded in `base64`.
         */
        @SerializedName("unsigned")
        @NonNull String unsigned;

        /**
         * Sign keys.
         */
        @SerializedName("keys")
        @NonNull KeyPair keys;

    }

    @Value
    public static class ResultOfSign extends JsonData {

        /**
         * Signed data combined with signature encoded in `base64`.
         */
        @SerializedName("signed")
        @NonNull String signed;

        /**
         * Signature encoded in `hex`.
         */
        @SerializedName("signature")
        @NonNull String signature;

    }

    @Value
    public static class ParamsOfVerifySignature extends JsonData {

        /**
         * Signed data that must be verified encoded in `base64`.
         */
        @SerializedName("signed")
        @NonNull String signed;

        /**
         * Signer's public key - 64 symbols hex string
         */
        @SerializedName("public")
        @NonNull String publicKey;

    }

    @Value
    public static class ResultOfVerifySignature extends JsonData {

        /**
         * Unsigned data encoded in `base64`.
         */
        @SerializedName("unsigned")
        @NonNull String unsigned;

    }

    @Value
    public static class ParamsOfHash extends JsonData {

        /**
         * Input data for hash calculation.
         */
        @SerializedName("data")
        @NonNull String data;

    }

    @Value
    public static class ResultOfHash extends JsonData {

        /**
         * Hash of input `data`.
         */
        @SerializedName("hash")
        @NonNull String hash;

    }

    @Value
    public static class ParamsOfScrypt extends JsonData {

        /**
         * The password bytes to be hashed. Must be encoded with `base64`.
         */
        @SerializedName("password")
        @NonNull String password;

        /**
         * Salt bytes that modify the hash to protect against Rainbow table attacks. Must be encoded with `base64`.
         */
        @SerializedName("salt")
        @NonNull String salt;

        /**
         * CPU/memory cost parameter
         */
        @SerializedName("log_n")
        @NonNull Number logN;

        /**
         * The block size parameter, which fine-tunes sequential memory read size and performance.
         */
        @SerializedName("r")
        @NonNull Number r;

        /**
         * Parallelization parameter.
         */
        @SerializedName("p")
        @NonNull Number p;

        /**
         * Intended output length in octets of the derived key.
         */
        @SerializedName("dk_len")
        @NonNull Number dkLen;

    }

    @Value
    public static class ResultOfScrypt extends JsonData {

        /**
         * Derived key.
         */
        @SerializedName("key")
        @NonNull String key;

    }

    @Value
    public static class ParamsOfNaclSignKeyPairFromSecret extends JsonData {

        /**
         * Secret key - unprefixed 0-padded to 64 symbols hex string
         */
        @SerializedName("secret")
        @NonNull String secretKey;

    }

    @Value
    public static class ParamsOfNaclSign extends JsonData {

        /**
         * Data that must be signed encoded in `base64`.
         */
        @SerializedName("unsigned")
        @NonNull String unsigned;

        /**
         * Signer's secret key - unprefixed 0-padded to 128 symbols hex string (concatenation of 64 symbols secret and 64 symbols public keys). See `nacl_sign_keypair_from_secret_key`.
         */
        @SerializedName("secret")
        @NonNull String secretKey;

    }

    @Value
    public static class ResultOfNaclSign extends JsonData {

        /**
         * Signed data, encoded in `base64`.
         */
        @SerializedName("signed")
        @NonNull String signed;

    }

    @Value
    public static class ParamsOfNaclSignOpen extends JsonData {

        /**
         * Signed data that must be unsigned.
         */
        @SerializedName("signed")
        @NonNull String signed;

        /**
         * Signer's public key - unprefixed 0-padded to 64 symbols hex string
         */
        @SerializedName("public")
        @NonNull String publicKey;

    }

    @Value
    public static class ResultOfNaclSignOpen extends JsonData {

        /**
         * Unsigned data, encoded in `base64`.
         */
        @SerializedName("unsigned")
        @NonNull String unsigned;

    }

    @Value
    public static class ResultOfNaclSignDetached extends JsonData {

        /**
         * Signature encoded in `hex`.
         */
        @SerializedName("signature")
        @NonNull String signature;

    }

    @Value
    public static class ParamsOfNaclSignDetachedVerify extends JsonData {

        /**
         * Unsigned data that must be verified.
         */
        @SerializedName("unsigned")
        @NonNull String unsigned;

        /**
         * Signature that must be verified.
         */
        @SerializedName("signature")
        @NonNull String signature;

        /**
         * Signer's public key - unprefixed 0-padded to 64 symbols hex string.
         */
        @SerializedName("public")
        @NonNull String publicKey;

    }

    @Value
    public static class ResultOfNaclSignDetachedVerify extends JsonData {

        /**
         * `true` if verification succeeded or `false` if it failed
         */
        @SerializedName("succeeded")
        @NonNull Boolean succeeded;

    }

    @Value
    public static class ParamsOfNaclBoxKeyPairFromSecret extends JsonData {

        /**
         * Secret key - unprefixed 0-padded to 64 symbols hex string
         */
        @SerializedName("secret")
        @NonNull String secretKey;

    }

    @Value
    public static class ParamsOfNaclBox extends JsonData {

        /**
         * Data that must be encrypted encoded in `base64`.
         */
        @SerializedName("decrypted")
        @NonNull String decrypted;

        /**
         * Nonce, encoded in `hex`
         */
        @SerializedName("nonce")
        @NonNull String nonce;

        /**
         * Receiver's public key - unprefixed 0-padded to 64 symbols hex string
         */
        @SerializedName("their_public")
        @NonNull String theirPublic;

        /**
         * Sender's private key - unprefixed 0-padded to 64 symbols hex string
         */
        @SerializedName("secret")
        @NonNull String secretKey;

    }

    @Value
    public static class ResultOfNaclBox extends JsonData {

        /**
         * Encrypted data encoded in `base64`.
         */
        @SerializedName("encrypted")
        @NonNull String encrypted;

    }

    @Value
    public static class ParamsOfNaclBoxOpen extends JsonData {

        /**
         * Data that must be decrypted.
         */
        @SerializedName("encrypted")
        @NonNull String encrypted;
        @SerializedName("nonce")
        @NonNull String nonce;

        /**
         * Sender's public key - unprefixed 0-padded to 64 symbols hex string
         */
        @SerializedName("their_public")
        @NonNull String theirPublic;

        /**
         * Receiver's private key - unprefixed 0-padded to 64 symbols hex string
         */
        @SerializedName("secret")
        @NonNull String secretKey;

    }

    @Value
    public static class ResultOfNaclBoxOpen extends JsonData {

        /**
         * Decrypted data encoded in `base64`.
         */
        @SerializedName("decrypted")
        @NonNull String decrypted;

    }

    @Value
    public static class ParamsOfNaclSecretBox extends JsonData {

        /**
         * Data that must be encrypted.
         */
        @SerializedName("decrypted")
        @NonNull String decrypted;

        /**
         * Nonce in `hex`
         */
        @SerializedName("nonce")
        @NonNull String nonce;

        /**
         * Secret key - unprefixed 0-padded to 64 symbols hex string
         */
        @SerializedName("key")
        @NonNull String key;

    }

    @Value
    public static class ParamsOfNaclSecretBoxOpen extends JsonData {

        /**
         * Data that must be decrypted.
         */
        @SerializedName("encrypted")
        @NonNull String encrypted;

        /**
         * Nonce in `hex`
         */
        @SerializedName("nonce")
        @NonNull String nonce;

        /**
         * Public key - unprefixed 0-padded to 64 symbols hex string
         */
        @SerializedName("key")
        @NonNull String key;

    }

    @Value
    public static class ParamsOfMnemonicWords extends JsonData {
        @SerializedName("dictionary")
        @Getter(AccessLevel.NONE)
        Number dictionary;

        /**
         * Dictionary identifier
         */
        public Optional<Number> dictionary() {
            return Optional.ofNullable(this.dictionary);
        }

    }

    @Value
    public static class ResultOfMnemonicWords extends JsonData {

        /**
         * The list of mnemonic words
         */
        @SerializedName("words")
        @NonNull String words;

    }

    @Value
    public static class ParamsOfMnemonicFromRandom extends JsonData {
        @SerializedName("dictionary")
        @Getter(AccessLevel.NONE)
        Number dictionary;
        @SerializedName("word_count")
        @Getter(AccessLevel.NONE)
        Number wordCount;

        /**
         * Dictionary identifier
         */
        public Optional<Number> dictionary() {
            return Optional.ofNullable(this.dictionary);
        }

        /**
         * Mnemonic word count
         */
        public Optional<Number> wordCount() {
            return Optional.ofNullable(this.wordCount);
        }

    }

    @Value
    public static class ResultOfMnemonicFromRandom extends JsonData {

        /**
         * String of mnemonic words
         */
        @SerializedName("phrase")
        @NonNull String phrase;

    }

    @Value
    public static class ParamsOfMnemonicFromEntropy extends JsonData {

        /**
         * Entropy bytes.
         */
        @SerializedName("entropy")
        @NonNull String entropy;
        @SerializedName("dictionary")
        @Getter(AccessLevel.NONE)
        Number dictionary;
        @SerializedName("word_count")
        @Getter(AccessLevel.NONE)
        Number wordCount;

        /**
         * Dictionary identifier
         */
        public Optional<Number> dictionary() {
            return Optional.ofNullable(this.dictionary);
        }

        /**
         * Mnemonic word count
         */
        public Optional<Number> wordCount() {
            return Optional.ofNullable(this.wordCount);
        }

    }

    @Value
    public static class ResultOfMnemonicFromEntropy extends JsonData {

        /**
         * Phrase
         */
        @SerializedName("phrase")
        @NonNull String phrase;

    }

    @Value
    public static class ParamsOfMnemonicVerify extends JsonData {

        /**
         * Phrase
         */
        @SerializedName("phrase")
        @NonNull String phrase;
        @SerializedName("dictionary")
        @Getter(AccessLevel.NONE)
        Number dictionary;
        @SerializedName("word_count")
        @Getter(AccessLevel.NONE)
        Number wordCount;

        /**
         * Dictionary identifier
         */
        public Optional<Number> dictionary() {
            return Optional.ofNullable(this.dictionary);
        }

        /**
         * Word count
         */
        public Optional<Number> wordCount() {
            return Optional.ofNullable(this.wordCount);
        }

    }

    @Value
    public static class ResultOfMnemonicVerify extends JsonData {

        /**
         * Flag indicating if the mnemonic is valid or not
         */
        @SerializedName("valid")
        @NonNull Boolean valid;

    }

    @Value
    public static class ParamsOfMnemonicDeriveSignKeys extends JsonData {

        /**
         * Phrase
         */
        @SerializedName("phrase")
        @NonNull String phrase;
        @SerializedName("path")
        @Getter(AccessLevel.NONE)
        String path;
        @SerializedName("dictionary")
        @Getter(AccessLevel.NONE)
        Number dictionary;
        @SerializedName("word_count")
        @Getter(AccessLevel.NONE)
        Number wordCount;

        /**
         * Derivation path, for instance "m/44'/396'/0'/0/0"
         */
        public Optional<String> path() {
            return Optional.ofNullable(this.path);
        }

        /**
         * Dictionary identifier
         */
        public Optional<Number> dictionary() {
            return Optional.ofNullable(this.dictionary);
        }

        /**
         * Word count
         */
        public Optional<Number> wordCount() {
            return Optional.ofNullable(this.wordCount);
        }

    }

    @Value
    public static class ParamsOfHDKeyXPrvFromMnemonic extends JsonData {

        /**
         * String with seed phrase
         */
        @SerializedName("phrase")
        @NonNull String phrase;
        @SerializedName("dictionary")
        @Getter(AccessLevel.NONE)
        Number dictionary;
        @SerializedName("word_count")
        @Getter(AccessLevel.NONE)
        Number wordCount;

        /**
         * Dictionary identifier
         */
        public Optional<Number> dictionary() {
            return Optional.ofNullable(this.dictionary);
        }

        /**
         * Mnemonic word count
         */
        public Optional<Number> wordCount() {
            return Optional.ofNullable(this.wordCount);
        }

    }

    @Value
    public static class ResultOfHDKeyXPrvFromMnemonic extends JsonData {

        /**
         * Serialized extended master private key
         */
        @SerializedName("xprv")
        @NonNull String xprv;

    }

    @Value
    public static class ParamsOfHDKeyDeriveFromXPrv extends JsonData {

        /**
         * Serialized extended private key
         */
        @SerializedName("xprv")
        @NonNull String xprv;

        /**
         * Child index (see BIP-0032)
         */
        @SerializedName("child_index")
        @NonNull Number childIndex;

        /**
         * Indicates the derivation of hardened/not-hardened key (see BIP-0032)
         */
        @SerializedName("hardened")
        @NonNull Boolean hardened;

    }

    @Value
    public static class ResultOfHDKeyDeriveFromXPrv extends JsonData {

        /**
         * Serialized extended private key
         */
        @SerializedName("xprv")
        @NonNull String xprv;

    }

    @Value
    public static class ParamsOfHDKeyDeriveFromXPrvPath extends JsonData {

        /**
         * Serialized extended private key
         */
        @SerializedName("xprv")
        @NonNull String xprv;

        /**
         * Derivation path, for instance "m/44'/396'/0'/0/0"
         */
        @SerializedName("path")
        @NonNull String path;

    }

    @Value
    public static class ResultOfHDKeyDeriveFromXPrvPath extends JsonData {

        /**
         * Derived serialized extended private key
         */
        @SerializedName("xprv")
        @NonNull String xprv;

    }

    @Value
    public static class ParamsOfHDKeySecretFromXPrv extends JsonData {

        /**
         * Serialized extended private key
         */
        @SerializedName("xprv")
        @NonNull String xprv;

    }

    @Value
    public static class ResultOfHDKeySecretFromXPrv extends JsonData {

        /**
         * Private key - 64 symbols hex string
         */
        @SerializedName("secret")
        @NonNull String secretKey;

    }

    @Value
    public static class ParamsOfHDKeyPublicFromXPrv extends JsonData {

        /**
         * Serialized extended private key
         */
        @SerializedName("xprv")
        @NonNull String xprv;

    }

    @Value
    public static class ResultOfHDKeyPublicFromXPrv extends JsonData {

        /**
         * Public key - 64 symbols hex string
         */
        @SerializedName("public")
        @NonNull String publicKey;

    }

    @Value
    public static class ParamsOfChaCha20 extends JsonData {

        /**
         * Source data to be encrypted or decrypted.
         */
        @SerializedName("data")
        @NonNull String data;

        /**
         * 256-bit key.
         */
        @SerializedName("key")
        @NonNull String key;

        /**
         * 96-bit nonce.
         */
        @SerializedName("nonce")
        @NonNull String nonce;

    }

    @Value
    public static class ResultOfChaCha20 extends JsonData {

        /**
         * Encrypted/decrypted data.
         */
        @SerializedName("data")
        @NonNull String data;

    }

    @Value
    public static class RegisteredSigningBox extends JsonData {

        /**
         * Handle of the signing box.
         */
        @SerializedName("handle")
        @NonNull Integer handle;

    }

    public static abstract class ParamsOfAppSigningBox {

        public static final GetPublicKey GetPublicKey = new GetPublicKey();


        @Value
        public static class GetPublicKey extends ParamsOfAppSigningBox {

        }


        @Value
        public static class Sign extends ParamsOfAppSigningBox {

            /**
             * Data to sign encoded as base64
             */
            @SerializedName("unsigned")
            @NonNull String unsigned;

        }
    }

    public static abstract class ResultOfAppSigningBox {


        @Value
        public static class GetPublicKey extends ResultOfAppSigningBox {

            /**
             * Signing box public key
             */
            @SerializedName("public_key")
            @NonNull String publicKey;

        }


        @Value
        public static class Sign extends ResultOfAppSigningBox {

            /**
             * Data signature encoded as hex
             */
            @SerializedName("signature")
            @NonNull String signature;

        }
    }

    @Value
    public static class ResultOfSigningBoxGetPublicKey extends JsonData {

        /**
         * Public key of signing box.
         */
        @SerializedName("pubkey")
        @NonNull String pubkey;

    }

    @Value
    public static class ParamsOfSigningBoxSign extends JsonData {

        /**
         * Signing Box handle.
         */
        @SerializedName("signing_box")
        @NonNull Integer signingBox;

        /**
         * Unsigned user data.
         */
        @SerializedName("unsigned")
        @NonNull String unsigned;

    }

    @Value
    public static class ResultOfSigningBoxSign extends JsonData {

        /**
         * Data signature.
         */
        @SerializedName("signature")
        @NonNull String signature;

    }

    @Value
    public static class RegisteredEncryptionBox extends JsonData {

        /**
         * Handle of the encryption box
         */
        @SerializedName("handle")
        @NonNull Integer handle;

    }

//   /**
//    * Register an application implemented signing box.
//    * @param appObject
//    */
//    public static CompletableFuture<RegisteredSigningBox> registerSigningBox(@NonNull Context context,  AppSigningBox appObject) {
//        return Module.futureCallback("crypto.register_signing_box", context, appObjectnull, RegisteredSigningBox.class);
//    }

    public static abstract class ParamsOfAppEncryptionBox {

        public static final GetInfo GetInfo = new GetInfo();


        @Value
        public static class GetInfo extends ParamsOfAppEncryptionBox {

        }


        @Value
        public static class Encrypt extends ParamsOfAppEncryptionBox {

            /**
             * Data, encoded in Base64
             */
            @SerializedName("data")
            @NonNull String data;

        }


        @Value
        public static class Decrypt extends ParamsOfAppEncryptionBox {

            /**
             * Data, encoded in Base64
             */
            @SerializedName("data")
            @NonNull String data;

        }
    }

    public static abstract class ResultOfAppEncryptionBox {


        @Value
        public static class GetInfo extends ResultOfAppEncryptionBox {
            @SerializedName("info")
            @NonNull EncryptionBoxInfo info;

        }


        @Value
        public static class Encrypt extends ResultOfAppEncryptionBox {

            /**
             * Encrypted data, encoded in Base64
             */
            @SerializedName("data")
            @NonNull String data;

        }


        @Value
        public static class Decrypt extends ResultOfAppEncryptionBox {

            /**
             * Decrypted data, encoded in Base64
             */
            @SerializedName("data")
            @NonNull String data;

        }
    }

    @Value
    public static class ParamsOfEncryptionBoxGetInfo extends JsonData {

        /**
         * Encryption box handle
         */
        @SerializedName("encryption_box")
        @NonNull Integer encryptionBox;

    }

    @Value
    public static class ResultOfEncryptionBoxGetInfo extends JsonData {

        /**
         * Encryption box information
         */
        @SerializedName("info")
        @NonNull EncryptionBoxInfo info;

    }

//   /**
//    * Register an application implemented encryption box.
//    * @param appObject
//    */
//    public static CompletableFuture<RegisteredEncryptionBox> registerEncryptionBox(@NonNull Context context,  AppEncryptionBox appObject) {
//        return Module.futureCallback("crypto.register_encryption_box", context, appObjectnull, RegisteredEncryptionBox.class);
//    }

    @Value
    public static class ParamsOfEncryptionBoxEncrypt extends JsonData {

        /**
         * Encryption box handle
         */
        @SerializedName("encryption_box")
        @NonNull Integer encryptionBox;

        /**
         * Data to be encrypted, encoded in Base64
         */
        @SerializedName("data")
        @NonNull String data;

    }

    @Value
    public static class ResultOfEncryptionBoxEncrypt extends JsonData {

        /**
         * Encrypted data, encoded in Base64.
         */
        @SerializedName("data")
        @NonNull String data;

    }

    @Value
    public static class ParamsOfEncryptionBoxDecrypt extends JsonData {

        /**
         * Encryption box handle
         */
        @SerializedName("encryption_box")
        @NonNull Integer encryptionBox;

        /**
         * Data to be decrypted, encoded in Base64
         */
        @SerializedName("data")
        @NonNull String data;

    }

    @Value
    public static class ResultOfEncryptionBoxDecrypt extends JsonData {

        /**
         * Decrypted data, encoded in Base64.
         */
        @SerializedName("data")
        @NonNull String data;

    }

    @Value
    public static class ParamsOfCreateEncryptionBox extends JsonData {

        /**
         * Encryption algorithm specifier including cipher parameters (key, IV, etc)
         */
        @SerializedName("algorithm")
        @NonNull EncryptionAlgorithm algorithm;

    }

}
