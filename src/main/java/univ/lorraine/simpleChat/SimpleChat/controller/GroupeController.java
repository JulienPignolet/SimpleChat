package univ.lorraine.simpleChat.SimpleChat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.asm.Advice.Exit;
import univ.lorraine.simpleChat.SimpleChat.model.EnumRole;
import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.GroupeUser;
import univ.lorraine.simpleChat.SimpleChat.model.Role;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.AddGroupAndMembersTemplate;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api( value="Simple Chat")
public class GroupeController {
	Logger logger = LoggerFactory.getLogger(GroupeController.class);

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
	 * @param user_id
	 * @param groupeName
	 * @param isPrivateChat
	 * @return Une reponse
	 */
	public ResponseEntity createGroupe(String user_id, String groupeName, String isPrivateChat)
	{
		try {
            Long userId = Long.parseLong(user_id);
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
    		
    		if( groupeName == null || groupeName == "")
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Json invalide ! Vueillez envoyer un json avec le nom du groupe !");
    		}
    		Groupe groupe = this.groupeService.create(groupeName, isPrivateChat); 
    		GroupeUser groupeUser = this.groupeUserService.create(groupe, user); 
    		groupeUser = this.groupeUserService.roleGroupeUser(groupeUser, role);
    		
    		this.groupeService.save(groupe);
    		this.userService.save(user);
    		this.groupeUserService.save(groupeUser);
            
    		return ResponseEntity.ok("groupe d'id "+groupe.getId()+" créé !");
        } catch (NumberFormatException  e) {
		    logger.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les données doivent être envoyé en JSON.");
        }
	}

	/**
	 * 
	 * @param adminGroupe_id
	 * @param user_id
	 * @param groupe_id
	 * @return Une reponse
	 */
	public ResponseEntity addMemberFct(String adminGroupe_id, String user_id, String groupe_id)
	{

		try {
			
			Long adminGroupeId = Long.parseLong(adminGroupe_id);
            User adminGroupe = this.userService.findById(adminGroupeId);
    		if(adminGroupe == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id "+adminGroupeId+" n'a pas été trouvé.");
    		}
    		
            Long userId = Long.parseLong(user_id);
            User user = this.userService.findById(userId);
    		if(user == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id "+userId+" n'a pas été trouvé.");
    		}

    		Groupe groupe = groupeService.findByIdAndDeletedatIsNull(groupe_id);
    		if(groupe == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le groupe d'Id '"+groupe_id+"' a été supprimé ou n'existe pas !");
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
	 * @param groupeTemplate
	 * @return Groupe créé ou un message d'erreur
	 */
    @ApiOperation(value = "Créé un groupe avec les données envoyées en post. L'utilisateur envoyé dans le post en devient Admin.")
	@PostMapping("/add/groupe")
	public ResponseEntity add(@RequestBody GroupeTemplate groupeTemplate) 
	{
		return this.createGroupe(groupeTemplate.getUserId(), groupeTemplate.getGroupe(), groupeTemplate.getIsPrivateChat()); 
	}
	
	/**
	 * 
	 * @param request
	 * @param groupeId
	 * @return Un groupe qui n'est pas supprimé et dont l'id égal à groupeId 
	 */
    @ApiOperation(value = "Retourne le groupe non supprimé (suppression logique) dont l'id est envoyé en get")
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
    @ApiOperation(value = "Ajoute un membre (user) à un groupe. Seul l'admin du groupe (Celui qui a créé le groupe) ou un superadmin peuvent ajouter un membre à ce dernier.")
	@PostMapping("/add/member")
	public ResponseEntity addMember(@RequestBody AddMemberTemplate addMemberTemplate)
	{
		return addMemberFct(addMemberTemplate.getAdminGroupeId(), addMemberTemplate.getUserId(), addMemberTemplate.getGroupeId());
	}
	
	/**
	 * 
	 * @param request
	 * @return Tous les groupes non supprimés
	 */
    @ApiOperation(value = "Retourne tous les groupes non supprimés (suppression logique)")
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
    @ApiOperation(value = "Retourne tous les membres non supprimés (suppression logique) d'un groupe.")
	@GetMapping("/find/Members/groupe/{groupeId}")
	public ResponseEntity<Collection<User>> findMembersGroupe(HttpServletRequest request, @PathVariable String groupeId)
	{
		Long gid = Long.parseLong(groupeId); 
		Collection<User> users = userService.findMembersGroupe(gid); 
		return ResponseEntity.ok(users); 
	}
	
	
    /**
     * 
     * @param addGroupeAndMembersTemplate
     * @return
     */
    @ApiOperation(value = "Créé un groupe et y ajoute tous les users dont les id sont envoyés en post.  Seul l'admin du groupe (Celui qui a créé le groupe) ou un superadmin peuvent ajouter des membres à ce dernier.")
	@PostMapping("/add/groupe-and-members")
	public ResponseEntity addGroupeAndMembers(@RequestBody AddGroupAndMembersTemplate addGroupeAndMembersTemplate) 
	{
		StringBuilder sb = new StringBuilder();
		ResponseEntity response = this.createGroupe(addGroupeAndMembersTemplate.getAdminGroupeId(), addGroupeAndMembersTemplate.getGroupeName(), addGroupeAndMembersTemplate.getIsPrivateChat()); 
		sb.append("\n");
		sb.append(response.getBody().toString());
		if(!response.getStatusCode().equals(HttpStatus.OK))
		{
			return response.ok(sb.toString());
		}
		
		int nbElementTab = 4; 
		int positionIdGroupe = 2; 
		String [] tabResponseBody = response.getBody().toString().split(" ", nbElementTab);
		String idGroupe = tabResponseBody[positionIdGroupe]; 
		Groupe gp = this.groupeService.findByIdAndDeletedatIsNull(idGroupe);
		for (Object userId : addGroupeAndMembersTemplate.getMembers()) {
			response = this.addMemberFct(addGroupeAndMembersTemplate.getAdminGroupeId(), userId.toString(), gp.getId().toString());
			sb.append("\n");
			sb.append(response.getBody().toString());
		}
		
		return ResponseEntity.ok(sb.toString()); 
		
	}
	
    @ApiOperation(value = "Retourne tous les groupes non supprimés (Suppression logique) d'un user dont l'id est envoyé en get")
	@GetMapping("/find/groups/user/{userId}")
	public ResponseEntity<Collection<Groupe>> findGroupsUser(HttpServletRequest request, @PathVariable String userId)
	{
		Long uid = Long.parseLong(userId); 
		Collection<Groupe> groups = groupeService.findGroupsByUser(uid); 
		return ResponseEntity.ok(groups); 
	}
	
}
