package services;

import enums.Role;
import exceptions.ValidationException;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.UserRepository;
import services.impl.UserServiceImpl;
import validators.Validator;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Validator<User> userValidator;

    @InjectMocks
    private UserServiceImpl userService;

    private final User testUser = new User("testUser", Role.USER);

    @Test
    @DisplayName("Успешное обновление пароля")
    void updatePasswordSuccess() {
        when(userRepository.getPasswordByLogin(testUser.login())).thenReturn("oldPassword");
        doNothing().when(userRepository).updatePassword(testUser, "newPassword");

        userService.updatePassword(testUser, "oldPassword", "newPassword");

        verify(userRepository, times(1)).updatePassword(testUser, "newPassword");
    }

    @Test
    @DisplayName("Обновление пароля с неверным старым паролем")
    void updatePasswordWrongOldPassword() {
        when(userRepository.getPasswordByLogin(testUser.login())).thenReturn("wrongPassword");

        assertThrows(ValidationException.class, () -> userService.updatePassword(testUser, "oldPassword", "newPassword"));
    }

    @Test
    @DisplayName("Успешная регистрация нового пользователя")
    void registerUserSuccess() {
        when(userRepository.getUser("newUser")).thenReturn(Optional.empty());
        doNothing().when(userValidator).validate(any(User.class));
        doNothing().when(userRepository).createUser(any(User.class), eq("password"));

        userService.registerUser("newUser", "password");

        verify(userRepository, times(1)).createUser(any(User.class), eq("password"));
    }

    @Test
    @DisplayName("Регистрация пользователя с существующим логином")
    void registerUserExistingLogin() {
        when(userRepository.getUser("existingUser")).thenReturn(Optional.of(testUser));

        userService.registerUser("existingUser", "password");

        verify(userRepository, never()).createUser(any(User.class), anyString());
    }

    @Test
    @DisplayName("Получение пользователя администратором")
    void getUserForAdminSuccess() {
        User adminUser = new User("adminUser", Role.ADMIN);
        when(userRepository.getUser("testUser")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.getUserForAdmin("testUser", adminUser);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
    }

    @Test
    @DisplayName("Попытка получения пользователя не администратором")
    void getUserForAdminNotAdmin() {
        User regularUser = new User("regularUser", Role.USER);

        Optional<User> result = userService.getUserForAdmin("testUser", regularUser);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Успешное получение пользователя по логину и паролю")
    void getUserSuccess() {
        when(userRepository.getUser("testUser")).thenReturn(Optional.of(testUser));
        when(userRepository.getPasswordByLogin("testUser")).thenReturn("password");

        Optional<User> result = userService.getUser("testUser", "password");

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
    }

    @Test
    @DisplayName("Получение пользователя с неверным паролем")
    void getUserWrongPassword() {
        when(userRepository.getUser("testUser")).thenReturn(Optional.of(testUser));
        when(userRepository.getPasswordByLogin("testUser")).thenReturn("wrongPassword");

        Optional<User> result = userService.getUser("testUser", "password");

        assertFalse(result.isPresent());
    }

}
