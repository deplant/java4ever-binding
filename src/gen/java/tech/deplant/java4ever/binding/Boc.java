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
 *  <h1>Module "boc"</h1>
 *  BOC manipulation module.
 *  @version EVER-SDK 1.34.2
 */
public class Boc {

    public interface BocCacheType {


    /**
    * Pin the BOC with `pin` name. Such BOC will not be removed from cache until it is unpinned
    * @param pin 
    */
    public record Pinned(@NonNull String pin) implements BocCacheType {}

        public static final Unpinned Unpinned = new Unpinned();


    /**
    * 

    */
    public record Unpinned() implements BocCacheType {}
}

    /**
    * 
    * @param boc BOC encoded as base64
    */
    public record ParamsOfParse(@NonNull String boc) {}

    /**
    * 
    * @param parsed JSON containing parsed BOC
    */
    public record ResultOfParse(@NonNull Map<String,Object> parsed) {}

    /**
    * 
    * @param boc BOC encoded as base64
    * @param id Shardstate identificator
    * @param workchainId Workchain shardstate belongs to
    */
    public record ParamsOfParseShardstate(@NonNull String boc, @NonNull String id, @NonNull Number workchainId) {}

    /**
    * 
    * @param blockBoc Key block BOC or zerostate BOC encoded as base64
    */
    public record ParamsOfGetBlockchainConfig(@NonNull String blockBoc) {}

    /**
    * 
    * @param configBoc Blockchain config BOC encoded as base64
    */
    public record ResultOfGetBlockchainConfig(@NonNull String configBoc) {}

    /**
    * 
    * @param boc BOC encoded as base64 or BOC handle
    */
    public record ParamsOfGetBocHash(@NonNull String boc) {}

    /**
    * 
    * @param hash BOC root hash encoded with hex
    */
    public record ResultOfGetBocHash(@NonNull String hash) {}

    /**
    * 
    * @param boc BOC encoded as base64 or BOC handle
    */
    public record ParamsOfGetBocDepth(@NonNull String boc) {}

    /**
    * 
    * @param depth BOC root cell depth
    */
    public record ResultOfGetBocDepth(@NonNull Number depth) {}

    /**
    * 
    * @param tvc Contract TVC image or image BOC handle
    */
    public record ParamsOfGetCodeFromTvc(@NonNull String tvc) {}

    /**
    * 
    * @param code Contract code encoded as base64
    */
    public record ResultOfGetCodeFromTvc(@NonNull String code) {}

    /**
    * 
    * @param bocRef Reference to the cached BOC
    */
    public record ParamsOfBocCacheGet(@NonNull String bocRef) {}

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
    public record ParamsOfBocCacheSet(@NonNull String boc, @NonNull BocCacheType cacheType) {}

    /**
    * 
    * @param bocRef Reference to the cached BOC
    */
    public record ResultOfBocCacheSet(@NonNull String bocRef) {}

    /**
    * 
    * @param pin Pinned name
    * @param bocRef Reference to the cached BOC. If it is provided then only referenced BOC is unpinned
    */
    public record ParamsOfBocCacheUnpin(@NonNull String pin, String bocRef) {}
    public interface BuilderOp {


    /**
    * Append integer to cell data.
    * @param size Bit size of the value.
    * @param value Value: - `Number` containing integer number. e.g. `123`, `-123`. - Decimal string. e.g. `"123"`, `"-123"`.- `0x` prefixed hexadecimal string.  e.g `0x123`, `0X123`, `-0x123`.
    */
    public record Integer(@NonNull Number size, @NonNull Map<String,Object> value) implements BuilderOp {}


    /**
    * Append bit string to cell data.
    * @param value Bit string content using bitstring notation. See `TON VM specification` 1.0. Contains hexadecimal string representation:- Can end with `_` tag.- Can be prefixed with `x` or `X`.- Can be prefixed with `x{` or `X{` and ended with `}`.<p>Contains binary string represented as a sequenceof `0` and `1` prefixed with `n` or `N`.<p>Examples:`1AB`, `x1ab`, `X1AB`, `x{1abc}`, `X{1ABC}``2D9_`, `x2D9_`, `X2D9_`, `x{2D9_}`, `X{2D9_}``n00101101100`, `N00101101100`
    */
    public record BitString(@NonNull String value) implements BuilderOp {}


