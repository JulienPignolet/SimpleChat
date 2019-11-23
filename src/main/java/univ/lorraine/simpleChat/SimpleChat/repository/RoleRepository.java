package univ.lorraine.simpleChat.SimpleChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.lorraine.simpleChat.SimpleChat.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findById(Long id);
	Role findByName(String name);
	List<Role> findAll();
}