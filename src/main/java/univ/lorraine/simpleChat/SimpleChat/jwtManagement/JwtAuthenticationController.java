package univ.lorraine.simpleChat.SimpleChat.jwtManagement;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
            authenticate(user.getUsername(), user.getPassword(),userDetails);
            final String token = jwtTokenUtil.generateToken(userDetails);
            user = userService.findByUsername(user.getUsername());

            JSONObject json = new JSONObject();
            json.put("user_key", token);
            json.put("user_id", user.getId());

            return new ResponseEntity<>(json.toString(), HttpStatus.OK);
        }
        catch (Exception e)
        {
            System.out.println("Invalid credentials");
            return new ResponseEntity<>("null", HttpStatus.BAD_REQUEST);
        }
    }

    private void authenticate(String username, String password, UserDetails userDetails) throws Exception {
        try {
            //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    username, password, userDetails.getAuthorities());

            Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
//            usernamePasswordAuthenticationToken
//                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
