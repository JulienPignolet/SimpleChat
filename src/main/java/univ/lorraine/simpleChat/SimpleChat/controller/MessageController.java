
package univ.lorraine.simpleChat.SimpleChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.ocsf.ClientRunnable;
import univ.lorraine.simpleChat.SimpleChat.ocsf.Message;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.MessageService;
import univ.lorraine.simpleChat.SimpleChat.service.SecurityService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

import java.util.HashMap;

@Controller
public class MessageController {
    private final UserService userService;

    private final GroupeService groupeService;

    private final MessageService messageService;

    private HashMap<Long, ClientRunnable> clientPool = new HashMap<>();

    @Autowired
    public MessageController(UserService userService, GroupeService groupeService, MessageService messageService) {
        this.userService = userService;
        this.groupeService = groupeService;
        this.messageService = messageService;
    }

    @PostMapping("/group/{id}/message")
    public ResponseEntity<Object> sendMessage(@RequestBody String msg)
    {
        try {
            Message JSON = new Message(msg);
            User user = userService.findById(JSON.getGroup_id());
            if(!clientPool.containsKey(JSON.getGroup_id()))
            {
                clientPool.put(JSON.getGroup_id(), new ClientRunnable(JSON.getGroup_id()));
                clientPool.get(JSON.getGroup_id()).start();
            }
            clientPool.get(JSON.getGroup_id()).sendMsg(msg);

            // sauvegarde
            Groupe groupe = groupeService.find(JSON.getGroup_id());
            messageService.save(
                    new univ.lorraine.simpleChat.SimpleChat.model.Message(
                            JSON.getMessage(),user,groupe));
//            user.sendMsg(msg);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/group/{id}/messages")
    public ResponseEntity<Object> byName(@PathVariable(value = "id") Long id)
    {
        User user = userService.findById(id);
        if(!clientPool.containsKey(id))
            return new ResponseEntity<Object>("{}", HttpStatus.NO_CONTENT);
        String messages = clientPool.get(id).getMessagesEnAttente();
        return new ResponseEntity<Object>(messages, HttpStatus.OK);
    }
}