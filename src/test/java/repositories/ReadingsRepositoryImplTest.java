package repositories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import enums.Role;
import exceptions.ValidationException;
import models.Readings;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.impl.ReadingsRepositoryImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Month;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReadingsRepositoryImplTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReadingsRepositoryImpl readingsRepository;

    @BeforeEach
    void setUp() throws Exception {
        lenient().when(dataSource.getConnection()).thenReturn(connection);
        lenient().when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        lenient().when(preparedStatement.executeQuery()).thenReturn(resultSet); // Применение lenient строгости
    }


    @Test
    @DisplayName("Успешное добавление показаний")
    void addReadingsSuccess() throws Exception {
        when(userRepository.getUserIdByLogin(anyString())).thenReturn(1);
        doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
        doNothing().when(preparedStatement).setString(anyInt(), anyString());
        doNothing().when(preparedStatement).setDouble(anyInt(), anyDouble());
        when(preparedStatement.executeUpdate()).thenReturn(1);

        User user = new User("testLogin", Role.USER);
        Readings readings = new Readings();
        readings.add("Горячая вода", 100);
        readings.add("Холодная вода", 200);

        readingsRepository.addReadings(user, Month.JANUARY, readings);

        verify(preparedStatement, times(readings.get().size())).executeUpdate();
    }

    @Test
    @DisplayName("Добавление показаний для несуществующего пользователя")
    void addReadingsUserNotFound() {
        when(userRepository.getUserIdByLogin(anyString())).thenReturn(-1);

        User user = new User("testLogin", Role.USER);
        Readings readings = new Readings();
        readings.add("Горячая вода", 100);
        readings.add("Холодная вода", 200);

        assertThrows(ValidationException.class, () -> readingsRepository.addReadings(user, Month.JANUARY, readings));
    }

    @Test
    @DisplayName("Получение всех показаний пользователя")
    void getAllReadingsSuccess() throws Exception {
        when(userRepository.getUserIdByLogin(anyString())).thenReturn(1);
        when(resultSet.next()).thenReturn(true, true, false); // Имитация двух записей, затем конец данных
        when(resultSet.getString("month")).thenReturn(Month.JANUARY.toString(), Month.FEBRUARY.toString());
        when(resultSet.getString("type")).thenReturn("Electricity");
        when(resultSet.getDouble("value")).thenReturn(100.0);

        User user = new User("testLogin", Role.USER);
        Optional<Map<Month, Readings>> readings = readingsRepository.getAllReadings(user);

        assertTrue(readings.isPresent());
        assertEquals(2, readings.get().size());
    }

    @Test
    @DisplayName("Получение показаний для пользователя без записей")
    void getAllReadingsEmpty() throws Exception {
        when(userRepository.getUserIdByLogin(anyString())).thenReturn(1);
        when(resultSet.next()).thenReturn(false); // Нет данных

        User user = new User("testLogin", Role.USER);
        Optional<Map<Month, Readings>> readings = readingsRepository.getAllReadings(user);

        assertTrue(readings.isPresent());
        assertTrue(readings.get().isEmpty());
    }

    @Test
    @DisplayName("Получение показаний по месяцу")
    void getReadingsByMonthSuccess() throws Exception {
        when(userRepository.getUserIdByLogin(anyString())).thenReturn(1);
        when(resultSet.next()).thenReturn(true, false); // Имитация одной записи, затем конец данных
        when(resultSet.getString("type")).thenReturn("Отопление");
        when(resultSet.getDouble("value")).thenReturn(150.0);

        User user = new User("testLogin", Role.USER);
        Optional<Readings> readings = readingsRepository.getReadingsByMonth(user, Month.JANUARY);

        assertTrue(readings.isPresent());
        assertEquals(1, readings.get().get().size());
    }

    @Test
    @DisplayName("Получение показаний за месяц, когда данных нет")
    void getReadingsByMonthEmpty() throws Exception {
        when(userRepository.getUserIdByLogin(anyString())).thenReturn(1);
        when(resultSet.next()).thenReturn(false); // Нет данных

        User user = new User("testLogin", Role.USER);
        Optional<Readings> readings = readingsRepository.getReadingsByMonth(user, Month.JANUARY);

        assertFalse(readings.isPresent());
    }

}
