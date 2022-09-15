package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.*;
import java.util.Arrays;

/**
 *  <strong>boc</strong>
 *  Contains methods of "boc" module.

 *  BOC manipulation module.
 *  @version EVER-SDK 1.37.0
 */
public class Boc {

    public interface BocCacheType {


    /**
    * Pin the BOC with `pin` name. Such BOC will not be removed from cache until it is unpinned
    * @param pin 
    */
    public record Pinned(String pin) implements BocCacheType {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }

        public static final Unpinned UNPINNED = new Unpinned();


    /**
    * 

    */
    public record Unpinned() implements BocCacheType {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}

    /**
    * 
    * @param boc BOC encoded as base64
    */
    public record ParamsOfParse(String boc) {}

    /**
    * 
    * @param parsed JSON containing parsed BOC
    */
    public record ResultOfParse(Map<String,Object> parsed) {}

    /**
    * 
    * @param boc BOC encoded as base64
    * @param id Shardstate identificator
    * @param workchainId Workchain shardstate belongs to
    */
    public record ParamsOfParseShardstate(String boc, String id, Number workchainId) {}

    /**
    * 
    * @param blockBoc Key block BOC or zerostate BOC encoded as base64
    */
    public record ParamsOfGetBlockchainConfig(String blockBoc) {}

    /**
    * 
    * @param configBoc Blockchain config BOC encoded as base64
    */
    public record ResultOfGetBlockchainConfig(String configBoc) {}

    /**
    * 
    * @param boc BOC encoded as base64 or BOC handle
    */
    public record ParamsOfGetBocHash(String boc) {}

    /**
    * 
    * @param hash BOC root hash encoded with hex
    */
    public record ResultOfGetBocHash(String hash) {}

    /**
    * 
    * @param boc BOC encoded as base64 or BOC handle
    */
    public record ParamsOfGetBocDepth(String boc) {}

    /**
    * 
    * @param depth BOC root cell depth
    */
    public record ResultOfGetBocDepth(Number depth) {}

    /**
    * 
    * @param tvc Contract TVC image or image BOC handle
    */
    public record ParamsOfGetCodeFromTvc(String tvc) {}

    /**
    * 
    * @param code Contract code encoded as base64
    */
    public record ResultOfGetCodeFromTvc(String code) {}

    /**
    * 
    * @param bocRef Reference to the cached BOC
    */
    public record ParamsOfBocCacheGet(String bocRef) {}

    /**
    * 
    * @param boc BOC encoded as base64.
    */
    public record ResultOfBocCacheGet(String boc) {}

    /**
    * 
    * @param boc BOC encoded as base64 or BOC reference
    * @param cacheType Cache type
    */
    public record ParamsOfBocCacheSet(String boc, BocCacheType cacheType) {}

    /**
    * 
    * @param bocRef Reference to the cached BOC
    */
    public record ResultOfBocCacheSet(String bocRef) {}

    /**
    * 
    * @param pin Pinned name
    * @param bocRef Reference to the cached BOC. If it is provided then only referenced BOC is unpinned
    */
    public record ParamsOfBocCacheUnpin(String pin, String bocRef) {}
    public interface BuilderOp {


    /**
    * Append integer to cell data.
    * @param size Bit size of the value.
    * @param value Value: - `Number` containing integer number. e.g. `123`, `-123`. - Decimal string. e.g. `"123"`, `"-123"`.- `0x` prefixed hexadecimal string.  e.g `0x123`, `0X123`, `-0x123`.
    */
    public record Integer(Number size, String value) implements BuilderOp {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Append bit string to cell data.
    * @param value Bit string content using bitstring notation. See `TON VM specification` 1.0. Contains hexadecimal string representation:- Can end with `_` tag.- Can be prefixed with `x` or `X`.- Can be prefixed with `x{` or `X{` and ended with `}`.<p>Contains binary string represented as a sequenceof `0` and `1` prefixed with `n` or `N`.<p>Examples:`1AB`, `x1ab`, `X1AB`, `x{1abc}`, `X{1ABC}``2D9_`, `x2D9_`, `X2D9_`, `x{2D9_}`, `X{2D9_}``n00101101100`, `N00101101100`
    */
    public record BitString(String value) implements BuilderOp {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Append ref to nested cells.
    * @param builder Nested cell builder.
    */
    public record Cell(BuilderOp[] builder) implements BuilderOp {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Append ref to nested cell.
    * @param boc Nested cell BOC encoded with `base64` or BOC cache key.
    */
    public record CellBoc(String boc) implements BuilderOp {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Address.
    * @param address Address in a common `workchain:account` or base64 format.
    */
    public record Address(String address) implements BuilderOp {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}

