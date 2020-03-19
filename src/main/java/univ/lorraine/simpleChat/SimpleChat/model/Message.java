package univ.lorraine.simpleChat.SimpleChat.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message extends Active {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity=Groupe.class)
    @JoinColumn(name = "groupe_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Groupe groupe;

    @ManyToOne
    private User author;


    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @Basic(optional = false)
    @Lob
    private String contenu;

    @Basic(optional = false)
    private String type;

    public Message(String contenu, User author, Groupe groupe, String type){
        this.contenu = contenu;
        this.author = author;
        this.groupe = groupe;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toJSON()
    {
        JsonObject json = Json.createObjectBuilder()
                .add("userId", author.getId())
                .add("userName", author.getUsername())
                .add("groupId", groupe.getId())
                .add("contenu", contenu)
                .add("type", type)
                .add("active", isActive())
                .build();
        return json.toString();
    }

    public Message() {
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}
