package univ.lorraine.simpleChat.SimpleChat.controller;

import java.util.ArrayList;
import java.util.Collection;
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
import univ.lorraine.simpleChat.SimpleChat.repository.GroupeRepository;
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
    
    
	@GetMapping("/add/groupe/{name}")
	public Groupe add(HttpServletRequest request, @PathVariable String name) 
	{
		Groupe groupe = new Groupe();
		groupe.setName(name);
		groupe.setPrivateChat(false);
		this.groupeService.save(groupe);

		return groupe;
	}
	
	@GetMapping("/groupe/{id}")
	public Optional<Groupe> groupeById(HttpServletRequest request, @PathVariable String id) 
	{
		Long i = Long.parseLong(id);  
		Optional<Groupe> groupe = this.groupeService.findById(i); 

		return groupe;
	}
	

	@GetMapping("/groupe/{groupe_id}/{user_id}/{role_id}")
	public String addUserToGroup(HttpServletRequest request, @PathVariable String groupe_id, @PathVariable String user_id, @PathVariable String role_id) 
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
		
		groupeUserService.save(groupeUser);
		
		//return groupeUserService.findByGroupeAndUser(groupe_i, user_i);
		return "User ajouté avec succès";
	}
	
	/*
	 Il manque des tests
	 */
}
