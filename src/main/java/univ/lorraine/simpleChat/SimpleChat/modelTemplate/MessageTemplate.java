package univ.lorraine.simpleChat.SimpleChat.modelTemplate;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "user_id",
        "groupe_id",
        "message"
})
public class MessageTemplate {
    @JsonProperty("user_id")
    private Long user_id;

    @JsonProperty("group_id")
    private Long group_id;

    @JsonProperty("message")
    private String message;

    @JsonProperty("user_id")
    public Long getUser_id() {
        return user_id;
    }

    @JsonProperty("user_id")
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    @JsonProperty("group_id")
    public Long getGroup_id() {
        return group_id;
    }

    @JsonProperty("group_id")
    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "{user_id:" + user_id +
                ", group_id:" + group_id +
                ", message:\"" + message + "\"" +
                '}';
    }
}
