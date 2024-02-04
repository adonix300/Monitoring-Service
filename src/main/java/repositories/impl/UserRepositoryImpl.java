package repositories.impl;

import enums.Role;
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
    private static final String INSERT_USER_SQL = "INSERT INTO app_schema.users(login, password, role) VALUES (?, ?, ?)";
    private static final String SELECT_USER_BY_LOGIN_SQL = "SELECT * FROM app_schema.users WHERE login = ?";
    private static final String SELECT_ALL_LOGINS_SQL = "SELECT login FROM app_schema.users";
    private static final String UPDATE_USERS_PASSWORD = "UPDATE app_schema.users SET password = ? WHERE login = ?";

    public UserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }



    public void updatePassword(User user, String password) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(UPDATE_USERS_PASSWORD);

            pstmt.setString(1, password);
            pstmt.setString(2, user.getLogin());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addUser(User user) {
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(INSERT_USER_SQL)) {

            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole().toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении пользователя");
        }
    }

    @Override
    public Optional<User> getUser(String login) {
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_BY_LOGIN_SQL)) {

            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(rs.getString("login"), rs.getString("password"), Role.valueOf(rs.getString("role"))));
                }
            }
        } catch (SQLException e) {
            //todo обработка
        }
        return Optional.empty();
    }

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
            //todo обработка
        }
        return logins;
    }
}