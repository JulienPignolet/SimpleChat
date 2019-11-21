package univ.lorraine.simpleChat.SimpleChat.modelTemplate;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"userId",
"role"
})
public class RoleTemplate {

	/**
	 * L'id du user qui souhaite ajouter un role
	 */
	@JsonProperty("userId")
	private String userId;
	
	/**
	 * Il s'agit du rôle : ex. ROLE_USER
	 */
	@JsonProperty("role")
	private String role;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	@JsonProperty("userId")
	public String getUserId() {
		return userId;
	}
	
	@JsonProperty("userId")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@JsonProperty("role")
	public String getRole() {
		return role;
	}
	
	@JsonProperty("role")
	public void setRole(String role) {
		this.role = role;
	}
	
	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}
	
	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}