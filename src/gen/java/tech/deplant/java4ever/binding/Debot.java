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
 *  <h1>debot</h1>
 *  Contains methods of "debot" module.

 *  <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Module for working with debot.
 *  @version EVER-SDK 1.34.2
 */
public class Debot {


    /**
    * 
    * @param description A short action description. Should be used by Debot Browser as name of menu item.
    * @param name Depends on action type. Can be a debot function name or a print string (for Print Action).
    * @param actionType Action type.
    * @param to ID of debot context to switch after action execution.
    * @param attributes Action attributes. In the form of "param=value,flag". attribute example: instant, args, fargs, sign.
    * @param misc Some internal action data. Used by debot only.
    */
    public record DebotAction(@NonNull String description, @NonNull String name, @NonNull Number actionType, @NonNull Number to, @NonNull String attributes, @NonNull String misc) {}

    /**
    * 
    * @param name DeBot short name.
    * @param version DeBot semantic version.
    * @param publisher The name of DeBot deployer.
    * @param caption Short info about DeBot.
    * @param author The name of DeBot developer.
    * @param support TON address of author for questions and donations.
    * @param hello String with the first messsage from DeBot.
    * @param language String with DeBot interface language (ISO-639).
    * @param dabi String with DeBot ABI.
    * @param icon DeBot icon.
    * @param interfaces Vector with IDs of DInterfaces used by DeBot.
    * @param dabiVersion ABI version ("x.y") supported by DeBot
    */
    public record DebotInfo(String name, String version, String publisher, String caption, String author, String support, String hello, String language, String dabi, String icon, @NonNull String[] interfaces, @NonNull String dabiVersion) {}

    /**
    * 
    * @param address Debot smart contract address
    */
    public record ParamsOfInit(@NonNull String address) {}

    /**
    * 
    * @param debotHandle Debot handle which references an instance of debot engine.
    * @param debotAbi Debot abi as json string.
    * @param info Debot metadata.
    */
    public record RegisteredDebot(@NonNull Integer debotHandle, @NonNull String debotAbi, @NonNull DebotInfo info) {}
    public interface ParamsOfAppDebotBrowser {


    /**
    * Print message to user.
    * @param msg A string that must be printed to user.
    */
    public record Log(@NonNull String msg) implements ParamsOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Switch debot to another context (menu).
    * @param contextId Debot context ID to which debot is switched.
    */
    public record Switch(@NonNull Number contextId) implements ParamsOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }

        public static final SwitchCompleted SWITCHCOMPLETED = new SwitchCompleted();


