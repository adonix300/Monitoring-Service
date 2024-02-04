package repositories;

import config.DataSourceFactory;
import enums.Role;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repositories.impl.UserRepositoryImpl;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryImplTest {
    DataSource dataSource = DataSourceFactory.createDataSource();
    private UserRepositoryImpl repository;
    private User user;

    @BeforeEach
    public void setUp() {
        repository = new UserRepositoryImpl(dataSource);
        user = new User("testLogin", "testPassword", Role.USER);
    }

    @Test
    @DisplayName("Проверка добавления пользователя и получения пользователя")
    public void testAddUser() {
        repository.addUser(user);

        Optional<User> result = repository.getUser("testLogin");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    @DisplayName("Проверка получения пользователя, когда его нет")
    public void testGetUserWhenNonePresent() {
        Optional<User> result = repository.getUser("nonExistentUser");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Проверка получения всех логинов")
    public void testGetAllLogins() {
        repository.addUser(user);

        List<String> result = repository.getAllLogins();

        assertTrue(result.contains("testLogin"));
    }
}