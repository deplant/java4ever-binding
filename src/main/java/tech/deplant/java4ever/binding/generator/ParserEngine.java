package tech.deplant.java4ever.binding.generator;

import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.generator.javapoet.*;
import tech.deplant.java4ever.binding.generator.reference.*;
import tech.deplant.java4ever.binding.io.JsonResource;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ParserEngine {

	public final static Map<String, String> RESERVED_WORDS = Map.of("public", "publicKey",
	                                                                "secret", "secretKey",
	                                                                "switch", "switchTo",
	                                                                "abi version", "ABIversion");

	public static String generateTypeName(String moduleName, ApiType apiType) {
		final String typeName = switch (apiType) {
			case GenericType gen -> generateTypeName(moduleName, gen.generic_args()[0]);
			case OptionalType opt -> generateTypeName(moduleName, opt.optional_inner());
			case ArrayType arr -> generateTypeName(moduleName, arr.array_item()) + "[]";
			case RefType ref -> ref.ref_name();
			case NoneType none -> null;
			default -> apiType.type();
		};
		if (typeName != null) {
			String typeCleaned = typeName.replaceAll(moduleName + "\\.", "");
			if ("Value".equals(typeCleaned)) {
				return "Map<String,Object>";
			}
			if ("ClientContext".equals(typeCleaned)) {
				return "Context";
			}
			return ParserUtils.capitalize(typeCleaned);
		} else {
			return null;
		}
	}

	public static String generateParamName(String paramName) {
		return ParserUtils.toParameterCase(paramName);
	}

	public static void parse() throws IOException {
		var ref = ContextBuilder.DEFAULT_MAPPER.readValue(new JsonResource("api.json").get(), ApiReference.class);
		final String apiVersion = ref.version();
		//for (var apiModule : ref.modules()) {
		var module = ref.modules()[1];

		final Map<String, ApiType> refs = new HashMap<>();
		final Map<String, String> childParent = new HashMap<>();

		final String moduleCapitalizedName = ParserUtils.capitalize(module.name());
		final TypeSpec.Builder moduleBuilder = ParserOfModule.classOfModule(module, moduleCapitalizedName, apiVersion);

		for (ApiType type : module.types()) {
			switch (type) {
//				case StructType struct
//						when
//						struct.name().length() > 8 &&
//						!"ParamsOf".equals(struct.name().substring(0, 8)) -> {
//					refs.put(struct.name(), struct);
//					moduleBuilder.addType(makeRecordFromStruct(moduleCapitalizedName, struct).build());
//				}
				case StructType struct -> {
					refs.put(struct.name(), struct);
					moduleBuilder.addType(ParserOfStruct.makeRecordFromStruct(moduleCapitalizedName, struct).build());
				}
				case EnumOfTypes eot -> {
					refs.put(eot.name(), eot);
					moduleBuilder.addType(ParserOfInterface.makeInterfaceFromEOT(moduleCapitalizedName,
					                                                             eot,
					                                                             childParent).build());
				}
				default -> {
				}
			}
		}
		for (ApiFunction function : module.functions()) {
			var methodBuilder = MethodSpec
					.methodBuilder(function.name())
					.addModifiers(Modifier.PUBLIC, Modifier.STATIC);

			var codeBuilder = CodeBlock.builder();
			String resultTypeName = generateTypeName(moduleCapitalizedName, function.result());
			if (resultTypeName != null) {
				methodBuilder.returns(ClassName.bestGuess(resultTypeName));
				codeBuilder.addStatement(
						// $S - "crypto.nacl_box_open"
						// $T - TypeName
						// $L - paramList
						"return  context.call($S, new $T($L), $T.class)",
						"crypto.nacl_box_open",
						TypeVariableName.get(resultTypeName),
						"encrypted, nonce, theirPublic, secretKey",
						TypeVariableName.get(resultTypeName));
				methodBuilder.addCode(codeBuilder.build());
			}
			for (ApiType functionParam : function.params()) {
				methodBuilder.addParameter(ParserOfParam.generateParam(functionParam, moduleCapitalizedName).build());
			}
			moduleBuilder.addMethod(methodBuilder.build());
		}
		JavaFile javaFile = JavaFile.builder("tech.deplant.java4ever.binding", moduleBuilder.build())
		                            .build();

		javaFile.writeTo(Paths.get("src/gen/java"));

		//System.out.println(moduleBuilder.build());
	}

	public static String getTypeName(ApiType type) {
		return switch (type) {
			case GenericType gen -> getTypeName(gen.generic_args()[0]);
			case RefType ref -> ref.ref_name();
			default -> type.name();
		};
	}

}
