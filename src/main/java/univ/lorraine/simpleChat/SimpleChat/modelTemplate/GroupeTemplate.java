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
"groupe",
"isPrivateChat"
})
public class GroupeTemplate {

	/**
	 * Est l'id du user qui souhaite ajouter le groupe
	 */
	@JsonProperty("userId")
	private String userId;
	/**
	 * Est le nom du groupe qu'on souhaite ajouter
	 */
	@JsonProperty("groupe")
	private String groupe;
	
	@JsonProperty("isPrivateChat")
	private String isPrivateChat;
	
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
	
	@JsonProperty("groupe")
	public String getGroupe() {
		return groupe;
	}
	
	@JsonProperty("groupe")
	public void setGroupe(String groupe) {
		this.groupe = groupe;
	}
	
	@JsonProperty("isPrivateChat")
	public String getIsPrivateChat() {
		return isPrivateChat;
	}
	
	@JsonProperty("isPrivateChat")
	public void setIsPrivateChat(String isPrivateChat) {
		this.isPrivateChat = isPrivateChat;
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