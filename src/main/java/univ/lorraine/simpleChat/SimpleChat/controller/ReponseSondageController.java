package univ.lorraine.simpleChat.SimpleChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import univ.lorraine.simpleChat.SimpleChat.service.ReponseSondageService;

@RestController
@RequestMapping("/api/sondage/reponseSondage")
@Api( value="Simple Chat")
public class ReponseSondageController {
	private final ReponseSondageService reponseSondageService;
	
    @Autowired
    public ReponseSondageController(ReponseSondageService reponseSondageService) {
        this.reponseSondageService = reponseSondageService;
    }
    

}

