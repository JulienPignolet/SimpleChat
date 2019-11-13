package univ.lorraine.simpleChat.SimpleChat.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "groupe")
public class Groupe {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String name;
    
    @Column
    private boolean isPrivateChat;
    
    @Basic
    @Temporal(TemporalType.DATE)
    private Date deletedat;

	@OneToMany(mappedBy = "groupe")
	private Collection<Message> messages;
    
    @Basic
    @Temporal(TemporalType.DATE)
    private Date createdat;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "groupe")
	private  Collection<GroupeUser> groupeUsers;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "groupe")
	private Collection<Sondage> listSondage;


	public Groupe() {
		this.setCreatedat(new Date());
		this.setDeletedat(null);
		this.groupeUsers = new ArrayList<>(); 
	}

	public Collection<Message> getMessages() {
		return messages;
	}

	public void setMessages(Collection<Message> messages) {
		this.messages = messages;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPrivateChat() {
		return isPrivateChat;
	}

	public void setPrivateChat(boolean isPrivateChat) {
		this.isPrivateChat = isPrivateChat;
	}

	public Collection<GroupeUser> getGroupeUsers() {
		return groupeUsers;
	}

	public void setGroupeUsers(Collection<GroupeUser> groupeUsers) {
		this.groupeUsers = groupeUsers;
	}

	public Date getDeletedat() {
		return deletedat;
	}

	public void setDeletedat(Date deletedat) {
		this.deletedat = deletedat;
	}

	public Date getCreatedat() {
		return createdat;
	}

	public void setCreatedat(Date createdat) {
		this.createdat = createdat;
	}

	public void addGroupeUser(GroupeUser groupeUser) {
		if(!this.groupeUsers.contains(groupeUser)) {
			this.groupeUsers.add(groupeUser); 
			groupeUser.setGroupe(this);
		}
	}
	
	public void removeGroupeUser(GroupeUser groupeUser) {
		if(this.groupeUsers.contains(groupeUser)) {
			this.groupeUsers.remove(groupeUser); 
			groupeUser.setGroupe(null);
		}
	}

	@Override
	public String toString() {
		return "Groupe [id=" + id + ", name=" + name + ", isPrivateChat=" + isPrivateChat + ", deletedat=" + deletedat
				+ ", messages=" + messages + ", createdat=" + createdat + ", groupeUsers=" + groupeUsers + "]";
	}

	public Collection<Sondage> getListSondage() {
		return listSondage;
	}

	public void setListSondage(Collection<Sondage> listSondage) {
		this.listSondage = listSondage;
	}

	public void addSondage(Sondage sondage) {
		if(!this.listSondage.contains(sondage)) {
			this.listSondage.add(sondage);
		}
	}

	public void removeSondage(Sondage sondage) {
		if(this.listSondage.contains(sondage)) {
			this.listSondage.remove(sondage);
		}
	}
}
