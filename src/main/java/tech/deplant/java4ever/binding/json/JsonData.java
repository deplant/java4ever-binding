package tech.deplant.java4ever.binding.json;

import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JsonData {

    // @Since for different TON-SDK versions
    // @Until for different TON-SDK versions

    public String toJson(Double version) {
        var gson = new GsonBuilder()
                .setVersion(version)
                //.registerTypeAdapterFactory(new InputMapSerializationAdapter())
                .create();
        return gson.toJson(this);
    }

    public String toJson() {
        var gson = new GsonBuilder()
                .registerTypeAdapterFactory(new InputMapSerializationAdapter())
                .create();
        return gson.toJson(this);
    }

}
