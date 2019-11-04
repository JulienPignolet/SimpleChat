package univ.lorraine.simpleChat.SimpleChat.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
				+ ", createdat=" + createdat + ", groupeUsers=" + groupeUsers + "]";
	}





    
    

}
