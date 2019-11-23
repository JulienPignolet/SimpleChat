package univ.lorraine.simpleChat.SimpleChat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import univ.lorraine.simpleChat.SimpleChat.model.ReponseSondage;
import univ.lorraine.simpleChat.SimpleChat.model.Sondage;
import univ.lorraine.simpleChat.SimpleChat.repository.SondageRepository;

@Service
public class SondageService {
    private final SondageRepository sondageRepository;

    @Autowired
    public SondageService(SondageRepository sondageRepository) {
        this.sondageRepository = sondageRepository;
    }

    public void save(Sondage sondage) {
        sondageRepository.save(sondage);
    }

    public Sondage findById(Long sondage_id) {
        Sondage sondage = sondageRepository.findById(sondage_id).isPresent() ? sondageRepository.findById(sondage_id).get() : null;
        return sondage;
    }

    public Sondage find(Long sondage_id) {
        return sondageRepository.findById(sondage_id).orElse(null);
    }

    public void addReponse(Sondage sondage, ReponseSondage reponse){
        sondage.addReponse(reponse);
        sondageRepository.save(sondage);
    }

    public void removeReponse(Sondage sondage, ReponseSondage reponse){
        sondage.removeReponse(reponse);
        sondageRepository.save(sondage);
    }
    
}