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
	"groupId",
	"userId"
})
public class DeleteGroupTemplate {
	
	@JsonProperty("groupId")
	private String groupId;
	@JsonProperty("userId")
	private String userId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	@JsonProperty("groupId")
	public String getGroupId() {
		return groupId;
	}
	
	@JsonProperty("groupId")
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@JsonProperty("userId")
	public String getUserId() {
		return userId;
	}
	
	@JsonProperty("userId")
	public void setUserId(String userId) {
		this.userId = userId;
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