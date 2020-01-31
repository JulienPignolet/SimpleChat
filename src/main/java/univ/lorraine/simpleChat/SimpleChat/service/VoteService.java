package univ.lorraine.simpleChat.SimpleChat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import univ.lorraine.simpleChat.SimpleChat.model.Sondage;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.model.Vote;
import univ.lorraine.simpleChat.SimpleChat.repository.VoteRepository;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    
    @Autowired
    public VoteService(VoteRepository voteRepository) {
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

	public boolean hasUserVoted(User user, Sondage sondage) {
        List<Vote> votes = voteRepository.findByUser(user);
        if (votes.isEmpty()) {
            return false;
        }

        for (Vote vote: votes) {
            if (vote.getReponseSondage().getSondage().getId().equals(sondage.getId())) {
                return true;
            }
        }

        return false;
    }
}
