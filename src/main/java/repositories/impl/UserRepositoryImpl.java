package repositories.impl;

import enums.Role;
import exceptions.ValidationException;
import logger.Logger;
import logger.impl.LoggerImpl;
import models.User;
import repositories.UserRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Реализация интерфейса UserRepository.
 * Хранит пользователей и их данные.
 */
public class UserRepositoryImpl implements UserRepository {
    private final Logger logger = LoggerImpl.getInstance();
    private final DataSource dataSource;

    /**
     * SQL-запрос для вставки нового пользователя в базу данных.
     * Запрос добавляет запись пользователя с указанными логином, паролем и ролью.
     */
    private static final String INSERT_USER_SQL = "INSERT INTO app_schema.users(login, password, role) VALUES (?, ?, ?)";

    /**
     * SQL-запрос для выбора пользователя из базы данных по логину.
     * Запрос извлекает данные о пользователе, у которого совпадает логин.
     */
    private static final String SELECT_USER_BY_LOGIN_SQL = "SELECT * FROM app_schema.users WHERE login = ?";

    /**
     * SQL-запрос для выбора всех логинов пользователей из базы данных.
     * Запрос извлекает все логины пользователей из таблицы пользователей.
     */
    private static final String SELECT_ALL_LOGINS_SQL = "SELECT login FROM app_schema.users";

    /**
     * SQL-запрос для обновления пароля пользователя в базе данных.
     * Запрос обновляет пароль пользователя с указанным логином на новый пароль.
     */
    private static final String UPDATE_USERS_PASSWORD = "UPDATE app_schema.users SET password = ? WHERE login = ?";

    public UserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public int getUserIdByLogin(String login) {
        String SELECT_USER_ID_SQL = "SELECT id FROM app_schema.users WHERE login = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_ID_SQL)) {

            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
                return -1;
            }
        } catch (SQLException e) {
            throw new ValidationException("Ошибка при получении ID пользователя: " + login, e);
        }

    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getPasswordByLogin(String login) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT password FROM app_schema.users WHERE login = ?")) {

            preparedStatement.setString(1, login);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                }
                return null;
            }
        } catch (SQLException e) {
            throw new ValidationException("Ошибка при получении пароля пользователя: " + login, e);
        }
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void updatePassword(User user, String password) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(UPDATE_USERS_PASSWORD);

            pstmt.setString(1, password);
            pstmt.setString(2, user.login());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ValidationException("Ошибка при обновлении пароля пользователя: " + user.login(), e);
        }
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void createUser(User user, String password) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_USER_SQL)) {

            pstmt.setString(1, user.login());
            pstmt.setString(2, password);
            pstmt.setString(3, user.role().toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new ValidationException("Ошибка при добавлении пользователя");
        }
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public Optional<User> getUser(String login) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_BY_LOGIN_SQL)) {

            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(rs.getString("login"), Role.valueOf(rs.getString("role"))));
                }
            }
        } catch (SQLException e) {
            throw new ValidationException("Ошибка при получении пользователя");
        }
        return Optional.empty();
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public List<String> getAllLogins() {
        List<String> logins = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_LOGINS_SQL)) {

            while (rs.next()) {
                logins.add(rs.getString("login"));
            }
        } catch (SQLException e) {
            throw new ValidationException("Ошибка при получении всех логинов");
        }
        return logins;
    }
}