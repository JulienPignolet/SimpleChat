package univ.lorraine.simpleChat.SimpleChat.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import univ.lorraine.simpleChat.SimpleChat.model.ReponseSondage;
import univ.lorraine.simpleChat.SimpleChat.model.Sondage;

public interface ReponseSondageRepository extends JpaRepository<ReponseSondage, Long>  {
	//ReponseSondage findByReponse(String reponse);
	Optional<ReponseSondage> findById(Long id);
	List<ReponseSondage> findAll();
	
	/*@Query(value = "SELECT * FROM reponsesondage join sondage on sondage.id = reponsesondage.sondage_id WHERE sondage.id = ?1", 
			  nativeQuery = true)
	Collection<ReponseSondage> findReponsesSondage(Long sondage_id);*/
}
