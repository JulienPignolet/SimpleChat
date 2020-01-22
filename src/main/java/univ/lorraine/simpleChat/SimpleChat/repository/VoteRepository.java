package univ.lorraine.simpleChat.SimpleChat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.model.Vote;


public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findById(Long id);
    List<Vote> findByUser(User user);
    List<Vote> findAll();
}
