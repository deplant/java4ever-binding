package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.util.Map;

/**
 * <strong>Crypto</strong>
 * Contains methods of "crypto" module of EVER-SDK API
 *
 * Crypto functions. 
 * @version 1.43.2
 */
public final class Crypto {
  /**
   * Performs prime factorization – decomposition of a composite number
   * into a product of smaller prime integers (factors).
   * See [https://en.wikipedia.org/wiki/Integer_factorization] Integer factorization
   *
   * @param composite  Hexadecimal representation of u64 composite number.
   */
  public static Crypto.ResultOfFactorize factorize(Context ctx, String composite) throws
      EverSdkException {
    return ctx.call("crypto.factorize", new Crypto.ParamsOfFactorize(composite), Crypto.ResultOfFactorize.class);
  }

  /**
   * Performs modular exponentiation for big integers (`base`^`exponent` mod `modulus`).
   * See [https://en.wikipedia.org/wiki/Modular_exponentiation] Modular exponentiation
   *
   * @param base  `base` argument of calculation.
   * @param exponent  `exponent` argument of calculation.
   * @param modulus  `modulus` argument of calculation.
   */
  public static Crypto.ResultOfModularPower modularPower(Context ctx, String base, String exponent,
      String modulus) throws EverSdkException {
    return ctx.call("crypto.modular_power", new Crypto.ParamsOfModularPower(base, exponent, modulus), Crypto.ResultOfModularPower.class);
  }

  /**
   *  Calculates CRC16 using TON algorithm.
   *
   * @param data Encoded with `base64`. Input data for CRC calculation.
   */
  public static Crypto.ResultOfTonCrc16 tonCrc16(Context ctx, String data) throws EverSdkException {
    return ctx.call("crypto.ton_crc16", new Crypto.ParamsOfTonCrc16(data), Crypto.ResultOfTonCrc16.class);
  }

  /**
   *  Generates random byte array of the specified length and returns it in `base64` format
   *
   * @param length  Size of random byte array.
   */
  public static Crypto.ResultOfGenerateRandomBytes generateRandomBytes(Context ctx, Integer length)
      throws EverSdkException {
    return ctx.call("crypto.generate_random_bytes", new Crypto.ParamsOfGenerateRandomBytes(length), Crypto.ResultOfGenerateRandomBytes.class);
  }

  /**
   *  Converts public key to ton safe_format
   *
   * @param publicKey  Public key - 64 symbols hex string
   */
  public static Crypto.ResultOfConvertPublicKeyToTonSafeFormat convertPublicKeyToTonSafeFormat(
      Context ctx, String publicKey) throws EverSdkException {
    return ctx.call("crypto.convert_public_key_to_ton_safe_format", new Crypto.ParamsOfConvertPublicKeyToTonSafeFormat(publicKey), Crypto.ResultOfConvertPublicKeyToTonSafeFormat.class);
  }

  /**
   *  Generates random ed25519 key pair.
   */
  public static Crypto.KeyPair generateRandomSignKeys(Context ctx) throws EverSdkException {
    return ctx.call("crypto.generate_random_sign_keys", null, Crypto.KeyPair.class);
  }

  /**
   *  Signs a data using the provided keys.
   *
   * @param unsigned  Data that must be signed encoded in `base64`.
   * @param keys  Sign keys.
   */
  public static Crypto.ResultOfSign sign(Context ctx, String unsigned, Crypto.KeyPair keys) throws
      EverSdkException {
    return ctx.call("crypto.sign", new Crypto.ParamsOfSign(unsigned, keys), Crypto.ResultOfSign.class);
  }

  /**
   *  Verifies signed data using the provided public key. Raises error if verification is failed.
   *
   * @param signed  Signed data that must be verified encoded in `base64`.
   * @param publicKey  Signer's public key - 64 symbols hex string
   */
  public static Crypto.ResultOfVerifySignature verifySignature(Context ctx, String signed,
      @JsonProperty("public") String publicKey) throws EverSdkException {
    return ctx.call("crypto.verify_signature", new Crypto.ParamsOfVerifySignature(signed, publicKey), Crypto.ResultOfVerifySignature.class);
  }

  /**
   *  Calculates SHA256 hash of the specified data.
   *
   * @param data Encoded with `base64`. Input data for hash calculation.
   */
  public static Crypto.ResultOfHash sha256(Context ctx, String data) throws EverSdkException {
    return ctx.call("crypto.sha256", new Crypto.ParamsOfHash(data), Crypto.ResultOfHash.class);
  }

  /**
   *  Calculates SHA512 hash of the specified data.
   *
   * @param data Encoded with `base64`. Input data for hash calculation.
   */
  public static Crypto.ResultOfHash sha512(Context ctx, String data) throws EverSdkException {
    return ctx.call("crypto.sha512", new Crypto.ParamsOfHash(data), Crypto.ResultOfHash.class);
  }

  /**
   * Derives key from `password` and `key` using `scrypt` algorithm.
   * See [https://en.wikipedia.org/wiki/Scrypt].
   *
   * # Arguments
   * - `log_n` - The log2 of the Scrypt parameter `N`
   * - `r` - The Scrypt parameter `r`
   * - `p` - The Scrypt parameter `p`
   * # Conditions
   * - `log_n` must be less than `64`
   * - `r` must be greater than `0` and less than or equal to `4294967295`
   * - `p` must be greater than `0` and less than `4294967295`
   * # Recommended values sufficient for most use-cases
   * - `log_n = 15` (`n = 32768`)
   * - `r = 8`
   * - `p = 1` Perform `scrypt` encryption
   *
   * @param password  The password bytes to be hashed. Must be encoded with `base64`.
   * @param salt  Salt bytes that modify the hash to protect against Rainbow table attacks. Must be encoded with `base64`.
   * @param logN  CPU/memory cost parameter
   * @param r  The block size parameter, which fine-tunes sequential memory read size and performance.
   * @param p  Parallelization parameter.
   * @param dkLen  Intended output length in octets of the derived key.
   */
  public static Crypto.ResultOfScrypt scrypt(Context ctx, String password, String salt,
      Integer logN, Integer r, Integer p, Integer dkLen) throws EverSdkException {
    return ctx.call("crypto.scrypt", new Crypto.ParamsOfScrypt(password, salt, logN, r, p, dkLen), Crypto.ResultOfScrypt.class);
  }

  /**
   * **NOTE:** In the result the secret key is actually the concatenation
   * of secret and public keys (128 symbols hex string) by design of [NaCL](http://nacl.cr.yp.to/sign.html).
   * See also [the stackexchange question](https://crypto.stackexchange.com/questions/54353/). Generates a key pair for signing from the secret key
   *
   * @param secretKey  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public static Crypto.KeyPair naclSignKeypairFromSecretKey(Context ctx,
      @JsonProperty("secret") String secretKey) throws EverSdkException {
    return ctx.call("crypto.nacl_sign_keypair_from_secret_key", new Crypto.ParamsOfNaclSignKeyPairFromSecret(secretKey), Crypto.KeyPair.class);
  }

  /**
   *  Signs data using the signer's secret key.
   *
   * @param unsigned  Data that must be signed encoded in `base64`.
   * @param secretKey  Signer's secret key - unprefixed 0-padded to 128 symbols hex string (concatenation of 64 symbols secret and 64 symbols public keys). See `nacl_sign_keypair_from_secret_key`.
   */
  public static Crypto.ResultOfNaclSign naclSign(Context ctx, String unsigned,
      @JsonProperty("secret") String secretKey) throws EverSdkException {
    return ctx.call("crypto.nacl_sign", new Crypto.ParamsOfNaclSign(unsigned, secretKey), Crypto.ResultOfNaclSign.class);
  }

