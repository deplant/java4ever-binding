package tech.deplant.java4ever.binding.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.ContextBuilder;
import tech.deplant.java4ever.binding.generator.javapoet.JavaFile;
import tech.deplant.java4ever.binding.generator.javapoet.TypeSpec;
import tech.deplant.java4ever.binding.generator.jtype.*;
import tech.deplant.java4ever.binding.generator.reference.*;
import tech.deplant.java4ever.binding.io.JsonResource;
import tech.deplant.java4ever.utils.Objs;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ParserEngine {

	private final static System.Logger logger = System.getLogger(ParserEngine.class.getName());

	public static ApiReference ofJsonResource(String resourceName) throws JsonProcessingException {
		return ContextBuilder.DEFAULT_MAPPER.readValue(new JsonResource(resourceName).get(),
		                                               ApiReference.class);
	}

//	public static ApiReference ofEverSdkLibrary(LibraryLoader loader) throws JsonProcessingException, EverSdkException {
//		var ctx = new ContextBuilder()
//				.setConfigJson("{}")
//				.buildNew(AbsolutePathLoader.ofSystemEnv("TON_CLIENT_LIB"));
//		return ContextBuilder.DEFAULT_MAPPER.convertValue(Client.getApiReference(ctx).api(), ApiReference.class);
//	}

	public static void parse(ApiReference parsedApiReference) throws IOException {

		final String apiVersion = parsedApiReference.version();
		// library to store all parsed types
		// they'll be used later to construct correct method params and bodies
		final Map<SdkType, JavaType> typeLibrary = new HashMap<>();
		// library to store references to child classes from EnumOfTypes
		final Map<SdkType, SdkInterfaceParent> eotReferences = new HashMap<>();

		// type parsing loop
		// we parse all modules to get info about all possible types
		// that's because function of one module can depend on type from other module
		// so we need to know about other modules types
		for (var module : parsedApiReference.modules()) {
			String moduleCapitalName = ParserUtils.capitalize(module.name());

			// type parsing loop
			for (ApiType type : module.types()) {
				switch (type) {
					case EnumOfConsts en -> {
						var jEnum = new JavaEnum(en.name(),
						                         en.enum_consts(),
						                         new JavaDocs(en.summary(), en.description()));
						typeLibrary.put(new SdkType(moduleCapitalName, en.name()), jEnum);
					}
					case StructType struct -> {
						boolean isParams = false;
						boolean isResult = false;
						if (struct.name().length() >= 8) {
							isParams = "ParamsOf".equals(struct.name().substring(0, 8));
							isResult = "ResultOf".equals(struct.name().substring(0, 8));
						}
						boolean isSimpleWrapper = struct.struct_fields().length == 1;
						typeLibrary.put(new SdkType(moduleCapitalName, struct.name()), new JavaRecord(struct,
						                                                                              struct.name(),
						                                                                              null,
						                                                                              isSimpleWrapper,
						                                                                              isParams,
						                                                                              isResult,
						                                                                              typeLibrary));
					}
					case EnumOfTypes eot -> {
						List<JavaRecord> records = new ArrayList<>();
						// patch for 'Abi' interface that messes with module with the same name
						String interfaceName = "Abi".equals(eot.name()) ? "ABI" : eot.name();
						for (ApiType eotChildType : eot.enum_types()) {
							switch (eotChildType) {
								case StructType str -> records.add(JavaRecord.ofApiType(str,
								                                                        typeLibrary,
								                                                        new SdkInterfaceParent(
										                                                        moduleCapitalName,
										                                                        interfaceName,
										                                                        str.name())));
								case RefType ref -> eotReferences.put(TypeReference.fromApiType(ref).toSdkType(),
								                                      new SdkInterfaceParent(moduleCapitalName,
								                                                             interfaceName,
								                                                             ref.name()));
								default -> throw new IllegalStateException(
										"Unexpected value: " + eotChildType);
							}
						}

						var javaInterface = new JavaInterface(eot,
						                                      interfaceName,
						                                      new JavaDocs(eot.summary(), eot.description()),
						                                      records);
						typeLibrary.put(new SdkType(moduleCapitalName, interfaceName), javaInterface);
					}
					default -> {
						typeLibrary.put(new SdkType(moduleCapitalName, type.name()), new JavaDummy(type));
					}
				}
			}
		}

		// add references from EOT interfaces
		eotReferences.forEach((typ, sup) ->
				                      Objs.notNullDo(
						                      typeLibrary.get(typ),
						                      jtype -> typeLibrary.put(typ,
						                                               ((JavaRecord) jtype).withSuperInterface(sup))
				                      ));

		// main file building loop
		// loops modules again, now to write them
		for (var module : parsedApiReference.modules()) {
			final TypeSpec.Builder moduleBuilder = ParserOfModule.classOfModule(module,
			                                                                    ParserUtils.capitalize(module.name()),
			                                                                    apiVersion);
			String moduleCapitalName = ParserUtils.capitalize(module.name());

			// let's find types that correspond to module in library
			var types = typeLibrary.entrySet().stream().filter(
					entry -> moduleCapitalName.equals(entry.getKey().module())

			).map(Map.Entry::getValue).toList();

			eotReferences.forEach((typ, sup) -> {
			});

			// let's write these types
			for (JavaType typeRef : types) {
				var builder = typeRef.poeticize();
				if (!Objects.isNull(builder)) {
					moduleBuilder.addType(builder.build());
				}
			}

			// function writing loop
			// functions receive full lib info
			for (ApiFunction function : module.functions()) {
				moduleBuilder.addMethod(new JavaFunction(module.name().toLowerCase(),
				                                         function,
				                                         typeLibrary).poeticize()
				                                                     .build());
			}

			// file writing loop
			JavaFile javaFile = JavaFile
					.builder("tech.deplant.java4ever.binding", moduleBuilder.build())
					.build();
			javaFile.writeTo(Paths.get("src/gen/java"));
		}
	}

	public record SdkType(String module, String name) {
	}

	public record SdkInterfaceParent(String module, String name, String variantName) {
	}

}
