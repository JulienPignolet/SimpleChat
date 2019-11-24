package univ.lorraine.simpleChat.SimpleChat.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import univ.lorraine.simpleChat.SimpleChat.model.*;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeUserService;
import univ.lorraine.simpleChat.SimpleChat.service.RoleService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

import java.util.Arrays;

@Component
public class DataInit implements ApplicationRunner {


    private final RoleService roleService;
    private final UserService userService;
    private final GroupeService groupService;
    private final GroupeUserService groupeUserService;

    @Autowired
    public DataInit(RoleService roleService , UserService userService,GroupeService groupService,GroupeUserService groupeUserService) {
        this.roleService = roleService;
        this.userService = userService;
        this.groupService = groupService;
        this.groupeUserService = groupeUserService;
    }

    @Override
    public void run(ApplicationArguments args) {
        String chanGeneral = "channel general";
        String defaultUser = "simplechat";
        Arrays.asList(EnumRole.values()).
                forEach(roleName -> addRoleIfNotExist(roleName.getRole()));
        addGroupIfNotExist(chanGeneral);

        addUserIfNotExist("test");
        addUserIfNotExist(defaultUser);

        assignUserToGroup(defaultUser,chanGeneral);

    }

    private void addRoleIfNotExist(String roleName){
        if(roleService.findByName(roleName) == null){
            Role nouveau = new Role(roleName);
            roleService.save(nouveau);
        }
    }

    private void addUserIfNotExist(String username){
        if(userService.findByUsername(username) == null){
            User nouveau = new User();
            nouveau.setUsername(username);
            nouveau.setPassword(username);
            for(Role role : roleService.findAll()){
                nouveau.addRole(role);
            }
            userService.save(nouveau);
        }
    }

    private void addGroupIfNotExist(String groupName){
        if(groupService.findByNameAndDeletedatIsNull(groupName) == null){
            Groupe nouveau = new Groupe();
            nouveau.setName(groupName);

            groupService.save(nouveau);
        }
    }

    private void assignUserToGroup(String username, String groupName){
        User user = userService.findByUsername(username);
        Groupe groupe = groupService.findByNameAndDeletedatIsNull(groupName);

        if(user != null && groupe != null && groupeUserService.findByGroupeUserActif(groupe.getId(), user.getId()) == null){
            Role adminGroup = roleService.findByName(EnumRole.ADMIN_GROUP.getRole());

            GroupeUser groupUser = new GroupeUser();

            groupUser.setUser(user);
            groupUser.setGroupe(groupe);
            groupUser.setRole(adminGroup);
            groupe.addGroupeUser(groupUser);

            groupeUserService.save(groupUser);
        }
    }
}
