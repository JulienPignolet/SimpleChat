package univ.lorraine.simpleChat.SimpleChat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import univ.lorraine.simpleChat.SimpleChat.controller.MessageController;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeUserService;
import univ.lorraine.simpleChat.SimpleChat.service.MessageService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

public class TestCommunicationGroupeOCSF {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupeService groupeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GroupeUserService groupeUserService;
    @Test
    public void commEntre2Users() {
        String msg = "{\n" +
                "        \"user_id\": 10,\n" +
                "        \"group_id\": 1,\n" +
                "        \"message\": \"blablabla\"" +
                "}";
        MessageController messageController = new MessageController(userService, groupeService, messageService, groupeUserService);
//        messageController.sendMessage(msg);
        msg = "{\n" +
                "        \"user_id\": 11,\n" +
                "        \"group_id\": 1,\n" +
                "        \"message\": \"blablabla\"" +
                "}";
//        messageController.sendMessage(msg);
        String response1 = String.valueOf(messageController.byName(1L, 10L));
        System.out.println(response1);
        String response2 = String.valueOf(messageController.byName(1L, 11L));
        System.out.println(response2);
        System.out.println(response1);
    }
}
