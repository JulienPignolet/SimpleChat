package univ.lorraine.simpleChat.SimpleChat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import univ.lorraine.simpleChat.SimpleChat.model.GroupeUser;


@Repository
public interface GroupeUserRepository extends JpaRepository<GroupeUser, Long>{
	Optional<GroupeUser> findById(Long id);
	GroupeUser findByGroupeAndUser(Long groupe_i, Long user_i);
	List<GroupeUser> findAll();
	List<GroupeUser> findByGroupe(Long groupe_id);
	List<GroupeUser> findByGroupeAndDeletedatIsNull(Long groupe_id);
}
