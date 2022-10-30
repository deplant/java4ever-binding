package tech.deplant.java4ever.binding.generator;

import com.fasterxml.jackson.annotation.JsonProperty;
import tech.deplant.java4ever.binding.generator.javapoet.AnnotationSpec;
import tech.deplant.java4ever.binding.generator.javapoet.ClassName;
import tech.deplant.java4ever.binding.generator.javapoet.CodeBlock;
import tech.deplant.java4ever.binding.generator.javapoet.ParameterSpec;
import tech.deplant.java4ever.binding.generator.reference.ApiType;

import static java.util.Objects.requireNonNullElse;

public class ParserOfParam {
	public static ParameterSpec.Builder generateParam(ApiType param, String moduleName) {
		String camelComponentName = ParserEngine.generateParamName(param.name());
		boolean reservedName = false;
		if (ParserEngine.RESERVED_WORDS.containsKey(camelComponentName)) {
			camelComponentName = ParserEngine.RESERVED_WORDS.get(camelComponentName);
			reservedName = true;
		}
		var componentBuilder = ParameterSpec.builder(ClassName.bestGuess(ParserEngine.generateTypeName(moduleName,
		                                                                                               param)),
		                                             camelComponentName);
		if (reservedName) {
			componentBuilder.addAnnotation(AnnotationSpec.builder(JsonProperty.class)
			                                             .addMember("value", "$S", param.name())
			                                             .build());
		}
		if (param.description() != null || param.summary() != null) {
			String componentJavaDoc = String.format("%s %s",
			                                        requireNonNullElse(param.summary(), ""),
			                                        requireNonNullElse(param.description(), ""));
			componentBuilder.addJavadoc(CodeBlock
					                            .builder().add(componentJavaDoc).build());
		}
		return componentBuilder;
	}
}
