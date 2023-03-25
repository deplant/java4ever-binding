package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.util.Map;

/**
 * <strong>Boc</strong>
 * Contains methods of "boc" module of EVER-SDK API
 *
 * BOC manipulation module. 
 * @version 1.42.1
 */
public final class Boc {
  /**
   * JSON structure is compatible with GraphQL API message object Parses message boc into a JSON
   *
   * @param boc  BOC encoded as base64
   */
  public static Boc.ResultOfParse parseMessage(Context ctx, String boc) throws EverSdkException {
    return ctx.call("boc.parse_message", new Boc.ParamsOfParse(boc), Boc.ResultOfParse.class);
  }

  /**
   * JSON structure is compatible with GraphQL API transaction object Parses transaction boc into a JSON
   *
   * @param boc  BOC encoded as base64
   */
  public static Boc.ResultOfParse parseTransaction(Context ctx, String boc) throws
      EverSdkException {
    return ctx.call("boc.parse_transaction", new Boc.ParamsOfParse(boc), Boc.ResultOfParse.class);
  }

  /**
   * JSON structure is compatible with GraphQL API account object Parses account boc into a JSON
   *
   * @param boc  BOC encoded as base64
   */
  public static Boc.ResultOfParse parseAccount(Context ctx, String boc) throws EverSdkException {
    return ctx.call("boc.parse_account", new Boc.ParamsOfParse(boc), Boc.ResultOfParse.class);
  }

  /**
   * JSON structure is compatible with GraphQL API block object Parses block boc into a JSON
   *
   * @param boc  BOC encoded as base64
   */
  public static Boc.ResultOfParse parseBlock(Context ctx, String boc) throws EverSdkException {
    return ctx.call("boc.parse_block", new Boc.ParamsOfParse(boc), Boc.ResultOfParse.class);
  }

  /**
   * JSON structure is compatible with GraphQL API shardstate object Parses shardstate boc into a JSON
   *
   * @param boc  BOC encoded as base64
   * @param id  Shardstate identifier
   * @param workchainId  Workchain shardstate belongs to
   */
  public static Boc.ResultOfParse parseShardstate(Context ctx, String boc, String id,
      Integer workchainId) throws EverSdkException {
    return ctx.call("boc.parse_shardstate", new Boc.ParamsOfParseShardstate(boc, id, workchainId), Boc.ResultOfParse.class);
  }

  /**
   *  Extract blockchain configuration from key block and also from zerostate.
   *
   * @param blockBoc  Key block BOC or zerostate BOC encoded as base64
   */
  public static Boc.ResultOfGetBlockchainConfig getBlockchainConfig(Context ctx, String blockBoc)
      throws EverSdkException {
    return ctx.call("boc.get_blockchain_config", new Boc.ParamsOfGetBlockchainConfig(blockBoc), Boc.ResultOfGetBlockchainConfig.class);
  }

  /**
   *  Calculates BOC root hash
   *
   * @param boc  BOC encoded as base64 or BOC handle
   */
  public static Boc.ResultOfGetBocHash getBocHash(Context ctx, String boc) throws EverSdkException {
    return ctx.call("boc.get_boc_hash", new Boc.ParamsOfGetBocHash(boc), Boc.ResultOfGetBocHash.class);
  }

  /**
   *  Calculates BOC depth
   *
   * @param boc  BOC encoded as base64 or BOC handle
   */
  public static Boc.ResultOfGetBocDepth getBocDepth(Context ctx, String boc) throws
      EverSdkException {
    return ctx.call("boc.get_boc_depth", new Boc.ParamsOfGetBocDepth(boc), Boc.ResultOfGetBocDepth.class);
  }

  /**
   *  Extracts code from TVC contract image
   *
   * @param tvc  Contract TVC image or image BOC handle
   */
  public static Boc.ResultOfGetCodeFromTvc getCodeFromTvc(Context ctx, String tvc) throws
      EverSdkException {
    return ctx.call("boc.get_code_from_tvc", new Boc.ParamsOfGetCodeFromTvc(tvc), Boc.ResultOfGetCodeFromTvc.class);
  }

