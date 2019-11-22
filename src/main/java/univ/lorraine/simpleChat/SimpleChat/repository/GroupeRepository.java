package univ.lorraine.simpleChat.SimpleChat.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import univ.lorraine.simpleChat.SimpleChat.model.Groupe;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Long>{
	Optional<Groupe> findById(Long id);
	Groupe findByIdAndDeletedatIsNull(Long id);
	Groupe findByNameAndDeletedatIsNull(String name);
	List<Groupe> findAll();
	List<Groupe> findByDeletedatIsNull();
	
	@Query(value = "SELECT * FROM groupe join groupe_user on groupe_user.groupe_id = groupe.id join user on user.id = groupe_user.user_id WHERE user.id = ?1 and groupe_user.deletedat is null", 
			  nativeQuery = true)
	Collection<Groupe> findGroupsByUser(Long uid);
}
