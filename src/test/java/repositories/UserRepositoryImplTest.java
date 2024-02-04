package repositories;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import enums.Role;
import exceptions.ValidationException;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtendWith;
import repositories.impl.UserRepositoryImpl;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    private static final String INSERT_USER_SQL = "INSERT INTO app_schema.users(login, password, role) VALUES (?, ?, ?)";

    private static final String SELECT_USER_BY_LOGIN_SQL = "SELECT * FROM app_schema.users WHERE login = ?";

    private static final String SELECT_ALL_LOGINS_SQL = "SELECT login FROM app_schema.users";

    private static final String UPDATE_USERS_PASSWORD = "UPDATE app_schema.users SET password = ? WHERE login = ?";

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    @DisplayName("Тест успешного создания пользователя")
    void createUserSuccess() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        userRepository.createUser(new User("testLogin", Role.USER), "password");

        verify(preparedStatement, times(1)).executeUpdate();
        verify(preparedStatement).setString(1, "testLogin");
        verify(preparedStatement).setString(2, "password");
        verify(preparedStatement).setString(3, Role.USER.toString());
    }


    @Test
    @DisplayName("Тест исключения SQLException при создании пользователя")
    void createUserSQLExceptionThrown() throws Exception {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException());

        assertThrows(ValidationException.class, () -> userRepository.createUser(new User("testLogin", Role.USER), "password"));
    }

    @Test
    @DisplayName("Тест успешного получения пользователя")
    void getUserSuccess() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("login")).thenReturn("testLogin");
        when(resultSet.getString("role")).thenReturn(Role.USER.toString());

        Optional<User> user = userRepository.getUser("testLogin");

        assertTrue(user.isPresent());
        assertEquals("testLogin", user.get().login());
        assertEquals(Role.USER, user.get().role());
    }

    @Test
    @DisplayName("Тест получения пользователя, когда он не найден")
    void getUserNotFound() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<User> user = userRepository.getUser("unknown");

        assertFalse(user.isPresent());
    }

    @Test
    @DisplayName("Успешное обновление пароля")
    void updatePasswordSuccess() throws Exception {
        when(connection.prepareStatement(UPDATE_USERS_PASSWORD)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setString(anyInt(), anyString());
        when(preparedStatement.executeUpdate()).thenReturn(1);

        userRepository.updatePassword(new User("testLogin", Role.USER), "newPassword");

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    @DisplayName("Тест исключения SQLException при обновлении пароля")
    void updatePasswordSQLExceptionThrown() throws Exception {
        when(connection.prepareStatement(UPDATE_USERS_PASSWORD)).thenThrow(new SQLException());

        assertThrows(ValidationException.class, () -> userRepository.updatePassword(new User("testLogin", Role.USER), "newPassword"));
    }

    @Test
    @DisplayName("Успешное получение идентификатора по логину")
    void getUserIdByLoginSuccess() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);

        int userId = userRepository.getUserIdByLogin("testLogin");

        assertEquals(1, userId);
    }

    @Test
    @DisplayName("Тест получения идентификатора по логину, когда пользователь не найден")
    void getUserIdByLoginNotFound() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        int userId = userRepository.getUserIdByLogin("unknown");

        assertEquals(-1, userId);
    }

    @Test
    @DisplayName("Успешное получение пароля по логину")
    void getPasswordByLoginSuccess() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("password")).thenReturn("password");

        String password = userRepository.getPasswordByLogin("testLogin");

        assertEquals("password", password);
    }

    @Test
    @DisplayName("Тест получения пароля по логину, когда пользователь не найден")
    void getPasswordByLoginNotFound() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        String password = userRepository.getPasswordByLogin("unknown");

        assertNull(password);
    }
    @Test
    @DisplayName("Успешное получение всех логинов")
    void getAllLoginsSuccess() throws Exception {
        when(connection.createStatement()).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery(SELECT_ALL_LOGINS_SQL)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString("login")).thenReturn("login1").thenReturn("login2");

        List<String> logins = userRepository.getAllLogins();

        assertNotNull(logins);
        assertEquals(2, logins.size());
        assertTrue(logins.contains("login1"));
        assertTrue(logins.contains("login2"));
    }

    @Test
    @DisplayName("Тест исключения SQLException при получении всех логинов")
    void getAllLoginsSQLExceptionThrown() throws Exception {
        when(connection.createStatement()).thenThrow(new SQLException());

        assertThrows(ValidationException.class, userRepository::getAllLogins);
    }

}
