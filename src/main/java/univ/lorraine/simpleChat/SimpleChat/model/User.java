package univ.lorraine.simpleChat.SimpleChat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@ApplicationScope
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @ManyToMany
    private Set<Role> roles;

    @Transient
    private String passwordConfirm;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private  Collection<GroupeUser> groupeUsers;

    //
	@JsonIgnore
	@ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(name="BUDDYMASTER_BUDDY",
			joinColumns={@JoinColumn(name="id")},
			inverseJoinColumns={@JoinColumn(name="BUDDY_ID")})
	private Collection<User> buddyMaster;

	@ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(name="BUDDYMASTER_BUDDY",
			joinColumns={@JoinColumn(name="BUDDY_ID")},
			inverseJoinColumns={@JoinColumn(name="id")})
	private Collection<User> buddyList;

    @OneToMany(mappedBy = "author")
    private Collection<Message> messages;
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
	
	public boolean containsRole(String role) {
		for (Role roleObject : this.getRoles()) {
			if(roleObject.getName().equals(role)) return true;
		}
		return false;
	}
}
