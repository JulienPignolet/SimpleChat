
package univ.lorraine.simpleChat.SimpleChat.controller;

import io.swagger.annotations.Api;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import univ.lorraine.simpleChat.SimpleChat.form.UserForm;
import univ.lorraine.simpleChat.SimpleChat.model.*;
import univ.lorraine.simpleChat.SimpleChat.ocsf.groupe.GroupeClientRunnable;
import univ.lorraine.simpleChat.SimpleChat.service.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@Api( value="Simple Chat")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final GroupeUserService groupeUserService;

    private final GroupeService groupeService;

    private final SecurityService securityService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MessageService messageService;
    private final RoleService roleService;

    private HashMap<Long, GroupeClientRunnable> clientPool = new HashMap<>();

    @Autowired
    public UserController(UserService userService, GroupeUserService groupeUserService, GroupeService groupeService, SecurityService securityService, BCryptPasswordEncoder bCryptPasswordEncoder, MessageService messageService, RoleService roleService) {
        this.userService = userService;
        this.groupeUserService = groupeUserService;
        this.groupeService = groupeService;
        this.securityService = securityService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.messageService = messageService;
        this.roleService = roleService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new UserForm());

        return "registration";
    }

    /**
     * @param userId  id de l'utilisateur auquel on ajoute l'ami
     * @return response avec le statut de la requete
     */
    @PostMapping("{userId}/addPassword")
    public ResponseEntity addBuddy(@PathVariable String userId, @RequestBody String secondPassword) {
        try {
            Long uId = Long.parseLong(userId);

            User user = userService.find(uId);

            if (user != null) {
                user.setSecondPassword(bCryptPasswordEncoder.encode(secondPassword));
                userService.save(user);

                return ResponseEntity.status(HttpStatus.OK).body("Mot de passe secondaire changé");
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Envoyer le deuxieme mot de passe uniquement en string.");
        }

    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody User user) {
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
            userService.saveAndEncryptPassword(user);
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

    /**
     * Activer ou desactiver un user
     * @param userId
     * @param active
     * @return
     */
    @PostMapping("/user/manage/{userId}")
    public ResponseEntity removeUser(@RequestBody String active,@PathVariable String userId) {
        try {
            Long uId = Long.parseLong(userId);
            boolean act = Boolean.parseBoolean(active);

            User user = userService.find(uId);

            if (user != null) {
                userService.manage(user,act);
                return ResponseEntity.ok("User managed !");

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Just send the user's id." + e.getMessage());
        }
    }

    /**
     * @param userId  id de l'utilisateur auquel on ajoute l'ami
     * @param buddyId id de l'ami
     * @return response avec le statut de la requete
     */
    @PostMapping("{userId}/findCommun")
    public ResponseEntity findGroupCommun(@PathVariable String userId, @RequestBody String buddyId) {
        try {
            Long uId = Long.parseLong(userId);
            Long bId = Long.parseLong(buddyId);

            List<GroupeUser> myGroups = groupeUserService.findByUser(uId);
            List<GroupeUser> friendGroups = groupeUserService.findByUser(bId);
            List<Groupe> res = new ArrayList<>();

            for (GroupeUser gu: myGroups) {
                if(friendGroups.stream().filter(groupUser -> gu.getGroupe().getId().equals(groupUser.getGroupe().getId())).findFirst().orElse(null) != null){
                    res.add(gu.getGroupe());
                }
            }
            return ResponseEntity.ok(res);
        } catch (NumberFormatException  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id should be sent in JSON. Just send the buddy's id.");
        }
    }

    /**
     * Recupere les roles d'un utilisateur
     * @param userId id de l'utilisateur
     * @return l'utilisateur
     */
    @GetMapping("/{userId}")
    public ResponseEntity findUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        if (user != null) {
            return ResponseEntity.ok(user.getRoles());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
    }
}