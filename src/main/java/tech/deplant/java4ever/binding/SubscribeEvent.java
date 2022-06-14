package tech.deplant.java4ever.binding;

import lombok.NonNull;
import lombok.Value;
import tech.deplant.java4ever.binding.json.JsonData;

import java.util.Map;

@Value
public class SubscribeEvent extends JsonData {
    @NonNull Map<String, Object> result;
}
