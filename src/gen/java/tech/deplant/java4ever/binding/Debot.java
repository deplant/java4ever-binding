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
public class Debot {


    /**
     * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Starts the DeBot.
     *
     * @param debotHandle Debot handle which references an instance of debot engine.
     */
    public static CompletableFuture<Void> start(@NonNull Context context, @NonNull Integer debotHandle) {
        return context.future("debot.start", new ParamsOfStart(debotHandle), Void.class);
    }

    /**
     * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Fetches DeBot metadata from blockchain.
     *
     * @param address Debot smart contract address.
     */
    public static CompletableFuture<ResultOfFetch> fetch(@NonNull Context context, @NonNull String address) {
        return context.future("debot.fetch", new ParamsOfFetch(address), ResultOfFetch.class);
    }

    /**
     * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Executes debot action.
     *
     * @param debotHandle Debot handle which references an instance of debot engine.
     * @param action      Debot Action that must be executed.
     */
    public static CompletableFuture<Void> execute(@NonNull Context context, @NonNull Integer debotHandle, @NonNull DebotAction action) {
        return context.future("debot.execute", new ParamsOfExecute(debotHandle, action), Void.class);
    }

    /**
     * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Sends message to Debot.
     *
     * @param debotHandle Debot handle which references an instance of debot engine.
     * @param message     BOC of internal message to debot encoded in base64 format.
     */
    public static CompletableFuture<Void> send(@NonNull Context context, @NonNull Integer debotHandle, @NonNull String message) {
        return context.future("debot.send", new ParamsOfSend(debotHandle, message), Void.class);
    }

    /**
     * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Destroys debot handle.
     *
     * @param debotHandle Debot handle which references an instance of debot engine.
     */
    public static CompletableFuture<Void> remove(@NonNull Context context, @NonNull Integer debotHandle) {
        return context.future("debot.remove", new ParamsOfRemove(debotHandle), Void.class);
    }

    @Value
    public static class DebotAction extends JsonData {

        /**
         * A short action description.
         */
        @SerializedName("description")
        @NonNull String description;

        /**
         * Depends on action type.
         */
        @SerializedName("name")
        @NonNull String name;

        /**
         * Action type.
         */
        @SerializedName("action_type")
        @NonNull Number actionType;

        /**
         * ID of debot context to switch after action execution.
         */
        @SerializedName("to")
        @NonNull Number to;

        /**
         * Action attributes.
         */
        @SerializedName("attributes")
        @NonNull String attributes;

        /**
         * Some internal action data.
         */
        @SerializedName("misc")
        @NonNull String misc;

    }

    @Value
    public static class DebotInfo extends JsonData {
        @SerializedName("name")
        @Getter(AccessLevel.NONE)
        String name;
        @SerializedName("version")
        @Getter(AccessLevel.NONE)
        String version;
        @SerializedName("publisher")
        @Getter(AccessLevel.NONE)
        String publisher;
        @SerializedName("caption")
        @Getter(AccessLevel.NONE)
        String caption;
        @SerializedName("author")
        @Getter(AccessLevel.NONE)
        String author;
        @SerializedName("support")
        @Getter(AccessLevel.NONE)
        String support;
        @SerializedName("hello")
        @Getter(AccessLevel.NONE)
        String hello;
        @SerializedName("language")
        @Getter(AccessLevel.NONE)
        String language;
        @SerializedName("dabi")
        @Getter(AccessLevel.NONE)
        String dabi;
        @SerializedName("icon")
        @Getter(AccessLevel.NONE)
        String icon;
        /**
         * Vector with IDs of DInterfaces used by DeBot.
         */
        @SerializedName("interfaces")
        @NonNull String[] interfaces;
        /**
         * ABI version ("x.y") supported by DeBot
         */
        @SerializedName("dabiVersion")
        @NonNull String dabiVersion;

        /**
         * DeBot short name.
         */
        public Optional<String> name() {
            return Optional.ofNullable(this.name);
        }

        /**
         * DeBot semantic version.
         */
        public Optional<String> version() {
            return Optional.ofNullable(this.version);
        }

        /**
         * The name of DeBot deployer.
         */
        public Optional<String> publisher() {
            return Optional.ofNullable(this.publisher);
        }

        /**
         * Short info about DeBot.
         */
        public Optional<String> caption() {
            return Optional.ofNullable(this.caption);
        }

        /**
         * The name of DeBot developer.
         */
        public Optional<String> author() {
            return Optional.ofNullable(this.author);
        }

        /**
         * TON address of author for questions and donations.
         */
        public Optional<String> support() {
            return Optional.ofNullable(this.support);
        }

        /**
         * String with the first messsage from DeBot.
         */
        public Optional<String> hello() {
            return Optional.ofNullable(this.hello);
        }

        /**
         * String with DeBot interface language (ISO-639).
         */
        public Optional<String> language() {
            return Optional.ofNullable(this.language);
        }

        /**
         * String with DeBot ABI.
         */
        public Optional<String> dabi() {
            return Optional.ofNullable(this.dabi);
        }

        /**
         * DeBot icon.
         */
        public Optional<String> icon() {
            return Optional.ofNullable(this.icon);
        }

    }

    @Value
    public static class ParamsOfInit extends JsonData {

        /**
         * Debot smart contract address
         */
        @SerializedName("address")
        @NonNull String address;

    }

