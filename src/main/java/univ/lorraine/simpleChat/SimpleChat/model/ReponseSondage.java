package univ.lorraine.simpleChat.SimpleChat.model;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "reponse_sondage")
public class ReponseSondage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String question;

    @Column
    private int nbVote;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "reponseSondage")
    private Collection<Vote> listVotes;

    @ManyToOne(targetEntity=Sondage.class)
    @JoinColumn(name = "sondage_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Sondage sondage;


    public void addVote(Vote vote) {
        if(!this.listVotes.contains(vote)) {
            this.listVotes.add(vote);
        }
    }

    public void removeVote(Vote vote) {
        if(this.listVotes.contains(vote)) {
            this.listVotes.remove(vote);
        }
    }

    public Collection<Vote> getListVotes() {
        return listVotes;
    }

    public void setListVotes(Collection<Vote> listVotes) {
        this.listVotes = listVotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getNbVote() {
        return nbVote;
    }

    public void setNbVote(int nbVote) {
        this.nbVote = nbVote;
    }
}
