package repositories;

import exceptions.ValidationException;
import models.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для работы с базой пользователей.
 * Позволяет добавлять новых пользователей, получать пользователя, а также получать логины всех пользователей
 */
public interface UserRepository {
    /**
     * Создает нового пользователя в базе данных.
     *
     * @param user     новый пользователь, который будет добавлен в базу данных.
     * @param password пароль для нового пользователя.
     * @throws ValidationException если произошла ошибка при добавлении пользователя в базу данных.
     */
    void createUser(User user, String password) throws ValidationException;

    /**
     * Получает идентификатор пользователя по его логину.
     *
     * @param login Логин пользователя.
     * @return Идентификатор пользователя или -1, если пользователь не найден.
     * @throws ValidationException если произошла ошибка при доступе к базе данных.
     */
    int getUserIdByLogin(String login) throws ValidationException;


    /**
     * Получает пароль для указанного логина.
     *
     * @param login Логин пользователя, для которого нужно получить пароль.
     * @return Пароль пользователя или null, если пользователь не найден.
     * @throws ValidationException если произошла ошибка при доступе к базе данных.
     */
    String getPasswordByLogin(String login) throws ValidationException;

    /**
     * Обновляет пароль для указанного пользователя.
     *
     * @param user     пользователь, для которого нужно обновить пароль.
     * @param password новый пароль.
     * @throws ValidationException если произошла ошибка при обновлении пароля в базе данных.
     */
    void updatePassword(User user, String password) throws ValidationException;

    /**
     * Получает пользователя из репозитория по логину.
     *
     * @param login Логин пользователя.
     * @return Optional, содержащий пользователя, если он есть, или пустой Optional, если пользователя нет.
     * @throws ValidationException если произошла ошибка при получении пользователя
     */
    Optional<User> getUser(String login);

    /**
     * Получает все логины пользователей из репозитория.
     *
     * @return Список всех логинов пользователей.
     * @throws ValidationException если произошла ошибка при получении всех логинов
     */
    List<String> getAllLogins();
}

