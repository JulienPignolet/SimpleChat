package univ.lorraine.simpleChat.SimpleChat.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import univ.lorraine.simpleChat.SimpleChat.model.User;

import java.security.Key;
import java.util.Base64;

@Service
public class SecurityService {

    private static final String jwtkey = "AesWOgsVInu5PQSqz53cqoZzOhcrZaIIYfUh/phYU4Y=";

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

    @Autowired
    public SecurityService(AuthenticationManager authenticationManager, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (userDetails instanceof UserDetails) {
            return ((UserDetails)userDetails).getUsername();
        }

        return null;
    }

    public void autoLogin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            logger.debug(String.format("Auto login %s successfully!", username));
        }
    }

    /**
     * Génère un JWT avec la clé du fichier key.txt
     * @param user utilise le username de l'utilisateur pour générer le token
     * @return
     */
    public static String getJWT(User user){
        byte[] bytekey = Base64.getDecoder().decode(jwtkey);
        Key key = Keys.hmacShaKeyFor(bytekey);
        return Jwts.builder().setSubject(user.getUsername()).signWith(key).compact();
    }
}
