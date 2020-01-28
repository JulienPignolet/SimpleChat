package univ.lorraine.simpleChat.SimpleChat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import univ.lorraine.simpleChat.SimpleChat.controller.MessageController;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.MessageTemplate;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeUserService;
import univ.lorraine.simpleChat.SimpleChat.service.MessageService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCommunicationGroupeOCSF {

    @Autowired
    MessageController messageController;
    @Test
    public void commEntre2Users() {
        messageController.addUserToOCSFClient(77L, 103L);
        messageController.addUserToOCSFClient(77L, 104L);
        messageController.addUserToOCSFClient(78L, 103L);
        messageController.addUserToOCSFClient(78L, 104L);
        messageController.addUserToOCSFClient(79L, 103L);
        messageController.addUserToOCSFClient(79L, 104L);

        MessageTemplate msg = new MessageTemplate();
        msg.setUser_id(103L);
        msg.setGroup_id(77L);
        msg.setMessage("Message 1");
        msg.setUrl(false);

        messageController.sendMessage(msg);

        String response1 = String.valueOf(messageController.getLiveMessages(77L, 103L).getBody());
        assertEquals("{ \"buffer\":[{\"user_id\":103, \"group_id\":77, \"message\":\"Message 1\", \"is_url\":false}]}", response1);
        String response2 = String.valueOf(messageController.getLiveMessages(78L, 104L).getBody());
        assertEquals("{\"buffer\":[]}", response2);
        String response3 = String.valueOf(messageController.getLiveMessages(79L, 104L).getBody());
        assertEquals("{\"buffer\":[]}", response3);

        msg.setUser_id(104L);
        msg.setGroup_id(78L);
        msg.setMessage("https://www.google.fr/");

        messageController.sendMessage(msg);

        response1 = String.valueOf(messageController.getLiveMessages(78L, 103L).getBody());
        assertEquals("{ \"buffer\":[{\"user_id\":104, \"group_id\":78, \"message\":\"https://www.google.fr/\", \"is_url\":true}]}", response1);
        response2 = String.valueOf(messageController.getLiveMessages(78L, 104L).getBody());
        assertEquals("{ \"buffer\":[{\"user_id\":104, \"group_id\":78, \"message\":\"https://www.google.fr/\", \"is_url\":true}]}", response2);

        response1 = String.valueOf(messageController.getLiveMessages(79L, 103L).getBody());
        assertEquals("{\"buffer\":[]}", response1);
        response2 = String.valueOf(messageController.getLiveMessages(79L, 104L).getBody());
        assertEquals("{\"buffer\":[]}", response2);

        response1 = String.valueOf(messageController.getLiveMessagesAdmin().getBody());
        assertEquals("{ \"buffer\":[{\"user_id\":103, \"group_id\":77, \"message\":\"Message 1\", \"is_url\":false},{\"user_id\":104, \"group_id\":78, \"message\":\"https://www.google.fr/\", \"is_url\":true}]}", response1);
    }
}
