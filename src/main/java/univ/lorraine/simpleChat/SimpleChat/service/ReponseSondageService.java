package univ.lorraine.simpleChat.SimpleChat.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import univ.lorraine.simpleChat.SimpleChat.model.ReponseSondage;
import univ.lorraine.simpleChat.SimpleChat.model.Sondage;
import univ.lorraine.simpleChat.SimpleChat.repository.ReponseSondageRepository;
import univ.lorraine.simpleChat.SimpleChat.repository.SondageRepository;

@Service
public class ReponseSondageService {
    private final ReponseSondageRepository reponseSondageRepository;
    private final SondageRepository sondageRepository;

    @Autowired
    public ReponseSondageService(ReponseSondageRepository reponseSondageRepository, SondageRepository sondageRepository) {
        this.reponseSondageRepository = reponseSondageRepository;
        this.sondageRepository = sondageRepository;
    }

    public void save(ReponseSondage reponseSondage) {
        reponseSondageRepository.save(reponseSondage);
    }
    

    /*public ReponseSondage findByReponse(String reponse) {
        return reponseSondageRepository.findByReponse(reponse);
    }*/

	public ReponseSondage findById(Long reponsesondage_id) {
		ReponseSondage reponseSondage = reponseSondageRepository.findById(reponsesondage_id).isPresent() ? reponseSondageRepository.findById(reponsesondage_id).get() : null;
        return reponseSondage;
	}

    public ReponseSondage find(Long reponsesondage_id) {
        return reponseSondageRepository.findById(reponsesondage_id).orElse(null);
    }

	public List<ReponseSondage> findAll(){
		return reponseSondageRepository.findAll(); 
	}
    /*
    public Collection<ReponseSondage> findReponsesSondage(Long sondage_id)
    {
    	return reponseSondageRepository.findReponsesSondage(sondage_id);
    }
    */
}