    /**
    * Append ref to nested cells.
    * @param builder Nested cell builder.
    */
    public record Cell(@NonNull BuilderOp[] builder) implements BuilderOp {}


    /**
    * Append ref to nested cell.
    * @param boc Nested cell BOC encoded with `base64` or BOC cache key.
    */
    public record CellBoc(@NonNull String boc) implements BuilderOp {}


    /**
    * Address.
    * @param address Address in a common `workchain:account` or base64 format.
    */
    public record Address(@NonNull String address) implements BuilderOp {}
}

    /**
    * 
    * @param builder Cell builder operations.
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided.
    */
    public record ParamsOfEncodeBoc(@NonNull BuilderOp[] builder, BocCacheType bocCache) {}

    /**
    * 
    * @param boc Encoded cell BOC or BOC cache key.
    */
    public record ResultOfEncodeBoc(@NonNull String boc) {}

    /**
    * 
    * @param code Contract code BOC encoded as base64 or code BOC handle
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided.
    */
    public record ParamsOfGetCodeSalt(@NonNull String code, BocCacheType bocCache) {}

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
    public record ParamsOfSetCodeSalt(@NonNull String code, @NonNull String salt, BocCacheType bocCache) {}

    /**
    * 
    * @param code Contract code with salt set. BOC encoded as base64 or BOC handle
    */
    public record ResultOfSetCodeSalt(@NonNull String code) {}

    /**
    * 
    * @param tvc Contract TVC image BOC encoded as base64 or BOC handle
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided.
    */
    public record ParamsOfDecodeTvc(@NonNull String tvc, BocCacheType bocCache) {}

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
    public record ResultOfEncodeTvc(@NonNull String tvc) {}

    /**
    * 
    * @param src Source address.
    * @param dst Destination address.
    * @param init Bag of cells with state init (used in deploy messages).
    * @param body Bag of cells with the message body encoded as base64.
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided
    */
    public record ParamsOfEncodeExternalInMessage(String src, @NonNull String dst, String init, String body, BocCacheType bocCache) {}

    /**
    * 
    * @param message Message BOC encoded with `base64`.
    * @param messageId Message id.
    */
    public record ResultOfEncodeExternalInMessage(@NonNull String message, @NonNull String messageId) {}

    /**
    * 
    * @param code Contract code BOC encoded as base64 or code BOC handle
    */
    public record ParamsOfGetCompilerVersion(@NonNull String code) {}

    /**
    * 
    * @param version Compiler version, for example 'sol 0.49.0'
    */
    public record ResultOfGetCompilerVersion(String version) {}
    /**
    * <h2>boc.parse_message</h2>
    * Parses message boc into a JSON JSON structure is compatible with GraphQL API message object
    * @param boc BOC encoded as base64 
    */
    public static CompletableFuture<ResultOfParse> parseMessage(@NonNull Context context, @NonNull String boc)  throws JsonProcessingException {
        return context.future("boc.parse_message", new ParamsOfParse(boc), ResultOfParse.class);
    }

    /**
    * <h2>boc.parse_transaction</h2>
    * Parses transaction boc into a JSON JSON structure is compatible with GraphQL API transaction object
    * @param boc BOC encoded as base64 
    */
    public static CompletableFuture<ResultOfParse> parseTransaction(@NonNull Context context, @NonNull String boc)  throws JsonProcessingException {
        return context.future("boc.parse_transaction", new ParamsOfParse(boc), ResultOfParse.class);
    }

    /**
    * <h2>boc.parse_account</h2>
    * Parses account boc into a JSON JSON structure is compatible with GraphQL API account object
    * @param boc BOC encoded as base64 
    */
    public static CompletableFuture<ResultOfParse> parseAccount(@NonNull Context context, @NonNull String boc)  throws JsonProcessingException {
        return context.future("boc.parse_account", new ParamsOfParse(boc), ResultOfParse.class);
    }

    /**
    * <h2>boc.parse_block</h2>
    * Parses block boc into a JSON JSON structure is compatible with GraphQL API block object
    * @param boc BOC encoded as base64 
    */
    public static CompletableFuture<ResultOfParse> parseBlock(@NonNull Context context, @NonNull String boc)  throws JsonProcessingException {
        return context.future("boc.parse_block", new ParamsOfParse(boc), ResultOfParse.class);
    }

