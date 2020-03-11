package univ.lorraine.simpleChat.SimpleChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import univ.lorraine.simpleChat.SimpleChat.model.GroupeUser;

import java.util.List;
import java.util.Optional;


@Repository
public interface GroupeUserRepository extends JpaRepository<GroupeUser, Long>{
	Optional<GroupeUser> findById(Long id);
	
	@Query(value = "SELECT * FROM groupe_user join groupe on groupe.id = groupe_user.groupe_id join user on user.id = groupe_user.user_id WHERE groupe.id = ?1 and user.id = ?2", 
			  nativeQuery = true)
	GroupeUser findByGroupeUser(Long groupe_id, Long user_id);

	@Query(value = "SELECT * FROM groupe_user join groupe on groupe.id = groupe_user.groupe_id join user on user.id = groupe_user.user_id join role on groupe_user.role_id = role.id  WHERE user.id = ?1 and role.id = ?2",
			nativeQuery = true)
	List<GroupeUser> findByGroupeUserAdmin(Long user_id, Long role_id);

	@Query(value = "SELECT * FROM groupe_user join groupe on groupe.id = groupe_user.groupe_id join user on user.id = groupe_user.user_id WHERE groupe.id = ?1 and user.id = ?2 and groupe_user.deletedat is null",
			  nativeQuery = true)
	GroupeUser findByGroupeUserActif(Long groupe_id, Long user_id);
	
	List<GroupeUser> findAll();
	List<GroupeUser> findAllByUserId(Long user_id);
	List<GroupeUser> findByGroupeId(Long groupe_id);
	List<GroupeUser> findByGroupeAndDeletedatIsNull(Long groupe_id);
	List<GroupeUser> findByGroupeIdAndUserId(Long groupe_id, Long user_id);
}
