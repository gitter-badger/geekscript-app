package service;

import model.User;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

/**
 * Created by sriram on 26/01/14.
 */
@Stateless(name = "UserRegistrationEJB")
public class UserRegistrationBean {


    @Inject
    private EntityManager entityManager;

    @Inject
    private Logger log;


    @Inject
    private Event<User> userEventSrc;

    public void register(User user) {
        log.info("Registering " + user.getName());
        entityManager.persist(user);
        userEventSrc.fire(user);
    }
}
