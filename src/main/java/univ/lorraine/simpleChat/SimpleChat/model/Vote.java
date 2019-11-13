package univ.lorraine.simpleChat.SimpleChat.model;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "vote")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity=User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne(targetEntity=ReponseSondage.class)
    @JoinColumn(name = "reponse_sondage_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private ReponseSondage reponseSondage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ReponseSondage getReponseSondage() {
        return reponseSondage;
    }

    public void setReponseSondage(ReponseSondage reponseSondage) {
        this.reponseSondage = reponseSondage;
    }
}
