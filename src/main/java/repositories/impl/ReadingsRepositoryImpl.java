package repositories.impl;

import logger.Logger;
import logger.impl.LoggerImpl;
import models.Readings;
import models.User;
import repositories.ReadingsRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация интерфейса ReadingsRepository.
 * Хранит показания всех пользователей.
 */
public class ReadingsRepositoryImpl implements ReadingsRepository {
    private final Logger logger = LoggerImpl.getInstance();
    private final DataSource dataSource;

    private static final String INSERT_READINGS_SQL = "INSERT INTO app_schema.readings(user_id, month, type, value) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_READINGS_BY_USER_SQL = "SELECT month, type, value FROM app_schema.readings WHERE user_id = ? ORDER BY month";
    private static final String SELECT_READINGS_BY_USER_AND_MONTH_SQL = "SELECT type, value FROM app_schema.readings WHERE user_id = ? AND month = ?";

    public ReadingsRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private int getUserIdByLogin(String login) {
        String SELECT_USER_ID_SQL = "SELECT id FROM app_schema.users WHERE login = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_ID_SQL)) {

            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            // Обработка ошибки
        }
        return -1; // Вернуть -1 в случае неудачи
    }

    @Override
    public void addReadings(User user, Month month, Readings readings) {
        int userId = getUserIdByLogin(user.getLogin()); // Получаем user.id по логину
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
                // Обработка ошибки
            }
        } else {
            // Обработка случая, когда пользователь с заданным логином не найден
        }
    }

    @Override
    public Optional<Map<Month, Readings>> getAllReadings(User user) {
        Map<Month, Readings> readingsMap = new HashMap<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT month, type, value FROM app_schema.readings WHERE user_id = ? ORDER BY month")) {

            pstmt.setInt(1, getUserIdByLogin(user.getLogin()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Month currentMonth = Month.valueOf(rs.getString("month").toUpperCase());
                    Readings readings = readingsMap.computeIfAbsent(currentMonth, k -> new Readings());
                    readings.add(rs.getString("type"), rs.getDouble("value"));
                }
            }
        } catch (SQLException e) {
            //logger.error("Ошибка при получении показаний", e);
            return Optional.empty();
        }
        return Optional.of(readingsMap);
    }

    @Override
    public Optional<Readings> getReadingsByMonth(User user, Month month) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_READINGS_BY_USER_AND_MONTH_SQL)) {

            pstmt.setInt(1, getUserIdByLogin(user.getLogin()));
            pstmt.setString(2, month.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                Readings readings = new Readings();
                while (rs.next()) {
                    readings.add(rs.getString("type"), rs.getDouble("value"));
                }
                return Optional.of(readings);
            }

        } catch (SQLException e) {
            //logger.error("Ошибка при получении показаний за месяц", e);
        }
        return Optional.empty();
    }
}