  /**
   * Verifies the signature in `signed` using the signer's public key `public`
   * and returns the message `unsigned`.
   *
   * If the signature fails verification, crypto_sign_open raises an exception. Verifies the signature and returns the unsigned message
   *
   * @param signed Encoded with `base64`. Signed data that must be unsigned.
   * @param publicKey  Signer's public key - unprefixed 0-padded to 64 symbols hex string
   */
  public static Crypto.ResultOfNaclSignOpen naclSignOpen(Context ctx, String signed,
      @JsonProperty("public") String publicKey) throws EverSdkException {
    return ctx.call("crypto.nacl_sign_open", new Crypto.ParamsOfNaclSignOpen(signed, publicKey), Crypto.ResultOfNaclSignOpen.class);
  }

  /**
   * Signs the message `unsigned` using the secret key `secret`
   * and returns a signature `signature`. Signs the message using the secret key and returns a signature.
   *
   * @param unsigned  Data that must be signed encoded in `base64`.
   * @param secretKey  Signer's secret key - unprefixed 0-padded to 128 symbols hex string (concatenation of 64 symbols secret and 64 symbols public keys). See `nacl_sign_keypair_from_secret_key`.
   */
  public static Crypto.ResultOfNaclSignDetached naclSignDetached(Context ctx, String unsigned,
      @JsonProperty("secret") String secretKey) throws EverSdkException {
    return ctx.call("crypto.nacl_sign_detached", new Crypto.ParamsOfNaclSign(unsigned, secretKey), Crypto.ResultOfNaclSignDetached.class);
  }

  /**
   *  Verifies the signature with public key and `unsigned` data.
   *
   * @param unsigned Encoded with `base64`. Unsigned data that must be verified.
   * @param signature Encoded with `hex`. Signature that must be verified.
   * @param publicKey  Signer's public key - unprefixed 0-padded to 64 symbols hex string.
   */
  public static Crypto.ResultOfNaclSignDetachedVerify naclSignDetachedVerify(Context ctx,
      String unsigned, String signature, @JsonProperty("public") String publicKey) throws
      EverSdkException {
    return ctx.call("crypto.nacl_sign_detached_verify", new Crypto.ParamsOfNaclSignDetachedVerify(unsigned, signature, publicKey), Crypto.ResultOfNaclSignDetachedVerify.class);
  }

  /**
   *  Generates a random NaCl key pair
   */
  public static Crypto.KeyPair naclBoxKeypair(Context ctx) throws EverSdkException {
    return ctx.call("crypto.nacl_box_keypair", null, Crypto.KeyPair.class);
  }

  /**
   *  Generates key pair from a secret key
   *
   * @param secretKey  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public static Crypto.KeyPair naclBoxKeypairFromSecretKey(Context ctx,
      @JsonProperty("secret") String secretKey) throws EverSdkException {
    return ctx.call("crypto.nacl_box_keypair_from_secret_key", new Crypto.ParamsOfNaclBoxKeyPairFromSecret(secretKey), Crypto.KeyPair.class);
  }

  /**
   * Encrypt and authenticate a message using the senders secret key, the receivers public
   * key, and a nonce. Public key authenticated encryption
   *
   * @param decrypted  Data that must be encrypted encoded in `base64`.
   * @param nonce  Nonce, encoded in `hex`
   * @param theirPublic  Receiver's public key - unprefixed 0-padded to 64 symbols hex string
   * @param secretKey  Sender's private key - unprefixed 0-padded to 64 symbols hex string
   */
  public static Crypto.ResultOfNaclBox naclBox(Context ctx, String decrypted, String nonce,
      String theirPublic, @JsonProperty("secret") String secretKey) throws EverSdkException {
    return ctx.call("crypto.nacl_box", new Crypto.ParamsOfNaclBox(decrypted, nonce, theirPublic, secretKey), Crypto.ResultOfNaclBox.class);
  }

  /**
   *  Decrypt and verify the cipher text using the receivers secret key, the senders public key, and the nonce.
   *
   * @param encrypted Encoded with `base64`. Data that must be decrypted.
   * @param nonce  Nonce
   * @param theirPublic  Sender's public key - unprefixed 0-padded to 64 symbols hex string
   * @param secretKey  Receiver's private key - unprefixed 0-padded to 64 symbols hex string
   */
  public static Crypto.ResultOfNaclBoxOpen naclBoxOpen(Context ctx, String encrypted, String nonce,
      String theirPublic, @JsonProperty("secret") String secretKey) throws EverSdkException {
    return ctx.call("crypto.nacl_box_open", new Crypto.ParamsOfNaclBoxOpen(encrypted, nonce, theirPublic, secretKey), Crypto.ResultOfNaclBoxOpen.class);
  }

  /**
   *  Encrypt and authenticate message using nonce and secret key.
   *
   * @param decrypted Encoded with `base64`. Data that must be encrypted.
   * @param nonce  Nonce in `hex`
   * @param key  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public static Crypto.ResultOfNaclBox naclSecretBox(Context ctx, String decrypted, String nonce,
      String key) throws EverSdkException {
    return ctx.call("crypto.nacl_secret_box", new Crypto.ParamsOfNaclSecretBox(decrypted, nonce, key), Crypto.ResultOfNaclBox.class);
  }

  /**
   *  Decrypts and verifies cipher text using `nonce` and secret `key`.
   *
   * @param encrypted Encoded with `base64`. Data that must be decrypted.
   * @param nonce  Nonce in `hex`
   * @param key  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public static Crypto.ResultOfNaclBoxOpen naclSecretBoxOpen(Context ctx, String encrypted,
      String nonce, String key) throws EverSdkException {
    return ctx.call("crypto.nacl_secret_box_open", new Crypto.ParamsOfNaclSecretBoxOpen(encrypted, nonce, key), Crypto.ResultOfNaclBoxOpen.class);
  }

  /**
   *  Prints the list of words from the specified dictionary
   *
   * @param dictionary  Dictionary identifier
   */
  public static Crypto.ResultOfMnemonicWords mnemonicWords(Context ctx,
      Crypto.MnemonicDictionary dictionary) throws EverSdkException {
    return ctx.call("crypto.mnemonic_words", new Crypto.ParamsOfMnemonicWords(dictionary), Crypto.ResultOfMnemonicWords.class);
  }

  /**
   * Generates a random mnemonic from the specified dictionary and word count Generates a random mnemonic
   *
   * @param dictionary  Dictionary identifier
   * @param wordCount  Mnemonic word count
   */
  public static Crypto.ResultOfMnemonicFromRandom mnemonicFromRandom(Context ctx,
      Crypto.MnemonicDictionary dictionary, Integer wordCount) throws EverSdkException {
    return ctx.call("crypto.mnemonic_from_random", new Crypto.ParamsOfMnemonicFromRandom(dictionary, wordCount), Crypto.ResultOfMnemonicFromRandom.class);
  }

