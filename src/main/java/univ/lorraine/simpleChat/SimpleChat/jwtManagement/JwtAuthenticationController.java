package univ.lorraine.simpleChat.SimpleChat.jwtManagement;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.service.UserDetailsServiceImpl;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserService userService;

    //@RequestMapping(value = "/login", method = RequestMethod.POST)
    @PostMapping("/authentication")
    public ResponseEntity<String> createAutentificationToken(@RequestBody User user) throws Exception {
        try {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            boolean rep = authenticate(user.getUsername(), user.getPassword(),userDetails);
            final String token = jwtTokenUtil.generateToken(userDetails);
            user = userService.findByUsername(user.getUsername());

            JSONObject json = new JSONObject();
            json.put("user_key", token);
            json.put("user_id", user.getId());

            if(rep){
                return new ResponseEntity<>(json.toString(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>("null", HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e)
        {
            logger.info("Invalid credentials");
            return new ResponseEntity<>("null", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean authenticate(String username, String password, UserDetails userDetails) throws Exception {
        try {
            //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                    username, password, userDetails.getAuthorities());
//
//            Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
////            usernamePasswordAuthenticationToken
////                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
//            SecurityContextHolder.getContext().setAuthentication(auth);

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            // Encode new password and store it
            // Report error
            return passwordEncoder.matches(password, userDetails.getPassword());

        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
