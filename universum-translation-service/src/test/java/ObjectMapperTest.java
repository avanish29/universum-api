import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ObjectMapperTest {
	public static class DynamicEntityRequest {
		private Map<String, Object> formFields = new HashMap<>();
		
		@JsonValue
		public Map<String, Object> getFormFields() {
			return formFields;
		}
		
		public void setFormFields(Map<String, Object> formFields) {
			this.formFields = formFields;
		}
		
		@JsonAnySetter
		public void addField(final String fieldName, final Object fieldValue) {
			this.formFields.put(fieldName, fieldValue);
		}
		
		@SuppressWarnings("unchecked")
		public void addMultiLingualEntity(final String entityName, final MultiLingualEntity multiLingualEntity) {
			((List<MultiLingualEntity>)this.formFields.computeIfAbsent(entityName, enitities -> new ArrayList<MultiLingualEntity>())).add(multiLingualEntity);
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class MultiLingualEntity {
		private String cfTrans;
		private String name;
		private String landCode;
	}
	
	
	public static void main(String[] args) throws Exception {
		DynamicEntityRequest response = new DynamicEntityRequest();
		response.addField("cfCountry.alpha2Code", "AF");
		response.addField("cfCountry.alpha3Code", "AFG");
		response.addField("cfCountry.code", "008");
		response.addMultiLingualEntity("cfCountryName", new MultiLingualEntity("O", "Afghanistan", "en_GB"));
		response.addMultiLingualEntity("cfCountryName", new MultiLingualEntity("او", "افغانستان", "ar_LB"));
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
		System.out.println(jsonString);
		
		DynamicEntityRequest request = mapper.readValue(jsonString, DynamicEntityRequest.class);
		System.out.println(request);
	}
}