  /**
   *  Generates mnemonic from pre-generated entropy
   *
   * @param entropy Hex encoded. Entropy bytes.
   * @param dictionary  Dictionary identifier
   * @param wordCount  Mnemonic word count
   */
  public static Crypto.ResultOfMnemonicFromEntropy mnemonicFromEntropy(Context ctx, String entropy,
      Crypto.MnemonicDictionary dictionary, Integer wordCount) throws EverSdkException {
    return ctx.call("crypto.mnemonic_from_entropy", new Crypto.ParamsOfMnemonicFromEntropy(entropy, dictionary, wordCount), Crypto.ResultOfMnemonicFromEntropy.class);
  }

  /**
   * The phrase supplied will be checked for word length and validated according to the checksum
   * specified in BIP0039. Validates a mnemonic phrase
   *
   * @param phrase  Phrase
   * @param dictionary  Dictionary identifier
   * @param wordCount  Word count
   */
  public static Crypto.ResultOfMnemonicVerify mnemonicVerify(Context ctx, String phrase,
      Crypto.MnemonicDictionary dictionary, Integer wordCount) throws EverSdkException {
    return ctx.call("crypto.mnemonic_verify", new Crypto.ParamsOfMnemonicVerify(phrase, dictionary, wordCount), Crypto.ResultOfMnemonicVerify.class);
  }

  /**
   * Validates the seed phrase, generates master key and then derives
   * the key pair from the master key and the specified path Derives a key pair for signing from the seed phrase
   *
   * @param phrase  Phrase
   * @param path  Derivation path, for instance "m/44'/396'/0'/0/0"
   * @param dictionary  Dictionary identifier
   * @param wordCount  Word count
   */
  public static Crypto.KeyPair mnemonicDeriveSignKeys(Context ctx, String phrase, String path,
      Crypto.MnemonicDictionary dictionary, Integer wordCount) throws EverSdkException {
    return ctx.call("crypto.mnemonic_derive_sign_keys", new Crypto.ParamsOfMnemonicDeriveSignKeys(phrase, path, dictionary, wordCount), Crypto.KeyPair.class);
  }

  /**
   *  Generates an extended master private key that will be the root for all the derived keys
   *
   * @param phrase  String with seed phrase
   * @param dictionary  Dictionary identifier
   * @param wordCount  Mnemonic word count
   */
  public static Crypto.ResultOfHDKeyXPrvFromMnemonic hdkeyXprvFromMnemonic(Context ctx,
      String phrase, Crypto.MnemonicDictionary dictionary, Integer wordCount) throws
      EverSdkException {
    return ctx.call("crypto.hdkey_xprv_from_mnemonic", new Crypto.ParamsOfHDKeyXPrvFromMnemonic(phrase, dictionary, wordCount), Crypto.ResultOfHDKeyXPrvFromMnemonic.class);
  }

  /**
   *  Returns extended private key derived from the specified extended private key and child index
   *
   * @param xprv  Serialized extended private key
   * @param childIndex  Child index (see BIP-0032)
   * @param hardened  Indicates the derivation of hardened/not-hardened key (see BIP-0032)
   */
  public static Crypto.ResultOfHDKeyDeriveFromXPrv hdkeyDeriveFromXprv(Context ctx, String xprv,
      Integer childIndex, Boolean hardened) throws EverSdkException {
    return ctx.call("crypto.hdkey_derive_from_xprv", new Crypto.ParamsOfHDKeyDeriveFromXPrv(xprv, childIndex, hardened), Crypto.ResultOfHDKeyDeriveFromXPrv.class);
  }

  /**
   *  Derives the extended private key from the specified key and path
   *
   * @param xprv  Serialized extended private key
   * @param path  Derivation path, for instance "m/44'/396'/0'/0/0"
   */
  public static Crypto.ResultOfHDKeyDeriveFromXPrvPath hdkeyDeriveFromXprvPath(Context ctx,
      String xprv, String path) throws EverSdkException {
    return ctx.call("crypto.hdkey_derive_from_xprv_path", new Crypto.ParamsOfHDKeyDeriveFromXPrvPath(xprv, path), Crypto.ResultOfHDKeyDeriveFromXPrvPath.class);
  }

  /**
   *  Extracts the private key from the serialized extended private key
   *
   * @param xprv  Serialized extended private key
   */
  public static Crypto.ResultOfHDKeySecretFromXPrv hdkeySecretFromXprv(Context ctx, String xprv)
      throws EverSdkException {
    return ctx.call("crypto.hdkey_secret_from_xprv", new Crypto.ParamsOfHDKeySecretFromXPrv(xprv), Crypto.ResultOfHDKeySecretFromXPrv.class);
  }

  /**
   *  Extracts the public key from the serialized extended private key
   *
   * @param xprv  Serialized extended private key
   */
  public static Crypto.ResultOfHDKeyPublicFromXPrv hdkeyPublicFromXprv(Context ctx, String xprv)
      throws EverSdkException {
    return ctx.call("crypto.hdkey_public_from_xprv", new Crypto.ParamsOfHDKeyPublicFromXPrv(xprv), Crypto.ResultOfHDKeyPublicFromXPrv.class);
  }

  /**
   *  Performs symmetric `chacha20` encryption.
   *
   * @param data Must be encoded with `base64`. Source data to be encrypted or decrypted.
   * @param key Must be encoded with `hex`. 256-bit key.
   * @param nonce Must be encoded with `hex`. 96-bit nonce.
   */
  public static Crypto.ResultOfChaCha20 chacha20(Context ctx, String data, String key, String nonce)
      throws EverSdkException {
    return ctx.call("crypto.chacha20", new Crypto.ParamsOfChaCha20(data, key, nonce), Crypto.ResultOfChaCha20.class);
  }

  /**
   * Crypto Box is a root crypto object, that encapsulates some secret (seed phrase usually)
   * in encrypted form and acts as a factory for all crypto primitives used in SDK:
   * keys for signing and encryption, derived from this secret.
   *
   * Crypto Box encrypts original Seed Phrase with salt and password that is retrieved
   * from `password_provider` callback, implemented on Application side.
   *
   * When used, decrypted secret shows up in core library's memory for a very short period
   * of time and then is immediately overwritten with zeroes. Creates a Crypto Box instance.
   *
   * @param secretEncryptionSalt  Salt used for secret encryption. For example, a mobile device can use device ID as salt.
   * @param secretKey  Cryptobox secret
   */
  public static Crypto.RegisteredCryptoBox createCryptoBox(Context ctx, String secretEncryptionSalt,
      @JsonProperty("secret") Crypto.CryptoBoxSecret secretKey, AppSigningBox appObject) throws
      EverSdkException {
    return ctx.callAppObject("crypto.create_crypto_box", new Crypto.ParamsOfCreateCryptoBox(secretEncryptionSalt, secretKey), appObject, Crypto.RegisteredCryptoBox.class);
  }

  /**
   *  Removes Crypto Box. Clears all secret data.
   */
  public static void removeCryptoBox(Context ctx, Crypto.RegisteredCryptoBox params) throws
      EverSdkException {
    ctx.callVoid("crypto.remove_crypto_box", params);
  }