    /**
    * 
    * @param builder Cell builder operations.
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided.
    */
    public record ParamsOfEncodeBoc(BuilderOp[] builder, BocCacheType bocCache) {}

    /**
    * 
    * @param boc Encoded cell BOC or BOC cache key.
    */
    public record ResultOfEncodeBoc(String boc) {}

    /**
    * 
    * @param code Contract code BOC encoded as base64 or code BOC handle
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided.
    */
    public record ParamsOfGetCodeSalt(String code, BocCacheType bocCache) {}

    /**
    * 
    * @param salt Contract code salt if present. BOC encoded as base64 or BOC handle
    */
    public record ResultOfGetCodeSalt(String salt) {}

    /**
    * 
    * @param code Contract code BOC encoded as base64 or code BOC handle
    * @param salt Code salt to set. BOC encoded as base64 or BOC handle
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided.
    */
    public record ParamsOfSetCodeSalt(String code, String salt, BocCacheType bocCache) {}

    /**
    * 
    * @param code Contract code with salt set. BOC encoded as base64 or BOC handle
    */
    public record ResultOfSetCodeSalt(String code) {}

    /**
    * 
    * @param tvc Contract TVC image BOC encoded as base64 or BOC handle
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided.
    */
    public record ParamsOfDecodeTvc(String tvc, BocCacheType bocCache) {}

    /**
    * 
    * @param code Contract code BOC encoded as base64 or BOC handle
    * @param codeHash Contract code hash
    * @param codeDepth Contract code depth
    * @param data Contract data BOC encoded as base64 or BOC handle
    * @param dataHash Contract data hash
    * @param dataDepth Contract data depth
    * @param library Contract library BOC encoded as base64 or BOC handle
    * @param tick `special.tick` field. Specifies the contract ability to handle tick transactions
    * @param tock `special.tock` field. Specifies the contract ability to handle tock transactions
    * @param splitDepth Is present and non-zero only in instances of large smart contracts
    * @param compilerVersion Compiler version, for example 'sol 0.49.0'
    */
    public record ResultOfDecodeTvc(String code, String codeHash, Number codeDepth, String data, String dataHash, Number dataDepth, String library, Boolean tick, Boolean tock, Number splitDepth, String compilerVersion) {}

    /**
    * 
    * @param code Contract code BOC encoded as base64 or BOC handle
    * @param data Contract data BOC encoded as base64 or BOC handle
    * @param library Contract library BOC encoded as base64 or BOC handle
    * @param tick `special.tick` field. Specifies the contract ability to handle tick transactions
    * @param tock `special.tock` field. Specifies the contract ability to handle tock transactions
    * @param splitDepth Is present and non-zero only in instances of large smart contracts
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided.
    */
    public record ParamsOfEncodeTvc(String code, String data, String library, Boolean tick, Boolean tock, Number splitDepth, BocCacheType bocCache) {}

    /**
    * 
    * @param tvc Contract TVC image BOC encoded as base64 or BOC handle of boc_cache parameter was specified
    */
    public record ResultOfEncodeTvc(String tvc) {}

    /**
    * 
    * @param src Source address.
    * @param dst Destination address.
    * @param init Bag of cells with state init (used in deploy messages).
    * @param body Bag of cells with the message body encoded as base64.
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided
    */
    public record ParamsOfEncodeExternalInMessage(String src, String dst, String init, String body, BocCacheType bocCache) {}

    /**
    * 
    * @param message Message BOC encoded with `base64`.
    * @param messageId Message id.
    */
    public record ResultOfEncodeExternalInMessage(String message, String messageId) {}

    /**
    * 
    * @param code Contract code BOC encoded as base64 or code BOC handle
    */
    public record ParamsOfGetCompilerVersion(String code) {}

    /**
    * 
    * @param version Compiler version, for example 'sol 0.49.0'
    */
    public record ResultOfGetCompilerVersion(String version) {}
    /**
    * <strong>boc.parse_message</strong>
    * Parses message boc into a JSON JSON structure is compatible with GraphQL API message object
    * @param boc BOC encoded as base64 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfParse}
    */
    public static ResultOfParse parseMessage(Context ctx, String boc) {
        return  ctx.call("boc.parse_message", new ParamsOfParse(boc), ResultOfParse.class);
    }

    /**
    * <strong>boc.parse_transaction</strong>
    * Parses transaction boc into a JSON JSON structure is compatible with GraphQL API transaction object
    * @param boc BOC encoded as base64 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfParse}
    */
    public static ResultOfParse parseTransaction(Context ctx, String boc) {
        return  ctx.call("boc.parse_transaction", new ParamsOfParse(boc), ResultOfParse.class);
    }

