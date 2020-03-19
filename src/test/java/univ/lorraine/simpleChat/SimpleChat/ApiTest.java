package univ.lorraine.simpleChat.SimpleChat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.Message;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.MessageService;
import static junit.framework.TestCase.assertTrue;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {



    @Autowired
    MessageService messageService;
    GroupeService groupeService;

    @Test
    public void testDeleteDrawpadMessageOfGroupe(){

        List<Message> messages = messageService.getDrawpadMessages(1L);
        assertTrue(messages.size() > 0);


    }
}
