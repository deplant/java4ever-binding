module java4ever.bindtest {
	requires org.slf4j;
	requires java.net.http;
	requires java4ever.binding;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires org.junit.jupiter.api;
	exports tech.deplant.java4ever.unit;
}