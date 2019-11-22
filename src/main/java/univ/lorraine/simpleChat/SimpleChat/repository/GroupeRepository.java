package univ.lorraine.simpleChat.SimpleChat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import univ.lorraine.simpleChat.SimpleChat.model.Groupe;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Long>{
	Optional<Groupe> findById(Long id);
	Groupe findByIdAndDeletedatIsNull(Long id);
	Groupe findByNameAndDeletedatIsNull(String name);
	List<Groupe> findAll();
	List<Groupe> findByDeletedatIsNull();
}
