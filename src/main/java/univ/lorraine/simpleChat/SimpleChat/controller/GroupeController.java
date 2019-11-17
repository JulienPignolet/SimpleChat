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
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.AddMemberTemplate;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.GroupeTemplate;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeUserService;
import univ.lorraine.simpleChat.SimpleChat.service.RoleService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
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
	
	/**
	 * 
	 * @param addMemberTemplate
	 * @return  Un message de confirmation d'ajout ou un message d'erreur
	 */
	@PostMapping("/add/member")
	public ResponseEntity addMember(@RequestBody AddMemberTemplate addMemberTemplate)
	{

		try {
			
			Long adminGroupeId = Long.parseLong(addMemberTemplate.getAdminGroupeId());
            User adminGroupe = this.userService.findById(adminGroupeId);
    		if(adminGroupe == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id "+adminGroupeId+" n'a pas été trouvé.");
    		}
    		
            Long userId = Long.parseLong(addMemberTemplate.getUserId());
            User user = this.userService.findById(userId);
    		if(user == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id "+userId+" n'a pas été trouvé.");
    		}

    		String groupeId = addMemberTemplate.getGroupeId();
    		Groupe groupe = groupeService.findByIdAndDeletedatIsNull(groupeId);
    		if(groupe == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le groupe d'Id '"+groupeId+"' a été supprimé ou n'existe pas !");
    		}
    		
    		GroupeUser groupeUserAdmin = groupeUserService.findByGroupeUserActif(groupe.getId(), adminGroupe.getId());
    		if( groupeUserAdmin == null )
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id '"+adminGroupeId+"' ne fait pas partir de ce groupe !");
    		}
    		
    		Role role = groupeUserAdmin.getRole(); 
    		ArrayList<String> acceptRoles = new ArrayList<>();
    		acceptRoles.add(EnumRole.ADMIN_GROUP.getRole());
    		acceptRoles.add(EnumRole.SUPER_ADMIN.getRole()); 
    		if( role == null || !acceptRoles.contains(role.getName())) 
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seul l'admin du groupe peut rajouter un membre au groupe !");
    		}
    		
    		GroupeUser groupeUserNew = groupeUserService.findByGroupeUserActif(groupe.getId(), user.getId());
    		if( groupeUserNew != null )
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id '"+user.getId()+"' est déjà membre de ce groupe !");
    		}
    		
    		GroupeUser groupeUser = new GroupeUser();
    		groupe.addGroupeUser(groupeUser);
    		user.addGroupeUser(groupeUser);
    		groupeUser.setRole(null);
    		
    		
    		
    		this.userService.save(user);
    		this.groupeService.save(groupe);
    		this.groupeUserService.save(groupeUser);
            
    		return ResponseEntity.ok("L'utilisateur d'id "+user.getId()+"a été ajouté au groupe d'id "+groupe.getId()+" avec succès !");
        } catch (NumberFormatException  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les données doivent être envoyé en JSON.");
        }
	}
	
	/**
	 * 
	 * @param request
	 * @return Tous les groupes non supprimés
	 */
	@GetMapping("/findAll/groupe")
	public ResponseEntity<Collection<Groupe>> findAllGroupe(HttpServletRequest request) 
	{
		Collection<Groupe> listGroupe = this.groupeService.findByDeletedatIsNull(); 
		return ResponseEntity.ok(listGroupe); 
	}
	
	/**
	 * 
	 * @param request
	 * @param groupeId
	 * @return Tous les membres d'un groupe
	 */
	@GetMapping("/find/Members/groupe/{groupeId}")
	public ResponseEntity<Collection<User>> findMembersGroupe(HttpServletRequest request, @PathVariable String groupeId)
	{
		Long gid = Long.parseLong(groupeId); 
		Collection<User> users = userService.findMembersGroupe(gid); 
		return ResponseEntity.ok(users); 
	}
	
	
	
}
