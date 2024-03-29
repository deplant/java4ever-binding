package tech.deplant.java4ever.binding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.lang.Boolean;
import java.lang.Deprecated;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigInteger;

/**
 * <strong>Debot</strong>
 * Contains methods of "debot" module of EVER-SDK API
 *
 * [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Module for working with debot. 
 * @version 1.45.0
 */
public final class Debot {
  /**
   * Downloads debot smart contract (code and data) from blockchain and creates
   * an instance of Debot Engine for it.
   *
   * # Remarks
   * It does not switch debot to context 0. Browser Callbacks are not called. [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Creates and instance of DeBot.
   *
   * @param address  Debot smart contract address
   */
  @Unstable
  @Deprecated
  public static Debot.RegisteredDebot init(int ctxId, String address, AppSigningBox appObject)
      throws EverSdkException {
    return EverSdk.callAppObject(ctxId, "debot.init", new Debot.ParamsOfInit(address), appObject, Debot.RegisteredDebot.class);
  }

  /**
   * Downloads debot smart contract from blockchain and switches it to
   * context zero.
   *
   * This function must be used by Debot Browser to start a dialog with debot.
   * While the function is executing, several Browser Callbacks can be called,
   * since the debot tries to display all actions from the context 0 to the user.
   *
   * When the debot starts SDK registers `BrowserCallbacks` AppObject.
   * Therefore when `debote.remove` is called the debot is being deleted and the callback is called
   * with `finish`=`true` which indicates that it will never be used again. [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Starts the DeBot.
   *
   * @param debotHandle  Debot handle which references an instance of debot engine.
   */
  @Unstable
  @Deprecated
  public static void start(int ctxId, Long debotHandle) throws EverSdkException {
    EverSdk.callVoid(ctxId, "debot.start", new Debot.ParamsOfStart(debotHandle));
  }

  /**
   * Downloads DeBot from blockchain and creates and fetches its metadata. [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Fetches DeBot metadata from blockchain.
   *
   * @param address  Debot smart contract address.
   */
  @Unstable
  @Deprecated
  public static Debot.ResultOfFetch fetch(int ctxId, String address) throws EverSdkException {
    return EverSdk.call(ctxId, "debot.fetch", new Debot.ParamsOfFetch(address), Debot.ResultOfFetch.class);
  }

  /**
   * Calls debot engine referenced by debot handle to execute input action.
   * Calls Debot Browser Callbacks if needed.
   *
   * # Remarks
   * Chain of actions can be executed if input action generates a list of subactions. [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Executes debot action.
   *
   * @param debotHandle  Debot handle which references an instance of debot engine.
   * @param action  Debot Action that must be executed.
   */
  @Unstable
  @Deprecated
  public static void execute(int ctxId, Long debotHandle, Debot.DebotAction action) throws
      EverSdkException {
    EverSdk.callVoid(ctxId, "debot.execute", new Debot.ParamsOfExecute(debotHandle, action));
  }

  /**
   * Used by Debot Browser to send response on Dinterface call or from other Debots. [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Sends message to Debot.
   *
   * @param debotHandle  Debot handle which references an instance of debot engine.
   * @param message  BOC of internal message to debot encoded in base64 format.
   */
  @Unstable
  @Deprecated
  public static void send(int ctxId, Long debotHandle, String message) throws EverSdkException {
    EverSdk.callVoid(ctxId, "debot.send", new Debot.ParamsOfSend(debotHandle, message));
  }

  /**
   * Removes handle from Client Context and drops debot engine referenced by that handle. [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Destroys debot handle.
   *
   * @param debotHandle  Debot handle which references an instance of debot engine.
   */
  @Unstable
  @Deprecated
  public static void remove(int ctxId, Long debotHandle) throws EverSdkException {
    EverSdk.callVoid(ctxId, "debot.remove", new Debot.ParamsOfRemove(debotHandle));
  }

  /**
   *  [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Describes how much funds will be debited from the target  contract balance as a result of the transaction.
   *
   * @param amount  Amount of nanotokens that will be sent to `dst` address.
   * @param dst  Destination address of recipient of funds.
   */
  public record Spending(BigInteger amount, String dst) {
  }

  /**
   *  [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Parameters for executing debot action.
   *
   * @param debotHandle  Debot handle which references an instance of debot engine.
   * @param action  Debot Action that must be executed.
   */
  public record ParamsOfExecute(Long debotHandle, Debot.DebotAction action) {
  }

