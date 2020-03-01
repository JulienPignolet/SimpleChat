	package univ.lorraine.simpleChat.SimpleChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.AddBlocklistTemplate;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

@RestController
@RequestMapping("api/blockList")
@Api( value="Simple Chat")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BlockController {

	 private final UserService userService;
	 private final boolean bloque = true; 
	 private final boolean debloque = false; 
	 

	@Autowired
	public BlockController(UserService userService) {
	    this.userService = userService;
	}
	
	/**
	 * Recupere les users blacklistés d'un utilisateur
	 * @param userId id de l'utilisateur
	 * @return liste les users blacklistés
	 */
	@ApiOperation(value = "Retourne les utilisateurs bloqués par l'utilisateur dont l'id est envoyé en get")
	@GetMapping("/{userId}")
	public ResponseEntity findUsersBlocked(@PathVariable Long userId) {
	    User user = userService.findById(userId);
	    if (user != null) return ResponseEntity.ok(user.getMyBlocklist());
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Utilisateur non trouvé !");
	}
	
	/**
	 * @param addBlocklistTemplate
	 * @return Une reponse avec le statut de la requête
	 */
	@ApiOperation(value = "Ajoute l'utilisateur d'id blockId à la liste des utilisateurs bloqués de l'utilisateur d'id userId")
	@PostMapping("/add")
	public ResponseEntity addUser(@RequestBody AddBlocklistTemplate addBlocklistTemplate) {
		return this.helpers(addBlocklistTemplate, this.bloque); 
	}
	
	/**
	 * @param addBlocklistTemplate
	 * @return Une reponse avec le statut de la requête
	 */
	@ApiOperation(value = "Suppprime l'utilisateur d'id blockId de la liste des utilisateurs bloqués par l'utilisateur d'id userId")
	@PostMapping("/remove")
	public ResponseEntity removeUser(@RequestBody AddBlocklistTemplate addBlocklistTemplate) {
		return this.helpers(addBlocklistTemplate, this.debloque); 
	}
	
	
	
	
	
	/**
	 * 
	 * Helper
	 */
	private ResponseEntity helpers(@RequestBody AddBlocklistTemplate addBlocklistTemplate, boolean decision)
	{
		try {
	        Long uId = Long.parseLong(addBlocklistTemplate.getUserId());
	        Long bId = Long.parseLong(addBlocklistTemplate.getBlockId());
	
	        User user = userService.findById(uId);
	        User bUser = userService.findById(bId);
	
	        if(user != null && bUser != null)
	        {
	        	String msg = (decision) ? userService.addBlockUser(user,bUser) : userService.removeBlockUser(user,bUser);
	            return ResponseEntity.ok(msg);
	        }
	            
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Utilisateur(s) non trouvé(s)");
	        
	    } catch (NumberFormatException  e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JSON invalide !");
	    }
	}

}
