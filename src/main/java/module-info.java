module java4ever.binding {
    requires jdk.incubator.foreign;
    requires jdk.unsupported;
    requires org.apache.logging.log4j;
    requires java.scripting;
    requires com.google.gson;
    requires static lombok;
    opens tech.deplant.java4ever.binding to com.google.gson;
    opens tech.deplant.java4ever.binding.json to com.google.gson;
    opens tech.deplant.java4ever.binding.response to com.google.gson;
    exports tech.deplant.java4ever.binding;
    exports tech.deplant.java4ever.binding.loader;
}