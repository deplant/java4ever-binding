package tech.deplant.java4ever.binding.generator;

import tech.deplant.java4ever.binding.generator.javapoet.CodeBlock;
import tech.deplant.java4ever.binding.generator.javapoet.TypeSpec;
import tech.deplant.java4ever.binding.generator.reference.ApiModule;

import javax.lang.model.element.Modifier;

import static java.util.Objects.requireNonNullElse;

public class ParserOfModule {
	public static TypeSpec.Builder classOfModule(ApiModule module, String moduleNameCapitalized, String version) {
		TypeSpec.Builder moduleBuilder = TypeSpec
				.classBuilder(moduleNameCapitalized + "_new")
				.addJavadoc(javadocOfModule(module, moduleNameCapitalized, version).build())
				.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
		return moduleBuilder;
	}

	public static CodeBlock.Builder javadocOfModule(ApiModule module, String moduleNameCapitalized, String version) {
		return CodeBlock
				.builder()
				.add(String.format("""
						                   <strong>%s</strong>
						                   Contains methods of "%s" module of EVER-SDK API
						                                
						                   %s %s
						                   @version %s
						                   """,
				                   moduleNameCapitalized,
				                   module.name(),
				                   requireNonNullElse(module.summary(), ""),
				                   requireNonNullElse(module.description(), ""),
				                   version));
	}
}
