package univ.lorraine.simpleChat.SimpleChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.Message;

import java.util.Collection;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.groupe = ?1")
    Collection<Message> get(Groupe groupe);

    Collection<Message> findByGroupeIdAndActiveIsTrue(Long groupe_id);

    @Query("SELECT m FROM Message m WHERE m.groupe= ?1 AND m.type = 'drawpad'")
    Collection<Message> getAllDrawpadMessages(Groupe groupe);



}
