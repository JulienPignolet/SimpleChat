package univ.lorraine.simpleChat.SimpleChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import univ.lorraine.simpleChat.SimpleChat.adapter.UserAdapter;
import univ.lorraine.simpleChat.SimpleChat.form.UserForm;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.service.SecurityService;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

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
