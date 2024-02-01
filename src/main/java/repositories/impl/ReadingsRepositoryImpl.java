package repositories.impl;

import models.Readings;
import models.User;
import repositories.ReadingsRepository;

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
    private final Map<User, Map<Month, Readings>> usersReadings = new HashMap<>();

    /**
     * {@inheritDoc}
     * Если пользователь еще не подавал показания, создает новую карту LinkedHashMap показаний для него.
     */
    public void addReadings(User user, Month month, Readings readings) {
        var readingsMap = usersReadings.computeIfAbsent(user, k -> new LinkedHashMap<>());
        readingsMap.put(month, readings);
    }

    /**
     * {@inheritDoc}
     */
    public Optional<Map<Month, Readings>> getAllReadings(User user) {
        return Optional.ofNullable(usersReadings.get(user));
    }

    /**
     * {@inheritDoc}
     */
    public Optional<Readings> getReadingsByMonth(User user, Month month) {
        var readingsMap = usersReadings.get(user);
        if (readingsMap != null) {
            return Optional.ofNullable(readingsMap.get(month));
        } else {
            return Optional.empty();
        }
    }
}
