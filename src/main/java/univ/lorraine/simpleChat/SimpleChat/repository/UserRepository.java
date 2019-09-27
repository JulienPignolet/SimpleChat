package univ.lorraine.simpleChat.SimpleChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.lorraine.simpleChat.SimpleChat.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}