  /**
   *  Get BOC from cache
   *
   * @param bocRef  Reference to the cached BOC
   */
  public static Boc.ResultOfBocCacheGet cacheGet(Context ctx, String bocRef) throws
      EverSdkException {
    return ctx.call("boc.cache_get", new Boc.ParamsOfBocCacheGet(bocRef), Boc.ResultOfBocCacheGet.class);
  }

  /**
   *  Save BOC into cache or increase pin counter for existing pinned BOC
   *
   * @param boc  BOC encoded as base64 or BOC reference
   * @param cacheType  Cache type
   */
  public static Boc.ResultOfBocCacheSet cacheSet(Context ctx, String boc,
      Boc.BocCacheType cacheType) throws EverSdkException {
    return ctx.call("boc.cache_set", new Boc.ParamsOfBocCacheSet(boc, cacheType), Boc.ResultOfBocCacheSet.class);
  }

  /**
   *  Unpin BOCs with specified pin defined in the `cache_set`. Decrease pin reference counter for BOCs with specified pin defined in the `cache_set`. BOCs which have only 1 pin and its reference counter become 0 will be removed from cache
   *
   * @param pin  Pinned name
   * @param bocRef If it is provided then only referenced BOC is unpinned Reference to the cached BOC.
   */
  public static void cacheUnpin(Context ctx, String pin, String bocRef) throws EverSdkException {
    ctx.callVoid("boc.cache_unpin", new Boc.ParamsOfBocCacheUnpin(pin, bocRef));
  }

  /**
   *  Encodes bag of cells (BOC) with builder operations. This method provides the same functionality as Solidity TvmBuilder. Resulting BOC of this method can be passed into Solidity and C++ contracts as TvmCell type.
   *
   * @param builder  Cell builder operations.
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static Boc.ResultOfEncodeBoc encodeBoc(Context ctx, Boc.BuilderOp[] builder,
      Boc.BocCacheType bocCache) throws EverSdkException {
    return ctx.call("boc.encode_boc", new Boc.ParamsOfEncodeBoc(builder, bocCache), Boc.ResultOfEncodeBoc.class);
  }

  /**
   *  Returns the contract code's salt if it is present.
   *
   * @param code  Contract code BOC encoded as base64 or code BOC handle
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static Boc.ResultOfGetCodeSalt getCodeSalt(Context ctx, String code,
      Boc.BocCacheType bocCache) throws EverSdkException {
    return ctx.call("boc.get_code_salt", new Boc.ParamsOfGetCodeSalt(code, bocCache), Boc.ResultOfGetCodeSalt.class);
  }

  /**
   * Returns the new contract code with salt. Sets new salt to contract code.
   *
   * @param code  Contract code BOC encoded as base64 or code BOC handle
   * @param salt BOC encoded as base64 or BOC handle Code salt to set.
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static Boc.ResultOfSetCodeSalt setCodeSalt(Context ctx, String code, String salt,
      Boc.BocCacheType bocCache) throws EverSdkException {
    return ctx.call("boc.set_code_salt", new Boc.ParamsOfSetCodeSalt(code, salt, bocCache), Boc.ResultOfSetCodeSalt.class);
  }

  /**
   *  Decodes tvc into code, data, libraries and special options.
   *
   * @param tvc  Contract TVC image BOC encoded as base64 or BOC handle
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static Boc.ResultOfDecodeTvc decodeTvc(Context ctx, String tvc, Boc.BocCacheType bocCache)
      throws EverSdkException {
    return ctx.call("boc.decode_tvc", new Boc.ParamsOfDecodeTvc(tvc, bocCache), Boc.ResultOfDecodeTvc.class);
  }

  /**
   *  Encodes tvc from code, data, libraries ans special options (see input params)
   *
   * @param code  Contract code BOC encoded as base64 or BOC handle
   * @param data  Contract data BOC encoded as base64 or BOC handle
   * @param library  Contract library BOC encoded as base64 or BOC handle
   * @param tick Specifies the contract ability to handle tick transactions `special.tick` field.
   * @param tock Specifies the contract ability to handle tock transactions `special.tock` field.
   * @param splitDepth  Is present and non-zero only in instances of large smart contracts
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static Boc.ResultOfEncodeTvc encodeTvc(Context ctx, String code, String data,
      String library, Boolean tick, Boolean tock, Integer splitDepth, Boc.BocCacheType bocCache)
      throws EverSdkException {
    return ctx.call("boc.encode_tvc", new Boc.ParamsOfEncodeTvc(code, data, library, tick, tock, splitDepth, bocCache), Boc.ResultOfEncodeTvc.class);
  }

  /**
   * Allows to encode any external inbound message. Encodes a message
   *
   * @param src  Source address.
   * @param dst  Destination address.
   * @param init  Bag of cells with state init (used in deploy messages).
   * @param body  Bag of cells with the message body encoded as base64.
   * @param bocCache The BOC itself returned if no cache type provided Cache type to put the result.
   */
  public static Boc.ResultOfEncodeExternalInMessage encodeExternalInMessage(Context ctx, String src,
      String dst, String init, String body, Boc.BocCacheType bocCache) throws EverSdkException {
    return ctx.call("boc.encode_external_in_message", new Boc.ParamsOfEncodeExternalInMessage(src, dst, init, body, bocCache), Boc.ResultOfEncodeExternalInMessage.class);
  }