  /**
   *  Get Crypto Box Info. Used to get `encrypted_secret` that should be used for all the cryptobox initializations except the first one.
   */
  public static Crypto.ResultOfGetCryptoBoxInfo getCryptoBoxInfo(Context ctx,
      Crypto.RegisteredCryptoBox params) throws EverSdkException {
    return ctx.call("crypto.get_crypto_box_info", params, Crypto.ResultOfGetCryptoBoxInfo.class);
  }

  /**
   * Attention! Store this data in your application for a very short period of time and overwrite it with zeroes ASAP. Get Crypto Box Seed Phrase.
   */
  public static Crypto.ResultOfGetCryptoBoxSeedPhrase getCryptoBoxSeedPhrase(Context ctx,
      Crypto.RegisteredCryptoBox params) throws EverSdkException {
    return ctx.call("crypto.get_crypto_box_seed_phrase", params, Crypto.ResultOfGetCryptoBoxSeedPhrase.class);
  }

  /**
   *  Get handle of Signing Box derived from Crypto Box.
   *
   * @param handle  Crypto Box Handle.
   * @param hdpath By default, Everscale HD path is used. HD key derivation path.
   * @param secretLifetime  Store derived secret for this lifetime (in ms). The timer starts after each signing box operation. Secrets will be deleted immediately after each signing box operation, if this value is not set.
   */
  public static Crypto.RegisteredSigningBox getSigningBoxFromCryptoBox(Context ctx, Integer handle,
      String hdpath, Integer secretLifetime) throws EverSdkException {
    return ctx.call("crypto.get_signing_box_from_crypto_box", new Crypto.ParamsOfGetSigningBoxFromCryptoBox(handle, hdpath, secretLifetime), Crypto.RegisteredSigningBox.class);
  }

  /**
   * Derives encryption keypair from cryptobox secret and hdpath and
   * stores it in cache for `secret_lifetime`
   * or until explicitly cleared by `clear_crypto_box_secret_cache` method.
   * If `secret_lifetime` is not specified - overwrites encryption secret with zeroes immediately after
   * encryption operation. Gets Encryption Box from Crypto Box.
   *
   * @param handle  Crypto Box Handle.
   * @param hdpath By default, Everscale HD path is used. HD key derivation path.
   * @param algorithm  Encryption algorithm.
   * @param secretLifetime  Store derived secret for encryption algorithm for this lifetime (in ms). The timer starts after each encryption box operation. Secrets will be deleted (overwritten with zeroes) after each encryption operation, if this value is not set.
   */
  public static Crypto.RegisteredEncryptionBox getEncryptionBoxFromCryptoBox(Context ctx,
      Integer handle, String hdpath, Crypto.BoxEncryptionAlgorithm algorithm,
      Integer secretLifetime) throws EverSdkException {
    return ctx.call("crypto.get_encryption_box_from_crypto_box", new Crypto.ParamsOfGetEncryptionBoxFromCryptoBox(handle, hdpath, algorithm, secretLifetime), Crypto.RegisteredEncryptionBox.class);
  }

  /**
   *  Removes cached secrets (overwrites with zeroes) from all signing and encryption boxes, derived from crypto box.
   */
  public static void clearCryptoBoxSecretCache(Context ctx, Crypto.RegisteredCryptoBox params)
      throws EverSdkException {
    ctx.callVoid("crypto.clear_crypto_box_secret_cache", params);
  }

  /**
   *  Register an application implemented signing box.
   */
  public static Crypto.RegisteredSigningBox registerSigningBox(Context ctx, AppSigningBox appObject)
      throws EverSdkException {
    return ctx.callAppObject("crypto.register_signing_box", null, appObject, Crypto.RegisteredSigningBox.class);
  }

  /**
   *  Creates a default signing box implementation.
   */
  public static Crypto.RegisteredSigningBox getSigningBox(Context ctx, Crypto.KeyPair params) throws
      EverSdkException {
    return ctx.call("crypto.get_signing_box", params, Crypto.RegisteredSigningBox.class);
  }

  /**
   *  Returns public key of signing key pair.
   */
  public static Crypto.ResultOfSigningBoxGetPublicKey signingBoxGetPublicKey(Context ctx,
      Crypto.RegisteredSigningBox params) throws EverSdkException {
    return ctx.call("crypto.signing_box_get_public_key", params, Crypto.ResultOfSigningBoxGetPublicKey.class);
  }

  /**
   *  Returns signed user data.
   *
   * @param signingBox  Signing Box handle.
   * @param unsigned Must be encoded with `base64`. Unsigned user data.
   */
  public static Crypto.ResultOfSigningBoxSign signingBoxSign(Context ctx, Integer signingBox,
      String unsigned) throws EverSdkException {
    return ctx.call("crypto.signing_box_sign", new Crypto.ParamsOfSigningBoxSign(signingBox, unsigned), Crypto.ResultOfSigningBoxSign.class);
  }

  /**
   *  Removes signing box from SDK.
   */
  public static void removeSigningBox(Context ctx, Crypto.RegisteredSigningBox params) throws
      EverSdkException {
    ctx.callVoid("crypto.remove_signing_box", params);
  }

  /**
   *  Register an application implemented encryption box.
   */
  public static Crypto.RegisteredEncryptionBox registerEncryptionBox(Context ctx,
      AppSigningBox appObject) throws EverSdkException {
    return ctx.callAppObject("crypto.register_encryption_box", null, appObject, Crypto.RegisteredEncryptionBox.class);
  }

  /**
   *  Removes encryption box from SDK
   */
  public static void removeEncryptionBox(Context ctx, Crypto.RegisteredEncryptionBox params) throws
      EverSdkException {
    ctx.callVoid("crypto.remove_encryption_box", params);
  }

  /**
   *  Queries info from the given encryption box
   *
   * @param encryptionBox  Encryption box handle
   */
  public static Crypto.ResultOfEncryptionBoxGetInfo encryptionBoxGetInfo(Context ctx,
      Integer encryptionBox) throws EverSdkException {
    return ctx.call("crypto.encryption_box_get_info", new Crypto.ParamsOfEncryptionBoxGetInfo(encryptionBox), Crypto.ResultOfEncryptionBoxGetInfo.class);
  }

  /**
   * Block cipher algorithms pad data to cipher block size so encrypted data can be longer then original data. Client should store the original data size after encryption and use it after
   * decryption to retrieve the original data from decrypted data. Encrypts data using given encryption box Note.
   *
   * @param encryptionBox  Encryption box handle
   * @param data  Data to be encrypted, encoded in Base64
   */
  public static Crypto.ResultOfEncryptionBoxEncrypt encryptionBoxEncrypt(Context ctx,
      Integer encryptionBox, String data) throws EverSdkException {
    return ctx.call("crypto.encryption_box_encrypt", new Crypto.ParamsOfEncryptionBoxEncrypt(encryptionBox, data), Crypto.ResultOfEncryptionBoxEncrypt.class);
  }

