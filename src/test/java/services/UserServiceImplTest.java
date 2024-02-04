package services;

import enums.Role;
import exceptions.ValidationException;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repositories.UserRepository;
import services.impl.UserServiceImpl;
import validators.Validator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @Mock
    private UserRepository repository;
    @Mock
    private Validator<User> validator;
    @InjectMocks
    private UserServiceImpl service;
    private User user;
    private User admin;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        user = new User("testLogin", "testPassword", Role.USER);
        admin = new User("admin", "admin", Role.ADMIN);
    }

    @AfterEach
    public void closeResources() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("Проверка смены пароля")
    public void testChangePassword() {
        service.updatePassword(user, "testPassword", "newPassword");

        assertEquals("newPassword", user.getPassword());
    }

    @Test
    @DisplayName("Проверка смены пароля с неверным старым паролем")
    public void testChangePasswordWithWrongOldPassword() {
        assertThrows(ValidationException.class, () -> service.updatePassword(user, "wrongPassword", "newPassword"));
    }

    @Test
    @DisplayName("Проверка смены пароля с пустым новым паролем")
    public void testChangePasswordWithEmptyNewPassword() {
        assertThrows(ValidationException.class, () -> service.updatePassword(user, "testPassword", ""));
    }

    @Test
    @DisplayName("Проверка регистрации пользователя")
    public void testRegisterUser() throws ValidationException {
        when(repository.getUser("testLogin")).thenReturn(Optional.empty());

        service.registerUser("testLogin", "testPassword");

        verify(validator, times(1)).validate(user);
        verify(repository, times(1)).addUser(user);
    }

    @Test
    @DisplayName("Проверка регистрации пользователя с существующим логином")
    public void testRegisterUserWithExistingLogin() throws ValidationException {
        when(repository.getUser("testLogin")).thenReturn(Optional.of(user));

        service.registerUser("testLogin", "testPassword");

        verify(validator, never()).validate(user);
        verify(repository, never()).addUser(user);
    }

    @Test
    @DisplayName("Проверка получения пользователя для админа по логину")
    public void testGetUserForAdmin() {
        when(repository.getUser("testLogin")).thenReturn(Optional.of(user));

        Optional<User> result = service.getUserForAdmin("testLogin", admin);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    @DisplayName("Проверка получения пользователя для админа с неверным логином")
    public void testGetUserForAdminWithWrongLogin() {
        when(repository.getUser("wrongLogin")).thenReturn(Optional.empty());

        Optional<User> result = service.getUserForAdmin("wrongLogin", admin);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Проверка аутентификации пользователя")
    public void testGetUserWithPassword() {
        when(repository.getUser("testLogin")).thenReturn(Optional.of(user));

        Optional<User> result = service.getUser("testLogin", "testPassword");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    @DisplayName("Проверка аутентификации пользователя с неверным паролем")
    public void testGetUserWithWrongPassword() {
        when(repository.getUser("testLogin")).thenReturn(Optional.of(user));

        Optional<User> result = service.getUser("testLogin", "wrongPassword");

        assertFalse(result.isPresent());
    }
}