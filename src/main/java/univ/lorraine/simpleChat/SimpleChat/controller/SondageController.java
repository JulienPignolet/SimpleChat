package univ.lorraine.simpleChat.SimpleChat.controller;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import univ.lorraine.simpleChat.SimpleChat.model.EnumRole;
import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.GroupeUser;
import univ.lorraine.simpleChat.SimpleChat.model.ReponseSondage;
import univ.lorraine.simpleChat.SimpleChat.model.Role;
import univ.lorraine.simpleChat.SimpleChat.model.Sondage;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.GroupeTemplate;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.SondageTemplate;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.ReponseSondageService;
import univ.lorraine.simpleChat.SimpleChat.service.SondageService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

@RestController
@RequestMapping("/api/sondage")
@Api(value = "Simple Chat")
public class SondageController {
    private final GroupeService groupeService;
    private final UserService userService;
    private final SondageService sondageService;
    private final ReponseSondageService reponseSondageService;

    @Autowired
    public SondageController(GroupeService groupeService, UserService userService, SondageService sondageService, ReponseSondageService reponseSondageService) {
        this.groupeService = groupeService;
        this.userService = userService;
        this.sondageService = sondageService;
        this.reponseSondageService = reponseSondageService;
    }

    /**
     * Recupere la question d'un sondage
     *
     * @param sondageId id du sondage
     * @return la question
     */
    @GetMapping("/{sondageId}")
    public ResponseEntity findSondage(@PathVariable Long sondageId) {
        Sondage sondage = sondageService.findById(sondageId);
        if (sondage != null) {
            return ResponseEntity.ok(sondage);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Poll not found");
        }
    }

    /**
     * Recupere les reponses d'un sondage
     *
     * @param sondageId id du sondage
     * @return liste des reponses du sondage
     */
    @GetMapping("/{sondageId}/reponseSondage")
    public ResponseEntity findReponses(@PathVariable Long sondageId) {
        Sondage sondage = sondageService.findById(sondageId);
        if (sondage != null) {
            return ResponseEntity.ok(sondage.getReponseSondages());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Poll not found");
        }
    }

    /**
     * Créer un sondage
     *
     * @param question    questin du sondage
     * @param groupe_id   groupe où le sondage est envoyé
     * @param user_id     initiateur du sondage
     * @param voteAnonyme définit si les votes sont anonymes
     * @return reponse selon le statut de création
     */
    public ResponseEntity createSondage(String question, String groupe_id, String user_id, String voteAnonyme) { // , Collection<String> reponses,String dateDebut, String dateFin
        try {
            Long groupeId = Long.parseLong(groupe_id);
            Groupe groupe = this.groupeService.find(groupeId);

            if (groupe == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le groupe d'id " + groupeId + " n'a pas été trouvé. Nous ne pouvons pas créer un sondage sans l'associer à un groupe.");
            }

            Long userId = Long.parseLong(user_id);
            User user = this.userService.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id " + userId + " n'a pas été trouvé. Nous ne pouvons pas créer un sondage sans initiateur.");
            }

            if (question == null || question == "") {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Json invalide ! Veuillez envoyer un json avec la question !");
            }
            /*
            if(reponses.size() < 2)
            {
            	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Il faut que le sondage ait au moins deux reponses possibles !");
            }
             */
            Sondage sondage = this.sondageService.create(question, groupe,Boolean.parseBoolean(voteAnonyme),user); // , null, null
            this.sondageService.save(sondage);

            return ResponseEntity.ok("sondage d'id " + sondage.getId() + " créé !");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les données doivent être envoyé en JSON.");
        }
    }

    /**
     * @param sondageTemplate
     * @return Sondage créé ou un message d'erreur
     */
    @ApiOperation(value = "Créé un sondage avec les données envoyées en post. L'utilisateur envoyé dans le post en devient Admin.")
    @PostMapping("/add")
    public ResponseEntity add(@RequestBody SondageTemplate sondageTemplate) {
        return this.createSondage(sondageTemplate.getQuestion(), sondageTemplate.getGroupeId(), sondageTemplate.getUserId(), sondageTemplate.getIsVotesAnonymes()); //, sondageTemplate.getReponsesSondage(), sondageTemplate.getDateDebut(), sondageTemplate.getDateFin()
    }

    /**
     * @param sondageId        id du sondage auquel on ajoute la reponse
     * @param reponseSondageId id de la reponsesondage
     * @return response avec le statut de la requete
     */
    @PostMapping("{sondageId}/add")
    public ResponseEntity addReponse(@PathVariable Long sondageId, @RequestBody String reponseSondageId) {
        try {
            Long sId = sondageId;
            Long rsId = Long.parseLong(reponseSondageId);

            Sondage sondage = sondageService.find(sId);
            ReponseSondage reponseSondage = reponseSondageService.find(rsId);

            if (sondage != null && reponseSondage != null) {
                if (sondage.getReponseSondages().contains(reponseSondage)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This is already an answer of your poll !");
                } else {
                    sondageService.addReponse(sondage, reponseSondage);
                    return ResponseEntity.ok("Answer added !");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Poll or poll's answer not found");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id should be sent in JSON. Just send the answer's id.");
        }
    }

    /**
     * @param sondageId        id du sondage auquel on enleve la reponse
     * @param reponseSondageId id de la reponse sondage
     * @return response avec le statut de la requete
     */
    @PostMapping("{sondageId}/remove")
    public ResponseEntity removeReponse(@PathVariable String sondageId, @RequestBody String reponseSondageId) {
        try {
            Long sId = Long.parseLong(sondageId);
            Long rsId = Long.parseLong(reponseSondageId);

            Sondage sondage = sondageService.find(sId);
            ReponseSondage reponseSondage = reponseSondageService.find(rsId);

            if (sondage != null && reponseSondage != null) {
                if (sondage.getReponseSondages().contains(reponseSondage)) {
                    sondageService.removeReponse(sondage, reponseSondage);
                    return ResponseEntity.ok("Reponse removed !");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Poll or answer not found");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Poll or answer not found");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id should be sent as String or error in JSON. Just send the answer's id.");
        }
    }


}
