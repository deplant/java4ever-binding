module java4ever.binding {
    requires jdk.incubator.foreign;
    requires jdk.unsupported;
    requires org.apache.logging.log4j;
    requires java.scripting;
    requires com.google.gson;
    requires static lombok;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.module.paramnames;
    opens tech.deplant.java4ever.binding.json to com.google.gson;
    opens tech.deplant.java4ever.binding.response to com.google.gson;
    opens tech.deplant.java4ever.binding to com.fasterxml.jackson.databind;
    exports tech.deplant.java4ever.binding;
    exports tech.deplant.java4ever.binding.loader;
}