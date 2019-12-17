package univ.lorraine.simpleChat.SimpleChat.adapter;

import univ.lorraine.simpleChat.SimpleChat.form.UserForm;
import univ.lorraine.simpleChat.SimpleChat.model.User;
import univ.lorraine.simpleChat.SimpleChat.service.SecurityService;

public class UserAdapter {

    public static User AdaptUserFormToUser(UserForm userForm){
        User user = new User();

        user.setPassword(userForm.getPassword());
        user.setUsername(userForm.getUsername());
        user.setPasswordConfirm(userForm.getPasswordConfirm());

        return user;
    }

    public static UserForm AdaptUserToUserForm(User user){
        UserForm userForm = new UserForm();

        userForm.setPassword(user.getPassword());
        userForm.setPasswordConfirm(user.getPasswordConfirm());
        userForm.setUsername(user.getUsername());

        return userForm;

    }
}
