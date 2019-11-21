package univ.lorraine.simpleChat.SimpleChat.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import univ.lorraine.simpleChat.SimpleChat.adapter.UserAdapter;
import univ.lorraine.simpleChat.SimpleChat.form.UserForm;
import univ.lorraine.simpleChat.SimpleChat.model.EnumRole;
import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.Role;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.ocsf.ClientRunnable;
import univ.lorraine.simpleChat.SimpleChat.ocsf.Message;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.MessageService;
import univ.lorraine.simpleChat.SimpleChat.service.RoleService;
import univ.lorraine.simpleChat.SimpleChat.service.SecurityService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

import javax.validation.Valid;
import java.util.HashMap;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
public class UserController {

    private final UserService userService;

    private final GroupeService groupeService;

    private final SecurityService securityService;

    private final MessageService messageService;
    private final RoleService roleService;

    private HashMap<Long, ClientRunnable> clientPool = new HashMap<>();

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
    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @PostMapping("/message")
    public ResponseEntity<Object> sendMessage(@RequestBody String msg)
    {
        try {
            Message JSON = new Message(msg);
            User user = userService.findById(JSON.getUser_id());
            if(!clientPool.containsKey(JSON.getUser_id()))
            {
                clientPool.put(JSON.getUser_id(), new ClientRunnable(JSON.getUser_id()));
                clientPool.get(JSON.getUser_id()).start();
            }
            clientPool.get(JSON.getUser_id()).sendMsg(msg);

            // sauvegarde
            Groupe groupe = groupeService.find(JSON.getGroup_id());
            messageService.save(
                    new univ.lorraine.simpleChat.SimpleChat.model.Message(
                            JSON.getMessage(),user,groupe));
//            user.sendMsg(msg);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<Object> byName(@PathVariable(value = "id") Long id)
    {
        User user = userService.findById(id);
        if(!clientPool.containsKey(id))
            return new ResponseEntity<Object>("{}", HttpStatus.NO_CONTENT);
        String messages = clientPool.get(id).getMessagesEnAttente();
        return new ResponseEntity<Object>(messages, HttpStatus.OK);
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
