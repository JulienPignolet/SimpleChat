package univ.lorraine.simpleChat.SimpleChat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import univ.lorraine.simpleChat.SimpleChat.model.GroupeUser;
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
	 * @return tous les groupeUser
	 */
	public List<GroupeUser> findAll(){
		return groupeUserRepository.findAll(); 
	}
}
