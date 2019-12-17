
package univ.lorraine.simpleChat.SimpleChat.controller;

import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.MessageTemplate;
import univ.lorraine.simpleChat.SimpleChat.ocsf.AutorisationException;
import univ.lorraine.simpleChat.SimpleChat.ocsf.ClientRunnable;
import univ.lorraine.simpleChat.SimpleChat.ocsf.Message;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeUserService;
import univ.lorraine.simpleChat.SimpleChat.service.MessageService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;
import java.util.HashMap;


@RestController
@RequestMapping("/api/message")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api( value="Simple Chat")
public class MessageController {
    private final UserService userService;
    private final GroupeService groupeService;
    private final MessageService messageService;
    private final GroupeUserService groupeUserService;

    public HashMap<Long, ClientRunnable> clientPool = new HashMap<>();

    public MessageController(UserService userService, GroupeService groupeService, MessageService messageService, GroupeUserService groupeUserService) {
        this.userService = userService;
        this.groupeService = groupeService;
        this.messageService = messageService;
        this.groupeUserService = groupeUserService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> sendMessage(@RequestBody MessageTemplate message)
    {
        try {
            User user = userService.findById(message.getGroup_id());
            if(groupeUserService.CountByGroupeIdAndUserId(message.getGroup_id(), message.getUser_id())) {
                if (!clientPool.containsKey(message.getGroup_id())) {
                    /**
                     *  ATTENTION : Il faut informer la BDD que nous créons un nouveau groupe
                     */
                    clientPool.put(message.getGroup_id(), new ClientRunnable(message.getGroup_id()));
                    clientPool.get(message.getGroup_id()).start();
                }
                //Il faudra vérifier que le user appartient au groupe
                clientPool.get(message.getGroup_id()).addUserToGroup(message.getUser_id());
                clientPool.get(message.getGroup_id()).sendMsg(message.toString(), userService.findById(message.getUser_id()).getUsername());
//                clientPool.get(message.getGroup_id()).sendMsg(message.toString());

                // Sauvegarde
//            Groupe groupe = groupeService.find(message.getGroup_id());
//            messageService.save( new univ.lorraine.simpleChat.SimpleChat.model.Message(message.getMessage(), user, groupe));
//            user.sendMsg(msg);
            }
        }
        catch(Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @param idGroupe
     * @param  idUser
     * TODO rajouter l idUser comme parametre dans le front
     * @return
     */
    @GetMapping("/{idGroupe}/{idUser}")
    public ResponseEntity<Object> byName(@PathVariable(value = "idGroupe") Long idGroupe, @PathVariable(value="idUser") Long idUser)
    {
    /*		/!\
     *
     * 		getMessagesEnAttentemessage(user_id) renvoie une exception si le user_id n'est pas présent dans la liste des
     * 		users du groupe. Il faudra donc faire un try catch et renvoyer une erreur HTTP 401 pour lui dire qu'il
     * 		n'est pas autorisé à consulter ce groupe.
     */

        // verifie si idGroupe n´existe pas dans dans clientPool)
        if (!clientPool.containsKey(idGroupe))
        {
            try {
                if(groupeUserService.CountByGroupeIdAndUserId(idGroupe, idUser))
                    clientPool.get(idGroupe).addUserToGroup(idUser);
            } catch (AutorisationException e) {
                e.printStackTrace();
                return new ResponseEntity<Object>("{}", HttpStatus.UNAUTHORIZED);
            }
                //Il faudra vérifier que le user appartient au groupe
        }

        // on récupere le clientRunnable
        ClientRunnable clientRunnable = clientPool.get(idGroupe);

        // on récupère les messages en attente
        try {
        	
            String messagesEnAttente = clientRunnable.getMessagesEnAttente(idUser);
            clientRunnable.viderBuffer(idUser);	// /!\ NE SERT ACTUELLEMENT PAS
            return new ResponseEntity<Object>(messagesEnAttente, HttpStatus.OK);
        } catch (AutorisationException e) {
            e.printStackTrace();
            return new ResponseEntity<Object>("{}", HttpStatus.UNAUTHORIZED);

        }

    }
}