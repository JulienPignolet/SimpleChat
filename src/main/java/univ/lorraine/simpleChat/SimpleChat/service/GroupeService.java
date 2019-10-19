package univ.lorraine.simpleChat.SimpleChat.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.GroupeUser;
import univ.lorraine.simpleChat.SimpleChat.repository.GroupeRepository;
import univ.lorraine.simpleChat.SimpleChat.repository.GroupeUserRepository;


@Service
public class GroupeService {

	@Autowired
	private GroupeUserRepository groupeUserRepository; 
	
	@Autowired
	private GroupeRepository groupeRepository; 
	
	
	public void save(Groupe groupe) {
        groupeRepository.save(groupe); 
    }
	
	public Optional<Groupe> findById(Long id) {
        return groupeRepository.findById(id); 
    }
	
	public GroupeUser findByGroupeUser(Long groupe_id, Long user_id) {
		return groupeUserRepository.findByGroupeAndUser(groupe_id, user_id); 
	}
	
	/**
	 * 
	 * @param groupe_id
	 * @return Tous les groupeUser
	 */
	public Collection<GroupeUser> findAllGroupeUser(Long groupe_id){
		return groupeUserRepository.findByGroupe(groupe_id); 
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