  /**
   * Block cipher algorithms pad data to cipher block size so encrypted data can be longer then original data. Client should store the original data size after encryption and use it after
   * decryption to retrieve the original data from decrypted data. Decrypts data using given encryption box Note.
   *
   * @param encryptionBox  Encryption box handle
   * @param data  Data to be decrypted, encoded in Base64
   */
  public static Crypto.ResultOfEncryptionBoxDecrypt encryptionBoxDecrypt(Context ctx,
      Integer encryptionBox, String data) throws EverSdkException {
    return ctx.call("crypto.encryption_box_decrypt", new Crypto.ParamsOfEncryptionBoxDecrypt(encryptionBox, data), Crypto.ResultOfEncryptionBoxDecrypt.class);
  }

  /**
   *  Creates encryption box with specified algorithm
   *
   * @param algorithm  Encryption algorithm specifier including cipher parameters (key, IV, etc)
   */
  public static Crypto.RegisteredEncryptionBox createEncryptionBox(Context ctx,
      Crypto.EncryptionAlgorithm algorithm) throws EverSdkException {
    return ctx.call("crypto.create_encryption_box", new Crypto.ParamsOfCreateEncryptionBox(algorithm), Crypto.RegisteredEncryptionBox.class);
  }

  /**
   * @param modularPower  Result of modular exponentiation
   */
  public static final record ResultOfModularPower(String modularPower) {
  }

  /**
   * @param theirPublic Must be encoded with `hex`. 256-bit key.
   * @param secretKey Must be encoded with `hex`. 256-bit key.
   * @param nonce Must be encoded with `hex`. 96-bit nonce.
   */
  public static final record NaclBoxParamsEB(String theirPublic,
      @JsonProperty("secret") String secretKey, String nonce) {
  }

  /**
   *  Signing box callbacks.
   */
  public sealed interface ParamsOfAppSigningBox {
    /**
     *  Get signing box public key
     */
    final record GetPublicKey() implements ParamsOfAppSigningBox {
      @JsonProperty("type")
      public String type() {
        return "GetPublicKey";
      }
    }

    /**
     *  Sign data
     *
     * @param unsigned  Data to sign encoded as base64
     */
    final record Sign(String unsigned) implements ParamsOfAppSigningBox {
      @JsonProperty("type")
      public String type() {
        return "Sign";
      }
    }
  }

  /**
   * @param encryptionBox  Encryption box handle
   * @param data  Data to be encrypted, encoded in Base64
   */
  public static final record ParamsOfEncryptionBoxEncrypt(Integer encryptionBox, String data) {
  }

  /**
   * @param data  Decrypted data, encoded in Base64.
   */
  public static final record ResultOfEncryptionBoxDecrypt(String data) {
  }

  /**
   * @param factors  Two factors of composite or empty if composite can't be factorized.
   */
  public static final record ResultOfFactorize(String[] factors) {
  }

  /**
   *  Crypto Box Secret.
   */
  public sealed interface CryptoBoxSecret {
    /**
     * This type should be used upon the first wallet initialization, all further initializations
     * should use `EncryptedSecret` type instead.
     *
     * Get `encrypted_secret` with `get_crypto_box_info` function and store it on your side. Creates Crypto Box from a random seed phrase. This option can be used if a developer doesn't want the seed phrase to leave the core library's memory, where it is stored encrypted.
     */
    final record RandomSeedPhrase(Crypto.MnemonicDictionary dictionary,
        Integer wordcount) implements CryptoBoxSecret {
      @JsonProperty("type")
      public String type() {
        return "RandomSeedPhrase";
      }
    }

    /**
     * This type should be used only upon the first wallet initialization, all further
     * initializations should use `EncryptedSecret` type instead.
     *
     * Get `encrypted_secret` with `get_crypto_box_info` function and store it on your side. Restores crypto box instance from an existing seed phrase. This type should be used when Crypto Box is initialized from a seed phrase, entered by a user.
     */
    final record PredefinedSeedPhrase(String phrase, Crypto.MnemonicDictionary dictionary,
        Integer wordcount) implements CryptoBoxSecret {
      @JsonProperty("type")
      public String type() {
        return "PredefinedSeedPhrase";
      }
    }

    /**
     * It is an object, containing seed phrase or private key, encrypted with
     * `secret_encryption_salt` and password from `password_provider`.
     *
     * Note that if you want to change salt or password provider, then you need to reinitialize
     * the wallet with `PredefinedSeedPhrase`, then get `EncryptedSecret` via `get_crypto_box_info`,
     * store it somewhere, and only after that initialize the wallet with `EncryptedSecret` type. Use this type for wallet reinitializations, when you already have `encrypted_secret` on hands. To get `encrypted_secret`, use `get_crypto_box_info` function after you initialized your crypto box for the first time.
     *
     * @param encryptedSecret  It is an object, containing encrypted seed phrase or private key (now we support only seed phrase).
     */
    final record EncryptedSecret(String encryptedSecret) implements CryptoBoxSecret {
      @JsonProperty("type")
      public String type() {
        return "EncryptedSecret";
      }
    }
  }

  /**
   * @param signed  Signed data that must be verified encoded in `base64`.
   * @param publicKey  Signer's public key - 64 symbols hex string
   */
  public static final record ParamsOfVerifySignature(String signed,
      @JsonProperty("public") String publicKey) {
  }

  /**
   * @param key Must be encoded with `hex`. 256-bit key.
   * @param nonce Must be encoded with `hex`. 96-bit nonce.
   */
  public static final record ChaCha20ParamsEB(String key, String nonce) {
  }

  /**
   * @param publicKey  Public key - 64 symbols hex string
   */
  public static final record ParamsOfConvertPublicKeyToTonSafeFormat(String publicKey) {
  }

  /**
   * @param encrypted Encoded with `base64`. Data that must be decrypted.
   * @param nonce  Nonce
   * @param theirPublic  Sender's public key - unprefixed 0-padded to 64 symbols hex string
   * @param secretKey  Receiver's private key - unprefixed 0-padded to 64 symbols hex string
   */
  public static final record ParamsOfNaclBoxOpen(String encrypted, String nonce, String theirPublic,
      @JsonProperty("secret") String secretKey) {
  }

  /**
   * @param entropy Hex encoded. Entropy bytes.
   * @param dictionary  Dictionary identifier
   * @param wordCount  Mnemonic word count
   */
  public static final record ParamsOfMnemonicFromEntropy(String entropy,
      Crypto.MnemonicDictionary dictionary, Integer wordCount) {
  }

  /**
   * @param publicKey  Public key - 64 symbols hex string
   */
  public static final record ResultOfHDKeyPublicFromXPrv(@JsonProperty("public") String publicKey) {
  }

  /**
   * @param base  `base` argument of calculation.
   * @param exponent  `exponent` argument of calculation.
   * @param modulus  `modulus` argument of calculation.
   */
  public static final record ParamsOfModularPower(String base, String exponent, String modulus) {
  }

  /**
   * @param phrase  String of mnemonic words
   */
  public static final record ResultOfMnemonicFromRandom(String phrase) {
  }

  /**
   * @param unsigned  Data that must be signed encoded in `base64`.
   * @param secretKey  Signer's secret key - unprefixed 0-padded to 128 symbols hex string (concatenation of 64 symbols secret and 64 symbols public keys). See `nacl_sign_keypair_from_secret_key`.
   */
  public static final record ParamsOfNaclSign(String unsigned,
      @JsonProperty("secret") String secretKey) {
  }

