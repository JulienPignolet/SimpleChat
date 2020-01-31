package univ.lorraine.simpleChat.SimpleChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.lorraine.simpleChat.SimpleChat.model.File;

public interface FileRepository extends JpaRepository<File, Long> {
}