    @Value
    public static class RegisteredDebot extends JsonData {

        /**
         * Debot handle which references an instance of debot engine.
         */
        @SerializedName("debot_handle")
        @NonNull Integer debotHandle;

        /**
         * Debot abi as json string.
         */
        @SerializedName("debot_abi")
        @NonNull String debotAbi;

        /**
         * Debot metadata.
         */
        @SerializedName("info")
        @NonNull DebotInfo info;

    }

    public static abstract class ParamsOfAppDebotBrowser {


        public static final SwitchCompleted SwitchCompleted = new SwitchCompleted();
        public static final GetSigningBox GetSigningBox = new GetSigningBox();

        @Value
        public static class Log extends ParamsOfAppDebotBrowser {

            /**
             * A string that must be printed to user.
             */
            @SerializedName("msg")
            @NonNull String msg;

        }

        @Value
        public static class Switch extends ParamsOfAppDebotBrowser {

            /**
             * Debot context ID to which debot is switched.
             */
            @SerializedName("context_id")
            @NonNull Number contextId;

        }

        @Value
        public static class SwitchCompleted extends ParamsOfAppDebotBrowser {

        }

        @Value
        public static class ShowAction extends ParamsOfAppDebotBrowser {

            /**
             * Debot action that must be shown to user as menu item. At least `description` property must be shown from <a target="_blank" href="DebotAction">DebotAction</a> structure.
             */
            @SerializedName("action")
            @NonNull DebotAction action;

        }

        @Value
        public static class Input extends ParamsOfAppDebotBrowser {

            /**
             * A prompt string that must be printed to user before input request.
             */
            @SerializedName("prompt")
            @NonNull String prompt;

        }

        /**
         * Signing box returned is owned and disposed by debot engine
         */
        @Value
        public static class GetSigningBox extends ParamsOfAppDebotBrowser {

        }


        @Value
        public static class InvokeDebot extends ParamsOfAppDebotBrowser {

            /**
             * Address of debot in blockchain.
             */
            @SerializedName("debot_addr")
            @NonNull String debotAddr;

            /**
             * Debot action to execute.
             */
            @SerializedName("action")
            @NonNull DebotAction action;

        }


        @Value
        public static class Send extends ParamsOfAppDebotBrowser {

            /**
             * Internal message to DInterface address.
             */
            @SerializedName("message")
            @NonNull String message;

        }


        @Value
        public static class Approve extends ParamsOfAppDebotBrowser {

            /**
             * DeBot activity details.
             */
            @SerializedName("activity")
            @NonNull Map<String, Object> activity;

        }
    }

    public static abstract class ResultOfAppDebotBrowser {


        public static final InvokeDebot InvokeDebot = new InvokeDebot();

        @Value
        public static class Input extends ResultOfAppDebotBrowser {

            /**
             * String entered by user.
             */
            @SerializedName("value")
            @NonNull String value;

        }

        @Value
        public static class GetSigningBox extends ResultOfAppDebotBrowser {

            /**
             * Signing box for signing data requested by debot engine.
             */
            @SerializedName("signing_box")
            @NonNull Integer signingBox;

        }

        @Value
        public static class InvokeDebot extends ResultOfAppDebotBrowser {

        }


        @Value
        public static class Approve extends ResultOfAppDebotBrowser {

            /**
             * Indicates whether the DeBot is allowed to perform the specified operation.
             */
            @SerializedName("approved")
            @NonNull Boolean approved;

        }
    }

    @Value
    public static class ParamsOfStart extends JsonData {

        /**
         * Debot handle which references an instance of debot engine.
         */
        @SerializedName("debot_handle")
        @NonNull Integer debotHandle;

    }
//   /**
//    * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Creates and instance of DeBot.
//    * @param address Debot smart contract address
//    * @param appObject
//    */
//    public static CompletableFuture<RegisteredDebot> init(@NonNull Context context, @NonNull String address,  AppDebotBrowser appObject) {
//        return Module.futureCallback("debot.init", context, new ParamsOfInit(address, appObject), RegisteredDebot.class);
//    }

    @Value
    public static class ParamsOfFetch extends JsonData {

        /**
         * Debot smart contract address.
         */
        @SerializedName("address")
        @NonNull String address;

    }

    @Value
    public static class ResultOfFetch extends JsonData {

        /**
         * Debot metadata.
         */
        @SerializedName("info")
        @NonNull DebotInfo info;

    }

    @Value
    public static class ParamsOfExecute extends JsonData {

        /**
         * Debot handle which references an instance of debot engine.
         */
        @SerializedName("debot_handle")
        @NonNull Integer debotHandle;

        /**
         * Debot Action that must be executed.
         */
        @SerializedName("action")
        @NonNull DebotAction action;

    }

    @Value
    public static class ParamsOfSend extends JsonData {

        /**
         * Debot handle which references an instance of debot engine.
         */
        @SerializedName("debot_handle")
        @NonNull Integer debotHandle;

        /**
         * BOC of internal message to debot encoded in base64 format.
         */
        @SerializedName("message")
        @NonNull String message;

    }

    @Value
    public static class ParamsOfRemove extends JsonData {

        /**
         * Debot handle which references an instance of debot engine.
         */
        @SerializedName("debot_handle")
        @NonNull Integer debotHandle;

    }

}
