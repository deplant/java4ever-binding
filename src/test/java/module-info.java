module java4ever.bindtest {
    requires static lombok;
    requires jdk.incubator.foreign;
    requires java4ever.binding;
    requires com.fasterxml.jackson.core;
    requires org.junit.jupiter.api;
    requires org.apache.logging.log4j;
    exports tech.deplant.java4ever.test;
}