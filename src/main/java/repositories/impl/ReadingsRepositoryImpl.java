package repositories.impl;

import exceptions.ValidationException;
import logger.Logger;
import logger.impl.LoggerImpl;
import models.Readings;
import models.User;
import repositories.ReadingsRepository;
import repositories.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация интерфейса ReadingsRepository.
 * Хранит показания всех пользователей.
 */
public class ReadingsRepositoryImpl implements ReadingsRepository {
    private final UserRepository userRepository;
    private final Logger logger = LoggerImpl.getInstance();
    private final DataSource dataSource;

    /**
     * SQL-запрос для вставки новых показаний в базу данных.
     * Запрос добавляет записи показаний для пользователя с указанным идентификатором, месяцем, типом и значением.
     */
    private static final String INSERT_READINGS_SQL = "INSERT INTO app_schema.readings(user_id, month, type, value) VALUES (?, ?, ?, ?)";

    /**
     * SQL-запрос для выбора всех показаний по месяцам для определенного пользователя.
     * Запрос извлекает месяц, тип и значение показаний для пользователя с указанным идентификатором, упорядоченные по месяцу.
     */
    private static final String SELECT_ALL_READINGS_BY_USER_SQL = "SELECT month, type, value FROM app_schema.readings WHERE user_id = ? ORDER BY month";

    /**
     * SQL-запрос для выбора показаний по месяцу и пользователю.
     * Запрос извлекает тип и значение показаний для пользователя с указанным идентификатором и месяцем.
     */
    private static final String SELECT_READINGS_BY_USER_AND_MONTH_SQL = "SELECT type, value FROM app_schema.readings WHERE user_id = ? AND month = ?";


    public ReadingsRepositoryImpl(DataSource dataSource, UserRepository repository) {
        this.dataSource = dataSource;
        this.userRepository = repository;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void addReadings(User user, Month month, Readings readings) {
        int userId = userRepository.getUserIdByLogin(user.login()); // Получаем user.id по логину
        Map<String, Double> readingsMap = readings.get();

        if (userId != -1) {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(INSERT_READINGS_SQL)) {

                for (var type : readingsMap.entrySet()) {
                    pstmt.setInt(1, userId);
                    pstmt.setString(2, month.toString());
                    pstmt.setString(3, type.getKey());
                    pstmt.setDouble(4, type.getValue());
                    pstmt.executeUpdate();
                }

            } catch (SQLException e) {
                throw new ValidationException("Ошибка при добавлении показаний для пользователя: " + user.login(), e);
            }
        } else {
            throw new ValidationException("Пользователь с логином " + user.login() + " не найден.");
        }
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public Optional<Map<Month, Readings>> getAllReadings(User user) {
        Map<Month, Readings> readingsMap = new HashMap<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_READINGS_BY_USER_SQL)) {

            pstmt.setInt(1, userRepository.getUserIdByLogin(user.login()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Month currentMonth = Month.valueOf(rs.getString("month").toUpperCase());
                    Readings readings = readingsMap.computeIfAbsent(currentMonth, k -> new Readings());
                    readings.add(rs.getString("type"), rs.getDouble("value"));
                }
            }
            return Optional.of(readingsMap);
        } catch (SQLException e) {
            throw new ValidationException("Ошибка при получении всех показаний для пользователя: " + user.login(), e);
        }
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public Optional<Readings> getReadingsByMonth(User user, Month month) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_READINGS_BY_USER_AND_MONTH_SQL)) {

            pstmt.setInt(1, userRepository.getUserIdByLogin(user.login()));
            pstmt.setString(2, month.toString());
            Readings readings = new Readings();

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean hasData = false;
                while (rs.next()) {
                    readings.add(rs.getString("type"), rs.getDouble("value"));
                    hasData = true;
                }
                if (hasData) {
                    return Optional.of(readings);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new ValidationException("Ошибка при получении показаний за месяц для пользователя: " + user.login(), e);
        }
    }
}

