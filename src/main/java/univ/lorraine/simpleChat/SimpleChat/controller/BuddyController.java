package univ.lorraine.simpleChat.SimpleChat.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.service.UserService;

@RestController
@RequestMapping("/api/buddy")
@Api( value="Simple Chat")
public class BuddyController {

    private final UserService userService;

    @Autowired
    public BuddyController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Recupere les amis d'un utilisateur
     * @param userId id de l'utilisateur
     * @return liste des amis (entite user)
     */
    @GetMapping("/{userId}")
    public ResponseEntity findBuddies(@PathVariable Long userId) {
        User user = userService.findById(userId);
        if (user != null) {
            return ResponseEntity.ok(user.getBuddyList());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
    }

    /**
     * @param userId  id de l'utilisateur auquel on ajoute l'ami
     * @param buddyId id de l'ami
     * @return response avec le statut de la requete
     */
    @PostMapping("{userId}/add")
    public ResponseEntity addBuddy(@PathVariable String userId, @RequestBody String buddyId) {
        try {
            Long uId = Long.parseLong(userId);
            Long bId = Long.parseLong(buddyId);

            User user = userService.find(uId);
            User buddy = userService.find(bId);

            if(user != null && buddy != null){
                if(user.getBuddyList().contains(buddy)){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is already your buddy !");
                }else {
                    userService.addBuddy(user,buddy);
                    return ResponseEntity.ok("Buddy added !");
                }
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User or Buddy not found");
            }
        } catch (NumberFormatException  e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id should be sent in JSON. Just send the buddy's id.");
        }
    }

    /**
     * @param userId  id de l'utilisateur auquel on enleve l'ami
     * @param buddyId id de l'ami
     * @return response avec le statut de la requete
     */
    @PostMapping("{userId}/remove")
    public ResponseEntity removeBuddy(@PathVariable String userId, @RequestBody String buddyId) {
        try {
            Long uId = Long.parseLong(userId);
            Long bId = Long.parseLong(buddyId);

            User user = userService.find(uId);
            User buddy = userService.find(bId);

            if(user != null && buddy != null){
                if (user.getBuddyList().contains(buddy)) {
                    userService.removeBuddy(user,buddy);
                    return ResponseEntity.ok("Buddy removed !");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User or Buddy not found");
                }
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User or Buddy not found");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id should be sent as String or error in JSON. Just send the buddy's id.");
        }
    }
}
