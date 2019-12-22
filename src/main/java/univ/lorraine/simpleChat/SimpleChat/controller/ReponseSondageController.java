package univ.lorraine.simpleChat.SimpleChat.controller;

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
import univ.lorraine.simpleChat.SimpleChat.model.ReponseSondage;
import univ.lorraine.simpleChat.SimpleChat.model.Sondage;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.service.ReponseSondageService;
import univ.lorraine.simpleChat.SimpleChat.service.SondageService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;
import univ.lorraine.simpleChat.SimpleChat.service.VoteService;

@RestController
@RequestMapping("/api/sondage/{sondageId}/reponseSondage")
@Api( value="Simple Chat")
public class ReponseSondageController {
	private final SondageService sondageService;
	private final ReponseSondageService reponseSondageService;
	private final VoteService voteService;
	private final UserService userService;
	
    @Autowired
    public ReponseSondageController(SondageService sondageService, ReponseSondageService reponseSondageService, VoteService voteService, UserService userService) {
    	this.sondageService = sondageService;
        this.reponseSondageService = reponseSondageService;
        this.voteService = voteService;
        this.userService = userService;
    }
    
    /**
     * Recupere les votes d'une reponse
     * @param reponseSondageId id du reponseSondage
     * @return liste des reponses du sondage
     */
    @GetMapping("/{reponseSondageId}")
    public ResponseEntity findVotes(@PathVariable Long reponseSondageId) {
        ReponseSondage reponseSondage = reponseSondageService.findById(reponseSondageId);
        if (reponseSondage != null) {
            return ResponseEntity.ok(reponseSondage.getListVotes());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Votes not found");
        }
    }
    
    
}