  /**
   *  [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Returning values from Debot Browser callbacks.
   */
  public sealed interface ResultOfAppDebotBrowser {
    /**
     *  Result of user input.
     *
     * @param value  String entered by user.
     */
    record Input(String value) implements ResultOfAppDebotBrowser {
      @JsonProperty("type")
      public String type() {
        return "Input";
      }
    }

    /**
     *  Result of getting signing box.
     *
     * @param signingBox Signing box is owned and disposed by debot engine Signing box for signing data requested by debot engine.
     */
    record GetSigningBox(Long signingBox) implements ResultOfAppDebotBrowser {
      @JsonProperty("type")
      public String type() {
        return "GetSigningBox";
      }
    }

    /**
     *  Result of debot invoking.
     */
    record InvokeDebot() implements ResultOfAppDebotBrowser {
      @JsonProperty("type")
      public String type() {
        return "InvokeDebot";
      }
    }

    /**
     *  Result of `approve` callback.
     *
     * @param approved  Indicates whether the DeBot is allowed to perform the specified operation.
     */
    record Approve(Boolean approved) implements ResultOfAppDebotBrowser {
      @JsonProperty("type")
      public String type() {
        return "Approve";
      }
    }
  }

  /**
   * Called by debot engine to communicate with debot browser. [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Debot Browser callbacks
   */
  public sealed interface ParamsOfAppDebotBrowser {
    /**
     *  Print message to user.
     *
     * @param msg  A string that must be printed to user.
     */
    record Log(String msg) implements ParamsOfAppDebotBrowser {
      @JsonProperty("type")
      public String type() {
        return "Log";
      }
    }

    /**
     *  Switch debot to another context (menu).
     *
     * @param contextId  Debot context ID to which debot is switched.
     */
    record Switch(Integer contextId) implements ParamsOfAppDebotBrowser {
      @JsonProperty("type")
      public String type() {
        return "Switch";
      }
    }

    /**
     *  Notify browser that all context actions are shown.
     */
    record SwitchCompleted() implements ParamsOfAppDebotBrowser {
      @JsonProperty("type")
      public String type() {
        return "SwitchCompleted";
      }
    }

    /**
     *  Show action to the user. Called after `switch` for each action in context.
     *
     * @param action  Debot action that must be shown to user as menu item. At least `description` property must be shown from [DebotAction] structure.
     */
    record ShowAction(Debot.DebotAction action) implements ParamsOfAppDebotBrowser {
      @JsonProperty("type")
      public String type() {
        return "ShowAction";
      }
    }

    /**
     *  Request user input.
     *
     * @param prompt  A prompt string that must be printed to user before input request.
     */
    record Input(String prompt) implements ParamsOfAppDebotBrowser {
      @JsonProperty("type")
      public String type() {
        return "Input";
      }
    }

    /**
     * Signing box returned is owned and disposed by debot engine Get signing box to sign data.
     */
    record GetSigningBox() implements ParamsOfAppDebotBrowser {
      @JsonProperty("type")
      public String type() {
        return "GetSigningBox";
      }
    }

    /**
     *  Execute action of another debot.
     *
     * @param debotAddr  Address of debot in blockchain.
     * @param action  Debot action to execute.
     */
    record InvokeDebot(String debotAddr,
        Debot.DebotAction action) implements ParamsOfAppDebotBrowser {
      @JsonProperty("type")
      public String type() {
        return "InvokeDebot";
      }
    }

    /**
     *  Used by Debot to call DInterface implemented by Debot Browser.
     *
     * @param message Message body contains interface function and parameters. Internal message to DInterface address.
     */
    record Send(String message) implements ParamsOfAppDebotBrowser {
      @JsonProperty("type")
      public String type() {
        return "Send";
      }
    }

    /**
     *  Requests permission from DeBot Browser to execute DeBot operation.
     *
     * @param activity  DeBot activity details.
     */
    record Approve(Debot.DebotActivity activity) implements ParamsOfAppDebotBrowser {
      @JsonProperty("type")
      public String type() {
        return "Approve";
      }
    }
  }

  /**
   *  [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md)
   *
   * @param info  Debot metadata.
   */
  public record ResultOfFetch(Debot.DebotInfo info) {
  }