  /**
   * @param secretKey  Private key - 64 symbols hex string
   */
  public static final record ResultOfHDKeySecretFromXPrv(@JsonProperty("secret") String secretKey) {
  }

  /**
   * @param signed  Signed data, encoded in `base64`.
   */
  public static final record ResultOfNaclSign(String signed) {
  }

  public enum CipherMode {
    CBC,

    CFB,

    CTR,

    ECB,

    OFB
  }

  public enum CryptoErrorCode {
    InvalidPublicKey(100),

    InvalidSecretKey(101),

    InvalidKey(102),

    InvalidFactorizeChallenge(106),

    InvalidBigInt(107),

    ScryptFailed(108),

    InvalidKeySize(109),

    NaclSecretBoxFailed(110),

    NaclBoxFailed(111),

    NaclSignFailed(112),

    Bip39InvalidEntropy(113),

    Bip39InvalidPhrase(114),

    Bip32InvalidKey(115),

    Bip32InvalidDerivePath(116),

    Bip39InvalidDictionary(117),

    Bip39InvalidWordCount(118),

    MnemonicGenerationFailed(119),

    MnemonicFromEntropyFailed(120),

    SigningBoxNotRegistered(121),

    InvalidSignature(122),

    EncryptionBoxNotRegistered(123),

    InvalidIvSize(124),

    UnsupportedCipherMode(125),

    CannotCreateCipher(126),

    EncryptDataError(127),

    DecryptDataError(128),

    IvRequired(129),

    CryptoBoxNotRegistered(130),

    InvalidCryptoBoxType(131),

    CryptoBoxSecretSerializationError(132),

    CryptoBoxSecretDeserializationError(133),

    InvalidNonceSize(134);

    private final Integer value;

    CryptoErrorCode(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer value() {
      return this.value;
    }
  }

  /**
   * @param nonce Must be encoded with `hex`. 96-bit nonce.
   */
  public static final record ChaCha20ParamsCB(String nonce) {
  }

  /**
   * @param unsigned  Unsigned data, encoded in `base64`.
   */
  public static final record ResultOfNaclSignOpen(String unsigned) {
  }

  /**
   *  Returning values from signing box callbacks.
   */
  public sealed interface ResultOfAppEncryptionBox {
    /**
     *  Result of getting encryption box info
     */
    final record GetInfo(Crypto.EncryptionBoxInfo info) implements ResultOfAppEncryptionBox {
      @JsonProperty("type")
      public String type() {
        return "GetInfo";
      }
    }

    /**
     *  Result of encrypting data
     *
     * @param data  Encrypted data, encoded in Base64
     */
    final record Encrypt(String data) implements ResultOfAppEncryptionBox {
      @JsonProperty("type")
      public String type() {
        return "Encrypt";
      }
    }

    /**
     *  Result of decrypting data
     *
     * @param data  Decrypted data, encoded in Base64
     */
    final record Decrypt(String data) implements ResultOfAppEncryptionBox {
      @JsonProperty("type")
      public String type() {
        return "Decrypt";
      }
    }
  }

  public static final record AesParamsEB(Crypto.CipherMode mode, String key, String iv) {
  }

  /**
   * @param pubkey Encoded with hex Public key of signing box.
   */
  public static final record ResultOfSigningBoxGetPublicKey(String pubkey) {
  }

  /**
   * @param secretKey  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public static final record ParamsOfNaclBoxKeyPairFromSecret(@JsonProperty("secret") String secretKey) {
  }

  /**
   * @param publicKey  Public key - 64 symbols hex string
   * @param secretKey  Private key - u64 symbols hex string
   */
  public static final record KeyPair(@JsonProperty("public") String publicKey,
      @JsonProperty("secret") String secretKey) {
  }

  /**
   * @param encryptionBox  Encryption box handle
   * @param data  Data to be decrypted, encoded in Base64
   */
  public static final record ParamsOfEncryptionBoxDecrypt(Integer encryptionBox, String data) {
  }

  /**
   * @param decrypted Encoded with `base64`. Data that must be encrypted.
   * @param nonce  Nonce in `hex`
   * @param key  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public static final record ParamsOfNaclSecretBox(String decrypted, String nonce, String key) {
  }

  /**
   * @param theirPublic Must be encoded with `hex`. 256-bit key.
   * @param nonce Must be encoded with `hex`. 96-bit nonce.
   */
  public static final record NaclBoxParamsCB(String theirPublic, String nonce) {
  }

  /**
   * @param valid  Flag indicating if the mnemonic is valid or not
   */
  public static final record ResultOfMnemonicVerify(Boolean valid) {
  }

  public static final record ResultOfGetCryptoBoxSeedPhrase(String phrase,
      Crypto.MnemonicDictionary dictionary, Integer wordcount) {
  }

  /**
   * @param signed  Signed data combined with signature encoded in `base64`.
   * @param signature  Signature encoded in `hex`.
   */
  public static final record ResultOfSign(String signed, String signature) {
  }

  /**
   * @param xprv  Serialized extended private key
   */
  public static final record ParamsOfHDKeyPublicFromXPrv(String xprv) {
  }

  public sealed interface BoxEncryptionAlgorithm {
    final record ChaCha20(Crypto.ChaCha20ParamsCB value) implements BoxEncryptionAlgorithm {
      @JsonProperty("type")
      public String type() {
        return "ChaCha20";
      }
    }

    final record NaclBox(Crypto.NaclBoxParamsCB value) implements BoxEncryptionAlgorithm {
      @JsonProperty("type")
      public String type() {
        return "NaclBox";
      }
    }

    final record NaclSecretBox(Crypto.NaclSecretBoxParamsCB value) implements BoxEncryptionAlgorithm {
      @JsonProperty("type")
      public String type() {
        return "NaclSecretBox";
      }
    }
  }

  public enum MnemonicDictionary {
    Ton(0),

    English(1),

    ChineseSimplified(2),

    ChineseTraditional(3),

    French(4),

    Italian(5),

    Japanese(6),

    Korean(7),

    Spanish(8);

    private final Integer value;

    MnemonicDictionary(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer value() {
      return this.value;
    }
  }

  /**
   * @param data Must be encoded with `base64`. Source data to be encrypted or decrypted.
   * @param key Must be encoded with `hex`. 256-bit key.
   * @param nonce Must be encoded with `hex`. 96-bit nonce.
   */
  public static final record ParamsOfChaCha20(String data, String key, String nonce) {
  }

  /**
   * @param signingBox  Signing Box handle.
   * @param unsigned Must be encoded with `base64`. Unsigned user data.
   */
  public static final record ParamsOfSigningBoxSign(Integer signingBox, String unsigned) {
  }

  /**
   * @param bytes  Generated bytes encoded in `base64`.
   */
  public static final record ResultOfGenerateRandomBytes(String bytes) {
  }

  /**
   * @param handle  Crypto Box Handle.
   * @param hdpath By default, Everscale HD path is used. HD key derivation path.
   * @param algorithm  Encryption algorithm.
   * @param secretLifetime  Store derived secret for encryption algorithm for this lifetime (in ms). The timer starts after each encryption box operation. Secrets will be deleted (overwritten with zeroes) after each encryption operation, if this value is not set.
   */
  public static final record ParamsOfGetEncryptionBoxFromCryptoBox(Integer handle, String hdpath,
      Crypto.BoxEncryptionAlgorithm algorithm, Integer secretLifetime) {
  }

