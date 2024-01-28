package repositories;

import enums.Role;
import interfaces.UserRepository;
import models.User;

import java.util.*;

/**
 * Реализация интерфейса UserRepository.
 * Хранит пользователей и их данные.
 */
public class UserRepositoryImpl implements UserRepository {
    private final Map<String, User> users = new HashMap<>();

    {
        users.put("login", new User("login", "pass", Role.USER));
        users.put("admin", new User("admin", "admin", Role.ADMIN));
    }
    /**
     * Добавляет пользователя в репозиторий.
     *
     * @param user Пользователь, который должен быть добавлен.
     */
    public void addUser(User user) {
        users.put(user.getLogin(), user);
    }

    /**
     * Возвращает пользователя с заданным логином.
     * Если пользователь с таким логином не найден, возвращает пустой Optional.
     *
     * @param login Логин пользователя.
     * @return Optional, содержащий пользователя, если он найден, или пустой Optional, если пользователь не найден.
     */
    public Optional<User> getUser(String login) {
        return Optional.ofNullable(users.get(login));
    }

    /**
     * Возвращает список всех логинов пользователей в репозитории.
     *
     * @return Список всех логинов пользователей.
     */
    public List<String> getAllLogins() {
        List<String> allLogins = new ArrayList<>();
        for (var users : users.entrySet()) {
            allLogins.add(users.getKey());
        }
        return allLogins;
    }
}
