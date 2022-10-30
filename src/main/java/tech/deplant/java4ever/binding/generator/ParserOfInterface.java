package tech.deplant.java4ever.binding.generator;

import tech.deplant.java4ever.binding.generator.javapoet.CodeBlock;
import tech.deplant.java4ever.binding.generator.javapoet.TypeSpec;
import tech.deplant.java4ever.binding.generator.reference.ApiType;
import tech.deplant.java4ever.binding.generator.reference.EnumOfTypes;
import tech.deplant.java4ever.binding.generator.reference.StructType;

import javax.lang.model.element.Modifier;
import java.util.Map;

import static java.util.Objects.requireNonNullElse;

public class ParserOfInterface {

	public static TypeSpec.Builder makeInterfaceFromEOT(String moduleName,
	                                                    EnumOfTypes eot,
	                                                    Map<String, String> childParent) {
		CodeBlock.Builder interfaceDocBuilder = CodeBlock
				.builder();
		if (eot.description() != null || eot.summary() != null) {
			interfaceDocBuilder.add(String.format("%s %s\n",
			                                      requireNonNullElse(eot.summary(), ""),
			                                      requireNonNullElse(eot.description(), "")));
		}
		TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(eot.name())
		                                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.SEALED);
		for (ApiType type : eot.enum_types()) {
			switch (type) {
				case StructType struct -> {
					interfaceBuilder.addType(ParserOfStruct.makeRecordFromStruct(moduleName, struct).build());
				}
				default -> throw new IllegalStateException("All EnumOfTypes children should be structs: " + type);
			}
		}

		return interfaceBuilder
				.addJavadoc(interfaceDocBuilder.build());
	}
}
