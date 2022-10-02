package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.*;
import java.util.Arrays;

/**
 *  <strong>debot</strong>
 *  Contains methods of "debot" module.

 *  <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Module for working with debot.
 *  @version EVER-SDK 1.37.0
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
    public record DebotAction(String description, String name, Number actionType, Number to, String attributes, String misc) {}

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
    public record DebotInfo(String name, String version, String publisher, String caption, String author, String support, String hello, String language, String dabi, String icon, String[] interfaces, String dabiVersion) {}

    /**
    * 
    * @param address Debot smart contract address
    */
    public record ParamsOfInit(String address) {}

    /**
    * 
    * @param debotHandle Debot handle which references an instance of debot engine.
    * @param debotAbi Debot abi as json string.
    * @param info Debot metadata.
    */
    public record RegisteredDebot(Integer debotHandle, String debotAbi, DebotInfo info) {}
    public interface ParamsOfAppDebotBrowser {


    /**
    * Print message to user.
    * @param msg A string that must be printed to user.
    */
    public record Log(String msg) implements ParamsOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Switch debot to another context (menu).
    * @param contextId Debot context ID to which debot is switched.
    */
    public record Switch(Number contextId) implements ParamsOfAppDebotBrowser {
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
    public record ShowAction(DebotAction action) implements ParamsOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Request user input.
    * @param prompt A prompt string that must be printed to user before input request.
    */
    public record Input(String prompt) implements ParamsOfAppDebotBrowser {
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
    public record InvokeDebot(String debotAddr, DebotAction action) implements ParamsOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Used by Debot to call DInterface implemented by Debot Browser.
    * @param message Internal message to DInterface address. Message body contains interface function and parameters.
    */
    public record Send(String message) implements ParamsOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Requests permission from DeBot Browser to execute DeBot operation.
    * @param activity DeBot activity details.
    */
    public record Approve(Map<String,Object> activity) implements ParamsOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}
    public interface ResultOfAppDebotBrowser {


    /**
    * Result of user input.
    * @param value String entered by user.
    */
    public record Input(String value) implements ResultOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }


    /**
    * Result of getting signing box.
    * @param signingBox Signing box for signing data requested by debot engine. Signing box is owned and disposed by debot engine
    */
    public record GetSigningBox(Integer signingBox) implements ResultOfAppDebotBrowser {
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
    public record Approve(Boolean approved) implements ResultOfAppDebotBrowser {
                               @JsonProperty("type")
                               public String type() { return getClass().getSimpleName(); }
                           }
}

    /**
    * 
    * @param debotHandle Debot handle which references an instance of debot engine.
    */
    public record ParamsOfStart(Integer debotHandle) {}

    /**
    * 
    * @param address Debot smart contract address.
    */
    public record ParamsOfFetch(String address) {}

    /**
    * 
    * @param info Debot metadata.
    */
    public record ResultOfFetch(DebotInfo info) {}

    /**
    * 
    * @param debotHandle Debot handle which references an instance of debot engine.
    * @param action Debot Action that must be executed.
    */
    public record ParamsOfExecute(Integer debotHandle, DebotAction action) {}

    /**
    * 
    * @param debotHandle Debot handle which references an instance of debot engine.
    * @param message BOC of internal message to debot encoded in base64 format.
    */
    public record ParamsOfSend(Integer debotHandle, String message) {}

    /**
    * 
    * @param debotHandle Debot handle which references an instance of debot engine.
    */
    public record ParamsOfRemove(Integer debotHandle) {}
    /**
    * <strong>debot.init</strong>
    * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Creates and instance of DeBot. Downloads debot smart contract (code and data) from blockchain and createsan instance of Debot Engine for it.<p># RemarksIt does not switch debot to context 0. Browser Callbacks are not called.
    * @param address Debot smart contract address 
    * @param appObject  
    * @return {@link tech.deplant.java4ever.binding.Debot.RegisteredDebot}
    */
    public static RegisteredDebot init(Context ctx, String address,  AppDebotBrowser appObject) throws ExecutionException, JsonProcessingException {
        return  ctx.callAppObject("debot.init", new ParamsOfInit(address), appObject, RegisteredDebot.class);
    }

    /**
    * <strong>debot.start</strong>
    * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Starts the DeBot. Downloads debot smart contract from blockchain and switches it tocontext zero.<p>This function must be used by Debot Browser to start a dialog with debot.While the function is executing, several Browser Callbacks can be called,since the debot tries to display all actions from the context 0 to the user.<p>When the debot starts SDK registers `BrowserCallbacks` AppObject.Therefore when `debote.remove` is called the debot is being deleted and the callback is calledwith `finish`=`true` which indicates that it will never be used again.
    * @param debotHandle Debot handle which references an instance of debot engine. 
    */
    public static void start(Context ctx, Integer debotHandle) throws ExecutionException, JsonProcessingException {
         ctx.callVoid("debot.start", new ParamsOfStart(debotHandle));
    }

    /**
    * <strong>debot.fetch</strong>
    * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Fetches DeBot metadata from blockchain. Downloads DeBot from blockchain and creates and fetches its metadata.
    * @param address Debot smart contract address. 
    * @return {@link tech.deplant.java4ever.binding.Debot.ResultOfFetch}
    */
    public static ResultOfFetch fetch(Context ctx, String address) throws ExecutionException, JsonProcessingException {
        return  ctx.call("debot.fetch", new ParamsOfFetch(address), ResultOfFetch.class);
    }

    /**
    * <strong>debot.execute</strong>
    * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Executes debot action. Calls debot engine referenced by debot handle to execute input action.Calls Debot Browser Callbacks if needed.<p># RemarksChain of actions can be executed if input action generates a list of subactions.
    * @param debotHandle Debot handle which references an instance of debot engine. 
    * @param action Debot Action that must be executed. 
    */
    public static void execute(Context ctx, Integer debotHandle, DebotAction action) throws ExecutionException, JsonProcessingException {
         ctx.callVoid("debot.execute", new ParamsOfExecute(debotHandle, action));
    }

    /**
    * <strong>debot.send</strong>
    * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Sends message to Debot. Used by Debot Browser to send response on Dinterface call or from other Debots.
    * @param debotHandle Debot handle which references an instance of debot engine. 
    * @param message BOC of internal message to debot encoded in base64 format. 
    */
    public static void send(Context ctx, Integer debotHandle, String message) throws ExecutionException, JsonProcessingException {
         ctx.callVoid("debot.send", new ParamsOfSend(debotHandle, message));
    }

    /**
    * <strong>debot.remove</strong>
    * <a target="_blank" href="UNSTABLE">UNSTABLE</a>(UNSTABLE.md) Destroys debot handle. Removes handle from Client Context and drops debot engine referenced by that handle.
    * @param debotHandle Debot handle which references an instance of debot engine. 
    */
    public static void remove(Context ctx, Integer debotHandle) throws ExecutionException, JsonProcessingException {
         ctx.callVoid("debot.remove", new ParamsOfRemove(debotHandle));
    }

}
