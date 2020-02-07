
package univ.lorraine.simpleChat.SimpleChat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.MessageTemplate;
import univ.lorraine.simpleChat.SimpleChat.ocsf.AutorisationException;
import univ.lorraine.simpleChat.SimpleChat.ocsf.MessageOCSF;
import univ.lorraine.simpleChat.SimpleChat.ocsf.admin.AdminClientRunnable;
import univ.lorraine.simpleChat.SimpleChat.ocsf.groupe.GroupeClientRunnable;
import univ.lorraine.simpleChat.SimpleChat.model.Message;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeUserService;
import univ.lorraine.simpleChat.SimpleChat.service.MessageService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/message")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api( value="Simple Chat")
public class MessageController {

    Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final UserService userService;
    private final GroupeService groupeService;
    private final MessageService messageService;
    private final GroupeUserService groupeUserService;

    private Map<Long, GroupeClientRunnable> clientPool = new HashMap<>();
    private AdminClientRunnable adminClient = new AdminClientRunnable();

    public MessageController(UserService userService, GroupeService groupeService, MessageService messageService, GroupeUserService groupeUserService) {
        this.userService = userService;
        this.groupeService = groupeService;
        this.messageService = messageService;
        this.groupeUserService = groupeUserService;
        adminClient.start();
    }

    @ApiOperation(value = "Envoie un message")
    @PostMapping("/send")
    public ResponseEntity<Object> sendMessage(@RequestBody MessageTemplate message)
    {
        try (Jsonb jsonb = JsonbBuilder.create())
        {
            User user = userService.findById(message.getUserId());
            if(groupeUserService.CountByGroupeIdAndUserId(message.getGroupId(), message.getUserId())) {
                if (!clientPool.containsKey(message.getGroupId())) {
                    clientPool.put(message.getGroupId(), new GroupeClientRunnable(message.getGroupId()));
                    clientPool.get(message.getGroupId()).start();
                }
                clientPool.get(message.getGroupId()).addUserToGroup(message.getUserId());

                String jsonMessage = jsonb.toJson(message);

                MessageOCSF newMessageOCSF = jsonb.fromJson(jsonMessage, MessageOCSF.class);

                newMessageOCSF.setUserName(userService.findById(newMessageOCSF.getUserId()).getUsername());

                clientPool.get(message.getGroupId()).sendMsg(newMessageOCSF);
                // Sauvegarde
                Groupe groupe = groupeService.find(message.getGroupId());
                messageService.save( new Message(message.getContenu(), user, groupe, message.getType()));
            }
        }
        catch(Exception e)
        {
            logger.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Retourne tous les messages reçus par le client OCSF")
    @GetMapping("/live/{idGroupe}/{idUser}")
    public ResponseEntity<Object> getLiveMessages(@PathVariable(value = "idGroupe") Long idGroupe
            , @PathVariable(value="idUser") Long idUser)
    {
        try {
            if (groupeUserService.CountByGroupeIdAndUserId(idGroupe, idUser)) {
                if (!clientPool.containsKey(idGroupe)) {
                    clientPool.put(idGroupe, new GroupeClientRunnable(idGroupe));
                    clientPool.get(idGroupe).start();
                }

                GroupeClientRunnable groupeClientRunnable = clientPool.get(idGroupe);
                groupeClientRunnable.addUserToGroup(idUser);

                String messagesEnAttente = groupeClientRunnable.getMessagesEnAttente(idUser);
                groupeClientRunnable.viderBuffer(idUser);
                return new ResponseEntity<>(messagesEnAttente, HttpStatus.OK);
            }
        }
        catch (AutorisationException e) {
            logger.info(e.getMessage());
            return new ResponseEntity<>("{}", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "Retourne tous les messages enregistrés")
    @GetMapping("/saved/{idGroupe}/{idUser}")
    public ResponseEntity<Object> getSavedMessages(@PathVariable(value = "idGroupe") Long idGroupe, @PathVariable(value="idUser") Long idUser)
    {
        try {
            if (groupeUserService.CountByGroupeIdAndUserId(idGroupe, idUser)) {
                String messagesEnAttente = messageService.get(idGroupe);
                return new ResponseEntity<>(messagesEnAttente, HttpStatus.OK);
            }
        }
        catch (AutorisationException e) {
            return new ResponseEntity<>("{}", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "Ajoute un utilisateur au client OCSF du groupe")
    @PostMapping("/add/{idGroupe}/{idUser}")
    public ResponseEntity<Object> addUserToOCSFClient(@PathVariable(value = "idGroupe") Long idGroupe, @PathVariable(value="idUser") Long idUser)
    {
        try {
            if (groupeUserService.CountByGroupeIdAndUserId(idGroupe, idUser)) {
                if (!clientPool.containsKey(idGroupe)) {
                    clientPool.put(idGroupe, new GroupeClientRunnable(idGroupe));
                    clientPool.get(idGroupe).start();
                }

                GroupeClientRunnable groupeClientRunnable = clientPool.get(idGroupe);
                groupeClientRunnable.addUserToGroup(idUser);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        catch (AutorisationException e) {
            logger.info(e.getMessage());
            return new ResponseEntity<>("{}", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "Retourne tous les messages envoyés")
    @GetMapping("/live/admin")
    public ResponseEntity<Object> getLiveMessagesAdmin()
    {
        try {
            String messagesEnAttente = adminClient.getMessagesEnAttente();
            adminClient.viderBuffer();
            return new ResponseEntity<>(messagesEnAttente, HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info(e.getMessage());
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static boolean isValid(String url)
    {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }
}