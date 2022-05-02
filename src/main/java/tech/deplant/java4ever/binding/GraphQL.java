package tech.deplant.java4ever.binding;

import lombok.Value;
import tech.deplant.java4ever.binding.json.JsonData;

public class GraphQL {

    public static class Filter extends JsonData {
        @Value
        public static class In extends Filter {
            String[] in;
        }

        @Value
        public static class Eq extends Filter {
            Integer eq;
        }

        @Value
        public static class Gt extends Filter {
            String gt;
        }

        @Value
        public static class Lt extends Filter {
            String lt;
        }
    }


}