    /**
    * Notify browser that all context actions are shown.

    */
    public record SwitchCompleted() implements ParamsOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Show action to the user. Called after `switch` for each action in context.
    * @param action Debot action that must be shown to user as menu item. At least `description` property must be shown from <a target="_blank" href="DebotAction">DebotAction</a> structure.
    */
    public record ShowAction(@NonNull DebotAction action) implements ParamsOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Request user input.
    * @param prompt A prompt string that must be printed to user before input request.
    */
    public record Input(@NonNull String prompt) implements ParamsOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }

        public static final GetSigningBox GETSIGNINGBOX = new GetSigningBox();


    /**
    * Get signing box to sign data. Signing box returned is owned and disposed by debot engine

    */
    public record GetSigningBox() implements ParamsOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Execute action of another debot.
    * @param debotAddr Address of debot in blockchain.
    * @param action Debot action to execute.
    */
    public record InvokeDebot(@NonNull String debotAddr, @NonNull DebotAction action) implements ParamsOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Used by Debot to call DInterface implemented by Debot Browser.
    * @param message Internal message to DInterface address. Message body contains interface function and parameters.
    */
    public record Send(@NonNull String message) implements ParamsOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Requests permission from DeBot Browser to execute DeBot operation.
    * @param activity DeBot activity details.
    */
    public record Approve(@NonNull Map<String,Object> activity) implements ParamsOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}
    public interface ResultOfAppDebotBrowser {


    /**
    * Result of user input.
    * @param value String entered by user.
    */
    public record Input(@NonNull String value) implements ResultOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Result of getting signing box.
    * @param signingBox Signing box for signing data requested by debot engine. Signing box is owned and disposed by debot engine
    */
    public record GetSigningBox(@NonNull Integer signingBox) implements ResultOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }

        public static final InvokeDebot INVOKEDEBOT = new InvokeDebot();


    /**
    * Result of debot invoking.

    */
    public record InvokeDebot() implements ResultOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Result of `approve` callback.
    * @param approved Indicates whether the DeBot is allowed to perform the specified operation.
    */
    public record Approve(@NonNull Boolean approved) implements ResultOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}

    /**
    * 
    * @param debotHandle Debot handle which references an instance of debot engine.
    */
    public record ParamsOfStart(@NonNull Integer debotHandle) {}

    /**
    * 
    * @param address Debot smart contract address.
    */
    public record ParamsOfFetch(@NonNull String address) {}

    /**
    * 
    * @param info Debot metadata.
    */
    public record ResultOfFetch(@NonNull DebotInfo info) {}

    /**
    * 
    * @param debotHandle Debot handle which references an instance of debot engine.
    * @param action Debot Action that must be executed.
    */
    public record ParamsOfExecute(@NonNull Integer debotHandle, @NonNull DebotAction action) {}

    /**
    * 
    * @param debotHandle Debot handle which references an instance of debot engine.
    * @param message BOC of internal message to debot encoded in base64 format.
    */
    public record ParamsOfSend(@NonNull Integer debotHandle, @NonNull String message) {}

    /**
    * 
    * @param debotHandle Debot handle which references an instance of debot engine.
    */
    public record ParamsOfRemove(@NonNull Integer debotHandle) {}
    /**
    * <h2>debot.init</h2>
    * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Creates and instance of DeBot. Downloads debot smart contract (code and data) from blockchain and createsan instance of Debot Engine for it.<p># RemarksIt does not switch debot to context 0. Browser Callbacks are not called.
    * @param address Debot smart contract address 
    * @param appObject  
    * @return {@link tech.deplant.java4ever.binding.Debot.RegisteredDebot}
    */
    public static CompletableFuture<RegisteredDebot> init(@NonNull Context context, @NonNull String address,  AppDebotBrowser appObject)  throws JsonProcessingException {
        return context.futureAppObject("debot.init", new ParamsOfInit(address), appObject, RegisteredDebot.class);
    }

    /**
    * <h2>debot.start</h2>
    * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Starts the DeBot. Downloads debot smart contract from blockchain and switches it tocontext zero.<p>This function must be used by Debot Browser to start a dialog with debot.While the function is executing, several Browser Callbacks can be called,since the debot tries to display all actions from the context 0 to the user.<p>When the debot starts SDK registers `BrowserCallbacks` AppObject.Therefore when `debote.remove` is called the debot is being deleted and the callback is calledwith `finish`=`true` which indicates that it will never be used again.
    * @param debotHandle Debot handle which references an instance of debot engine. 
    * @return {@link tech.deplant.java4ever.binding.Debot.Void}
    */
    public static CompletableFuture<Void> start(@NonNull Context context, @NonNull Integer debotHandle)  throws JsonProcessingException {
        return context.future("debot.start", new ParamsOfStart(debotHandle), Void.class);
    }

    /**
    * <h2>debot.fetch</h2>
    * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Fetches DeBot metadata from blockchain. Downloads DeBot from blockchain and creates and fetches its metadata.
    * @param address Debot smart contract address. 
    * @return {@link tech.deplant.java4ever.binding.Debot.ResultOfFetch}
    */
    public static CompletableFuture<ResultOfFetch> fetch(@NonNull Context context, @NonNull String address)  throws JsonProcessingException {
        return context.future("debot.fetch", new ParamsOfFetch(address), ResultOfFetch.class);
    }

    /**
    * <h2>debot.execute</h2>
    * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Executes debot action. Calls debot engine referenced by debot handle to execute input action.Calls Debot Browser Callbacks if needed.<p># RemarksChain of actions can be executed if input action generates a list of subactions.
    * @param debotHandle Debot handle which references an instance of debot engine. 
    * @param action Debot Action that must be executed. 
    * @return {@link tech.deplant.java4ever.binding.Debot.Void}
    */
    public static CompletableFuture<Void> execute(@NonNull Context context, @NonNull Integer debotHandle, @NonNull DebotAction action)  throws JsonProcessingException {
        return context.future("debot.execute", new ParamsOfExecute(debotHandle, action), Void.class);
    }

    /**
    * <h2>debot.send</h2>
    * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Sends message to Debot. Used by Debot Browser to send response on Dinterface call or from other Debots.
    * @param debotHandle Debot handle which references an instance of debot engine. 
    * @param message BOC of internal message to debot encoded in base64 format. 
    * @return {@link tech.deplant.java4ever.binding.Debot.Void}
    */
    public static CompletableFuture<Void> send(@NonNull Context context, @NonNull Integer debotHandle, @NonNull String message)  throws JsonProcessingException {
        return context.future("debot.send", new ParamsOfSend(debotHandle, message), Void.class);
    }

    /**
    * <h2>debot.remove</h2>
    * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Destroys debot handle. Removes handle from Client Context and drops debot engine referenced by that handle.
    * @param debotHandle Debot handle which references an instance of debot engine. 
    * @return {@link tech.deplant.java4ever.binding.Debot.Void}
    */
    public static CompletableFuture<Void> remove(@NonNull Context context, @NonNull Integer debotHandle)  throws JsonProcessingException {
        return context.future("debot.remove", new ParamsOfRemove(debotHandle), Void.class);
    }

}
