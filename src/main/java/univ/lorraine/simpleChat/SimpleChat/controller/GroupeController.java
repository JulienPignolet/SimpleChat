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
import univ.lorraine.simpleChat.SimpleChat.model.EnumRole;
import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.GroupeUser;
import univ.lorraine.simpleChat.SimpleChat.model.Role;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.GroupeTemplate;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeUserService;
import univ.lorraine.simpleChat.SimpleChat.service.RoleService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("api/groupe")
@Api( value="Simple Chat")
public class GroupeController {

	private final UserService userService;
    private final GroupeService groupeService;
    private final GroupeUserService groupeUserService;
    private final RoleService roleService;

	@Autowired
	public GroupeController(UserService userService, GroupeService groupeService, GroupeUserService groupeUserService, RoleService roleService) {
		this.userService = userService;
		this.groupeService = groupeService;
		this.groupeUserService = groupeUserService;
		this.roleService = roleService;
	}


	/**
	 * 
	 * @param groupeTemplate
	 * @return Groupe créé ou un message d'erreur
	 */
	@PostMapping("/add/groupe")
	public ResponseEntity add(@RequestBody GroupeTemplate groupeTemplate) 
	{
		
		try {
            Long userId = Long.parseLong(groupeTemplate.getUserId());
            User user = this.userService.findById(userId);
    		if(user == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id "+userId+" n'a pas été trouvé. Nous ne pouvons pas créer un groupe sans admin.");
    		}

    		Role role = this.roleService.findByName(EnumRole.ADMIN_GROUP.getRole()); 
    		if(role == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Il faut d'abord créer un role "+EnumRole.ADMIN_GROUP.getRole()+" pour pouvoir créer un groupe.");
    		}
    		
    		String groupeName = groupeTemplate.getGroupe(); 
    		if( groupeName == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Json invalide ! Vueillez envoyer un json avec le nom du groupe !");
    		}
    		Groupe groupe = this.groupeService.create(groupeTemplate.getGroupe(), groupeTemplate.getIsPrivateChat()); 
    		GroupeUser groupeUser = this.groupeUserService.create(groupe, user); 
    		groupeUser = this.groupeUserService.roleGroupeUser(groupeUser, role);
    		
    		this.groupeService.save(groupe);
    		this.userService.save(user);
    		this.groupeUserService.save(groupeUser);
            
    		return ResponseEntity.ok("groupe créé !");
        } catch (NumberFormatException  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les données doivent être envoyé en JSON.");
        }
	}
	
	/**
	 * 
	 * @param request
	 * @param groupeId
	 * @return Un groupe qui n'est pas supprimé et dont l'id égal à groupeId 
	 */
	@GetMapping("/find/groupe/{groupeId}")
	public ResponseEntity<Groupe> findGroupe(HttpServletRequest request, @PathVariable String groupeId) 
	{
		Groupe  groupe = this.groupeService.findByIdAndDeletedatIsNull(groupeId);
		if (groupe != null) return ResponseEntity.ok(groupe);
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);   
	}
	
	
}