    /**
    * <strong>boc.parse_account</strong>
    * Parses account boc into a JSON JSON structure is compatible with GraphQL API account object
    * @param boc BOC encoded as base64 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfParse}
    */
    public static ResultOfParse parseAccount(Context ctx, String boc) {
        return  ctx.call("boc.parse_account", new ParamsOfParse(boc), ResultOfParse.class);
    }

    /**
    * <strong>boc.parse_block</strong>
    * Parses block boc into a JSON JSON structure is compatible with GraphQL API block object
    * @param boc BOC encoded as base64 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfParse}
    */
    public static ResultOfParse parseBlock(Context ctx, String boc) {
        return  ctx.call("boc.parse_block", new ParamsOfParse(boc), ResultOfParse.class);
    }

    /**
    * <strong>boc.parse_shardstate</strong>
    * Parses shardstate boc into a JSON JSON structure is compatible with GraphQL API shardstate object
    * @param boc BOC encoded as base64 
    * @param id Shardstate identificator 
    * @param workchainId Workchain shardstate belongs to 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfParse}
    */
    public static ResultOfParse parseShardstate(Context ctx, String boc, String id, Number workchainId) {
        return  ctx.call("boc.parse_shardstate", new ParamsOfParseShardstate(boc, id, workchainId), ResultOfParse.class);
    }

    /**
    * <strong>boc.get_blockchain_config</strong>
    * Extract blockchain configuration from key block and also from zerostate.
    * @param blockBoc Key block BOC or zerostate BOC encoded as base64 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfGetBlockchainConfig}
    */
    public static ResultOfGetBlockchainConfig getBlockchainConfig(Context ctx, String blockBoc) {
        return  ctx.call("boc.get_blockchain_config", new ParamsOfGetBlockchainConfig(blockBoc), ResultOfGetBlockchainConfig.class);
    }

    /**
    * <strong>boc.get_boc_hash</strong>
    * Calculates BOC root hash
    * @param boc BOC encoded as base64 or BOC handle 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfGetBocHash}
    */
    public static ResultOfGetBocHash getBocHash(Context ctx, String boc) {
        return  ctx.call("boc.get_boc_hash", new ParamsOfGetBocHash(boc), ResultOfGetBocHash.class);
    }

    /**
    * <strong>boc.get_boc_depth</strong>
    * Calculates BOC depth
    * @param boc BOC encoded as base64 or BOC handle 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfGetBocDepth}
    */
    public static ResultOfGetBocDepth getBocDepth(Context ctx, String boc) {
        return  ctx.call("boc.get_boc_depth", new ParamsOfGetBocDepth(boc), ResultOfGetBocDepth.class);
    }

    /**
    * <strong>boc.get_code_from_tvc</strong>
    * Extracts code from TVC contract image
    * @param tvc Contract TVC image or image BOC handle 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfGetCodeFromTvc}
    */
    public static ResultOfGetCodeFromTvc getCodeFromTvc(Context ctx, String tvc) {
        return  ctx.call("boc.get_code_from_tvc", new ParamsOfGetCodeFromTvc(tvc), ResultOfGetCodeFromTvc.class);
    }

    /**
    * <strong>boc.cache_get</strong>
    * Get BOC from cache
    * @param bocRef Reference to the cached BOC 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfBocCacheGet}
    */
    public static ResultOfBocCacheGet cacheGet(Context ctx, String bocRef) {
        return  ctx.call("boc.cache_get", new ParamsOfBocCacheGet(bocRef), ResultOfBocCacheGet.class);
    }

    /**
    * <strong>boc.cache_set</strong>
    * Save BOC into cache
    * @param boc BOC encoded as base64 or BOC reference 
    * @param cacheType Cache type 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfBocCacheSet}
    */
    public static ResultOfBocCacheSet cacheSet(Context ctx, String boc, BocCacheType cacheType) {
        return  ctx.call("boc.cache_set", new ParamsOfBocCacheSet(boc, cacheType), ResultOfBocCacheSet.class);
    }

    /**
    * <strong>boc.cache_unpin</strong>
    * Unpin BOCs with specified pin. BOCs which don't have another pins will be removed from cache
    * @param pin Pinned name 
    * @param bocRef Reference to the cached BOC. If it is provided then only referenced BOC is unpinned
    */
    public static void cacheUnpin(Context ctx, String pin,  String bocRef) {
         ctx.callVoid("boc.cache_unpin", new ParamsOfBocCacheUnpin(pin, bocRef));
    }

