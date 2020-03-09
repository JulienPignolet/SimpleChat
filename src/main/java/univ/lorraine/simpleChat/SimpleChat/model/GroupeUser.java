package univ.lorraine.simpleChat.SimpleChat.model;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "groupe_user")
public class GroupeUser {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Basic
    @Temporal(TemporalType.DATE)
    private Date deletedat;
    
    @Basic
    @Temporal(TemporalType.DATE)
    private Date createdat;
    
    @ManyToOne(targetEntity=User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private User user;
    
    @ManyToOne(targetEntity=Groupe.class)
    @JoinColumn(name = "groupe_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Groupe groupe;
    
	@ManyToOne(targetEntity=Role.class)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = true)
	@JsonBackReference
    private Role role;

	
	public GroupeUser() {
		this.setCreatedat(new Date());
		this.setDeletedat(null);
	}
	
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

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

	@Override
	public String toString() {
		return "GroupeUser [id=" + id + ", deletedat=" + deletedat + ", createdat=" + createdat + ", user=" + user
				+ ", groupe=" + groupe + ", role=" + role + "]";
	}
   
	public boolean isAdminGroup()
	{
		ArrayList<String> acceptRoles = new ArrayList<>();
		acceptRoles.add(EnumRole.ADMIN_GROUP.getRole());
		acceptRoles.add(EnumRole.SUPER_ADMIN.getRole()); 
		if(this.getRole() == null) return false; 
		return acceptRoles.contains(this.getRole().getName()); 
	}
    
}