  /**
   * @param signed Encoded with `base64`. Signed data that must be unsigned.
   * @param publicKey  Signer's public key - unprefixed 0-padded to 64 symbols hex string
   */
  public static final record ParamsOfNaclSignOpen(String signed,
      @JsonProperty("public") String publicKey) {
  }

  /**
   * @param phrase  Phrase
   */
  public static final record ResultOfMnemonicFromEntropy(String phrase) {
  }

  /**
   * @param algorithm  Encryption algorithm specifier including cipher parameters (key, IV, etc)
   */
  public static final record ParamsOfCreateEncryptionBox(Crypto.EncryptionAlgorithm algorithm) {
  }

  /**
   * @param phrase  Phrase
   * @param dictionary  Dictionary identifier
   * @param wordCount  Word count
   */
  public static final record ParamsOfMnemonicVerify(String phrase,
      Crypto.MnemonicDictionary dictionary, Integer wordCount) {
  }

  /**
   * @param xprv  Serialized extended private key
   * @param path  Derivation path, for instance "m/44'/396'/0'/0/0"
   */
  public static final record ParamsOfHDKeyDeriveFromXPrvPath(String xprv, String path) {
  }

  /**
   * @param key Encoded with `hex`. Derived key.
   */
  public static final record ResultOfScrypt(String key) {
  }

  /**
   * @param handle  Crypto Box Handle.
   * @param hdpath By default, Everscale HD path is used. HD key derivation path.
   * @param secretLifetime  Store derived secret for this lifetime (in ms). The timer starts after each signing box operation. Secrets will be deleted immediately after each signing box operation, if this value is not set.
   */
  public static final record ParamsOfGetSigningBoxFromCryptoBox(Integer handle, String hdpath,
      Integer secretLifetime) {
  }

  /**
   * @param handle  Handle of the signing box.
   */
  public static final record RegisteredSigningBox(Integer handle) {
  }

  /**
   * @param encrypted  Encrypted data encoded in `base64`.
   */
  public static final record ResultOfNaclBox(String encrypted) {
  }

  /**
   * @param words  The list of mnemonic words
   */
  public static final record ResultOfMnemonicWords(String words) {
  }

  /**
   * @param data Encoded with `base64`. Encrypted/decrypted data.
   */
  public static final record ResultOfChaCha20(String data) {
  }

  /**
   *  Encryption box information.
   *
   * @param hdpath  Derivation path, for instance "m/44'/396'/0'/0/0"
   * @param algorithm  Cryptographic algorithm, used by this encryption box
   * @param options  Options, depends on algorithm and specific encryption box implementation
   * @param publicKey  Public information, depends on algorithm
   */
  public static final record EncryptionBoxInfo(String hdpath, String algorithm,
      Map<String, Object> options, @JsonProperty("public") Map<String, Object> publicKey) {
  }

  /**
   * @param xprv  Serialized extended master private key
   */
  public static final record ResultOfHDKeyXPrvFromMnemonic(String xprv) {
  }

  /**
   * @param xprv  Serialized extended private key
   */
  public static final record ResultOfHDKeyDeriveFromXPrv(String xprv) {
  }

  public sealed interface EncryptionAlgorithm {
    final record AES(Crypto.AesParamsEB value) implements EncryptionAlgorithm {
      @JsonProperty("type")
      public String type() {
        return "AES";
      }
    }

    final record ChaCha20(Crypto.ChaCha20ParamsEB value) implements EncryptionAlgorithm {
      @JsonProperty("type")
      public String type() {
        return "ChaCha20";
      }
    }

    final record NaclBox(Crypto.NaclBoxParamsEB value) implements EncryptionAlgorithm {
      @JsonProperty("type")
      public String type() {
        return "NaclBox";
      }
    }

    final record NaclSecretBox(Crypto.NaclSecretBoxParamsEB value) implements EncryptionAlgorithm {
      @JsonProperty("type")
      public String type() {
        return "NaclSecretBox";
      }
    }
  }

  /**
   *  Interface for data encryption/decryption
   */
  public sealed interface ParamsOfAppEncryptionBox {
    /**
     *  Get encryption box info
     */
    final record GetInfo() implements ParamsOfAppEncryptionBox {
      @JsonProperty("type")
      public String type() {
        return "GetInfo";
      }
    }

    /**
     *  Encrypt data
     *
     * @param data  Data, encoded in Base64
     */
    final record Encrypt(String data) implements ParamsOfAppEncryptionBox {
      @JsonProperty("type")
      public String type() {
        return "Encrypt";
      }
    }

    /**
     *  Decrypt data
     *
     * @param data  Data, encoded in Base64
     */
    final record Decrypt(String data) implements ParamsOfAppEncryptionBox {
      @JsonProperty("type")
      public String type() {
        return "Decrypt";
      }
    }
  }

  /**
   * @param decrypted  Data that must be encrypted encoded in `base64`.
   * @param nonce  Nonce, encoded in `hex`
   * @param theirPublic  Receiver's public key - unprefixed 0-padded to 64 symbols hex string
   * @param secretKey  Sender's private key - unprefixed 0-padded to 64 symbols hex string
   */
  public static final record ParamsOfNaclBox(String decrypted, String nonce, String theirPublic,
      @JsonProperty("secret") String secretKey) {
  }

  /**
   * @param secretKey  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public static final record ParamsOfNaclSignKeyPairFromSecret(@JsonProperty("secret") String secretKey) {
  }

  /**
   *  Returning values from signing box callbacks.
   */
  public sealed interface ResultOfAppSigningBox {
    /**
     *  Result of getting public key
     *
     * @param publicKey  Signing box public key
     */
    final record GetPublicKey(String publicKey) implements ResultOfAppSigningBox {
      @JsonProperty("type")
      public String type() {
        return "GetPublicKey";
      }
    }

    /**
     *  Result of signing data
     *
     * @param signature  Data signature encoded as hex
     */
    final record Sign(String signature) implements ResultOfAppSigningBox {
      @JsonProperty("type")
      public String type() {
        return "Sign";
      }
    }
  }

  /**
   * @param data Encoded with `base64`. Input data for hash calculation.
   */
  public static final record ParamsOfHash(String data) {
  }

  public sealed interface ResultOfAppPasswordProvider {
    /**
     * @param encryptedPassword  Password, encrypted and encoded to base64. Crypto box uses this password to decrypt its secret (seed phrase).
     * @param appEncryptionPubkey Used together with `encryption_public_key` to decode `encrypted_password`. Hex encoded public key of a temporary key pair, used for password encryption on application side.
     */
    final record GetPassword(String encryptedPassword,
        String appEncryptionPubkey) implements ResultOfAppPasswordProvider {
      @JsonProperty("type")
      public String type() {
        return "GetPassword";
      }
    }
  }

  /**
   * @param unsigned Encoded with `base64`. Unsigned data that must be verified.
   * @param signature Encoded with `hex`. Signature that must be verified.
   * @param publicKey  Signer's public key - unprefixed 0-padded to 64 symbols hex string.
   */
  public static final record ParamsOfNaclSignDetachedVerify(String unsigned, String signature,
      @JsonProperty("public") String publicKey) {
  }