  /**
   *  Returns the compiler version used to compile the code.
   *
   * @param code  Contract code BOC encoded as base64 or code BOC handle
   */
  public static Boc.ResultOfGetCompilerVersion getCompilerVersion(Context ctx, String code) throws
      EverSdkException {
    return ctx.call("boc.get_compiler_version", new Boc.ParamsOfGetCompilerVersion(code), Boc.ResultOfGetCompilerVersion.class);
  }

  /**
   * @param boc  BOC encoded as base64 or BOC handle
   */
  public static final record ParamsOfGetBocDepth(String boc) {
  }

  /**
   * @param code  Contract code BOC encoded as base64 or code BOC handle
   */
  public static final record ParamsOfGetCompilerVersion(String code) {
  }

  /**
   * @param code  Contract code BOC encoded as base64 or code BOC handle
   * @param salt BOC encoded as base64 or BOC handle Code salt to set.
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static final record ParamsOfSetCodeSalt(String code, String salt,
      Boc.BocCacheType bocCache) {
  }

  /**
   * @param boc  BOC encoded as base64.
   */
  public static final record ResultOfBocCacheGet(String boc) {
  }

  /**
   * @param tvc  Contract TVC image BOC encoded as base64 or BOC handle
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static final record ParamsOfDecodeTvc(String tvc, Boc.BocCacheType bocCache) {
  }

  /**
   * @param message  Message BOC encoded with `base64`.
   * @param messageId  Message id.
   */
  public static final record ResultOfEncodeExternalInMessage(String message, String messageId) {
  }

  /**
   * @param boc  BOC encoded as base64 or BOC reference
   * @param cacheType  Cache type
   */
  public static final record ParamsOfBocCacheSet(String boc, Boc.BocCacheType cacheType) {
  }

  /**
   * @param code  Contract code BOC encoded as base64 or BOC handle
   * @param data  Contract data BOC encoded as base64 or BOC handle
   * @param library  Contract library BOC encoded as base64 or BOC handle
   * @param tick Specifies the contract ability to handle tick transactions `special.tick` field.
   * @param tock Specifies the contract ability to handle tock transactions `special.tock` field.
   * @param splitDepth  Is present and non-zero only in instances of large smart contracts
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static final record ParamsOfEncodeTvc(String code, String data, String library,
      Boolean tick, Boolean tock, Integer splitDepth, Boc.BocCacheType bocCache) {
  }

  /**
   * @param configBoc  Blockchain config BOC encoded as base64
   */
  public static final record ResultOfGetBlockchainConfig(String configBoc) {
  }

