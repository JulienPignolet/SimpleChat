package univ.lorraine.simpleChat.SimpleChat.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.GroupeUser;
import univ.lorraine.simpleChat.SimpleChat.model.Role;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.repository.GroupeRepository;
import univ.lorraine.simpleChat.SimpleChat.repository.GroupeUserRepository;
import univ.lorraine.simpleChat.SimpleChat.repository.RoleRepository;
import univ.lorraine.simpleChat.SimpleChat.repository.UserRepository;


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
	 * @param isPrivateChat (ce parametre est un string. les valeurs possibles sont 0, false, 1 ou true)
	 * @param username (l'utilisateur courant : admin du groupe)
	 * @return Le groupe crée
	 */
	public Groupe create(String name, String isPrivateChat, String username) {
		
		Groupe groupe = new Groupe();
		groupe.setName(name);
		boolean	chatPrive = false;
		if(isPrivateChat == "1" || isPrivateChat == "true") chatPrive = true;
		groupe.setPrivateChat(chatPrive);
		
		User user = userRepository.findByUsername(username);
		GroupeUser groupeUser = new GroupeUser();
		user.addGroupeUser(groupeUser);
		Role role = this.roleRepository.findByName("ROLE_ADMIN_GROUPE"); 
		groupeUser.setRole(role);
		groupe.addGroupeUser(groupeUser);
		
		groupeRepository.save(groupe);
		userRepository.save(user);
		groupeUserRepository.save(groupeUser);
		
        return groupe;
    }
	
	
	public Optional<Groupe> findById(Long id) {
        return groupeRepository.findById(id); 
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

