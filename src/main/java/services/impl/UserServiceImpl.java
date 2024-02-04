package services.impl;

import enums.Role;
import exceptions.ValidationException;
import logger.Logger;
import logger.impl.LoggerImpl;
import models.User;
import repositories.UserRepository;
import services.UserService;
import validators.Validator;

import java.util.List;
import java.util.Optional;

/**
 * Реализация интерфейса UserService.
 * Использует UserRepository для хранения данных пользователей и Validator для проверки данных пользователей.
 */
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final Validator<User> validator;
    private final static Logger logger = LoggerImpl.getInstance();

    public UserServiceImpl(UserRepository repository, Validator<User> validator) {
        this.repository = repository;
        this.validator = validator;
    }

    /**
     * {@inheritDoc}
     *
     * @throws ValidationException Если старый пароль или новый пароль не прошли валидацию.
     */
    public void updatePassword(User user, String oldPassword, String newPassword) {
        if (!oldPassword.equals(repository.getPasswordByLogin(user.login()))) {
            throw new ValidationException("Неверный старый пароль");
        }
        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            throw new ValidationException("Неверное значение");
        }
        repository.updatePassword(user, newPassword);
        System.out.println("Пароль успешно сменен");
    }

    /**
     * {@inheritDoc}
     * Если пользователь с таким логином уже существует, выводит сообщение об этом.
     *
     * @throws ValidationException Если данные нового пользователя не прошли валидацию.
     */
    public void registerUser(String login, String password) throws ValidationException {
        Optional<User> user = repository.getUser(login);
        if (user.isEmpty()) {
            user = Optional.of(new User(login, Role.USER));
            validator.validate(user.get());
            if (password.isEmpty()) {
                throw new ValidationException("Неверное значение пароля");
            }
            repository.createUser(user.get(), password);
            System.out.println("Вы успешно зарегистрировались.\n");
            logger.info("Пользователь " + login + " успешно зарегистрировался.");
        } else {
            System.out.println("Такой логин уже существует.\n");
        }
    }

    /**
     * {@inheritDoc}
     * Если пользователь с таким логином не найден, выводит сообщение об этом.
     */
    public Optional<User> getUserForAdmin(String login, User admin) {
        if (admin.role() == Role.ADMIN) {
            Optional<User> user = repository.getUser(login);
            if (user.isEmpty()) {
                System.out.println("Такого пользователя не существует");
            }
            return user;
        } else {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getAllLogins() {
        return repository.getAllLogins();
    }

    /**
     * {@inheritDoc}
     * Если пользователь с таким логином не найден или заданный пароль не совпадает, выводит сообщение об этом.
     */
    public Optional<User> getUser(String login, String password) {
        Optional<User> user = repository.getUser(login);
        if (user.isPresent() && password.equals(repository.getPasswordByLogin(login))) {
            return user;
        } else {
            System.out.println("Неверный логин или пароль.\n");
            return Optional.empty();
        }
    }
}
