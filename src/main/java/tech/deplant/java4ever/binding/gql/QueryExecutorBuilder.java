package tech.deplant.java4ever.binding.gql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import tech.deplant.java4ever.binding.JsonContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryExecutorBuilder {

	String method;

	String fields;

	List<String> args = new ArrayList<>();

	public QueryExecutorBuilder(String method, String fields) {
		this.method = method;
		this.fields = fields;
	}

	public <T> void addToQuery(String name, T someValue) {
		var mapper = JsonContext.ABI_JSON_MAPPER()
		                        .configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false)
		                        .setSerializationInclusion(JsonInclude.Include.NON_NULL);
		try {
			Map<String,T> mapping = Map.of(name, someValue);
			this.args.add(mapper.writeValueAsString(mapping));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> void addToQuery(T someValue) {
		var mapper = JsonContext.ABI_JSON_MAPPER()
		                        .configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false)
		                        .setSerializationInclusion(JsonInclude.Include.NON_NULL);;
		try {
			this.args.add(mapper.writeValueAsString(someValue));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}


	public String toGraphQLQuery() {
		return """
            query {
              %s( %s ){
                %s
              }
            }
            """.formatted(this.method, String.join(", ", args), this.fields);
	}

}
