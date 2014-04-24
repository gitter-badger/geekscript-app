package service;

import model.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Created by sriram on 16/02/14.
 */

@Stateless(name = "LoginService")
public class LoginServiceImpl {


    @Inject
    private Logger logger;

    public void login(User user) {
        logger.info("Logging in by " + user.getName());

    }
}
