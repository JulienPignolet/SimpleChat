package univ.lorraine.simpleChat.SimpleChat.jwtManagement;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import univ.lorraine.simpleChat.SimpleChat.model.Groupe;
import univ.lorraine.simpleChat.SimpleChat.model.GroupeUser;
import univ.lorraine.simpleChat.SimpleChat.model.Role;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.service.GroupeService;
import univ.lorraine.simpleChat.SimpleChat.service.UserDetailsServiceImpl;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);

    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsServiceImpl userDetailsService;

    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final GroupeService groupeService;

    @Autowired
    public JwtAuthenticationController(JwtTokenUtil jwtTokenUtil, UserDetailsServiceImpl userDetailsService, UserService userService, BCryptPasswordEncoder passwordEncoder, GroupeService groupeService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.groupeService = groupeService;
    }

    //@RequestMapping(value = "/login", method = RequestMethod.POST)
    @PostMapping("/authentication")
    public ResponseEntity<String> createAutentificationToken(@RequestBody User user) {
        try {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            boolean rep = authenticate(user.getPassword(),userDetails);
            final String token = jwtTokenUtil.generateToken(userDetails);
            user = userService.findByUsername(user.getUsername());

            JSONObject json = new JSONObject();
            json.put("user_key", token);
            json.put("user_id", user.getId());

            JSONArray jArray = new JSONArray();
            for (Role r : user.getRoles()) {
                jArray.add(r);
            }
            // role
            //json.put("user_role", jArray);

            if(user != null && user.getId() != null){
                List<GroupeUser> groupeUser = this.groupeService.findAllByGroupeUserAdmin(user.getId());
                List<Groupe> listeGroupe = new ArrayList<>();
                for (GroupeUser gu: groupeUser) {
                    listeGroupe.add(gu.getGroupe());
                }
                // liste des groupes
                //json.put("liste_groupe",  listeGroupe);
            }

            System.out.println(json.toString());
            if(rep){
                return new ResponseEntity<>(json.toString(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>("BAD CREDENTIALS", HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.info("Invalid credentials");
            return new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean authenticate(String password, UserDetails userDetails) throws Exception {
        try {
            return passwordEncoder.matches(password, userDetails.getPassword());

        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
