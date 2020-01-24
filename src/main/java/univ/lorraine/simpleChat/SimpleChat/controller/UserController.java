
package univ.lorraine.simpleChat.SimpleChat.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import univ.lorraine.simpleChat.SimpleChat.form.UserForm;
import univ.lorraine.simpleChat.SimpleChat.model.EnumRole;
import univ.lorraine.simpleChat.SimpleChat.model.Role;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.ocsf.groupe.GroupeClientRunnable;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.MessageService;
import univ.lorraine.simpleChat.SimpleChat.service.RoleService;
import univ.lorraine.simpleChat.SimpleChat.service.SecurityService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

import java.util.HashMap;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
public class UserController {

    private final UserService userService;

    private final GroupeService groupeService;

    private final SecurityService securityService;

    private final MessageService messageService;
    private final RoleService roleService;

    private HashMap<Long, GroupeClientRunnable> clientPool = new HashMap<>();

    @Autowired
    public UserController(UserService userService, GroupeService groupeService, SecurityService securityService, MessageService messageService, RoleService roleService) {
        this.userService = userService;
        this.groupeService = groupeService;
        this.securityService = securityService;
        this.messageService = messageService;
        this.roleService = roleService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new UserForm());

        return "registration";
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody User user) {
        System.out.println(user.getUsername() + user.getPassword() + user.getPasswordConfirm());
        //TODO CHECK FORM ERROR
        Role role = roleService.findByName(EnumRole.SUPER_ADMIN.getRole());
        JSONObject json = new JSONObject();
        boolean success = (role != null);

        if(userService.usernameAlreadyExist(user)) {
            success = false;
            json.put("errorMessage", "Le pseudonyme est déjà utilisé");
        }
        json.put("success", success);

        if(success) {
            userService.addRole(user, role);
            userService.save(user);
            securityService.autoLogin(user.getUsername(), user.getPasswordConfirm());
        }

        return new ResponseEntity<>(json.toString(), (success ? HttpStatus.OK : HttpStatus.BAD_REQUEST));
    }

    //We don't define /login POST controller, it is provided by Spring Security
    @GetMapping("/authentication")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "authentication";
    }

    @GetMapping({"/"})
    public String index(Model model) {
        return "index";
    }

    @GetMapping({"/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }
}