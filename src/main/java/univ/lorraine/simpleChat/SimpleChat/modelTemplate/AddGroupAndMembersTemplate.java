package univ.lorraine.simpleChat.SimpleChat.modelTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"adminGroupeId",
	"groupeName",
	"isPrivateChat",
	"members"
})
public class AddGroupAndMembersTemplate {

	@JsonProperty("adminGroupeId")
	private String adminGroupeId;
	@JsonProperty("groupeName")
	private String groupeName;
	@JsonProperty("isPrivateChat")
	private String isPrivateChat;
	@JsonProperty("members")
	private List<Object> members = new ArrayList<Object>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	@JsonProperty("adminGroupeId")
	public String getAdminGroupeId() {
		return adminGroupeId;
	}
	
	@JsonProperty("adminGroupeId")
	public void setAdminGroupeId(String adminGroupeId) {
		this.adminGroupeId = adminGroupeId;
	}
	
	@JsonProperty("groupeName")
	public String getGroupeName() {
	return groupeName;
	}
	
	@JsonProperty("groupeName")
	public void setGroupeName(String groupeName) {
		this.groupeName = groupeName;
	}
	
	@JsonProperty("isPrivateChat")
	public String getIsPrivateChat() {
		return isPrivateChat;
	}
	
	@JsonProperty("isPrivateChat")
	public void setIsPrivateChat(String isPrivateChat) {
		this.isPrivateChat = isPrivateChat;
	}
	
	@JsonProperty("members")
	public List<Object> getMembers() {
		return members;
	}
	
	@JsonProperty("members")
	public void setMembers(List<Object> members) {
		this.members = members;
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