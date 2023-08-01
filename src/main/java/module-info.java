open module java4ever.binding {
	requires java.compiler;
	requires transitive com.fasterxml.jackson.databind;
	requires transitive deplant.commons;
	requires transitive deplant.javapoet;
	requires com.fasterxml.jackson.datatype.jdk8;
	requires com.fasterxml.jackson.datatype.jsr310;
	requires com.fasterxml.jackson.module.paramnames;

	exports tech.deplant.java4ever.binding;
	exports tech.deplant.java4ever.binding.loader;
	exports tech.deplant.java4ever.binding.io;
	exports tech.deplant.java4ever.binding.generator;
	exports tech.deplant.java4ever.binding.generator.reference;
	exports tech.deplant.java4ever.binding.generator.jtype;
}