package univ.lorraine.simpleChat.SimpleChat.model;

public enum EnumRole {
	
	SUPER_ADMIN("SUPER_ADMIN", "ROLE_SUPER_ADMIN"),
	ADMIN("ADMIN", "ROLE_ADMIN"),
	ADMIN_GROUP("ADMIN_GROUP", "ROLE_ADMIN_GROUP"),
	USER("USER", "ROLE_USER");
	
	private String name = ""; 
	private String role = ""; 
	
	private EnumRole(String name, String role)
	{
		this.name = name;
		this.role = role; 
	}
	
	public String getRole()
	{
		return role;
	}
	
	public String toString()
	{
		return name; 
	}
}
