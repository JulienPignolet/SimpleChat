package univ.lorraine.simpleChat.SimpleChat.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity=Groupe.class)
    @JoinColumn(name = "groupe_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Groupe groupe;

    @ManyToOne
    @Column(nullable = false)
    private User user;

    @ManyToMany
    private Collection<MessageFile> files;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @Basic(optional = false)
    private String contenu;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}