  /**
   *  [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md)
   *
   * @param debotHandle  Debot handle which references an instance of debot engine.
   */
  public record ParamsOfRemove(Long debotHandle) {
  }

  /**
   *  [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Structure for storing debot handle returned from `init` function.
   *
   * @param debotHandle  Debot handle which references an instance of debot engine.
   * @param debotAbi  Debot abi as json string.
   * @param info  Debot metadata.
   */
  public record RegisteredDebot(Long debotHandle, String debotAbi, Debot.DebotInfo info) {
  }

  /**
   *  [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Parameters to start DeBot. DeBot must be already initialized with init() function.
   *
   * @param debotHandle  Debot handle which references an instance of debot engine.
   */
  public record ParamsOfStart(Long debotHandle) {
  }

  /**
   *  [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Describes the operation that the DeBot wants to perform.
   */
  public sealed interface DebotActivity {
    /**
     *  DeBot wants to create new transaction in blockchain.
     *
     * @param msg  External inbound message BOC.
     * @param dst  Target smart contract address.
     * @param out  List of spendings as a result of transaction.
     * @param fee  Transaction total fee.
     * @param setcode  Indicates if target smart contract updates its code.
     * @param signkey  Public key from keypair that was used to sign external message.
     * @param signingBoxHandle  Signing box handle used to sign external message.
     */
    record Transaction(String msg, String dst, Debot.Spending[] out, BigInteger fee,
        Boolean setcode, String signkey, Long signingBoxHandle) implements DebotActivity {
      @JsonProperty("type")
      public String type() {
        return "Transaction";
      }
    }
  }

  /**
   *  [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Parameters to fetch DeBot metadata.
   *
   * @param address  Debot smart contract address.
   */
  public record ParamsOfFetch(String address) {
  }

  /**
   *  [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Parameters of `send` function.
   *
   * @param debotHandle  Debot handle which references an instance of debot engine.
   * @param message  BOC of internal message to debot encoded in base64 format.
   */
  public record ParamsOfSend(Long debotHandle, String message) {
  }

  /**
   *  [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Describes DeBot metadata.
   *
   * @param name  DeBot short name.
   * @param version  DeBot semantic version.
   * @param publisher  The name of DeBot deployer.
   * @param caption  Short info about DeBot.
   * @param author  The name of DeBot developer.
   * @param support  TON address of author for questions and donations.
   * @param hello  String with the first messsage from DeBot.
   * @param language  String with DeBot interface language (ISO-639).
   * @param dabi  String with DeBot ABI.
   * @param icon  DeBot icon.
   * @param interfaces  Vector with IDs of DInterfaces used by DeBot.
   * @param dabiversion  ABI version ("x.y") supported by DeBot
   */
  public record DebotInfo(String name, String version, String publisher, String caption,
      String author, String support, String hello, String language, String dabi, String icon,
      String[] interfaces, String dabiversion) {
  }

  /**
   *  [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Parameters to init DeBot.
   *
   * @param address  Debot smart contract address
   */
  public record ParamsOfInit(String address) {
  }

  public enum DebotErrorCode {
    DebotStartFailed(801),

    DebotFetchFailed(802),

    DebotExecutionFailed(803),

    DebotInvalidHandle(804),

    DebotInvalidJsonParams(805),

    DebotInvalidFunctionId(806),

    DebotInvalidAbi(807),

    DebotGetMethodFailed(808),

    DebotInvalidMsg(809),

    DebotExternalCallFailed(810),

    DebotBrowserCallbackFailed(811),

    DebotOperationRejected(812),

    DebotNoCode(813);

    private final Integer value;

    DebotErrorCode(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer value() {
      return this.value;
    }
  }

  /**
   *  [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Describes a debot action in a Debot Context.
   *
   * @param description Should be used by Debot Browser as name of menu item. A short action description.
   * @param name Can be a debot function name or a print string (for Print Action). Depends on action type.
   * @param actionType  Action type.
   * @param to  ID of debot context to switch after action execution.
   * @param attributes In the form of "param=value,flag". attribute example: instant, args, fargs, sign. Action attributes.
   * @param misc Used by debot only. Some internal action data.
   */
  public record DebotAction(String description, String name, Integer actionType, Integer to,
      String attributes, String misc) {
  }
}
