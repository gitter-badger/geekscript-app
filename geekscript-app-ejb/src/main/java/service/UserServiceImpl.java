package service;

import data.UserRepository;
import model.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sriram on 24/4/14.
 */
@Stateless
public class UserServiceImpl implements UserService {

    @Inject
    private UserRepository repository;

    @Inject
    private Validator validator;


    public List<User> listAllUsers() {
        return repository.findAllOrderedByName();
    }

    @Override
    public User getUser(long id) {
        return repository.findById(id);
    }

    @Override
    public void createUser(User user) {
        validateUser(user);
        repository.register(user);
    }

    @Override
    public boolean isEmailAlreadyExists(String emailId) {
        User user = null;
        try {
            user = repository.findByEmail(emailId);
        } catch (NoResultException e) {
            // ignore
        }
        return user != null;
    }

    private void validateUser(User user) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (isEmailAlreadyExists(user.getEmail())) {
            throw new ValidationException("Unique Email Violation");
        }
    }

}

