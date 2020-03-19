package univ.lorraine.simpleChat.SimpleChat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.lorraine.simpleChat.SimpleChat.model.*;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.AddGroupAndMembersTemplate;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.AddMemberTemplate;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.DeleteGroupTemplate;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.DeleteUserInGroupTemplate;
import univ.lorraine.simpleChat.SimpleChat.modelTemplate.GroupeTemplate;
import univ.lorraine.simpleChat.SimpleChat.service.*;

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
    private final MessageService messageService;

	@Autowired
	public GroupeController(UserService userService, GroupeService groupeService, GroupeUserService groupeUserService, RoleService roleService, MessageService messageService) {
		this.userService = userService;
		this.groupeService = groupeService;
		this.groupeUserService = groupeUserService;
		this.roleService = roleService;
		this.messageService = messageService;
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
	 * @return Absolument tous les groupes
	 */
    @ApiOperation(value = "Retourne absolument tous les groupes")
	@GetMapping("/findAll/groupe/all")
	public ResponseEntity<Collection<Groupe>> findAllGroupeAll(HttpServletRequest request) 
	{
		Collection<Groupe> listGroupe = this.groupeService.findAll(); 
		return ResponseEntity.ok(listGroupe); 
	}
    
    /**
	 * 
	 * @param request
	 * @return Tous les groupes supprimés
	 */
    @ApiOperation(value = "Retourne tous les groupes supprimés (suppression logique)")
	@GetMapping("/findAll/groupe-deleted")
	public ResponseEntity<Collection<Groupe>> findAllGroupDeleted(HttpServletRequest request) 
	{
		Collection<Groupe> listGroupe = this.groupeService.findByDeletedatIsNotNull(); 
		return ResponseEntity.ok(listGroupe); 
	}
	
	/**
	 * 
	 * @param request
	 * @param groupeId
	 * @return Tous les membres non supprimés d'un groupe
	 */
    @ApiOperation(value = "Retourne tous les membres non supprimés (suppression logique) d'un groupe.")
	@GetMapping("/find/Members/groupe/{groupeId}")
	public ResponseEntity<Collection<User>> findMembersGroupe(HttpServletRequest request, @PathVariable String groupeId)
	{
		Collection<User> users = new ArrayList<>();
		try {
			Long gid = Long.parseLong(groupeId);
			users = userService.findMembersGroupe(gid);
		}
		catch (Exception e)
		{
			logger.warn(e.getMessage());
		}
		return ResponseEntity.ok(users);
	}

    /**
	 * 
	 * @param request
	 * @param groupeId
	 * @return Tous les membres non supprimés d'un groupe non supprimé
	 */
    @ApiOperation(value = "Retourne tous les membres non supprimés (suppression logique) d'un groupe non supprimé.")
	@GetMapping("/find/Members/group-not-deleted/{groupeId}")
	public ResponseEntity<Collection<User>> findMembersGroupNotDeleted(HttpServletRequest request, @PathVariable String groupeId)
	{
		Collection<User> users = new ArrayList<>();
		try {
			Long gid = Long.parseLong(groupeId);
			Groupe group = groupeService.findByIdAndDeletedatIsNull(groupeId);
			if( group == null )	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(users);
			users = userService.findMembersGroupe(gid);
		}
		catch (Exception e)
		{
			logger.warn(e.getMessage());
		}
		return ResponseEntity.ok(users);
	}
    
	/**
	 *
	 * @param request
	 * @param groupeId
	 * @return Tous les membres d'un groupe
	 */
	@ApiOperation(value = "Retourne tous les amis présent dans le groupe.")
	@GetMapping("/find/Members/groupe/amis/{groupeId}/{userId}")
	public ResponseEntity<Collection<User>> findFriendsInGroupe(HttpServletRequest request, @PathVariable String groupeId,@PathVariable String userId)
	{
		Collection<User> users ;
		ArrayList<User> amis = new ArrayList<>();
		try {
			Long gid = Long.parseLong(groupeId);
			users = userService.findMembersGroupe(gid);

			User user = userService.find(Long.parseLong(userId));

			User friend;
			List<User> tempUser = new ArrayList<>(user.getBuddyList());
			for (User u : tempUser) {
				friend = users.stream().filter(temp -> u.getId().equals(temp.getId())).findAny().orElse(null);
				if(friend != null){
					amis.add(friend);
				}
				friend = null;
			}
		}
		catch (Exception e)
		{
			logger.warn(e.getMessage());
		}
		return ResponseEntity.ok(amis);
	}

	/**
	 *
	 * @param request
	 * @param groupeId
	 * @return Tous les membres d'un groupe
	 */
	@ApiOperation(value = "Retourne tous les bloqués présent dans le groupe.")
	@GetMapping("/find/Members/groupe/bloque/{groupeId}/{userId}")
	public ResponseEntity<Collection<User>> findBlockedInGroupe(HttpServletRequest request, @PathVariable String groupeId,@PathVariable String userId)
	{
		Collection<User> users ;
		ArrayList<User> listeBloque = new ArrayList<>();
		try {
			Long gid = Long.parseLong(groupeId);
			users = userService.findMembersGroupe(gid);
			User user = userService.find(Long.parseLong(userId));

			User blocked;
			List<User> tempUser = new ArrayList<>(user.getMyBlocklist());
			for (User u : tempUser) {
				blocked = users.stream().filter(temp -> u.getId().equals(temp.getId())).findAny().orElse(null);
				if(blocked != null){
					listeBloque.add(blocked);
				}
				blocked = null;
			}
		}
		catch (Exception e)
		{
			logger.warn(e.getMessage());
		}
		return ResponseEntity.ok(listeBloque);
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

	/**
	 *
	 * @param request
	 * @return les groupes ou l'utilisateur est admin
	 */
	@ApiOperation(value = "Retourne les groupes non supprimés (suppression logique) où l'utilisateur est admin")
	@GetMapping("/find/groupe/admin/{userId}")
	public ResponseEntity<Collection<Groupe>> findGroupeByUserAdmin(HttpServletRequest request,@PathVariable String userId)
	{
		List<GroupeUser> groupeUser = this.groupeService.findAllByGroupeUserAdmin(Long.parseLong(userId));
		List<Groupe> listeGroupe = new ArrayList<>();
		for (GroupeUser gu: groupeUser) {
			Groupe groupe = gu.getGroupe();  
			if(groupe.getDeletedat() == null) listeGroupe.add(groupe);
		}
		return ResponseEntity.ok(listeGroupe);

	}
	
	/**
	 * 
	 * @param deleteUserInGroupTemplate
	 * @return  Un message de confirmation de la suppression d'un utilisateur dans un groupe ou un message d'erreur
	 */
    @ApiOperation(value = "Supprime définitivement un membre (user) dans un groupe. Seul l'admin du groupe (Celui qui a créé le groupe) ou un superadmin peuvent supprimer un membre.")
	@PostMapping("/delete/member")
	public ResponseEntity deleteMember(@RequestBody DeleteUserInGroupTemplate deleteUserInGroupTemplate)
	{
    	try {
			
			Long adminGroupeIdOrSuperAdminId = Long.parseLong(deleteUserInGroupTemplate.getUserId());
            User adminGroupOrSuperAdmin = this.userService.findById(adminGroupeIdOrSuperAdminId);
    		if(adminGroupOrSuperAdmin == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id "+adminGroupeIdOrSuperAdminId+" n'a pas été trouvé.");
    		}
    		
            Long userDelId = Long.parseLong(deleteUserInGroupTemplate.getUserDelId());
            User userDel = this.userService.findById(userDelId);
    		if(userDel == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id "+userDelId+" n'a pas été trouvé.");
    		}

    		Groupe groupe = groupeService.findByIdAndDeletedatIsNull(deleteUserInGroupTemplate.getGroupId());
    		if(groupe == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le groupe d'Id '"+deleteUserInGroupTemplate.getGroupId()+"' a été supprimé ou n'existe pas !");
    		}
    		
    		GroupeUser groupeUserToDelete = groupeUserService.findByGroupeUserActif(groupe.getId(), userDelId);
    		if( groupeUserToDelete == null )
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id '"+userDelId+"' n'est pas membre de ce groupe. Par conséquent, il ne peut être supprimé du groupe !");
    		}
    		
    		if(!adminGroupOrSuperAdmin.isSuperAdmin())
    		{
    			GroupeUser groupeUserAdmin = groupeUserService.findByGroupeUserActif(groupe.getId(), adminGroupeIdOrSuperAdminId);
    			if( groupeUserAdmin == null )
        		{
        			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id '"+userDelId+"' ne fait pas partir de ce groupe !");
        		}
    			
        		if( !groupeUserAdmin.isAdminGroup() ) 
        		{
        			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seul l'admin du groupe et le super admin  peuvent supprimer un membre d'un groupe !");
        		}
    		}
    		
    		
    		groupeUserService.deleteInDatabase(groupeUserToDelete);
    			
            
    		return ResponseEntity.ok("L'utilisateur d'id "+userDelId+"a été supprimé du groupe d'id "+groupe.getId()+" avec succès !");
        } catch (NumberFormatException  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les données doivent être envoyé en JSON.");
        }
    	
	}
    
    
    /**
	 * 
	 * @param deleteGroupTemplate
	 * @return  Un message de confirmation de la suppression du groupe ou un message d'erreur
	 */
    @ApiOperation(value = "Supprime (cache) un groupe (suppression logique). Seul l'admin du groupe (Celui qui a créé le groupe) ou un superadmin peuvent le supprimer.")
	@PostMapping("/hide/group")
	public ResponseEntity hideGroup(@RequestBody DeleteGroupTemplate deleteGroupTemplate)
	{
    	try {
			
			Long userId = Long.parseLong(deleteGroupTemplate.getUserId());
            User user = this.userService.findById(userId);
    		if(user == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id "+userId+" n'a pas été trouvé.");
    		}

    		Groupe groupe = groupeService.findByIdAndDeletedatIsNull(deleteGroupTemplate.getGroupId());
    		if(groupe == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le groupe d'Id '"+deleteGroupTemplate.getGroupId()+"' a été supprimé ou n'existe pas !");
    		}
    		
    		if(!user.isSuperAdmin())
    		{
    			GroupeUser groupeUser = groupeUserService.findByGroupeUserActif(groupe.getId(), user.getId());
    			if( groupeUser == null )
        		{
        			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id '"+userId+"' ne fait pas partir de ce groupe !");
        		}
    			
        		if( !groupeUser.isAdminGroup() ) 
        		{
        			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seul l'admin du groupe et le super admin  peuvent le supprimer !");
        		}
    		}
    		
    		
    		groupeService.hideGroup(groupe);
    		groupeService.save(groupe);
    			
            
    		return ResponseEntity.ok("Le groupe d'id "+groupe.getId()+"a été supprimé avec succès !");
        } catch (NumberFormatException  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les données doivent être envoyé en JSON.");
        }
    	
	}
    
    /**
	 * 
	 * @param deleteGroupTemplate
	 * @return  Un message de confirmation de la réintégration d'un groupe ou un message d'erreur
	 */
    @ApiOperation(value = "Réintègre (rend visible) un groupe. Seul l'admin du groupe (Celui qui a créé le groupe) ou un superadmin peuvent le rendre visible.")
	@PostMapping("/show/group")
	public ResponseEntity showGroup(@RequestBody DeleteGroupTemplate deleteGroupTemplate)
	{
    	try {
			
			Long userId = Long.parseLong(deleteGroupTemplate.getUserId());
            User user = this.userService.findById(userId);
    		if(user == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id "+userId+" n'a pas été trouvé.");
    		}

    		Long groupId = Long.parseLong(deleteGroupTemplate.getGroupId()); 
    		Groupe groupe = groupeService.findById(groupId).get();
    		if(groupe == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le groupe d'Id '"+deleteGroupTemplate.getGroupId()+"' a été supprimé ou n'existe pas !");
    		}
    		
    		if(!user.isSuperAdmin())
    		{
    			GroupeUser groupeUser = groupeUserService.findByGroupeUserActif(groupe.getId(), user.getId());
    			if( groupeUser == null )
        		{
        			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id '"+userId+"' ne fait pas partir de ce groupe !");
        		}
    			
        		if( !groupeUser.isAdminGroup() ) 
        		{
        			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seul l'admin du groupe et le super admin  peuvent le rendre visible !");
        		}
    		}
    		
    		
    		groupeService.showGroup(groupe);
    		groupeService.save(groupe);
    			
            
    		return ResponseEntity.ok("Le groupe d'id "+groupe.getId()+"a été rendu visible avec succès !");
        } catch (NumberFormatException  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les données doivent être envoyé en JSON.");
        }
    	
	}
    
    
    /**
     * 
     * @param userId
     * @param groupId
     * @return
     */
    @ApiOperation(value = "Retourne le rôle d'un utilisateur dans un groupe.")
	@GetMapping("/role/user-in-group/{userId}/{groupId}")
	public ResponseEntity roleUserInGroup(@PathVariable String userId, @PathVariable String groupId)
	{
    	try {
			
			Long userIdConvert = Long.parseLong(userId);
            User user = this.userService.findById(userIdConvert);
    		if(user == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id "+userId+" n'a pas été trouvé.");
    		}

    		Groupe groupe = groupeService.findByIdAndDeletedatIsNull(groupId);
    		if(groupe == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le groupe d'Id '"+groupId+"' a été supprimé ou n'existe pas !");
    		}
    		
    		GroupeUser groupeUser = groupeUserService.findByGroupeUserActif(groupe.getId(), userIdConvert);
    		if( groupeUser == null )
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id '"+userId+"' n'est pas membre de ce groupe.");
    		}
    			
    		return ResponseEntity.ok(groupeUser.getRole());
        } catch (NumberFormatException  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les données doivent être envoyé en JSON.");
        }
    	
	}
	

    /**
   	 * 
   	 * @param DeleteGroupTemplate
   	 * @return  Un message de confirmation ou un message d'erreur
   	 */
    @ApiOperation(value = "Retourne le rôle des utilisateurs d'un groupe.")
   	@GetMapping("/role/user-in-group/{groupId}")
   	public ResponseEntity roleUsersInGroup(@PathVariable String groupId)
   	{
       	try {
   			
       		Groupe groupe = groupeService.findByIdAndDeletedatIsNull(groupId);
       		if(groupe == null)
       		{
       			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le groupe d'Id '"+groupId+"' a été supprimé ou n'existe pas !");
       		}
       		
       		ArrayList<GroupeUser> groupeUsers = (ArrayList<GroupeUser>) groupeUserService.findGroupeUsersAndDeletedatIsNull(groupe.getId());
       		return ResponseEntity.ok(groupeUsers);
       } catch (NumberFormatException  e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les données doivent être envoyé en JSON.");
       }
       	
   	}
    
    

    @ApiOperation(value = "Ajoute un admin à un groupe existant")
    @PostMapping("/addAdmin/{groupeId}/{userId}")
	public ResponseEntity add(@PathVariable String groupeId, @PathVariable String userId) 
	{
    	try {
			
            Long userIdConv = Long.parseLong(userId);
            User user = this.userService.findById(userIdConv);
    		if(user == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur d'id "+userId+" n'a pas été trouvé.");
    		}

    		Groupe groupe = groupeService.findByIdAndDeletedatIsNull(groupeId);
    		if(groupe == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le groupe d'Id '"+groupeId+"' a été supprimé ou n'existe pas !");
    		}
    		
    		
    		Role role = this.roleService.findByName(EnumRole.ADMIN_GROUP.getRole()); 
    		if(role == null)
    		{
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Il faut d'abord créer un role "+EnumRole.ADMIN_GROUP.getRole()+" pour pouvoir ajouter un admin au groupe.");
    		}
    		
    		GroupeUser groupeUser = groupeUserService.findByGroupeUserActif(groupe.getId(), user.getId());
    		if( groupeUser == null )
    		{
    			groupeUser = this.groupeUserService.create(groupe, user); 
        		groupe.addGroupeUser(groupeUser);
        		user.addGroupeUser(groupeUser);
    		}
    		
    		groupeUser = this.groupeUserService.roleGroupeUser(groupeUser, role);
    		
    		this.userService.save(user);
    		this.groupeService.save(groupe);
    		this.groupeUserService.save(groupeUser);
            
    		return ResponseEntity.ok("L'utilisateur d'id "+user.getId()+"a été ajouté au groupe d'id "+groupe.getId()+" en tant que admin avec succès !");
        } catch (NumberFormatException  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les données doivent être envoyé en JSON.");
        }
	}

	@ApiOperation(value="Supprime tous les messages de type drawpad au sein d'un groupe")
	@GetMapping("/deleteDrawpadMessages/{groupeId}")
	public ResponseEntity deleteAllDrawpadMessages(@PathVariable String groupeId)
	{
		try {
			Groupe groupe = groupeService.findByIdAndDeletedatIsNull(groupeId);
			if(groupe == null)
			{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le groupe d'Id '"+groupeId+"' a été supprimé ou n'existe pas !");
			}

			List<Message> drawpadMessages = this.messageService.getDrawpadMessages(groupe);
			for (Message message :
					drawpadMessages) {
				this.messageService.deleteInDatabase(message);
			}
			return ResponseEntity.ok("Tous les messages de type Drawpad ont été supprimés avec succès !");

		} catch (NumberFormatException  e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les données doivent être envoyé en JSON.");
		}
}
}
