package univ.lorraine.simpleChat.SimpleChat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.service.SecurityService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JWTTests {


    @Test
    public void TestGeneratingToken()
    {
        User user = new User();
        user.setUsername("alex");
        String currentKey = SecurityService.getJWT(user);
        //String expectedKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhbGV4In0.C5dclFAMRkFipw80OqYwP2PzR9VDVj3f8cPVEIdpxto";
        String expectedKey = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGV4In0.acdhk2lYyeEeNcJY4aPYlrE4hb1hiJATdMpiuk3gMNc";
        assertThat(currentKey).isEqualTo(expectedKey);

    }

}
