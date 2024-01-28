package services;

import enums.Role;
import exceptions.ValidationException;
import interfaces.Logger;
import interfaces.UserService;
import interfaces.Validator;
import logger.LoggerImpl;
import models.User;
import repositories.UserRepositoryImpl;

import java.util.List;
import java.util.Optional;

/**
 * Реализация интерфейса UserService.
 * Предоставляет методы для работы с пользователями, такие как изменение пароля, регистрация нового пользователя и получение информации о пользователе.
 * Использует UserRepository для хранения данных пользователей и Validator для проверки данных пользователей.
 */
public class UserServiceImpl implements UserService {
    private final UserRepositoryImpl repository;
    private final Validator<User> validator;
    private final static Logger logger = LoggerImpl.getInstance();

    public UserServiceImpl(UserRepositoryImpl repository, Validator<User> validator) {
        this.repository = repository;
        this.validator = validator;
    }

    /**
     * Изменяет пароль пользователя.
     * Если старый пароль неверен или новый пароль пуст, выбрасывает исключение ValidationException.
     *
     * @param user Пользователь, который хочет изменить пароль.
     * @param oldPassword Старый пароль пользователя.
     * @param newPassword Новый пароль пользователя.
     * @throws ValidationException Если старый пароль неверен или новый пароль пуст.
     */
    public void changePassword(User user, String oldPassword, String newPassword) {
        if (oldPassword.isEmpty() || !oldPassword.equals(user.getPassword())) {
            throw new ValidationException("Неверный старый пароль\n");
        }
        if (newPassword.isEmpty()) {
            throw new ValidationException("Неверное значение\n");
        }
        user.setPassword(newPassword);
        if (user.getPassword().equals(newPassword)) {
            System.out.println("Пароль успешно сменен.\n");
            logger.info("Пользователь " + user.getLogin() + " сменил пароль.");
        }
    }

    /**
     * Регистрирует нового пользователя с заданным логином и паролем.
     * Если пользователь с таким логином уже существует, выводит сообщение об этом.
     *
     * @param login Логин нового пользователя.
     * @param password Пароль нового пользователя.
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
     * Возвращает пользователя с заданным логином.
     * Если пользователь с таким логином не найден, выводит сообщение об этом.
     *
     * @param login Логин пользователя.
     * @return Optional, содержащий пользователя, если он найден, или пустой Optional, если пользователь не найден.
     */
    public Optional<User> getUser(String login) {
        Optional<User> user = repository.getUser(login);
        user.ifPresentOrElse(
                u -> {},
                () -> System.out.println("Неверный логин.\n")
        );
        return user;
    }

    /**
     * Метод возвращает список всех логинов пользователей.
     *
     * @return Список логинов всех пользователей.
     */
    public List<String> getLogins() {
        return repository.getAllLogins();
    }

    /**
     * Возвращает пользователя с заданным логином и паролем.
     * Если пользователь с таким логином не найден или заданный пароль не совпадает, выводит сообщение об этом.
     *
     * @param login Логин пользователя.
     * @param password Пароль пользователя.
     * @return Optional, содержащий пользователя, если он найден, или пустой Optional, если пользователь не найден.
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
