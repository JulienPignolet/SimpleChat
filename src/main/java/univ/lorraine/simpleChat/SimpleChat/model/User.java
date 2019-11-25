package univ.lorraine.simpleChat.SimpleChat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@ApplicationScope
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

//    @JsonIgnore
    private String password;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

//    @JsonIgnore
    @Transient
    private String passwordConfirm;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private  Collection<GroupeUser> groupeUsers;

    @JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private  Collection<Vote> listVotes;

    //
	@JsonIgnore
	@ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(name="BUDDYMASTER_BUDDY",
			joinColumns={@JoinColumn(name="id")},
			inverseJoinColumns={@JoinColumn(name="BUDDY_ID")})
	private Collection<User> buddyMaster;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER,cascade={CascadeType.ALL})
	@JoinTable(name="BUDDYMASTER_BUDDY",
			joinColumns={@JoinColumn(name="BUDDY_ID")},
			inverseJoinColumns={@JoinColumn(name="id")})
	private Collection<User> buddyList;
	
	@JsonIgnore
    @OneToMany(mappedBy = "author")
    private Collection<Message> messages;

	@JsonIgnore
	@OneToMany(mappedBy = "initiateur")
	private Collection<Sondage> listSondage;
	
	public User()
	{
		this.roles = new HashSet<Role>();
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}
	
	public void addRole(Role role)
	{
		if(!this.containsRole(role.getName())) this.roles.add(role);
	}
	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Collection<User> getBuddyMaster() {
		return buddyMaster;
	}

	public void setBuddyMaster(Collection<User> buddyMaster) {
		this.buddyMaster = buddyMaster;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public Collection<GroupeUser> getGroupeUsers() {
		return groupeUsers;
	}

	public void setGroupeUsers(Collection<GroupeUser> groupeUsers) {
		this.groupeUsers = groupeUsers;
	}

	public void addGroupeUser(GroupeUser groupeUser) {
		if(!this.groupeUsers.contains(groupeUser)) {
			this.groupeUsers.add(groupeUser); 
			groupeUser.setUser(this);
		}
	}
	
	public void removeGroupeUser(GroupeUser groupUser) {
		if(this.groupeUsers.contains(groupUser)) {
			this.groupeUsers.remove(groupUser); 
			groupUser.setUser(null);
		}
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", roles=" + roles
				+ ", passwordConfirm=" + passwordConfirm + ", groupeUsers=" + groupeUsers + "]";
	}


	public Collection<User> getBuddyList() {
		return buddyList;
	}

	public void addBuddy(User buddy) {
		//if(buddyList == null) buddyList = new ArrayList<>();
		if(!this.buddyList.contains(buddy)) {
			this.buddyList.add(buddy);
		}
	}

	public void removeBuddy(User buddy) {
		if(this.buddyList.contains(buddy)) {
			this.buddyList.remove(buddy);
		}
	}

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

	public boolean containsRole(String role) {
		for (Role roleObject : this.getRoles()) {
			if(roleObject.getName().equals(role)) return true;
		}
		return false;
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

	public Collection<Vote> getListVotes() {
		return listVotes;
	}

	public void setListVotes(Collection<Vote> listVotes) {
		this.listVotes = listVotes;
	}

	public void setBuddyList(Collection<User> buddyList) {
		this.buddyList = buddyList;
	}

	public Collection<Message> getMessages() {
		return messages;
	}

	public void setMessages(Collection<Message> messages) {
		this.messages = messages;
	}

	public Collection<Sondage> getListSondage() {
		return listSondage;
	}

	public void setListSondage(Collection<Sondage> listSondage) {
		this.listSondage = listSondage;
	}
}
