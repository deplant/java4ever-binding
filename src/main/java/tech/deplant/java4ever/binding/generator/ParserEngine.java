package tech.deplant.java4ever.binding.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.generator.javapoet.ClassName;
import tech.deplant.java4ever.binding.generator.javapoet.CodeBlock;
import tech.deplant.java4ever.binding.generator.javapoet.TypeSpec;
import tech.deplant.java4ever.binding.generator.reference.*;
import tech.deplant.java4ever.binding.io.JsonResource;

import javax.lang.model.element.Modifier;

import static java.util.Objects.requireNonNullElse;

public class ParserEngine {

	public static String convertType(ApiType apiType) {
		final String typeName = switch (apiType) {
			case OptionalType opt -> convertType(opt.optional_inner());
			case ArrayType arr -> convertType(arr.array_item()) + "[]";
			case RefType ref -> ref.ref_name();
			default -> apiType.type();
		};
		if ("Value".equals(typeName)) {
			return "Map<String,Object>";
		}
		return typeName;
	}

	public static TypeSpec.Builder generateClass(ApiModule module, String version) {
		CodeBlock.Builder moduleJavaDocBlock = CodeBlock
				.builder()
				.add(String.format("<strong>%s</strong>\n", module.name()))
				.add(String.format("Contains methods of \"%s\" module of EVER-SDK API\n", module.name()))
				.add("\n")
				.add("Provides message encoding and decoding according to the ABI specification\n")
				.add(String.format("@version EVER-SDK %s", version));

		TypeSpec.Builder moduleBuilder = TypeSpec
				.classBuilder(module.name())
				.addJavadoc(moduleJavaDocBlock.build())
				.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
		return moduleBuilder;
	}

	public static TypeSpec.Builder generateStruct(StructType struct) {
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
			//System.out.println(convertType(component));
			CodeBlock.Builder componentDocBuilder = CodeBlock
					.builder();
			if (component.description() != null || component.summary() != null) {
				componentDocBuilder.add(String.format("@param %s %s %s\n",
				                                      component.name(),
				                                      requireNonNullElse(component.summary(), ""),
				                                      requireNonNullElse(component.description(), "")));
			}

			structDocBuilder.add(componentDocBuilder.build());
			structBuilder
					.addRecordComponent(ClassName.bestGuess(convertType(component)), component.name());
		}
		return structBuilder
				//.addMethod(MethodSpec.CompactConstructorBuilder()
				//                     .addModifiers(Modifier.PUBLIC).build())
				.addJavadoc(structDocBuilder.build());
	}

	public static void parse() throws JsonProcessingException {
		var ref = ContextBuilder.DEFAULT_MAPPER.readValue(new JsonResource("api.json").get(), ApiReference.class);
		String apiVersion = ref.version();

		//for (var apiModule : ref.modules()) {
		var module = ref.modules()[7];

		TypeSpec.Builder moduleBuilder = generateClass(module, apiVersion);

		for (var type : module.types()) {
			switch (type) {
				case StructType struct -> moduleBuilder.addType(generateStruct(struct).build());
				default -> {
				}
			}
		}
		System.out.println(moduleBuilder.build());
	}

}
