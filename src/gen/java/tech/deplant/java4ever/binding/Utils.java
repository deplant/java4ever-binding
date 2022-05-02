package tech.deplant.java4ever.binding;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import tech.deplant.java4ever.binding.json.JsonData;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
public class Utils {

    /**
     * Converts address from any TON format to any TON format
     *
     * @param address      Account address in any TON format.
     * @param outputFormat Specify the format to convert to.
     */
    public static CompletableFuture<ResultOfConvertAddress> convertAddress(@NonNull Context context, @NonNull String address, @NonNull AddressStringFormat outputFormat) {
        return context.future("utils.convert_address", new ParamsOfConvertAddress(address, outputFormat), ResultOfConvertAddress.class);
    }

    /**
     * Validates and returns the type of any TON address.
     *
     * @param address Account address in any TON format.
     */
    public static CompletableFuture<ResultOfGetAddressType> getAddressType(@NonNull Context context, @NonNull String address) {
        return context.future("utils.get_address_type", new ParamsOfGetAddressType(address), ResultOfGetAddressType.class);
    }

    /**
     * Calculates storage fee for an account over a specified time period
     *
     * @param account
     * @param period
     */
    public static CompletableFuture<ResultOfCalcStorageFee> calcStorageFee(@NonNull Context context, @NonNull String account, @NonNull Number period) {
        return context.future("utils.calc_storage_fee", new ParamsOfCalcStorageFee(account, period), ResultOfCalcStorageFee.class);
    }

    /**
     * Compresses data using Zstandard algorithm
     *
     * @param uncompressed Uncompressed data. Must be encoded as base64.
     * @param level        Compression level, from 1 to 21. Where: 1 - lowest compression level (fastest compression); 21 - highest compression level (slowest compression). If level is omitted, the default compression level is used (currently `3`).
     */
    public static CompletableFuture<ResultOfCompressZstd> compressZstd(@NonNull Context context, @NonNull String uncompressed, Number level) {
        return context.future("utils.compress_zstd", new ParamsOfCompressZstd(uncompressed, level), ResultOfCompressZstd.class);
    }

    /**
     * Decompresses data using Zstandard algorithm
     *
     * @param compressed Compressed data. Must be encoded as base64.
     */
    public static CompletableFuture<ResultOfDecompressZstd> decompressZstd(@NonNull Context context, @NonNull String compressed) {
        return context.future("utils.decompress_zstd", new ParamsOfDecompressZstd(compressed), ResultOfDecompressZstd.class);
    }

    /**
     *
     */
    public enum AccountAddressType {

        /**
         *
         */
        AccountId,

        /**
         *
         */
        Hex,

        /**
         *
         */
        Base64
    }

    public static abstract class AddressStringFormat {

        public static final AccountId AccountId = new AccountId();
        public static final Hex Hex = new Hex();

        @Value
        public static class AccountId extends AddressStringFormat {

        }

        @Value
        public static class Hex extends AddressStringFormat {

        }


        @Value
        public static class Base64 extends AddressStringFormat {
            @SerializedName("url")
            @NonNull Boolean url;
            @SerializedName("test")
            @NonNull Boolean test;
            @SerializedName("bounce")
            @NonNull Boolean bounce;

        }
    }

    @Value
    public static class ParamsOfConvertAddress extends JsonData {

        /**
         * Account address in any TON format.
         */
        @SerializedName("address")
        @NonNull String address;

        /**
         * Specify the format to convert to.
         */
        @SerializedName("output_format")
        @NonNull AddressStringFormat outputFormat;

    }

    @Value
    public static class ResultOfConvertAddress extends JsonData {

        /**
         * Address in the specified format
         */
        @SerializedName("address")
        @NonNull String address;

    }

    @Value
    public static class ParamsOfGetAddressType extends JsonData {

        /**
         * Account address in any TON format.
         */
        @SerializedName("address")
        @NonNull String address;

    }

    @Value
    public static class ResultOfGetAddressType extends JsonData {

        /**
         * Account address type.
         */
        @SerializedName("address_type")
        @NonNull AccountAddressType addressType;

    }

    @Value
    public static class ParamsOfCalcStorageFee extends JsonData {
        @SerializedName("account")
        @NonNull String account;
        @SerializedName("period")
        @NonNull Number period;

    }

    @Value
    public static class ResultOfCalcStorageFee extends JsonData {
        @SerializedName("fee")
        @NonNull String fee;

    }

    @Value
    public static class ParamsOfCompressZstd extends JsonData {

        /**
         * Uncompressed data.
         */
        @SerializedName("uncompressed")
        @NonNull String uncompressed;
        @SerializedName("level")
        @Getter(AccessLevel.NONE)
        Number level;

        /**
         * Compression level, from 1 to 21. Where: 1 - lowest compression level (fastest compression); 21 - highest compression level (slowest compression). If level is omitted, the default compression level is used (currently `3`).
         */
        public Optional<Number> level() {
            return Optional.ofNullable(this.level);
        }

    }

    @Value
    public static class ResultOfCompressZstd extends JsonData {

        /**
         * Compressed data.
         */
        @SerializedName("compressed")
        @NonNull String compressed;

    }

    @Value
    public static class ParamsOfDecompressZstd extends JsonData {

        /**
         * Compressed data.
         */
        @SerializedName("compressed")
        @NonNull String compressed;

    }

    @Value
    public static class ResultOfDecompressZstd extends JsonData {

        /**
         * Decompressed data.
         */
        @SerializedName("decompressed")
        @NonNull String decompressed;

    }

}
