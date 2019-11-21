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
"groupeId",
"userId",
"adminGroupeId"
})
public class AddMemberTemplate {

	@JsonProperty("groupeId")
	private String groupeId;
	@JsonProperty("userId")
	private String userId;
	@JsonProperty("adminGroupeId")
	private String adminGroupeId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	@JsonProperty("groupeId")
	public String getGroupeId() {
		return groupeId;
	}
	
	@JsonProperty("groupeId")
	public void setGroupeId(String groupeId) {
		this.groupeId = groupeId;
	}
	
	@JsonProperty("userId")
	public String getUserId() {
		return userId;
	}
	
	@JsonProperty("userId")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@JsonProperty("adminGroupeId")
	public String getAdminGroupeId() {
		return adminGroupeId;
	}
	
	@JsonProperty("adminGroupeId")
	public void setAdminGroupeId(String adminGroupeId) {
		this.adminGroupeId = adminGroupeId;
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