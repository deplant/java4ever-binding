package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.*;
import java.util.Arrays;

/**
 *  <strong>utils</strong>
 *  Contains methods of "utils" module.

 *  Misc utility Functions.
 *  @version EVER-SDK 1.37.0
 */
public class Utils {

    public interface AddressStringFormat {

        public static final AccountId ACCOUNTID = new AccountId();


    /**
    * 

    */
    public record AccountId() implements AddressStringFormat {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }

        public static final Hex HEX = new Hex();


    /**
    * 

    */
    public record Hex() implements AddressStringFormat {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * 
    * @param url 
    * @param test 
    * @param bounce 
    */
    public record Base64(Boolean url, Boolean test, Boolean bounce) implements AddressStringFormat {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}
    public enum AccountAddressType {
        
        
        AccountId,

        
        Hex,

        
        Base64
    }

    /**
    * 
    * @param address Account address in any TON format.
    * @param outputFormat Specify the format to convert to.
    */
    public record ParamsOfConvertAddress(String address, AddressStringFormat outputFormat) {}

    /**
    * 
    * @param address Address in the specified format
    */
    public record ResultOfConvertAddress(String address) {}

    /**
    * 
    * @param address Account address in any TON format.
    */
    public record ParamsOfGetAddressType(String address) {}

    /**
    * 
    * @param addressType Account address type.
    */
    public record ResultOfGetAddressType(AccountAddressType addressType) {}

    /**
    * 
    * @param account 
    * @param period 
    */
    public record ParamsOfCalcStorageFee(String account, Number period) {}

    /**
    * 
    * @param fee 
    */
    public record ResultOfCalcStorageFee(String fee) {}

    /**
    * 
    * @param uncompressed Uncompressed data. Must be encoded as base64.
    * @param level Compression level, from 1 to 21. Where: 1 - lowest compression level (fastest compression); 21 - highest compression level (slowest compression). If level is omitted, the default compression level is used (currently `3`).
    */
    public record ParamsOfCompressZstd(String uncompressed, Number level) {}

    /**
    * 
    * @param compressed Compressed data. Must be encoded as base64.
    */
    public record ResultOfCompressZstd(String compressed) {}

    /**
    * 
    * @param compressed Compressed data. Must be encoded as base64.
    */
    public record ParamsOfDecompressZstd(String compressed) {}

    /**
    * 
    * @param decompressed Decompressed data. Must be encoded as base64.
    */
    public record ResultOfDecompressZstd(String decompressed) {}
    /**
    * <strong>utils.convert_address</strong>
    * Converts address from any TON format to any TON format
    * @param address Account address in any TON format. 
    * @param outputFormat Specify the format to convert to. 
    * @return {@link tech.deplant.java4ever.binding.Utils.ResultOfConvertAddress}
    */
    public static ResultOfConvertAddress convertAddress(Context ctx, String address, AddressStringFormat outputFormat) throws ExecutionException, JsonProcessingException {
        return  ctx.call("utils.convert_address", new ParamsOfConvertAddress(address, outputFormat), ResultOfConvertAddress.class);
    }

    /**
    * <strong>utils.get_address_type</strong>
    * Validates and returns the type of any TON address. Address types are the following<p>`0:919db8e740d50bf349df2eea03fa30c385d846b991ff5542e67098ee833fc7f7` - standard TON address mostcommonly used in all cases. Also called as hex address`919db8e740d50bf349df2eea03fa30c385d846b991ff5542e67098ee833fc7f7` - account ID. A part of fulladdress. Identifies account inside particular workchain`EQCRnbjnQNUL80nfLuoD+jDDhdhGuZH/VULmcJjugz/H9wam` - base64 address. Also called "user-friendly".Was used at the beginning of TON. Now it is supported for compatibility
    * @param address Account address in any TON format. 
    * @return {@link tech.deplant.java4ever.binding.Utils.ResultOfGetAddressType}
    */
    public static ResultOfGetAddressType getAddressType(Context ctx, String address) throws ExecutionException, JsonProcessingException {
        return  ctx.call("utils.get_address_type", new ParamsOfGetAddressType(address), ResultOfGetAddressType.class);
    }

    /**
    * <strong>utils.calc_storage_fee</strong>
    * Calculates storage fee for an account over a specified time period
    * @param account  
    * @param period  
    * @return {@link tech.deplant.java4ever.binding.Utils.ResultOfCalcStorageFee}
    */
    public static ResultOfCalcStorageFee calcStorageFee(Context ctx, String account, Number period) throws ExecutionException, JsonProcessingException {
        return  ctx.call("utils.calc_storage_fee", new ParamsOfCalcStorageFee(account, period), ResultOfCalcStorageFee.class);
    }

    /**
    * <strong>utils.compress_zstd</strong>
    * Compresses data using Zstandard algorithm
    * @param uncompressed Uncompressed data. Must be encoded as base64.
    * @param level Compression level, from 1 to 21. Where: 1 - lowest compression level (fastest compression); 21 - highest compression level (slowest compression). If level is omitted, the default compression level is used (currently `3`). 
    * @return {@link tech.deplant.java4ever.binding.Utils.ResultOfCompressZstd}
    */
    public static ResultOfCompressZstd compressZstd(Context ctx, String uncompressed,  Number level) throws ExecutionException, JsonProcessingException {
        return  ctx.call("utils.compress_zstd", new ParamsOfCompressZstd(uncompressed, level), ResultOfCompressZstd.class);
    }

    /**
    * <strong>utils.decompress_zstd</strong>
    * Decompresses data using Zstandard algorithm
    * @param compressed Compressed data. Must be encoded as base64.
    * @return {@link tech.deplant.java4ever.binding.Utils.ResultOfDecompressZstd}
    */
    public static ResultOfDecompressZstd decompressZstd(Context ctx, String compressed) throws ExecutionException, JsonProcessingException {
        return  ctx.call("utils.decompress_zstd", new ParamsOfDecompressZstd(compressed), ResultOfDecompressZstd.class);
    }

}
