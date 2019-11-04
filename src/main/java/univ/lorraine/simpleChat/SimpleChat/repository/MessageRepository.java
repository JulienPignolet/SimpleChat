package univ.lorraine.simpleChat.SimpleChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import univ.lorraine.simpleChat.SimpleChat.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