  /**
   * @param code  Contract code BOC encoded as base64 or code BOC handle
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static final record ParamsOfGetCodeSalt(String code, Boc.BocCacheType bocCache) {
  }

  /**
   * @param builder  Cell builder operations.
   * @param bocCache  Cache type to put the result. The BOC itself returned if no cache type provided.
   */
  public static final record ParamsOfEncodeBoc(Boc.BuilderOp[] builder, Boc.BocCacheType bocCache) {
  }

  /**
   * @param src  Source address.
   * @param dst  Destination address.
   * @param init  Bag of cells with state init (used in deploy messages).
   * @param body  Bag of cells with the message body encoded as base64.
   * @param bocCache The BOC itself returned if no cache type provided Cache type to put the result.
   */
  public static final record ParamsOfEncodeExternalInMessage(String src, String dst, String init,
      String body, Boc.BocCacheType bocCache) {
  }

  public enum BocErrorCode {
    InvalidBoc(201),

    SerializationError(202),

    InappropriateBlock(203),

    MissingSourceBoc(204),

    InsufficientCacheSize(205),

    BocRefNotFound(206),

    InvalidBocRef(207);

    private final Integer value;

    BocErrorCode(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer value() {
      return this.value;
    }
  }

  /**
   * @param tvc  Contract TVC image or image BOC handle
   */
  public static final record ParamsOfGetCodeFromTvc(String tvc) {
  }

  /**
   * @param salt BOC encoded as base64 or BOC handle Contract code salt if present.
   */
  public static final record ResultOfGetCodeSalt(String salt) {
  }

  /**
   * @param boc  BOC encoded as base64 or BOC handle
   */
  public static final record ParamsOfGetBocHash(String boc) {
  }

  /**
   * @param blockBoc  Key block BOC or zerostate BOC encoded as base64
   */
  public static final record ParamsOfGetBlockchainConfig(String blockBoc) {
  }

  /**
   * @param bocRef  Reference to the cached BOC
   */
  public static final record ParamsOfBocCacheGet(String bocRef) {
  }

  /**
   * @param depth  BOC root cell depth
   */
  public static final record ResultOfGetBocDepth(Integer depth) {
  }

  /**
   * @param bocRef  Reference to the cached BOC
   */
  public static final record ResultOfBocCacheSet(String bocRef) {
  }

  /**
   * @param boc  BOC encoded as base64
   * @param id  Shardstate identifier
   * @param workchainId  Workchain shardstate belongs to
   */
  public static final record ParamsOfParseShardstate(String boc, String id, Integer workchainId) {
  }

  /**
   * @param code  Contract code BOC encoded as base64 or BOC handle
   * @param codeHash  Contract code hash
   * @param codeDepth  Contract code depth
   * @param data  Contract data BOC encoded as base64 or BOC handle
   * @param dataHash  Contract data hash
   * @param dataDepth  Contract data depth
   * @param library  Contract library BOC encoded as base64 or BOC handle
   * @param tick Specifies the contract ability to handle tick transactions `special.tick` field.
   * @param tock Specifies the contract ability to handle tock transactions `special.tock` field.
   * @param splitDepth  Is present and non-zero only in instances of large smart contracts
   * @param compilerVersion  Compiler version, for example 'sol 0.49.0'
   */
  public static final record ResultOfDecodeTvc(String code, String codeHash, Integer codeDepth,
      String data, String dataHash, Integer dataDepth, String library, Boolean tick, Boolean tock,
      Integer splitDepth, String compilerVersion) {
  }

  /**
   * @param tvc  Contract TVC image BOC encoded as base64 or BOC handle of boc_cache parameter was specified
   */
  public static final record ResultOfEncodeTvc(String tvc) {
  }

  /**
   * @param code BOC encoded as base64 or BOC handle Contract code with salt set.
   */
  public static final record ResultOfSetCodeSalt(String code) {
  }

  /**
   * @param parsed  JSON containing parsed BOC
   */
  public static final record ResultOfParse(Map<String, Object> parsed) {
  }

