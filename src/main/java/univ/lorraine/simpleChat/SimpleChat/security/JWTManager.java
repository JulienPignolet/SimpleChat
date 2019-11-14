package univ.lorraine.simpleChat.SimpleChat.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Base64;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import univ.lorraine.simpleChat.SimpleChat.model.User;

import java.security.Key;


public class JWTManager {

    private static final String key = "AesWOgsVInu5PQSqz53cqoZzOhcrZaIIYfUh/phYU4Y=";

    /**
     * Génère un JWT avec la clé du fichier key.txt
     * @param user utilise le username de l'utilisateur pour générer le token
     * @return
     */
    public static String getJWT(User user){
        byte[] bytekey = Base64.getDecoder().decode(key);
        Key key = Keys.hmacShaKeyFor(bytekey);
        return Jwts.builder().setSubject(user.getUsername()).signWith(key).compact();
    }
}
