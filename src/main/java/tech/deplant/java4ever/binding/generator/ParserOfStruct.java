package tech.deplant.java4ever.binding.generator;

import tech.deplant.java4ever.binding.generator.javapoet.CodeBlock;
import tech.deplant.java4ever.binding.generator.javapoet.TypeSpec;
import tech.deplant.java4ever.binding.generator.reference.ApiType;
import tech.deplant.java4ever.binding.generator.reference.StructType;

import javax.lang.model.element.Modifier;

import static java.util.Objects.requireNonNullElse;

public class ParserOfStruct {
	public static TypeSpec.Builder makeRecordFromStruct(String moduleName, StructType struct) {
		CodeBlock.Builder structDocBuilder = CodeBlock
				.builder();
		if (struct.description() != null || struct.summary() != null) {
			structDocBuilder.add(String.format("%s %s\n",
			                                   requireNonNullElse(struct.summary(), ""),
			                                   requireNonNullElse(struct.description(), "")));
		}
		TypeSpec.Builder structBuilder = TypeSpec
				.recordBuilder(struct.name())
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

		for (ApiType component : struct.struct_fields()) {
			structBuilder
					.addRecordComponent(ParserOfParam.generateParam(component, moduleName).build());
		}
		return structBuilder
				//.addMethod(MethodSpec.CompactConstructorBuilder()
				//                     .addModifiers(Modifier.PUBLIC).build())
				.addJavadoc(structDocBuilder.build());
	}
}
