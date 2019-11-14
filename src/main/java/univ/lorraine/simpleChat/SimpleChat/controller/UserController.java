package univ.lorraine.simpleChat.SimpleChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import univ.lorraine.simpleChat.SimpleChat.adapter.UserAdapter;
import univ.lorraine.simpleChat.SimpleChat.form.UserForm;
import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.ocsf.ClientRunnable;
import univ.lorraine.simpleChat.SimpleChat.ocsf.Message;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.MessageService;
import univ.lorraine.simpleChat.SimpleChat.service.SecurityService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

import javax.validation.Valid;
import java.util.HashMap;

@Controller
public class UserController {

    private final UserService userService;

    private final GroupeService groupeService;

    private final SecurityService securityService;

    private final MessageService messageService;

    private HashMap<Long, ClientRunnable> clientPool = new HashMap<>();

    @Autowired
    public UserController(UserService userService, GroupeService groupeService, SecurityService securityService, MessageService messageService) {
        this.userService = userService;
        this.groupeService = groupeService;
        this.securityService = securityService;
        this.messageService = messageService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new UserForm());

        return "registration";
    }


    @PostMapping("/registration")
    public String registration(@Valid UserForm userForm, BindingResult bindingResult, Model model) {
        //userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if(!userForm.getPasswordConfirm().equals(userForm.getPassword())){
            model.addAttribute("error","Password don't match");
            return "registration";
        }

        if(userService.findByUsername(userForm.getUsername()) != null){
            model.addAttribute("error","Username already exist");
            return "registration";
        }

        User user = UserAdapter.AdaptUserFormToUser(userForm);

        userService.save(user);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "welcome";
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

    @GetMapping({"/"})
    public String index(Model model) {
        return "index";
    }

    @GetMapping({"/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }
}
