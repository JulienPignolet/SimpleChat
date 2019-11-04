package univ.lorraine.simpleChat.SimpleChat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import univ.lorraine.simpleChat.SimpleChat.model.*;
import univ.lorraine.simpleChat.SimpleChat.repository.GroupeRepository;
import univ.lorraine.simpleChat.SimpleChat.repository.GroupeUserRepository;
import univ.lorraine.simpleChat.SimpleChat.repository.RoleRepository;
import univ.lorraine.simpleChat.SimpleChat.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;


@Service
public class GroupeService {

	@Autowired
	private GroupeUserRepository groupeUserRepository; 
	
	@Autowired
	private GroupeRepository groupeRepository; 
	
	@Autowired 
	private UserRepository userRepository; 
	
	@Autowired 
	private RoleRepository roleRepository; 
	
	
	public void save(Groupe groupe) {
        groupeRepository.save(groupe); 
    }
	
	
	/**
	 * 
	 * @param name
	 * @param isPrivateChat (ce parametre est un string. les valeurs possibles sont 0 ou 1)
	 * @param username (l'utilisateur courant : admin du groupe)
	 * @return Le groupe crée ou un message d'erreur
	 */
	public Groupe create(String name, String isPrivateChat, String username) {
		
		Groupe groupe = new Groupe();
		groupe.setName(name);
		boolean	chatPrive = false;
		int tmp = Integer.parseInt(isPrivateChat);
		if(tmp == 1) chatPrive = true;
		groupe.setPrivateChat(chatPrive);
		
		User user = userRepository.findByUsername(username);
		if(user == null)
		{
			System.out.println("L'utilisateur '"+username+"' n'a pas été trouvé. Nous ne pouvons pas créer un groupe sans admin.");
			return null;
		}
		
		GroupeUser groupeUser = new GroupeUser();
		user.addGroupeUser(groupeUser);
		Role role = this.roleRepository.findByName(EnumRole.ADMIN_GROUP.getRole()); 
		if(role == null)
		{
			System.out.println("Il faut d'abord créer un role 'ROLE_ADMIN_GROUP' pour pouvoir créer un groupe."); 
			return null; 
		}
		groupeUser.setRole(role);
		groupe.addGroupeUser(groupeUser);
		
		groupeRepository.save(groupe);
		userRepository.save(user);
		groupeUserRepository.save(groupeUser);
		
        return groupe;
    }
	
	/**
	 * 
	 * @param groupeId
	 * @return  Un groupe qui n'est pas supprimé et dont l'id égal à groupeId 
	 */
	public Groupe findByIdAndDeletedatIsNull(String groupeId)
	{
		Long id = Long.parseLong(groupeId);
		Groupe groupe = groupeRepository.findByIdAndDeletedatIsNull(id);
		return groupe;
	}
	
	
	/**
	 * 
	 * @param groupeId: est l'identifiant du groupe dans lequel on souhaite ajouter un membre
	 * @param usernameNew : est le username de l'utilisateur qu'on souhaite ajouter au groupe
	 * @param usernameAdmin : est le username de l'utilisateur qui a déclenché l'ajout
	 * @return Le groupeUser du membre ou un message d'erreur
	 */
	public GroupeUser addMember(String groupeId, String usernameNew, String usernameAdmin)
	{
		User admin = userRepository.findByUsername(usernameAdmin);
		if(admin == null) 
		{
			System.out.println("L'utilisateur de username '"+usernameAdmin+"' existe pas !");
			return null;
		}
		
		User futurMembre = userRepository.findByUsername(usernameNew);
		if(futurMembre == null) 
		{
			System.out.println("L'utilisateur de username '"+usernameNew+"' existe pas !");
			return null;
		}
		
		Groupe groupe = this.findByIdAndDeletedatIsNull(groupeId);
		if(groupe == null)
		{
			System.out.println("Le groupe d'Id '"+groupeId+"' a été supprimé ou n'existe pas !");
			return null;
		}
			
		GroupeUser groupeUserAdmin = groupeUserRepository.findByGroupeUserActif(groupe.getId(), admin.getId());
		if( groupeUserAdmin == null )
		{
			System.out.println("L'utilisateur de username '"+usernameAdmin+"' ne fait pas partir de ce groupe !");
			return null;
		}
		
		Role role = groupeUserAdmin.getRole(); 
		ArrayList<String> acceptRoles = new ArrayList<>();
		acceptRoles.add(EnumRole.ADMIN_GROUP.getRole());
		acceptRoles.add(EnumRole.SUPER_ADMIN.getRole()); 
		if( role == null || !acceptRoles.contains(role.getName())) 
		{
			System.out.println("Seul l'admin du groupe peut rajouter un membre au groupe !");
			return null;
		}
		
		GroupeUser groupeUserNew = groupeUserRepository.findByGroupeUserActif(groupe.getId(), futurMembre.getId());
		if( groupeUserNew != null )
		{
			System.out.println("L'utilisateur de username '"+usernameNew+"' est déjà membre de ce groupe !");
			return null;
		}
		
		GroupeUser groupeUser = new GroupeUser();
		groupe.addGroupeUser(groupeUser);
		futurMembre.addGroupeUser(groupeUser);
		groupeUser.setRole(null);
		
		userRepository.save(futurMembre);
		groupeRepository.save(groupe);
		groupeUserRepository.save(groupeUser);
		
		
		return groupeUser;
	}
	
	
	
	
	public Optional<Groupe> findById(Long id) {
        return groupeRepository.findById(id); 
    }

	public Groupe find(Long id) {
		return groupeRepository.getOne(id);
	}

	public GroupeUser findByGroupeUser(Long groupe_id, Long user_id) {
		return groupeUserRepository.findByGroupeUser(groupe_id, user_id); 
	}
	
	/**
	 * 
	 * @param groupe_id
	 * @return Tous les groupeUser
	 */
	public Collection<GroupeUser> findAllGroupeUser(Long groupe_id){
		return groupeUserRepository.findByGroupeId(groupe_id); 
	}
	
	/**
	 * 
	 * @param groupe_id
	 * @return Tous les groupeUser non supprimés
	 */
	public Collection<GroupeUser> findAllGroupeUserNotDeleted(Long groupe_id){
		return groupeUserRepository.findByGroupeAndDeletedatIsNull(groupe_id); 
	}
	
	
	/**
	 * 
	 * @return Tous les groupes 
	 */
	public Collection<Groupe> findAll(){
		return groupeRepository.findAll();
	}
	
	/**
	 * 
	 * @return Tous les groupes non supprimés
	 */
	public Collection<Groupe> findByDeletedatIsNull(){
		return groupeRepository.findByDeletedatIsNull();
	}
	
	
}

