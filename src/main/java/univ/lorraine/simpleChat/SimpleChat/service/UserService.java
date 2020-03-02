package univ.lorraine.simpleChat.SimpleChat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import univ.lorraine.simpleChat.SimpleChat.model.Role;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.repository.RoleRepository;
import univ.lorraine.simpleChat.SimpleChat.repository.UserRepository;

import java.util.Collection;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void save(User user) {
        userRepository.save(user);
    }
    public void saveAndEncryptPassword(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

	public User findById(Long user_id) {
		User user = userRepository.findById(user_id).isPresent() ? userRepository.findById(user_id).get() : null;
        return user;
	}

    public User find(Long user_id) {
        return userRepository.findById(user_id).orElse(null);
    }

    public void addBuddy(User user, User buddy){
        user.addBuddy(buddy);
        userRepository.save(user);
    }

    public void removeBuddy(User user, User buddy){
        user.removeBuddy(buddy);
        userRepository.save(user);
    }

    public boolean usernameAlreadyExist(User user) {
        return this.findByUsername(user.getUsername()) != null;
    }

    public Collection<User> findAll(){
        return userRepository.findAll();
    }
    
    public void addRole(User user, Role role)
    {
        user.addRole(role);
    }
    
    public Collection<User> findMembersGroupe(Long groupe_id)
    {
    	return userRepository.findMembersGroupe(groupe_id);
    }
    
    public String addBlockUser(User user, User bUser)
    {
    	String msg = user.addUserToMyBlocklist(bUser);
    	userRepository.save(user);
    	return msg; 
    }
    
    public String removeBlockUser(User user, User bUser)
    {
    	String msg = user.removeUserToMyBlocklist(bUser);
    	userRepository.save(user);
    	return msg;
    }

    public void manage(User user, boolean active) {
        user.setActive(active);
        userRepository.save(user);
    }
}