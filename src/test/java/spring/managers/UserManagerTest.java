package spring.managers;

import aca.proto.BankMessage;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.BankingContext;
import spring.entities.User;
import spring.interfaces.IUserManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BankingContext.class)
class UserManagerTest {
    private static IUserManager userManager;

    @BeforeAll
    static void initialize() {
        ApplicationContext context = new AnnotationConfigApplicationContext(BankingContext.class);
        userManager = context.getBean(UserManager.class);
    }

    @Test
    void registerNewUser() {
        BankMessage msg = BankMessage.newBuilder()
                .setRegistration(BankMessage.Registration.newBuilder()
                        .setFirstName("Venera")
                        .setLastName("Melkonyan")
                        .setUsername("Ven")
                        .setEmail("veneramelkonyan@gmail.com")
                        .setPassword("111")
                        .build())
                .build();

        Pair<User, BankMessage> pair = userManager.verifyRegistration(msg);
        assertNotNull(pair.getKey());
        assertTrue(pair.getKey().getFirstName().equals("Venera"));
    }

    @Test
    void registerWithExistingUsername() {
        BankMessage msg = BankMessage.newBuilder()
                .setRegistration(BankMessage.Registration.newBuilder()
                        .setFirstName("Venera")
                        .setLastName("Melkonyan")
                        .setUsername("Ven")
                        .setEmail("venera@gmail.com")
                        .setPassword("111")
                        .build())
                .build();

        Pair<User, BankMessage> pair = userManager.verifyRegistration(msg);
        assertNull(pair.getKey());
    }
}