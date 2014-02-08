package data;

import model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by sriram on 28/01/14.
 */
@ApplicationScoped
public class UserRepository {

    @Inject
    private EntityManager em;


    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public User findByEmail(String email) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> userCriteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = userCriteriaQuery.from(User.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        userCriteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get("email"), email));
        return em.createQuery(userCriteriaQuery).getSingleResult();
    }

    public List<User> findAllOrderedByName() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> userCriteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = userCriteriaQuery.from(User.class);
        userCriteriaQuery.select(userRoot).orderBy(criteriaBuilder.asc(userRoot.get("name")));
        return em.createQuery(userCriteriaQuery).getResultList();
    }


}