  /**
   * @param hash  BOC root hash encoded with hex
   */
  public static final record ResultOfGetBocHash(String hash) {
  }

  /**
   * @param boc  Encoded cell BOC or BOC cache key.
   */
  public static final record ResultOfEncodeBoc(String boc) {
  }

  /**
   * @param boc  BOC encoded as base64
   */
  public static final record ParamsOfParse(String boc) {
  }

  /**
   * @param pin  Pinned name
   * @param bocRef If it is provided then only referenced BOC is unpinned Reference to the cached BOC.
   */
  public static final record ParamsOfBocCacheUnpin(String pin, String bocRef) {
  }

  /**
   * @param version  Compiler version, for example 'sol 0.49.0'
   */
  public static final record ResultOfGetCompilerVersion(String version) {
  }

  /**
   * @param code  Contract code encoded as base64
   */
  public static final record ResultOfGetCodeFromTvc(String code) {
  }

  /**
   *  Cell builder operation.
   */
  public sealed interface BuilderOp {
    /**
     *  Append integer to cell data.
     *
     * @param size  Bit size of the value.
     * @param value e.g. `123`, `-123`. - Decimal string. e.g. `"123"`, `"-123"`.
     * - `0x` prefixed hexadecimal string.
     *   e.g `0x123`, `0X123`, `-0x123`. Value: - `Number` containing integer number.
     */
    final record Integer(java.lang.Integer size, String value) implements BuilderOp {
      @JsonProperty("type")
      public String type() {
        return "Integer";
      }
    }

    /**
     *  Append bit string to cell data.
     *
     * @param value Contains hexadecimal string representation:
     * - Can end with `_` tag.
     * - Can be prefixed with `x` or `X`.
     * - Can be prefixed with `x{` or `X{` and ended with `}`.
     *
     * Contains binary string represented as a sequence
     * of `0` and `1` prefixed with `n` or `N`.
     *
     * Examples:
     * `1AB`, `x1ab`, `X1AB`, `x{1abc}`, `X{1ABC}`
     * `2D9_`, `x2D9_`, `X2D9_`, `x{2D9_}`, `X{2D9_}`
     * `n00101101100`, `N00101101100` Bit string content using bitstring notation. See `TON VM specification` 1.0.
     */
    final record BitString(String value) implements BuilderOp {
      @JsonProperty("type")
      public String type() {
        return "BitString";
      }
    }

    /**
     *  Append ref to nested cells.
     *
     * @param builder  Nested cell builder.
     */
    final record Cell(Boc.BuilderOp[] builder) implements BuilderOp {
      @JsonProperty("type")
      public String type() {
        return "Cell";
      }
    }

    /**
     *  Append ref to nested cell.
     *
     * @param boc  Nested cell BOC encoded with `base64` or BOC cache key.
     */
    final record CellBoc(String boc) implements BuilderOp {
      @JsonProperty("type")
      public String type() {
        return "CellBoc";
      }
    }

    /**
     *  Address.
     *
     * @param address  Address in a common `workchain:account` or base64 format.
     */
    final record Address(String address) implements BuilderOp {
      @JsonProperty("type")
      public String type() {
        return "Address";
      }
    }
  }

  public sealed interface BocCacheType {
    /**
     * Such BOC will not be removed from cache until it is unpinned BOCs can have several pins and each of the pins has reference counter indicating how many
     * times the BOC was pinned with the pin. BOC is removed from cache after all references for all
     * pins are unpinned with `cache_unpin` function calls. Pin the BOC with `pin` name.
     */
    final record Pinned(String pin) implements BocCacheType {
      @JsonProperty("type")
      public String type() {
        return "Pinned";
      }
    }

    /**
     * BOC resides there until it is replaced with other BOCs if it is not used BOC is placed into a common BOC pool with limited size regulated by LRU (least recently used) cache lifecycle.
     */
    final record Unpinned() implements BocCacheType {
      @JsonProperty("type")
      public String type() {
        return "Unpinned";
      }
    }
  }
}
