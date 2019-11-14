package univ.lorraine.simpleChat.SimpleChat.model;

import org.springframework.web.context.annotation.ApplicationScope;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Base64;
import java.util.Collection;
import java.util.Set;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

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


    private String JWT;

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

}
