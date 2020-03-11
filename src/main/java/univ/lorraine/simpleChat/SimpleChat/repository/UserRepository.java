package univ.lorraine.simpleChat.SimpleChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import univ.lorraine.simpleChat.SimpleChat.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findById(Long id);
	List<User> findAll();
	List<User> findAllByActiveIsTrue();

	@Query(value = "SELECT * FROM user join groupe_user on groupe_user.user_id = user.id join groupe on groupe.id = groupe_user.groupe_id WHERE groupe.id = ?1 and groupe_user.deletedat is null", 
			  nativeQuery = true)
	Collection<User> findMembersGroupe(Long groupe_id);
}