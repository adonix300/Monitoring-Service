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
    public void changePassword(User user, String oldPassword, String newPassword) {
        if (oldPassword.isEmpty() || !oldPassword.equals(user.getPassword())) {
            throw new ValidationException("Неверный старый пароль");
        }
        if (newPassword.isEmpty()) {
            throw new ValidationException("Неверное значение");
        }
        user.setPassword(newPassword);
        if (user.getPassword().equals(newPassword)) {
            System.out.println("Пароль успешно сменен.");
            logger.info("Пользователь " + user.getLogin() + " сменил пароль.");
        }
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
            user = Optional.of(new User(login, password, Role.USER));
            validator.validate(user.get());
            repository.addUser(user.get());
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
        if (admin.getRole() == Role.ADMIN) {
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
    public List<String> getLogins() {
        return repository.getAllLogins();
    }

    /**
     * {@inheritDoc}
     * Если пользователь с таким логином не найден или заданный пароль не совпадает, выводит сообщение об этом.
     */
    public Optional<User> getUser(String login, String password) {
        Optional<User> user = repository.getUser(login);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            logger.info("Пользователь " + login + " прошел аутентификацию. ");
            return user;
        } else {
            System.out.println("Неверный логин или пароль.\n");
            return Optional.empty();
        }
    }
}
