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

    @Autowired
    public ReponseSondageService(ReponseSondageRepository reponseSondageRepository) {
        this.reponseSondageRepository = reponseSondageRepository;
    }

    public void save(ReponseSondage reponseSondage) {
        reponseSondageRepository.save(reponseSondage);
    }


	public ReponseSondage findById(Long reponsesondage_id) {
		return reponseSondageRepository.findById(reponsesondage_id).isPresent() ? reponseSondageRepository.findById(reponsesondage_id).get() : null;
	}

    public ReponseSondage find(Long reponsesondage_id) {
        return reponseSondageRepository.findById(reponsesondage_id).orElse(null);
    }

	public List<ReponseSondage> findAll(){
		return reponseSondageRepository.findAll(); 
	}

}