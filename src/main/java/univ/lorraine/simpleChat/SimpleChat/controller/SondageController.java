package univ.lorraine.simpleChat.SimpleChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import univ.lorraine.simpleChat.SimpleChat.model.*;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.MessageTemplate;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.SondageTemplate;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.VoteTemplate;
import univ.lorraine.simpleChat.SimpleChat.service.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/sondage")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "Simple Chat")
public class SondageController {
    private final GroupeService groupeService;
    private final GroupeUserService groupeUserService;
    private final UserService userService;
    private final SondageService sondageService;
    private final ReponseSondageService reponseSondageService;
    private final MessageController messageController;
    private final VoteService voteService;

    @Autowired
    public SondageController(
            GroupeService groupeService,
            GroupeUserService groupeUserService,
            UserService userService,
            SondageService sondageService,
            ReponseSondageService reponseSondageService,
            MessageController messageController,
            VoteService voteService
    ) {
        this.groupeService = groupeService;
        this.groupeUserService = groupeUserService;
        this.userService = userService;
        this.sondageService = sondageService;
        this.reponseSondageService = reponseSondageService;
        this.messageController = messageController;
        this.voteService = voteService;
    }

    /**
     * Recupere la question d'un sondage
     *
     * @param sondageId id du sondage
     * @return la question
     */
    @GetMapping("/{sondageId}")
    public ResponseEntity<Object> findSondage(@PathVariable Long sondageId) {
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
    public ResponseEntity<Object> findReponses(@PathVariable Long sondageId) {
        Sondage sondage = sondageService.findById(sondageId);
        if (sondage != null) {
            return ResponseEntity.ok(sondage.getReponseSondages());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Poll not found");
        }
    }

    /**
     * @param template le template du sondage à créer
     * @return Sondage créé ou un message d'erreur
     */
    @ApiOperation(value = "Créé un sondage avec les données envoyées en post. L'utilisateur envoyé dans le post en devient Admin.")
    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody SondageTemplate template) {
        try {
            Long groupeId = Long.parseLong(template.getGroupeId());
            Groupe groupe = this.groupeService.find(groupeId);

            if (groupe == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le groupe d'id " + groupeId + " n'a pas été trouvé. Nous ne pouvons pas créer un sondage sans l'associer à un groupe.");
            }

            Long userId = Long.parseLong(template.getUserId());
            User user = this.userService.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id " + userId + " n'a pas été trouvé. Nous ne pouvons pas créer un sondage sans initiateur.");
            }

            String question = template.getQuestion();
            if (question == null || question.equals("")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Json invalide ! Veuillez envoyer un json avec la question !");
            }

            boolean voteAnonyme = Boolean.parseBoolean(template.getVotesAnonymes());

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date dateFin;
            try {
                dateFin = dateFormatter.parse(template.getDateFin());
            } catch (ParseException ex1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le format de la date est incorrect. Format attendu : yyyy-MM-dd / date reçue : \"" + template.getDateFin() + "\"");
            }

            Sondage sondage = this.sondageService.create(question, groupe, voteAnonyme, user, template.getReponsesSondage(), dateFin);
            this.sondageService.save(sondage);

            for (String reponse : template.getReponsesSondage()) {
                ReponseSondage reponseSondage = new ReponseSondage();
                reponseSondage.setReponse(reponse);
                reponseSondage.setNbVote(0);
                reponseSondage.setSondage(sondage);

                this.reponseSondageService.save(reponseSondage);
            }

            sondage = sondageService.findById(sondage.getId());

            this.sendSondageMessage(sondage, user, groupe);

            return ResponseEntity.ok(sondage);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les données doivent être envoyé en JSON.");
        }
    }

    private void sendSondageMessage(Sondage sondage, User user, Groupe groupe) {
        MessageTemplate messageTemplate = new MessageTemplate();
        messageTemplate.setGroup_id(groupe.getId());
        messageTemplate.setUser_id(user.getId());
        messageTemplate.setMessage("sondage_id:" + sondage.getId());

        this.messageController.sendMessage(messageTemplate);
    }

    @PostMapping("/{sondageId}/reponse/{reponseSondageId}/vote")
    public ResponseEntity<Object> vote(@PathVariable Long sondageId, @PathVariable Long reponseSondageId, @RequestBody VoteTemplate template) {
        Sondage sondage = sondageService.findById(sondageId);
        if (sondage == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le sondage d'id" + sondageId + " n'a pas été trouvé.");
        }

        ReponseSondage reponseSondage = reponseSondageService.findById(reponseSondageId);
        if (reponseSondage == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La réponse d'id " + reponseSondageId + " n'a pas été trouvée.");
        }

        Long userId = Long.parseLong(template.getUserId());
        User user = this.userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id " + userId + " n'a pas été trouvé.");
        }

        if (voteService.hasUserVoted(user, sondage)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'utilisateur d'id " + userId + " à déjà voté sur ce sondage.");
        }

        boolean isUserInGroup = groupeUserService.findByGroupeUserActif(sondage.getGroupe().getId(), user.getId()) != null;
        if (!isUserInGroup) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("L'utilisateur d'id " + userId + " n'appartient pas au groupe de ce sondage.");
        }

        Vote vote = new Vote();
        vote.setReponseSondage(reponseSondage);
        vote.setUser(user);

        this.voteService.save(vote);

        reponseSondage.addVote(vote);
        this.reponseSondageService.save(reponseSondage);

        return ResponseEntity.ok("A voté!");
    }
}
