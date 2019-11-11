package univ.lorraine.simpleChat.SimpleChat.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import univ.lorraine.simpleChat.SimpleChat.model.EnumRole;
import univ.lorraine.simpleChat.SimpleChat.model.Role;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.RoleTemplate;
import univ.lorraine.simpleChat.SimpleChat.service.RoleService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;


@RestController
@RequestMapping("/api/role")
public class RoleController {
	
	private final RoleService roleService;
	private final UserService userService;

	@Autowired
	public RoleController(RoleService roleService, UserService userService) {
		this.roleService = roleService;
		this.userService = userService;
	}
	
	/**
	 * 
	 * @param roleTemplate : La classe qui stoke le json envoyé en post
	 * @return le rôle créé ou un message d'erreur
	 */
    @PostMapping("/add/role")
    public ResponseEntity addRole(@RequestBody RoleTemplate roleTemplate) {
        try {
            Long supadId = Long.parseLong(roleTemplate.getUserId());
            User superadmin = this.userService.findById(supadId);

            if(superadmin == null)
            {
            	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les données doivent être envoyé en JSON. Vueillez renseigner un userId valide !");
            }
            
            if(!superadmin.containsRole(EnumRole.SUPER_ADMIN.getRole()))
            {
            	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vous êtes pas autorisé à ajouter un rôle !");
            }
            Role role = this.roleService.create(roleTemplate.getRole());
            return ResponseEntity.ok(role.toString());
            
        } catch (NumberFormatException  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les données doivent être envoyé en JSON.");
        }
    }
}
