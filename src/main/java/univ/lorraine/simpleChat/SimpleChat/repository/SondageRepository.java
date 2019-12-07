package univ.lorraine.simpleChat.SimpleChat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import univ.lorraine.simpleChat.SimpleChat.model.Sondage;

public interface SondageRepository extends JpaRepository<Sondage, Long> {
	Optional<Sondage> findById(Long id);
	List<Sondage> findAll();
}
