package univ.lorraine.simpleChat.SimpleChat.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import univ.lorraine.simpleChat.SimpleChat.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findById(Long id);
    
	@Query(value = "SELECT * FROM user join groupe_user on groupe_user.user_id = user.id join groupe on groupe.id = groupe_user.groupe_id WHERE groupe.id = ?1 and groupe_user.deletedat is null", 
			  nativeQuery = true)
	Collection<User> findMembersGroupe(Long groupe_id);
}