  /**
   * @param length  Size of random byte array.
   */
  public static final record ParamsOfGenerateRandomBytes(Integer length) {
  }

  /**
   * @param password  The password bytes to be hashed. Must be encoded with `base64`.
   * @param salt  Salt bytes that modify the hash to protect against Rainbow table attacks. Must be encoded with `base64`.
   * @param logN  CPU/memory cost parameter
   * @param r  The block size parameter, which fine-tunes sequential memory read size and performance.
   * @param p  Parallelization parameter.
   * @param dkLen  Intended output length in octets of the derived key.
   */
  public static final record ParamsOfScrypt(String password, String salt, Integer logN, Integer r,
      Integer p, Integer dkLen) {
  }

  /**
   * @param secretEncryptionSalt  Salt used for secret encryption. For example, a mobile device can use device ID as salt.
   * @param secretKey  Cryptobox secret
   */
  public static final record ParamsOfCreateCryptoBox(String secretEncryptionSalt,
      @JsonProperty("secret") Crypto.CryptoBoxSecret secretKey) {
  }

  public static final record RegisteredCryptoBox(Integer handle) {
  }

  /**
   * @param handle  Handle of the encryption box.
   */
  public static final record RegisteredEncryptionBox(Integer handle) {
  }

  /**
   * @param dictionary  Dictionary identifier
   * @param wordCount  Mnemonic word count
   */
  public static final record ParamsOfMnemonicFromRandom(Crypto.MnemonicDictionary dictionary,
      Integer wordCount) {
  }

  /**
   * @param data Encoded with `base64`. Input data for CRC calculation.
   */
  public static final record ParamsOfTonCrc16(String data) {
  }

  /**
   * @param hash Encoded with 'hex'. Hash of input `data`.
   */
  public static final record ResultOfHash(String hash) {
  }

  /**
   * @param encryptionBox  Encryption box handle
   */
  public static final record ParamsOfEncryptionBoxGetInfo(Integer encryptionBox) {
  }

  /**
   * @param xprv  Serialized extended private key
   */
  public static final record ParamsOfHDKeySecretFromXPrv(String xprv) {
  }

  /**
   * @param phrase  Phrase
   * @param path  Derivation path, for instance "m/44'/396'/0'/0/0"
   * @param dictionary  Dictionary identifier
   * @param wordCount  Word count
   */
  public static final record ParamsOfMnemonicDeriveSignKeys(String phrase, String path,
      Crypto.MnemonicDictionary dictionary, Integer wordCount) {
  }

  /**
   * @param encryptedSecret  Secret (seed phrase) encrypted with salt and password.
   */
  public static final record ResultOfGetCryptoBoxInfo(String encryptedSecret) {
  }

  /**
   * @param nonce  Nonce in `hex`
   */
  public static final record NaclSecretBoxParamsCB(String nonce) {
  }

  /**
   * @param phrase  String with seed phrase
   * @param dictionary  Dictionary identifier
   * @param wordCount  Mnemonic word count
   */
  public static final record ParamsOfHDKeyXPrvFromMnemonic(String phrase,
      Crypto.MnemonicDictionary dictionary, Integer wordCount) {
  }

  /**
   * @param tonPublicKey  Public key represented in TON safe format.
   */
  public static final record ResultOfConvertPublicKeyToTonSafeFormat(String tonPublicKey) {
  }

  /**
   * @param composite  Hexadecimal representation of u64 composite number.
   */
  public static final record ParamsOfFactorize(String composite) {
  }

  /**
   * @param data Padded to cipher block size Encrypted data, encoded in Base64.
   */
  public static final record ResultOfEncryptionBoxEncrypt(String data) {
  }

  /**
   * @param dictionary  Dictionary identifier
   */
  public static final record ParamsOfMnemonicWords(Crypto.MnemonicDictionary dictionary) {
  }

  public static final record AesInfo(Crypto.CipherMode mode, String iv) {
  }

  /**
   * @param xprv  Derived serialized extended private key
   */
  public static final record ResultOfHDKeyDeriveFromXPrvPath(String xprv) {
  }

  /**
   * @param xprv  Serialized extended private key
   * @param childIndex  Child index (see BIP-0032)
   * @param hardened  Indicates the derivation of hardened/not-hardened key (see BIP-0032)
   */
  public static final record ParamsOfHDKeyDeriveFromXPrv(String xprv, Integer childIndex,
      Boolean hardened) {
  }

  /**
   * To secure the password while passing it from application to the library,
   * the library generates a temporary key pair, passes the pubkey
   * to the passwordProvider, decrypts the received password with private key,
   * and deletes the key pair right away.
   *
   * Application should generate a temporary nacl_box_keypair
   * and encrypt the password with naclbox function using nacl_box_keypair.secret
   * and encryption_public_key keys + nonce = 24-byte prefix of encryption_public_key. Interface that provides a callback that returns an encrypted password, used for cryptobox secret encryption
   */
  public sealed interface ParamsOfAppPasswordProvider {
    /**
     * @param encryptionPublicKey  Temporary library pubkey, that is used on application side for password encryption, along with application temporary private key and nonce. Used for password decryption on library side.
     */
    final record GetPassword(String encryptionPublicKey) implements ParamsOfAppPasswordProvider {
      @JsonProperty("type")
      public String type() {
        return "GetPassword";
      }
    }
  }

  /**
   * @param unsigned  Unsigned data encoded in `base64`.
   */
  public static final record ResultOfVerifySignature(String unsigned) {
  }

  /**
   * @param encrypted Encoded with `base64`. Data that must be decrypted.
   * @param nonce  Nonce in `hex`
   * @param key  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  public static final record ParamsOfNaclSecretBoxOpen(String encrypted, String nonce, String key) {
  }

  /**
   * @param signature Encoded with `hex`. Data signature.
   */
  public static final record ResultOfSigningBoxSign(String signature) {
  }

  /**
   * @param key  Secret key - unprefixed 0-padded to 64 symbols hex string
   * @param nonce  Nonce in `hex`
   */
  public static final record NaclSecretBoxParamsEB(String key, String nonce) {
  }

  /**
   * @param decrypted  Decrypted data encoded in `base64`.
   */
  public static final record ResultOfNaclBoxOpen(String decrypted) {
  }

  /**
   * @param signature  Signature encoded in `hex`.
   */
  public static final record ResultOfNaclSignDetached(String signature) {
  }

  /**
   * @param unsigned  Data that must be signed encoded in `base64`.
   * @param keys  Sign keys.
   */
  public static final record ParamsOfSign(String unsigned, Crypto.KeyPair keys) {
  }

  /**
   * @param succeeded  `true` if verification succeeded or `false` if it failed
   */
  public static final record ResultOfNaclSignDetachedVerify(Boolean succeeded) {
  }

  /**
   * @param info  Encryption box information
   */
  public static final record ResultOfEncryptionBoxGetInfo(Crypto.EncryptionBoxInfo info) {
  }

  /**
   * @param crc  Calculated CRC for input data.
   */
  public static final record ResultOfTonCrc16(Integer crc) {
  }
}
