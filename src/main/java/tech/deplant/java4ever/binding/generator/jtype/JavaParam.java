package tech.deplant.java4ever.binding.generator.jtype;

import com.fasterxml.jackson.annotation.JsonProperty;
import tech.deplant.java4ever.binding.generator.ParserEngine;
import tech.deplant.java4ever.binding.generator.ParserUtils;
import tech.deplant.java4ever.binding.generator.TypeReference;
import tech.deplant.java4ever.binding.generator.javapoet.AnnotationSpec;
import tech.deplant.java4ever.binding.generator.javapoet.ParameterSpec;
import tech.deplant.java4ever.binding.generator.javapoet.TypeName;
import tech.deplant.java4ever.binding.generator.reference.ApiType;

import java.util.Map;

public record JavaParam(TypeName refClassName,
                        String parameterName,
                        String origParamName,
                        String summary,
                        String description,
                        JavaType libType) {

	public final static Map<String, String> RESERVED_FIELD_NAMES = Map.of("public", "publicKey",
	                                                                      "secret", "secretKey",
	                                                                      "switch", "switchTo",
	                                                                      "abi version", "ABIversion");

	public static JavaParam ofApiType(ApiType paramType,
	                                  Map<ParserEngine.SdkType, JavaType> typeLibrary) {

		// TYPE NAME
		var typeReference = TypeReference.fromApiType(paramType);
		var javaType = typeReference.toTypeDeclaration(typeLibrary);
		TypeName className;
		if (javaType instanceof JavaDummy dummy) {
			className = TypeReference.fromApiType(dummy.type()).toTypeName();
		} else {
			className = typeReference.toTypeName();
		}


		// PARAM NAME
		String camelComponentName = ParserUtils.camelCase(paramType.name()); // camel cased, can clash with reserved
		String reservedName = RESERVED_FIELD_NAMES.getOrDefault(camelComponentName,
		                                                        camelComponentName); // checks for reserved words or defaults to camelComponentName

		if ("Context".equals(className.toString())) {
			camelComponentName = "ctx";
		} else {
			camelComponentName = ParserUtils.camelCase(paramType.name());
		}

		return new JavaParam(className,
		                     reservedName,
		                     camelComponentName,
		                     paramType.summary(),
		                     paramType.description(),
		                     typeReference.toTypeDeclaration(typeLibrary));
	}

	public static AnnotationSpec renamedFieldAnnotation(String originalName) {
		return AnnotationSpec.builder(JsonProperty.class)
		                     .addMember("value", "$S", originalName)
		                     .build();
	}

	public ParameterSpec.Builder poeticize() {
		var componentBuilder = ParameterSpec.builder(refClassName(), parameterName());
		// ANNOTATION
		if (!parameterName().equals(origParamName())) {
			// if name was changed by reserved word filter
			// save original name as annotation for Jackson
			componentBuilder.addAnnotation(renamedFieldAnnotation(origParamName()));
		}
		// JAVADOC
		componentBuilder.addJavadoc(new JavaDocs(summary(), description()).poeticize().build());
		// OK
		return componentBuilder;
	}

}
