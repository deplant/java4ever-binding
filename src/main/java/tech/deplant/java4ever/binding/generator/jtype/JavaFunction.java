package tech.deplant.java4ever.binding.generator.jtype;

import tech.deplant.java4ever.binding.AppSigningBox;
import tech.deplant.java4ever.binding.Context;
import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.binding.Unstable;
import tech.deplant.java4ever.binding.generator.ParserEngine;
import tech.deplant.java4ever.binding.generator.ParserUtils;
import tech.deplant.java4ever.binding.generator.TypeReference;
import tech.deplant.java4ever.binding.generator.javapoet.AnnotationSpec;
import tech.deplant.java4ever.binding.generator.javapoet.ClassName;
import tech.deplant.java4ever.binding.generator.javapoet.CodeBlock;
import tech.deplant.java4ever.binding.generator.javapoet.MethodSpec;
import tech.deplant.java4ever.binding.generator.reference.ApiFunction;
import tech.deplant.java4ever.binding.generator.reference.ApiType;
import tech.deplant.java4ever.binding.generator.reference.StructType;
import tech.deplant.java4ever.utils.Objs;
import tech.deplant.java4ever.utils.Strings;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record JavaFunction(String functionModule,
                           ApiFunction function,
                           //JavaType functionReturn,
                           Map<ParserEngine.SdkType, JavaType> typeLibrary) {

	private final static System.Logger logger = System.getLogger(JavaFunction.class.getName());

	private String constructCallParams(MethodSpec.Builder methodBuilder,
	                                   List<Object> statementArgs,
	                                   JavaParam param) {
		StringBuilder template = new StringBuilder();
		if (Objs.isNotNull(param.libType()) &&
		    param.libType().isStructure() &&
		    Strings.safeSubstrEquals(param.libType().name(), 0, 8, false, "ParamsOf")
		) {
			StructType structType = (StructType) param.libType().type();
			template.append("new $T(");
			statementArgs.add(param.refClassName());

			String innerFields = Arrays
					.stream(structType.struct_fields())
					.map(
							apiType -> constructCallParams(methodBuilder,
							                               statementArgs,
							                               JavaParam.ofApiType(apiType,
							                                                   typeLibrary())))
					.collect(Collectors.joining(", "));
			template.append(innerFields);
			template.append(")");
		} else {
			template.append("$N");
			var spec = param.poeticize().build();
			methodBuilder.addParameter(spec);
			statementArgs.add(spec);
		}
		return template.toString();
	}

	public MethodSpec.Builder poeticize() {

		// METADATA
		var methodBuilder = MethodSpec
				.methodBuilder(ParserUtils.camelCase(function().name()))
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.addException(ClassName.get(EverSdkException.class));

		// BODY
		// Args for all call params
		List<Object> statementArgs = new ArrayList<>();
		// Context param
		methodBuilder.addParameter(ClassName.get(Context.class), "ctx");
		// Function SDK name
		// adds function name as a first arg to statementArgs array
		statementArgs.add(String.format("%s.%s", functionModule().toLowerCase(), function().name()));
		// call template for all variants
		String templateString = "%RETURN_KEY%ctx.%CALL_TYPE%($S, %PARAMS%%APP_OBJ%%RETURN_CLASS%)";

		for (ApiType param : function().params()) {
			logger.log(System.Logger.Level.DEBUG, function().name() + "\\" + param.name() + "\\" + param.type());
			JavaParam parsedParam = JavaParam.ofApiType(param, typeLibrary());
			switch (param.name()) {
				case "context", "_context" -> {
					// we're always adding context, so no reason to do something
				}
				case "params" -> {
					templateString = templateString.replace("%PARAMS%", constructCallParams(methodBuilder,
					                                                                        statementArgs,
					                                                                        parsedParam));
				}
				case "app_object", "password_provider" -> {
					templateString = templateString.replace("%APP_OBJ%", ", appObject");
					templateString = templateString.replace("%CALL_TYPE%", "callAppObject");
					methodBuilder.addParameter(ClassName.get(AppSigningBox.class), "appObject");
				}
				case default -> logger.log(System.Logger.Level.ERROR, "Unknown parameter: " + param.name());
			}
		}

		templateString = templateString.replace("%APP_OBJ%", "");
		templateString = templateString.replace("%PARAMS%", "null");

		if (Objs.isNotNull(function().result())) {
			var resultReference = TypeReference.fromApiType(function().result());
			if (!resultReference.isVoid()) {
				//
				templateString = templateString.replace("%RETURN_KEY%", "return ");
				templateString = templateString.replace("%CALL_TYPE%", "call");
				templateString = templateString.replace("%RETURN_CLASS%", ", $T.class");
				var typeName = resultReference.toTypeName();
				// adds return class to method builder
				methodBuilder.returns(typeName);
				// adds return class as a final arg to statementArgs array
				statementArgs.add(typeName);
			}
		}
		// void result
		templateString = templateString.replace("%RETURN_KEY%", "");
		templateString = templateString.replace("%CALL_TYPE%", "callVoid");
		templateString = templateString.replace("%RETURN_CLASS%", "");

		logger.log(System.Logger.Level.DEBUG, "Template: " + templateString);
		statementArgs.forEach(arg -> logger.log(System.Logger.Level.DEBUG, "Arg[]: " + arg.toString()));

		methodBuilder.addCode(CodeBlock
				                      .builder()
				                      .addStatement(
						                      templateString,
						                      statementArgs.toArray())
				                      .build());

		// check for Unstable notation in comments
		if ((Objs.isNotNull(function().summary()) && function().summary().toUpperCase().contains("UNSTABLE")) ||
		    (Objs.isNotNull(function().description()) && function().description().toUpperCase().contains("UNSTABLE"))) {
			methodBuilder.addAnnotation(AnnotationSpec.builder(Unstable.class).build());
		}

		// check for Deprecated notation in comments
		if ((Objs.isNotNull(function().summary()) && function().summary().toUpperCase().contains("DEPRECATED")) ||
		    (Objs.isNotNull(function().description()) &&
		     function().description().toUpperCase().contains("DEPRECATED"))) {
			methodBuilder.addAnnotation(AnnotationSpec.builder(Deprecated.class).build());
		}

		// JAVADOC
		methodBuilder.addJavadoc(new JavaDocs(function().summary(), function().description()).poeticize().build());

		return methodBuilder;
	}
}