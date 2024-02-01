package repositories.impl;

import enums.Role;
import models.User;
import repositories.UserRepository;

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
     * {@inheritDoc}
     */
    public void addUser(User user) {
        users.put(user.getLogin(), user);
    }

    /**
     * {@inheritDoc}
     */
    public Optional<User> getUser(String login) {
        return Optional.ofNullable(users.get(login));
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getAllLogins() {
        List<String> allLogins = new ArrayList<>();
        for (var users : users.entrySet()) {
            allLogins.add(users.getKey());
        }
        return allLogins;
    }
}
