package univ.lorraine.simpleChat.SimpleChat.ocsf;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Message {
    private Long user_id;
    private Long group_id;
    private String message;

    private Message(Long user_id, Long group_id, String message) {
        this.user_id = user_id;
        this.group_id = group_id;
        this.message = message;
    }

    public Message(String JSON) {
        JsonObject objet = new JsonParser().parse(JSON).getAsJsonObject();
        this.user_id = objet.get("user_id").getAsLong();
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
        return "{user_id:" + user_id +
                ", group_id:" + group_id +
                ", message:'" + message + "\"" +
                '}';
    }

    public String getMessage() {
        return message;
    }
}
