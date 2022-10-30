package tech.deplant.java4ever.binding.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.generator.javapoet.ClassName;
import tech.deplant.java4ever.binding.generator.javapoet.CodeBlock;
import tech.deplant.java4ever.binding.generator.javapoet.TypeSpec;
import tech.deplant.java4ever.binding.generator.reference.*;
import tech.deplant.java4ever.binding.io.JsonResource;

import javax.lang.model.element.Modifier;
import java.util.Map;

import static java.util.Objects.requireNonNullElse;

public class ParserEngine {

	public final static Map<String, String> reserved = Map.of("public", "publicKey",
	                                                          "secret", "secretKey",
	                                                          "switch", "switchTo");

	public static String generateTypeName(String moduleName, ApiType apiType) {
		final String typeName = switch (apiType) {
			case OptionalType opt -> generateTypeName(moduleName, opt.optional_inner());
			case ArrayType arr -> generateTypeName(moduleName, arr.array_item()) + "[]";
			case RefType ref -> ref.ref_name();
			default -> apiType.type();
		};
		String typeCleaned = typeName.replaceAll(moduleName + "\\.", "");
		if ("Value".equals(typeCleaned)) {
			return "Map<String,Object>";
		}
		return ParserUtils.capitalize(typeCleaned);
	}

	public static String generateParamName(String paramName) {
		String camelCasedName = ParserUtils.toCamelCase(paramName);
		return reserved.getOrDefault(camelCasedName, camelCasedName);
	}

	public static TypeSpec.Builder generateClass(String moduleNameCapitalized, String version) {
		CodeBlock.Builder moduleJavaDocBlock = CodeBlock
				.builder()
				.add(String.format("<strong>%s</strong>\n", moduleNameCapitalized))
				.add(String.format("Contains methods of \"%s\" module of EVER-SDK API\n", moduleNameCapitalized))
				.add("\n")
				.add("Provides message encoding and decoding according to the ABI specification\n")
				.add(String.format("@version EVER-SDK %s", version));

		TypeSpec.Builder moduleBuilder = TypeSpec
				.classBuilder(moduleNameCapitalized)
				.addJavadoc(moduleJavaDocBlock.build())
				.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
		return moduleBuilder;
	}

	public static TypeSpec.Builder generateRecord(String moduleName, StructType struct) {
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
			String camelComponentName = generateParamName(component.name());
			boolean reservedName = false;
			if (reserved.containsKey(camelComponentName)) {
				camelComponentName = reserved.get(camelComponentName);
				reservedName = true;
			}
			if (component.description() != null || component.summary() != null) {
				componentDocBuilder.add(String.format("@param %s %s %s\n",
				                                      camelComponentName,
				                                      requireNonNullElse(component.summary(), ""),
				                                      requireNonNullElse(component.description(), "")));
			}

			structDocBuilder.add(componentDocBuilder.build());
			structBuilder
					.addRecordComponent(ClassName.bestGuess(generateTypeName(moduleName, component)),
					                    camelComponentName);
		}
		return structBuilder
				//.addMethod(MethodSpec.CompactConstructorBuilder()
				//                     .addModifiers(Modifier.PUBLIC).build())
				.addJavadoc(structDocBuilder.build());
	}

	public static void parse() throws JsonProcessingException {
		var ref = ContextBuilder.DEFAULT_MAPPER.readValue(new JsonResource("api.json").get(), ApiReference.class);
		final String apiVersion = ref.version();
		//for (var apiModule : ref.modules()) {
		var module = ref.modules()[7];

		final String moduleCapitalizedName = ParserUtils.capitalize(module.name());
		final TypeSpec.Builder moduleBuilder = generateClass(moduleCapitalizedName, apiVersion);

		for (var type : module.types()) {
			switch (type) {
				case StructType struct -> moduleBuilder.addType(generateRecord(moduleCapitalizedName, struct).build());
				default -> {
				}
			}
		}
		System.out.println(moduleBuilder.build());
	}

}
