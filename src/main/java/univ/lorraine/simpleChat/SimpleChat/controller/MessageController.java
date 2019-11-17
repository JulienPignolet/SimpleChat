
package univ.lorraine.simpleChat.SimpleChat.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.ocsf.AutorisationException;
import univ.lorraine.simpleChat.SimpleChat.ocsf.ClientRunnable;
import univ.lorraine.simpleChat.SimpleChat.ocsf.Message;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.MessageService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;
import java.util.HashMap;


@RestController
@RequestMapping("/api/message")
@Api( value="Simple Chat")
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
            	/**
            	 *  ATTENTION : Il faut informer la BDD que nous créons un nouveau groupe
            	 */
                clientPool.put(JSON.getGroup_id(), new ClientRunnable(JSON.getGroup_id()));
                clientPool.get(JSON.getGroup_id()).start();
            }
            clientPool.get(JSON.getGroup_id()).sendMsg(msg);

            // Sauvegarde
            Groupe groupe = groupeService.find(JSON.getGroup_id());
            messageService.save( new univ.lorraine.simpleChat.SimpleChat.model.Message(JSON.getMessage(), user, groupe));
//            user.sendMsg(msg);
        }
        catch(Exception e)
        {
            e.printStackTrace();
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
    @GetMapping("/group/{idGroupe}/messages")
    public ResponseEntity<Object> byName(@PathVariable(value = "idGroupe") long idGroupe, long idUser)
    {
    /*		/!\
     *
     * 		getMessagesEnAttenteJSON(user_id) renvoie une exception si le user_id n'est pas présent dans la liste des
     * 		users du groupe. Il faudra donc faire un try catch et renvoyer une erreur HTTP 401 pour lui dire qu'il
     * 		n'est pas autorisé à consulter ce groupe.
     */

        // verifie si idGroupe n´existe pas dans dans clientPool)
        if (!clientPool.containsKey(idGroupe))
            return new ResponseEntity<Object>("{}", HttpStatus.NO_CONTENT);

        // on récupere le clientRunnable
        ClientRunnable clientRunnable = clientPool.get(idGroupe);

        // on récupère les messages en attente
        try {
        	
            String messagesEnAttente = clientRunnable.getMessagesEnAttente(idUser);
            //clientRunnable.viderBuffer(idUser);	// /!\ NE SERT ACTUELLEMENT PAS
            return new ResponseEntity<Object>(messagesEnAttente, HttpStatus.OK);
        } catch (AutorisationException e) {
            e.printStackTrace();
            return new ResponseEntity<Object>("{}", HttpStatus.UNAUTHORIZED);

        }

    }
}