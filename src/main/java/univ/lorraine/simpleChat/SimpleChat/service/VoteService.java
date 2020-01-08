package univ.lorraine.simpleChat.SimpleChat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import univ.lorraine.simpleChat.SimpleChat.model.Sondage;
import univ.lorraine.simpleChat.SimpleChat.model.Vote;
import univ.lorraine.simpleChat.SimpleChat.repository.SondageRepository;
import univ.lorraine.simpleChat.SimpleChat.repository.VoteRepository;

@Service
public class VoteService {
	private final SondageRepository sondageRepository;
    private final VoteRepository voteRepository;
    
    @Autowired
    public VoteService(SondageRepository sondageRepository, VoteRepository voteRepository) {
        this.sondageRepository = sondageRepository;
        this.voteRepository = voteRepository;
    }
    
    public void save(Vote vote) {
    	voteRepository.save(vote);
    }

    public Vote findById(Long vote_id) {
        Vote vote = voteRepository.findById(vote_id).isPresent() ? voteRepository.findById(vote_id).get() : null;
        return vote;
    }

    public Vote find(Long vote_id) {
        return voteRepository.findById(vote_id).orElse(null);
    }

	public List<Vote> findAll(){
		return voteRepository.findAll(); 
	}

}
