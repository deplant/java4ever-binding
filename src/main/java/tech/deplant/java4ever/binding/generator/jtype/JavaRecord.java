package tech.deplant.java4ever.binding.generator.jtype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tech.deplant.java4ever.binding.generator.ParserEngine;
import tech.deplant.java4ever.binding.generator.javapoet.*;
import tech.deplant.java4ever.binding.generator.reference.ApiType;
import tech.deplant.java4ever.binding.generator.reference.StructType;
import tech.deplant.java4ever.utils.Objs;

import javax.lang.model.element.Modifier;
import java.util.Map;

public record JavaRecord(StructType originalType,
                         String name,
                         ParserEngine.SdkInterfaceParent superInterface,
                         boolean isSimpleWrapper,
                         boolean isParams,
                         boolean isResult,
                         Map<ParserEngine.SdkType, JavaType> typeLibrary) implements JavaType {


	private final static System.Logger logger = System.getLogger(JavaRecord.class.getName());

	public static JavaRecord ofApiType(StructType struct,
	                                   Map<ParserEngine.SdkType, JavaType> typeLibrary,
	                                   ParserEngine.SdkInterfaceParent superInterface) {
		boolean isParams = false;
		boolean isResult = false;
		if (struct.name().length() >= 8) {
			isParams = "ParamsOf".equals(struct.name().substring(0, 8));
			isResult = "ResultOf".equals(struct.name().substring(0, 8));
		}
		boolean isSimpleWrapper = struct.struct_fields().length == 1;
		return new JavaRecord(struct,
		                      struct.name(),
		                      superInterface,
		                      isSimpleWrapper,
		                      isParams,
		                      isResult,
		                      typeLibrary);
	}

	@Override
	public TypeSpec.Builder poeticize() {
		// METADATA
		TypeSpec.Builder structBuilder = TypeSpec
				.recordBuilder(this.originalType.name())
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
		// RECORD PARAMS
		for (ApiType component : this.originalType.struct_fields()) {
			structBuilder
					.addRecordComponent(JavaParam.ofApiType(component, typeLibrary()).poeticize().build());
		}
		// JAVADOC
		structBuilder.addJavadoc(new JavaDocs(originalType().summary(), originalType().description()).poeticize()
		                                                                                             .build());
		// for records that are implementing interfaces of EnumOfTypes
		if (Objs.isNotNull(superInterface())) {
			// if record is a subtype of EnumOfTypes
			// it should implement super interface
			structBuilder.addSuperinterface(ClassName.bestGuess(superInterface().name()));
			// and have special type() getter
			//structBuilder.addMethod(virtualTypeField(originalType().name()).build());
			structBuilder.addMethod(virtualTypeField(superInterface().variantName()).build());
			structBuilder.addJavadoc(CodeBlock.builder().add("{@inheritDoc}").build());
		}

		// for AbiEvent we should add special Jackson annotation to ignore 'outputs' tag.
		// It shouldn't be deserialized as it's deprecated and unused,
		// but still it's presented in multisig contracts, so we should not throw error.
		if ("AbiEvent".equals(name())) {
			structBuilder.addAnnotation(AnnotationSpec.builder(JsonIgnoreProperties.class)
			                                          .addMember("value", "$S", "outputs")
			                                          .build());
		}

		return structBuilder;
	}

	@Override
	public ApiType type() {
		return originalType();
	}

	@Override
	public boolean isFlatType() {
		return false;
	}

	@Override
	public boolean isStructure() {
		return true;
	}

	/**
	 * Method that generates virtual type() getter that is used to generate correct JSON.
	 *
	 * @param typeName stringified name of subclass
	 * @return builder of getter method spec
	 */
	private MethodSpec.Builder virtualTypeField(String typeName) {
		return MethodSpec
				.methodBuilder("type")
				.addModifiers(Modifier.PUBLIC)
				.returns(ClassName.STRING)
				.addStatement("return \"" + typeName + "\"")
				.addAnnotation(JavaParam.renamedFieldAnnotation("type"));
	}

	public JavaRecord withSuperInterface(ParserEngine.SdkInterfaceParent superInterface) {
		return new JavaRecord(
				originalType(),
				name(),
				superInterface,
				isSimpleWrapper(),
				isParams(),
				isResult(),
				typeLibrary()
		);
	}
}
