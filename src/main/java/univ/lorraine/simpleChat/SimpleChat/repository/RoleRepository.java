package univ.lorraine.simpleChat.SimpleChat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.lorraine.simpleChat.SimpleChat.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findById(Long id);
	Role findByName(String name); 
}