    /**
    * <strong>boc.encode_boc</strong>
    * Encodes bag of cells (BOC) with builder operations. This method provides the same functionality as Solidity TvmBuilder. Resulting BOC of this method can be passed into Solidity and C++ contracts as TvmCell type.
    * @param builder Cell builder operations. 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided. 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfEncodeBoc}
    */
    public static ResultOfEncodeBoc encodeBoc(Context ctx, BuilderOp[] builder,  BocCacheType bocCache) {
        return  ctx.call("boc.encode_boc", new ParamsOfEncodeBoc(builder, bocCache), ResultOfEncodeBoc.class);
    }

    /**
    * <strong>boc.get_code_salt</strong>
    * Returns the contract code's salt if it is present.
    * @param code Contract code BOC encoded as base64 or code BOC handle 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided. 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfGetCodeSalt}
    */
    public static ResultOfGetCodeSalt getCodeSalt(Context ctx, String code,  BocCacheType bocCache) {
        return  ctx.call("boc.get_code_salt", new ParamsOfGetCodeSalt(code, bocCache), ResultOfGetCodeSalt.class);
    }

    /**
    * <strong>boc.set_code_salt</strong>
    * Sets new salt to contract code. Returns the new contract code with salt.
    * @param code Contract code BOC encoded as base64 or code BOC handle 
    * @param salt Code salt to set. BOC encoded as base64 or BOC handle
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided. 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfSetCodeSalt}
    */
    public static ResultOfSetCodeSalt setCodeSalt(Context ctx, String code, String salt,  BocCacheType bocCache) {
        return  ctx.call("boc.set_code_salt", new ParamsOfSetCodeSalt(code, salt, bocCache), ResultOfSetCodeSalt.class);
    }

    /**
    * <strong>boc.decode_tvc</strong>
    * Decodes tvc into code, data, libraries and special options.
    * @param tvc Contract TVC image BOC encoded as base64 or BOC handle 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided. 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfDecodeTvc}
    */
    public static ResultOfDecodeTvc decodeTvc(Context ctx, String tvc,  BocCacheType bocCache) {
        return  ctx.call("boc.decode_tvc", new ParamsOfDecodeTvc(tvc, bocCache), ResultOfDecodeTvc.class);
    }

    /**
    * <strong>boc.encode_tvc</strong>
    * Encodes tvc from code, data, libraries ans special options (see input params)
    * @param code Contract code BOC encoded as base64 or BOC handle 
    * @param data Contract data BOC encoded as base64 or BOC handle 
    * @param library Contract library BOC encoded as base64 or BOC handle 
    * @param tick `special.tick` field. Specifies the contract ability to handle tick transactions
    * @param tock `special.tock` field. Specifies the contract ability to handle tock transactions
    * @param splitDepth Is present and non-zero only in instances of large smart contracts 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided. 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfEncodeTvc}
    */
    public static ResultOfEncodeTvc encodeTvc(Context ctx,  String code,  String data,  String library,  Boolean tick,  Boolean tock,  Number splitDepth,  BocCacheType bocCache) {
        return  ctx.call("boc.encode_tvc", new ParamsOfEncodeTvc(code, data, library, tick, tock, splitDepth, bocCache), ResultOfEncodeTvc.class);
    }

    /**
    * <strong>boc.encode_external_in_message</strong>
    * Encodes a message Allows to encode any external inbound message.
    * @param src Source address. 
    * @param dst Destination address. 
    * @param init Bag of cells with state init (used in deploy messages). 
    * @param body Bag of cells with the message body encoded as base64. 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfEncodeExternalInMessage}
    */
    public static ResultOfEncodeExternalInMessage encodeExternalInMessage(Context ctx,  String src, String dst,  String init,  String body,  BocCacheType bocCache) {
        return  ctx.call("boc.encode_external_in_message", new ParamsOfEncodeExternalInMessage(src, dst, init, body, bocCache), ResultOfEncodeExternalInMessage.class);
    }

    /**
    * <strong>boc.get_compiler_version</strong>
    * Returns the compiler version used to compile the code.
    * @param code Contract code BOC encoded as base64 or code BOC handle 
    * @return {@link tech.deplant.java4ever.binding.Boc.ResultOfGetCompilerVersion}
    */
    public static ResultOfGetCompilerVersion getCompilerVersion(Context ctx, String code) {
        return  ctx.call("boc.get_compiler_version", new ParamsOfGetCompilerVersion(code), ResultOfGetCompilerVersion.class);
    }

}
