package rest;

import data.UserRepository;
import service.UserRegistrationBean;
import model.User;

import java.util.*;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by sriram on 26/01/14.
 */
// The Java class will be hosted at the URI path "/helloworld"
@Path("/users")
public class UserRESTService {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Logger log;

    @Inject
    private Validator validator;

    @Inject
    private UserRegistrationBean registration;

    @Inject
    private UserRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listAllUsers() {
//        final List<User> users = entityManager.createQuery("select u from User u order by u.name").getResultList();
        return repository.findAllOrderedByName();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public User lookupMemberById(@PathParam("id") long id) {
        User user = repository.findById(id);
        if (user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return user;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMember(User user) {

        Response.ResponseBuilder builder = null;

        try {
            // Validates user using bean validation
            validateUser(user);

            registration.register(user);

            // Create an "ok" response
            builder = Response.ok();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("email", "Email taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    private void validateUser(User user) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(user.getEmail())) {
            throw new ValidationException("Unique Email Violation");
        }
    }

    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<String, String>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

    public boolean emailAlreadyExists(String email) {
        User user = null;
        try {
            user = repository.findByEmail(email);
        } catch (NoResultException e) {
            // ignore
        }
        return user != null;
    }


}
