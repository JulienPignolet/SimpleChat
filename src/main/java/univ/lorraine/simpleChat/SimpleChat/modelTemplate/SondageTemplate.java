package univ.lorraine.simpleChat.SimpleChat.modelTemplate;

import java.util.Collection;
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
"question",
"isVotesAnonymes",
"groupeId"
})
public class SondageTemplate {

	@JsonProperty("userId")
	private String userId;

	@JsonProperty("question")
	private String question;
	
	@JsonProperty("votesAnonymes")
	private String votesAnonymes;

	@JsonProperty("groupeId")
	private String groupeId;
	
	@JsonProperty("dateFin")
	private String dateFin;

	@JsonProperty("reponsesSondage")
	private String[] reponsesSondage;

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
	
	@JsonProperty("votesAnonymes")
	public String getVotesAnonymes() {
		return votesAnonymes;
	}
	
	@JsonProperty("votesAnonymes")
	public void setIsVotesAnonymes(String votesAnonymes) {
		this.votesAnonymes = votesAnonymes;
	}
	
	@JsonProperty("groupeId")
	public String getGroupeId() {
		return groupeId;
	}
	
	@JsonProperty("groupeId")
	public void setGroupeId(String groupeId) {
		this.groupeId = groupeId;
	}
	
	@JsonProperty("dateFin")
	public String getDateFin()
	{
		return dateFin;
	}
	
	@JsonProperty("dateFin")
	public void setDateFin(String dateFin)
	{
		this.dateFin = dateFin;
	}
	
	@JsonProperty("reponsesSondage")
	public String[] getReponsesSondage() {
		return reponsesSondage;
	}
	
	@JsonProperty("reponsesSondage")
	public void setReponsesSondage(String[] reponsesSondage)
	{
		this.reponsesSondage = reponsesSondage;
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