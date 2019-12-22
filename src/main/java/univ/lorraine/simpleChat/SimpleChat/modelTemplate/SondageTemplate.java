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

	///**
	// * Est l'id du user qui souhaite ajouter le sondage
	// */
	//@JsonProperty("userId")
	//private String userId;
	
	/**
	 * Est l'id du sondage
	 */
	@JsonProperty("sondageId")
	private String sondageId;
	
	/**
	 * Est le nom de la question du sondage qu'on souhaite ajouter
	 */
	@JsonProperty("question")
	private String question;
	
	//@JsonProperty("isVotesAnonymes")
	//private String isVotesAnonymes;
	
	///**
	// * Est le nom du groupe qu'on souhaite ajouter
	// */
	//@JsonProperty("groupe")
	//private String groupe;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	//@JsonProperty("userId")
	//public String getUserId() {
	//	return userId;
	//}
	
	//@JsonProperty("userId")
	//public void setUserId(String userId) {
	//	this.userId = userId;
	//}
	
	@JsonProperty("sondageId")
	public String getSondageId() {
		return sondageId;
	}
	
	@JsonProperty("sondageId")
	public void setSondageId(String sondageId) {
		this.sondageId = sondageId;
	}
	
	@JsonProperty("question")
	public String getQuestion() {
		return question;
	}
	
	@JsonProperty("question")
	public void setQuestion(String question) {
		this.question = question;
	}
	
	//@JsonProperty("isVotesAnonymes")
	//public String getIsVotesAnonymes() {
	//	return isVotesAnonymes;
	//}
	
	//@JsonProperty("isVotesAnonymes")
	//public void setIsVotesAnonymes(String isVotesAnonymes) {
	//	this.isVotesAnonymes = isVotesAnonymes;
	//}
	
	//@JsonProperty("groupe")
	//public String getGroupe() {
	//	return groupe;
	//}
	
	//@JsonProperty("groupe")
	//public void setGroupe(String groupe) {
	//	this.groupe = groupe;
	//}
	
	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}
	
	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}