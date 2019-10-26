package univ.lorraine.simpleChat.SimpleChat.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.GroupeUser;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeUserService;
/*
	CECI EST UN CONTROLLER DE TEST (api)
	Ainsi, toutes les actions seront en get. 
	C'est plus simple pour le test. 
*/


@RestController
@RequestMapping("/groupe-test")
public class GroupeTestController {

    
    @Autowired
    private GroupeService groupeService;
    
    @Autowired
    private GroupeUserService groupeUserService;
    
    
    /**
     * 
     * @param request
     * @param principal
     * @param name : nom du groupe
     * @param isPrivateChat : valeurs possibles sont 0 ou 1.
     * @return return le groupe qu'on vient de créer
     */
	@GetMapping("/add/groupe/{name}/{isPrivateChat}")
	public Groupe add(HttpServletRequest request, Principal principal, @PathVariable String name, @PathVariable String isPrivateChat) 
	{
		return this.groupeService.create(name, isPrivateChat, principal.getName());
	}
	
	/**
	 * 
	 * @param request
	 * @param groupeId
	 * @return Un groupe qui n'est pas supprimé et dont l'id égal à groupeId 
	 */
	@GetMapping("/find/groupe/{groupeId}")
	public Groupe findGroupe(HttpServletRequest request, @PathVariable String groupeId) 
	{
		return this.groupeService.findByIdAndDeletedatIsNull(groupeId);
	}
	
	/**
	 * 
	 * @param request
	 * @param groupeId : est l'identifiant du groupe dans lequel on souhaite ajouter un membre
	 * @param usernameNew : est le username de l'utilisateur qu'on souhaite ajouter au groupe
	 * @return Le groupeUser du membre ou un message d'erreur
	 */
	@GetMapping("/add/member/{groupeId}/{usernameNew}")
	public GroupeUser addMember(HttpServletRequest request, Principal principal, @PathVariable String groupeId, @PathVariable String usernameNew)
	{	
		return this.groupeService.addMember(groupeId, usernameNew, principal.getName());
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
