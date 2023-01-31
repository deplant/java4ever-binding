module java4ever.binding {
	requires java.compiler;
	requires org.slf4j.jdk.platform.logging;
	requires transitive java4ever.utils;
	requires transitive com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.datatype.jdk8;
	requires com.fasterxml.jackson.datatype.jsr310;
	requires com.fasterxml.jackson.module.paramnames;
	exports tech.deplant.java4ever.binding;
	exports tech.deplant.java4ever.binding.loader;
	exports tech.deplant.java4ever.binding.io;
	exports tech.deplant.java4ever.binding.generator;
	exports tech.deplant.java4ever.binding.generator.reference;
	exports tech.deplant.java4ever.binding.generator.jtype;
	opens tech.deplant.java4ever.binding to com.fasterxml.jackson.databind;
	opens tech.deplant.java4ever.binding.generator to com.fasterxml.jackson.databind;
	opens tech.deplant.java4ever.binding.generator.reference to com.fasterxml.jackson.databind;
	opens tech.deplant.java4ever.binding.generator.jtype to com.fasterxml.jackson.databind;
	//exports tech.deplant.java4ever.binding.tst;
}