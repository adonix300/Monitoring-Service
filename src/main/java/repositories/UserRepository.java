package repositories;

import models.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для работы с базой пользователей
 */
public interface UserRepository {
    /**
     * Добавляет пользователя в репозиторий.
     *
     * @param user Пользователь, который должен быть добавлен.
     */
    void addUser(User user);

    /**
     * Получает пользователя из репозитория по логину.
     *
     * @param login Логин пользователя.
     * @return Optional, содержащий пользователя, если он есть, или пустой Optional, если пользователя нет.
     */
    Optional<User> getUser(String login);

    /**
     * Получает все логины пользователей из репозитория.
     *
     * @return Список всех логинов пользователей.
     */
    List<String> getAllLogins();
}

