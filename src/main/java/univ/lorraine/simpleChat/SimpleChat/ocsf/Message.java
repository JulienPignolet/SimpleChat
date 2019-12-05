package univ.lorraine.simpleChat.SimpleChat.ocsf;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Message {
    private Long user_id;
    private String user_name;
    private Long group_id;
    private String message;

    private Message(Long user_id, String user_name , Long group_id, String message) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.group_id = group_id;
        this.message = message;
    }

    public Message(String json) {
		JsonObject objet = new JsonParser().parse(json).getAsJsonObject();
        this.user_id = objet.get("user_id").getAsLong();
        this.user_name = objet.get("user_name").getAsString();
        this.group_id = objet.get("group_id").getAsLong();
        this.message = objet.get("message").getAsString();
    }

    public Long getUser_id() {
        return user_id;
    }

    public Long getGroup_id() {
        return group_id;
    }

    @Override
    public String toString() {
        return "{\"user_id\":" + user_id +
        		", \"user_name\":" + user_name +
                ", \"group_id\":" + group_id +
                ", \"message\":\"" + message + "\"}";
    }

    public String getMessage() {
        return message;
    }
}
