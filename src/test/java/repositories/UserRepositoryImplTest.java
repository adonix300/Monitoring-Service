package repositories;

import enums.Role;
import models.User;
import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryImplTest {
    private UserRepositoryImpl userRepository;

    @BeforeEach
    public void init() {
        userRepository = new UserRepositoryImpl();
    }

    @Test
    public void testAddUser() {
        User user = new User("testLogin", "testPassword", Role.USER);
        userRepository.addUser(user);
        Optional<User> result = userRepository.getUser("testLogin");
        assertTrue(result.isPresent());
        assertEquals("testLogin", result.get().getLogin());
    }

    @Test
    public void testGetUser() {
        User user = new User("login", "pass", Role.USER);
        userRepository.addUser(user);
        Optional<User> result = userRepository.getUser("login");
        assertTrue(result.isPresent());
        assertEquals("login", result.get().getLogin());
    }

    @Test
    public void testGetAllLogins() {
        User user1 = new User("login1", "pass1", Role.USER);
        User user2 = new User("login2", "pass2", Role.USER);
        userRepository.addUser(user1);
        userRepository.addUser(user2);
        List<String> result = userRepository.getAllLogins();
        assertTrue(result.contains("login1"));
        assertTrue(result.contains("login2"));
    }
}