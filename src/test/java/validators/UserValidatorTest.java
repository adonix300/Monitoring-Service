package validators;

import enums.Role;
import exceptions.ValidationException;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import validators.impl.UserValidator;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTest {
    private UserValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new UserValidator();
    }

    @Test
    @DisplayName("Проверка валидации пользователя с корректными данными")
    public void testValidateWithValidUser() {
        User user = new User("testUser", Role.USER);

        assertDoesNotThrow(() -> validator.validate(user));
    }

    @Test
    @DisplayName("Проверка валидации пользователя с пустым логином")
    public void testValidateWithEmptyLogin() {
        User user = new User("", Role.USER);

        assertThrows(ValidationException.class, () -> validator.validate(user));
    }

}