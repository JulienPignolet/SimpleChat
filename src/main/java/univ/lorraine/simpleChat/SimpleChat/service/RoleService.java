package univ.lorraine.simpleChat.SimpleChat.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import univ.lorraine.simpleChat.SimpleChat.model.Role;
import univ.lorraine.simpleChat.SimpleChat.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository; 
	
	public void save(Role role) {
        roleRepository.save(role); 
    }
	
	public Optional<Role> findById(Long id){
		return roleRepository.findById(id); 
	}
	
	public Role create(String name)
	{
		Role role = new Role();
		role.setName(name);
		roleRepository.save(role); 
		return role;
	}
	
}
