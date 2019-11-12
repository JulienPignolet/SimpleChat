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

    public static String getStringKey()
    {
        try {
            String fileName = System.getProperty("user.dir")+"/src/main/resources/static/key.txt";
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            return br.readLine();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    public static String getJWT(User user){
        byte[] bytekey = Base64.getDecoder().decode(getStringKey());
        Key key = Keys.hmacShaKeyFor(bytekey);
        return Jwts.builder().setSubject(user.getUsername()).signWith(key).compact();
    }
}
