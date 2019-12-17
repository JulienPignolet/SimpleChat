package univ.lorraine.simpleChat.SimpleChat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import univ.lorraine.simpleChat.SimpleChat.model.Role;
import univ.lorraine.simpleChat.SimpleChat.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

	private final RoleRepository roleRepository;

	@Autowired
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public void save(Role role) {
        roleRepository.save(role); 
    }
	
	public Optional<Role> findById(Long id){
		return roleRepository.findById(id); 
	}
	
	public Role findByName(String name) 
	{
		return this.roleRepository.findByName(name); 
	}

	public List<Role> findAll(){
		return roleRepository.findAll();
	}
	public Role create(String name)
	{
		Role role = new Role(name);
		roleRepository.save(role); 
		return role;
	}
	
}
