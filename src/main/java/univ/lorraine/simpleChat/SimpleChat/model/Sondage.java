package univ.lorraine.simpleChat.SimpleChat.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "sondage")
public class Sondage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String question;

    @Column
    private boolean status;

    @Basic
    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Basic
    @Temporal(TemporalType.DATE)
    private Date dateFin;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sondage")
    private Collection<ReponseSondage> reponseSondages;

    @ManyToOne(targetEntity=Groupe.class)
    @JoinColumn(name = "groupe_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Groupe groupe;

    @ManyToOne(targetEntity=User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private User initiateur;

    @Column
    private boolean votesAnonymes;

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public void addReponse(ReponseSondage reponseSondages) {
        if(!this.reponseSondages.contains(reponseSondages)) {
            this.reponseSondages.add(reponseSondages);
        }
    }

    public void removeReponse(ReponseSondage reponseSondages) {
        if(this.reponseSondages.contains(reponseSondages)) {
            this.reponseSondages.remove(reponseSondages);
        }
    }

    public Collection<ReponseSondage> getReponseSondages() {
        return reponseSondages;
    }

    public void setReponseSondages(Collection<ReponseSondage> reponseSondages) {
        this.reponseSondages = reponseSondages;
    }

    public boolean isVotesAnonymes() {
        return votesAnonymes;
    }

    public void setVotesAnonymes(boolean votesAnonymes) {
        this.votesAnonymes = votesAnonymes;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public User getInitiateur() {
        return initiateur;
    }

    public void setInitiateur(User initiateur) {
        this.initiateur = initiateur;
    }
}
