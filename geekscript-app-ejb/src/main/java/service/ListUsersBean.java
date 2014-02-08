package service;

import model.User;

import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by sriram on 26/01/14.
 */
@Stateless(name = "ListUsersEJB")
public class ListUsersBean {


    @Inject
    private EntityManager entityManager;

    private List<User> users;

    @Produces
    public List<User> getUsers() {
        return users;
    }


}
