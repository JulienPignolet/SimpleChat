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
	"groupId",
	"userDelId"
})
public class DeleteUserInGroupTemplate {

	@JsonProperty("userId")
	private String userId;
	@JsonProperty("groupId")
	private String groupId;
	@JsonProperty("userDelId")
	private String userDelId;
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
	
	@JsonProperty("groupId")
	public String getGroupId() {
		return groupId;
	}
	
	@JsonProperty("groupId")
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@JsonProperty("userDelId")
	public String getUserDelId() {
		return userDelId;
	}
	
	@JsonProperty("userDelId")
	public void setUserDelId(String userDelId) {
		this.userDelId = userDelId;
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