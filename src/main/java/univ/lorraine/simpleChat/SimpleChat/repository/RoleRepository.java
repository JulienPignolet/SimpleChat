package univ.lorraine.simpleChat.SimpleChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.lorraine.simpleChat.SimpleChat.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}