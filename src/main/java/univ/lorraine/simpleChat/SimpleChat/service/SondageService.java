package univ.lorraine.simpleChat.SimpleChat.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.GroupeUser;
import univ.lorraine.simpleChat.SimpleChat.model.ReponseSondage;
import univ.lorraine.simpleChat.SimpleChat.model.Sondage;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.repository.GroupeUserRepository;
import univ.lorraine.simpleChat.SimpleChat.repository.SondageRepository;

@Service
public class SondageService {
    private final SondageRepository sondageRepository;
    private final GroupeUserRepository groupeUserRepository;

    @Autowired
    public SondageService(SondageRepository sondageRepository, GroupeUserRepository groupeUserRepository) {
        this.sondageRepository = sondageRepository;
        this.groupeUserRepository = groupeUserRepository;
    }

    public void save(Sondage sondage) {
        sondageRepository.save(sondage);
    }
    
	/**
	 * 
	 * @param question question du sondage
	 * @param groupe groupe où le sondage est lancé
	 * @param voteAnonyme definit si les votes sont anonymes
	 * @param user initiateur du sondage
	 * @return Le sondage créé
	 */
	public Sondage create(String question, Groupe groupe, boolean voteAnonyme, User user, String[] reponses, Date dateFin) {

		Sondage sondage = new Sondage();
		sondage.setQuestion(question);
		sondage.setGroupe(groupe);
        sondage.setVotesAnonymes(voteAnonyme);
        sondage.setInitiateur(user);
        sondage.setDateFin(dateFin);

        return sondage;
    }

    public Sondage findById(Long sondage_id) {
        Sondage sondage = sondageRepository.findById(sondage_id).isPresent() ? sondageRepository.findById(sondage_id).get() : null;
        return sondage;
    }

    public Sondage find(Long sondage_id) {
        return sondageRepository.findById(sondage_id).orElse(null);
    }

	public List<Sondage> findAll(){
		return sondageRepository.findAll(); 
	}

    public void addReponse(Sondage sondage, ReponseSondage reponseSondage){
        sondage.addReponse(reponseSondage);
        sondageRepository.save(sondage);
    }

    public void removeReponse(Sondage sondage, ReponseSondage reponseSondage){
        sondage.removeReponse(reponseSondage);
        sondageRepository.save(sondage);
    }
   
}