package services;

import models.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для работы с пользователями.
 * Предоставляет такие методы, как изменение пароля, регистрация нового пользователя
 * и получение информации о пользователе или пользователях.
 */
public interface UserService {

    /**
     * Изменяет пароль пользователя.
     *
     * @param user        Пользователь, который меняет пароль.
     * @param oldPassword Старый пароль пользователя.
     * @param newPassword Новый пароль пользователя.
     */
    void changePassword(User user, String oldPassword, String newPassword);

    /**
     * Регистрирует нового пользователя.
     *
     * @param login    Логин нового пользователя.
     * @param password Пароль нового пользователя.
     */
    void registerUser(String login, String password);

    /**
     * Предоставляет пользователя админу по логину.
     *
     * @param login Логин пользователя.
     * @param admin Учетная запись админа
     * @return Optional, содержащий пользователя, если он есть, или пустой Optional, если пользователя нет.
     */
    Optional<User> getUserForAdmin(String login, User admin);

    /**
     * Получает пользователя по логину и паролю.
     *
     * @param login    Логин пользователя.
     * @param password Пароль пользователя.
     * @return Optional, содержащий пользователя, если он есть, или пустой Optional, если пользователя нет.
     */
    Optional<User> getUser(String login, String password);

    /**
     * Получает все логины пользователей.
     *
     * @return Список всех логинов пользователей.
     */
    List<String> getAllLogins();
}
