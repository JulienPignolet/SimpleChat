package univ.lorraine.simpleChat.SimpleChat.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

import java.util.ArrayList;
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
    private User author;

    @ManyToMany
    private Collection<MessageFile> files;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @Basic(optional = false)
    private String contenu;

    public Message(String contenu, User author, Groupe groupe){
        this.contenu = contenu;
        this.author = author;
        this.groupe = groupe;
        this.files = new ArrayList<>(); 
    }

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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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

    public String toJSON() {
        return "{\n" +
                "    \"user_id\":" + author.getId() + ", \n" +
                "    \"user_name\":\"" + author.getUsername() + "\", \n" +
                "    \"group_id\": " + groupe.getId() + ", \n" +
                "    \"message\": \"" + contenu + "\"" +
                "}";
    }

    public Message() {
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}
