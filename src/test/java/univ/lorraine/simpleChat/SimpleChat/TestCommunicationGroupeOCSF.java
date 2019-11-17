package univ.lorraine.simpleChat.SimpleChat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import univ.lorraine.simpleChat.SimpleChat.controller.MessageController;
import univ.lorraine.simpleChat.SimpleChat.model.Message;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.MessageService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

import java.io.StringReader;
import java.util.function.DoubleToIntFunction;

public class TestCommunicationGroupeOCSF {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupeService groupeService;

    @Autowired
    private MessageService messageService;
    @Test
    public void commEntre2Users() {
        String msg = "{\n" +
                "        \"user_id\": 10,\n" +
                "        \"group_id\": 1,\n" +
                "        \"message\": \"blablabla\"" +
                "}";
        MessageController messageController = new MessageController(userService, groupeService, messageService);
        messageController.sendMessage(msg);
        msg = "{\n" +
                "        \"user_id\": 11,\n" +
                "        \"group_id\": 1,\n" +
                "        \"message\": \"blablabla\"" +
                "}";
        messageController.sendMessage(msg);
        String response1 = String.valueOf(messageController.byName(1, 10));
        System.out.println(response1);
        String response2 = String.valueOf(messageController.byName(1, 11));
        System.out.println(response2);
        response1 = String.valueOf(messageController.byName(1, 10));
        System.out.println(response1);
    }
}
