package univ.lorraine.simpleChat.SimpleChat.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.GroupeUser;
import univ.lorraine.simpleChat.SimpleChat.model.Role;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeUserService;
import univ.lorraine.simpleChat.SimpleChat.service.RoleService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;
/*
	CECI EST UN CONTROLLER DE TEST
	Ainsi, toutes les toutes les actions seront en post. 
	C'est plus simple pour le test. 
*/


@RestController
@RequestMapping("/groupe-test")
public class GroupeTestController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private GroupeService groupeService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private GroupeUserService groupeUserService;
    
    
    /**
     * 
     * @param request
     * @param name
     * @return Le groupe qu'on vient de créer
     */
	@GetMapping("/add/groupe/{name}/{isPrivateChat}")
	public Groupe add(HttpServletRequest request, Principal principal, @PathVariable String name, @PathVariable String isPrivateChat) 
	{
		return this.groupeService.create(name, isPrivateChat, principal.getName());
	}
	
	
	/**
	 * 
	 * @param request
	 * @param id
	 * @return Le groupe dont on a l'id en parametre
	 */
	@GetMapping("/groupe/{id}")
	public Optional<Groupe> groupeById(HttpServletRequest request, @PathVariable String id) 
	{
		Long i = Long.parseLong(id);  
		Optional<Groupe> groupe = this.groupeService.findById(i); 

		return groupe;
	}
	
	
	/**
	 * 
	 * @param request
	 * @param groupe_id
	 * @param user_id
	 * @param role_id
	 * @return Le groupeUser qu'on vient d'ajouter (Ceci est la garantie qu'on a ajouté un user dans un groupe)
	 */
	@GetMapping("/add/user/{groupe_id}/{user_id}/{role_id}")
	public GroupeUser addUserToGroup(HttpServletRequest request, @PathVariable String groupe_id, @PathVariable String user_id, @PathVariable String role_id) 
	{
		Long groupe_i = Long.parseLong(groupe_id); 
		Long user_i = Long.parseLong(user_id); 
		Long role_i = null;
		if(role_id != "null") 
		{
			role_i = Long.parseLong(role_id);
		}
		Optional<Groupe> groupe = groupeService.findById(groupe_i);
		Optional<User> user = userService.findById(user_i);
		GroupeUser groupeUser = new GroupeUser();
		groupe.get().addGroupeUser(groupeUser);
		user.get().addGroupeUser(groupeUser);
		if(role_i != null)
		{
			Optional<Role> role = roleService.findById(role_i);
			groupeUser.setRole(role.get());
		}
		
		userService.save(user.get());
		groupeService.save(groupe.get());
		groupeUserService.save(groupeUser);
		
		return groupeUserService.findByGroupeAndUser(groupe_i, user_i);
	}
	
	
	/**
	 * 
	 * @param request
	 * @param groupe_id
	 * @param user_id
	 * @return Le groupeUser dont groupe et le user sont en parametres
	 */
	@GetMapping("/groupe-user/{groupe_id}/{user_id}")
	public GroupeUser groupeUser(HttpServletRequest request, @PathVariable String groupe_id, @PathVariable String user_id) {
		Long groupe_i = Long.parseLong(groupe_id); 
		Long user_i = Long.parseLong(user_id); 
		return groupeUserService.findByGroupeAndUser(groupe_i, user_i);
	}
	
	
	/**
	 * 
	 * @param request
	 * @param groupe_id
	 * @return Tous les groupeUser d'un groupe spécifique
	 */
	@GetMapping("/groupe-user/groupe/{groupe_id}")
	public List<GroupeUser> groupeUserGroupe(HttpServletRequest request, @PathVariable String groupe_id) {
		Long groupe_i = Long.parseLong(groupe_id); 
		return groupeUserService.findByGroupe(groupe_i);
	}
	
	/*
	 Il manque des tests
	 */
}
