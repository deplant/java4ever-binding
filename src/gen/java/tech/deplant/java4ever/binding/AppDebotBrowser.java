package tech.deplant.java4ever.binding;

import java.util.Map;

public interface AppDebotBrowser {
    void log(String msg);
    void switchTo(Number contextId);
    void switchCompleted();
    void showAction(Debot.DebotAction action);
    String input(String prompt);
    Integer getSigningBox();
    Void invokeDebot(String debotAddr,Debot.DebotAction action);
    void send(String message);
    Boolean approve(Map<String,Object> activity);
}