    /**
    * <h2>boc.parse_shardstate</h2>
    * Parses shardstate boc into a JSON JSON structure is compatible with GraphQL API shardstate object
    * @param boc BOC encoded as base64 
    * @param id Shardstate identificator 
    * @param workchainId Workchain shardstate belongs to 
    */
    public static CompletableFuture<ResultOfParse> parseShardstate(@NonNull Context context, @NonNull String boc, @NonNull String id, @NonNull Number workchainId)  throws JsonProcessingException {
        return context.future("boc.parse_shardstate", new ParamsOfParseShardstate(boc, id, workchainId), ResultOfParse.class);
    }

    /**
    * <h2>boc.get_blockchain_config</h2>
    * Extract blockchain configuration from key block and also from zerostate.
    * @param blockBoc Key block BOC or zerostate BOC encoded as base64 
    */
    public static CompletableFuture<ResultOfGetBlockchainConfig> getBlockchainConfig(@NonNull Context context, @NonNull String blockBoc)  throws JsonProcessingException {
        return context.future("boc.get_blockchain_config", new ParamsOfGetBlockchainConfig(blockBoc), ResultOfGetBlockchainConfig.class);
    }

    /**
    * <h2>boc.get_boc_hash</h2>
    * Calculates BOC root hash
    * @param boc BOC encoded as base64 or BOC handle 
    */
    public static CompletableFuture<ResultOfGetBocHash> getBocHash(@NonNull Context context, @NonNull String boc)  throws JsonProcessingException {
        return context.future("boc.get_boc_hash", new ParamsOfGetBocHash(boc), ResultOfGetBocHash.class);
    }

    /**
    * <h2>boc.get_boc_depth</h2>
    * Calculates BOC depth
    * @param boc BOC encoded as base64 or BOC handle 
    */
    public static CompletableFuture<ResultOfGetBocDepth> getBocDepth(@NonNull Context context, @NonNull String boc)  throws JsonProcessingException {
        return context.future("boc.get_boc_depth", new ParamsOfGetBocDepth(boc), ResultOfGetBocDepth.class);
    }

    /**
    * <h2>boc.get_code_from_tvc</h2>
    * Extracts code from TVC contract image
    * @param tvc Contract TVC image or image BOC handle 
    */
    public static CompletableFuture<ResultOfGetCodeFromTvc> getCodeFromTvc(@NonNull Context context, @NonNull String tvc)  throws JsonProcessingException {
        return context.future("boc.get_code_from_tvc", new ParamsOfGetCodeFromTvc(tvc), ResultOfGetCodeFromTvc.class);
    }

    /**
    * <h2>boc.cache_get</h2>
    * Get BOC from cache
    * @param bocRef Reference to the cached BOC 
    */
    public static CompletableFuture<ResultOfBocCacheGet> cacheGet(@NonNull Context context, @NonNull String bocRef)  throws JsonProcessingException {
        return context.future("boc.cache_get", new ParamsOfBocCacheGet(bocRef), ResultOfBocCacheGet.class);
    }

    /**
    * <h2>boc.cache_set</h2>
    * Save BOC into cache
    * @param boc BOC encoded as base64 or BOC reference 
    * @param cacheType Cache type 
    */
    public static CompletableFuture<ResultOfBocCacheSet> cacheSet(@NonNull Context context, @NonNull String boc, @NonNull BocCacheType cacheType)  throws JsonProcessingException {
        return context.future("boc.cache_set", new ParamsOfBocCacheSet(boc, cacheType), ResultOfBocCacheSet.class);
    }

    /**
    * <h2>boc.cache_unpin</h2>
    * Unpin BOCs with specified pin. BOCs which don't have another pins will be removed from cache
    * @param pin Pinned name 
    * @param bocRef Reference to the cached BOC. If it is provided then only referenced BOC is unpinned
    */
    public static CompletableFuture<Void> cacheUnpin(@NonNull Context context, @NonNull String pin,  String bocRef)  throws JsonProcessingException {
        return context.future("boc.cache_unpin", new ParamsOfBocCacheUnpin(pin, bocRef), Void.class);
    }

    /**
    * <h2>boc.encode_boc</h2>
    * Encodes bag of cells (BOC) with builder operations. This method provides the same functionality as Solidity TvmBuilder. Resulting BOC of this method can be passed into Solidity and C++ contracts as TvmCell type.
    * @param builder Cell builder operations. 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided. 
    */
    public static CompletableFuture<ResultOfEncodeBoc> encodeBoc(@NonNull Context context, @NonNull BuilderOp[] builder,  BocCacheType bocCache)  throws JsonProcessingException {
        return context.future("boc.encode_boc", new ParamsOfEncodeBoc(builder, bocCache), ResultOfEncodeBoc.class);
    }

