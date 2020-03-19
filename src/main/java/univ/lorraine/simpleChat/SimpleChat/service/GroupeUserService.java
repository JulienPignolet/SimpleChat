package univ.lorraine.simpleChat.SimpleChat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.GroupeUser;
import univ.lorraine.simpleChat.SimpleChat.model.Role;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.ocsf.AutorisationException;
import univ.lorraine.simpleChat.SimpleChat.repository.GroupeUserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GroupeUserService {
	
	private final GroupeUserRepository groupeUserRepository;

	@Autowired
	public GroupeUserService(GroupeUserRepository groupeUserRepository) {
		this.groupeUserRepository = groupeUserRepository;
	}

	public void save(GroupeUser groupeUser) {
        groupeUserRepository.save(groupeUser); 
    }
	
	public void deleteInDatabase(GroupeUser groupeUser)
	{
		groupeUserRepository.delete(groupeUser);
	}
	
	/**
	 * 
	 * @param groupe
	 * @param user
	 * @return Le groupeUser créé
	 */
	public GroupeUser create(Groupe groupe, User user)
	{
		GroupeUser groupeUser = new GroupeUser();
		user.addGroupeUser(groupeUser);
		groupe.addGroupeUser(groupeUser);
		return groupeUser;
	}
	
	/**
	 * 
	 * @param groupeUser
	 * @param role
	 * @return GroupeUser
	 */
	public GroupeUser roleGroupeUser(GroupeUser groupeUser, Role role)
	{
		groupeUser.setRole(role);
		return groupeUser;
	}
	
	/**
	 * 
	 * @param id
	 * @return GroupeUser 
	 */
	public Optional<GroupeUser> findById(Long id) {
		return groupeUserRepository.findById(id); 
	}
	
	/**
	 * 
	 * @param groupe_i
	 * @param user_i
	 * @return groupeUser
	 */
	public GroupeUser findByGroupeAndUser(Long groupe_i, Long user_i) {
		return groupeUserRepository.findByGroupeUser(groupe_i, user_i); 
	}
	
	/**
	 * 
	 * @param groupe_id
	 * @return Tous les groupeUser d'un groupe spécifique
	 */
	public List<GroupeUser> findByGroupe(Long groupe_id){
		return groupeUserRepository.findByGroupeId(groupe_id); 
	}
	
	/**
	 * 
	 * @param groupe_id
	 * @return Tous les groupeUser qui n'ont pas été supprimés d'un groupe spécifique
	 */
	public List<GroupeUser> findByGroupeAndDeletedatIsNull(Long groupe_id){
		return groupeUserRepository.findByGroupeAndDeletedatIsNull(groupe_id); 
	}
	
	/**
	 * 
	 * @return Tous les groupeUser
	 */
	public List<GroupeUser> findAll(){
		return groupeUserRepository.findAll(); 
	}

	public boolean CountByGroupeIdAndUserId(Long groupe_id, Long user_id) throws AutorisationException {
		boolean authorized = false;
		authorized = groupeUserRepository.findByGroupeIdAndUserId(groupe_id, user_id).size() > 0;
		if(authorized)
			return true;
		throw new AutorisationException(groupe_id, user_id);
	}
	
	public GroupeUser findByGroupeUserActif(Long groupeId, Long userId)
	{
		return groupeUserRepository.findByGroupeUserActif(groupeId, userId);
	}
	public List<GroupeUser> findGroupeUsersAndDeletedatIsNull(Long groupeId)
	{
		return groupeUserRepository.findGroupeUsersAndDeletedatIsNull(groupeId);
	}
  
	public List<GroupeUser> findByUser(Long user_id){
		return groupeUserRepository.findAllByUserId(user_id);
	}
}
