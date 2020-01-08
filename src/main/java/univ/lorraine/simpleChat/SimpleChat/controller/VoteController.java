package univ.lorraine.simpleChat.SimpleChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import univ.lorraine.simpleChat.SimpleChat.model.ReponseSondage;
import univ.lorraine.simpleChat.SimpleChat.model.Sondage;
import univ.lorraine.simpleChat.SimpleChat.service.ReponseSondageService;
import univ.lorraine.simpleChat.SimpleChat.service.SondageService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;
import univ.lorraine.simpleChat.SimpleChat.service.VoteService;


@RestController
@RequestMapping("/api/sondage/{sondageId}/reponseSondage/{reponseSondageId}/{voteId}")
@Api( value="Simple Chat")
public class VoteController {
	private final UserService userService;
	private final SondageService sondageService;
	private final ReponseSondageService reponseSondageService;
	private final VoteService voteService;
	
    @Autowired
    public VoteController(UserService  userService, SondageService sondageService, ReponseSondageService reponseSondageService, VoteService voteService) {
        this.userService = userService;
        this.sondageService = sondageService;
        this.reponseSondageService = reponseSondageService;
        this.voteService = voteService;
    }
}