    /**
    * <h2>boc.get_code_salt</h2>
    * Returns the contract code's salt if it is present.
    * @param code Contract code BOC encoded as base64 or code BOC handle 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided. 
    */
    public static CompletableFuture<ResultOfGetCodeSalt> getCodeSalt(@NonNull Context context, @NonNull String code,  BocCacheType bocCache)  throws JsonProcessingException {
        return context.future("boc.get_code_salt", new ParamsOfGetCodeSalt(code, bocCache), ResultOfGetCodeSalt.class);
    }

    /**
    * <h2>boc.set_code_salt</h2>
    * Sets new salt to contract code. Returns the new contract code with salt.
    * @param code Contract code BOC encoded as base64 or code BOC handle 
    * @param salt Code salt to set. BOC encoded as base64 or BOC handle
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided. 
    */
    public static CompletableFuture<ResultOfSetCodeSalt> setCodeSalt(@NonNull Context context, @NonNull String code, @NonNull String salt,  BocCacheType bocCache)  throws JsonProcessingException {
        return context.future("boc.set_code_salt", new ParamsOfSetCodeSalt(code, salt, bocCache), ResultOfSetCodeSalt.class);
    }

    /**
    * <h2>boc.decode_tvc</h2>
    * Decodes tvc into code, data, libraries and special options.
    * @param tvc Contract TVC image BOC encoded as base64 or BOC handle 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided. 
    */
    public static CompletableFuture<ResultOfDecodeTvc> decodeTvc(@NonNull Context context, @NonNull String tvc,  BocCacheType bocCache)  throws JsonProcessingException {
        return context.future("boc.decode_tvc", new ParamsOfDecodeTvc(tvc, bocCache), ResultOfDecodeTvc.class);
    }

    /**
    * <h2>boc.encode_tvc</h2>
    * Encodes tvc from code, data, libraries ans special options (see input params)
    * @param code Contract code BOC encoded as base64 or BOC handle 
    * @param data Contract data BOC encoded as base64 or BOC handle 
    * @param library Contract library BOC encoded as base64 or BOC handle 
    * @param tick `special.tick` field. Specifies the contract ability to handle tick transactions
    * @param tock `special.tock` field. Specifies the contract ability to handle tock transactions
    * @param splitDepth Is present and non-zero only in instances of large smart contracts 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided. 
    */
    public static CompletableFuture<ResultOfEncodeTvc> encodeTvc(@NonNull Context context,  String code,  String data,  String library,  Boolean tick,  Boolean tock,  Number splitDepth,  BocCacheType bocCache)  throws JsonProcessingException {
        return context.future("boc.encode_tvc", new ParamsOfEncodeTvc(code, data, library, tick, tock, splitDepth, bocCache), ResultOfEncodeTvc.class);
    }

    /**
    * <h2>boc.encode_external_in_message</h2>
    * Encodes a message Allows to encode any external inbound message.
    * @param src Source address. 
    * @param dst Destination address. 
    * @param init Bag of cells with state init (used in deploy messages). 
    * @param body Bag of cells with the message body encoded as base64. 
    * @param bocCache Cache type to put the result. The BOC itself returned if no cache type provided
    */
    public static CompletableFuture<ResultOfEncodeExternalInMessage> encodeExternalInMessage(@NonNull Context context,  String src, @NonNull String dst,  String init,  String body,  BocCacheType bocCache)  throws JsonProcessingException {
        return context.future("boc.encode_external_in_message", new ParamsOfEncodeExternalInMessage(src, dst, init, body, bocCache), ResultOfEncodeExternalInMessage.class);
    }

    /**
    * <h2>boc.get_compiler_version</h2>
    * Returns the compiler version used to compile the code.
    * @param code Contract code BOC encoded as base64 or code BOC handle 
    */
    public static CompletableFuture<ResultOfGetCompilerVersion> getCompilerVersion(@NonNull Context context, @NonNull String code)  throws JsonProcessingException {
        return context.future("boc.get_compiler_version", new ParamsOfGetCompilerVersion(code), ResultOfGetCompilerVersion.class);
    }

}
