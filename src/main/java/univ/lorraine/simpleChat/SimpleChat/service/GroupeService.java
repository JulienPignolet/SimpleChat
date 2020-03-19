package univ.lorraine.simpleChat.SimpleChat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import univ.lorraine.simpleChat.SimpleChat.model.EnumRole;
import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.GroupeUser;
import univ.lorraine.simpleChat.SimpleChat.repository.GroupeRepository;
import univ.lorraine.simpleChat.SimpleChat.repository.GroupeUserRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class GroupeService {

	private final GroupeUserRepository groupeUserRepository;
	
	private final GroupeRepository groupeRepository;

	private final RoleService roleService;


	@Autowired
	public GroupeService(GroupeUserRepository groupeUserRepository, GroupeRepository groupeRepository, RoleService roleService) {
		this.groupeUserRepository = groupeUserRepository;
		this.groupeRepository = groupeRepository;
		this.roleService = roleService;
	}


	public void save(Groupe groupe) {
        groupeRepository.save(groupe); 
    }
	
	
	/**
	 * 
	 * @param name : Le nom du groupe
	 * @param isPrivateChat (ce parametre est un string. les valeurs possibles sont 0 ou 1)
	 * @return Le groupe créé
	 */
	public Groupe create(String name, String isPrivateChat) {
		
		Groupe groupe = new Groupe();
		groupe.setName(name);
		boolean	chatPrive = false;
		int tmp = Integer.parseInt(isPrivateChat);
		if(tmp == 1) chatPrive = true;
		groupe.setPrivateChat(chatPrive);
		
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
	
	
	public Optional<Groupe> findById(Long id) {
        return groupeRepository.findById(id); 
    }

	public Groupe find(Long id) {
		return groupeRepository.getOne(id);
	}

	public GroupeUser findByGroupeUser(Long groupe_id, Long user_id) {
		return groupeUserRepository.findByGroupeUser(groupe_id, user_id);
	}

	public List<GroupeUser> findAllByGroupeUserAdmin(Long user_id) {
		Long role_id = roleService.findByName(EnumRole.ADMIN_GROUP.getRole()).getId();
		return groupeUserRepository.findByGroupeUserAdmin(user_id, role_id);
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
	
	
	/**
	 * 
	 * @return Tous les groupes supprimés
	 */
	public Collection<Groupe> findByDeletedatIsNotNull(){
		return groupeRepository.findByDeletedatIsNotNull();
	}
	
	public Groupe findByNameAndDeletedatIsNull(String name)
	{
		return groupeRepository.findByNameAndDeletedatIsNull(name);
	}


	public Collection<Groupe> findGroupsByUser(Long uid) {
		return groupeRepository.findGroupsByUser(uid);
	}
	
	public void hideGroup(Groupe group)
	{
		group.setDeletedat( new Date() );
		this.save(group);
	}
	
	public void showGroup(Groupe group)
	{
		group.setDeletedat(null);
		this.save(group);
	}
	
	
}

