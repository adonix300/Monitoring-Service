package services;

import enums.Role;
import interfaces.Validator;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repositories.UserRepositoryImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepositoryImpl repository;
    @Mock
    private Validator<User> validator;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testChangePassword() {
        User user = new User("testLogin", "oldPassword", Role.USER);
        when(repository.getUser("testLogin")).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.changePassword(user, "oldPassword", "newPassword"));
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    public void testRegisterUser() {
        User user = new User("testLogin", "testPassword", Role.USER);
        when(repository.getUser("testLogin")).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> userService.registerUser("testLogin", "testPassword"));
        verify(repository, times(1)).addUser(user);
    }

    @Test
    public void testGetUser() {
        User user = new User("testLogin", "testPassword", Role.USER);
        when(repository.getUser("testLogin")).thenReturn(Optional.of(user));
        Optional<User> result = userService.getUser("testLogin");
        assertTrue(result.isPresent());
        assertEquals("testLogin", result.get().getLogin());
    }

    @Test
    public void testGetUserWithPassword() {
        User user = new User("testLogin", "testPassword", Role.USER);
        when(repository.getUser("testLogin")).thenReturn(Optional.of(user));
        Optional<User> result = userService.getUser("testLogin", "testPassword");
        assertTrue(result.isPresent());
        assertEquals("testLogin", result.get().getLogin());
    }
}