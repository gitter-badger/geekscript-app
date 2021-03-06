import data.UserRepository;
import model.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import service.UserService;
import service.UserServiceImpl;
import util.Resources;

import javax.inject.Inject;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;

/**
 * Created by sriram on 27/01/14.
 */
@RunWith(Arquillian.class)
public class UserServiceTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        JavaArchive jarArchive = ShrinkWrap.create(JavaArchive.class, "test-ejb.jar")
                .addClasses(User.class, UserRepository.class, UserService.class, UserServiceImpl.class, Resources.class, UserServiceTest.class)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsManifestResource("test-ds.xml", "test-ds.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        // System.out.println(jarArchive.toString(true));

        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "test-web.war")
                .addClasses(User.class, UserService.class, Resources.class)
//                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
//                .addAsWebInfResource("test-ds.xml", "test-ds.xml");


        //webArchive.addAsLibrary(jar);

        return ShrinkWrap.create(EnterpriseArchive.class, "test.ear")
                .addAsModule(webArchive)
                .addAsModule(jarArchive);

    }

    @Inject
    UserService userService;

    @Inject
    private Logger log;

    @Test
    public void testRegister() throws Exception {
        User user = new User();
        user.setName("Sriram");
        user.setPassword("Password");
        user.setEmail("sriram@gmail.com");
        user.setPhoneNumber("2125551234");
        userService.createUser(user);
        assertNotNull(user.getId());
        log.info(user.getName() + " was persisted with id " + user.getId());
    }


}
