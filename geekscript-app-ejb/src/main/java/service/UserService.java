package service;

import model.User;

import java.util.List;

/**
 * Created by sriram on 24/4/14.
 */
public interface UserService {

    List<User> listAllUsers();

    User getUser(long id);

    void createUser(User user);

    boolean isEmailAlreadyExists(String emailId);

}
