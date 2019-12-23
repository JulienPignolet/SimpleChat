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
"sondageId",
"question"
})
public class SondageTemplate {

	/**
	 * Est l'id du user qui souhaite ajouter le sondage
	 */
	@JsonProperty("userId")
	private String userId;

	// TODO ajouter date debut, date fin
	/**
	 * Est le nom de la question du sondage qu'on souhaite ajouter
	 */
	@JsonProperty("question")
	private String question;
	
	@JsonProperty("isVotesAnonymes")
	private String isVotesAnonymes;
	
	/**
	 * Est l'id du groupe où l'on ajoute le sondage
	 */
	@JsonProperty("groupeId")
	private String groupeId;

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

	@JsonProperty("question")
	public String getQuestion() {
		return question;
	}

	@JsonProperty("question")
	public void setQuestion(String question) {
		this.question = question;
	}
	
	@JsonProperty("isVotesAnonymes")
	public String getIsVotesAnonymes() {
		return isVotesAnonymes;
	}
	
	@JsonProperty("isVotesAnonymes")
	public void setIsVotesAnonymes(String isVotesAnonymes) {
		this.isVotesAnonymes = isVotesAnonymes;
	}
	
	@JsonProperty("groupe")
	public String getGroupeId() {
		return groupeId;
	}
	
	@JsonProperty("groupe")
	public void setGroupeId(String groupeId) {
		this.groupeId = groupeId;
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