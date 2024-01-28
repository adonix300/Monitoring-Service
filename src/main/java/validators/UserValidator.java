package validators;

import exceptions.ValidationException;
import interfaces.Validator;
import models.User;

/**
 * Реализация интерфейса Validator для объектов класса User.
 * Проверяет, что логин и пароль пользователя не пусты.
 */
public class UserValidator implements Validator<User> {
    /**
     * Проверяет, что логин и пароль пользователя не пусты.
     * Если логин или пароль пусты, выбрасывает исключение ValidationException.
     *
     * @param user Пользователь, который должен быть проверен.
     * @throws ValidationException Если логин или пароль пользователя пусты.
     */
    @Override
    public void validate(User user) {
        if (user.getLogin().isEmpty()) {
            throw new ValidationException("Логин не может быть пустым.\n");
        }
        if (user.getPassword().isEmpty()) {
            throw new ValidationException("Пароль не может быть пустым.\n");
        }
    }
}
