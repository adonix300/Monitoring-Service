package repositories;

import interfaces.ReadingsRepository;
import models.User;

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
    private final Map<User, Map<Month, Map<String, Double>>> usersReadings = new HashMap<>();

    /**
     * Добавляет показания пользователя за указанный месяц.
     * Если пользователь еще не подавал показания, создает новую карту LinkedHashMap показаний для него.
     *
     * @param user     Пользователь, который подает показания.
     * @param month    Месяц, за который подаются показания.
     * @param readings Показания, которые нужно добавить.
     */
    public void addReadings(User user, Month month, Map<String, Double> readings) {
        var readingsMap = usersReadings.computeIfAbsent(user, k -> new LinkedHashMap<>());
        readingsMap.put(month, readings);
    }

    /**
     * Возвращает все показания для указанного пользователя.
     *
     * @param user Пользователь, для которого необходимо получить все показания.
     * @return Optional, содержащий карту показаний пользователя, если они есть, или пустой Optional, если показаний нет.
     */
    public Optional<Map<Month, Map<String, Double>>> getAllReadings(User user) {
        return Optional.ofNullable(usersReadings.get(user));
    }

    /**
     * Возвращает показания пользователя за указанный месяц.
     * Если пользователь не подавал показания за этот месяц, возвращает пустой Optional.
     *
     * @param user  Пользователь, для которого нужно получить показания.
     * @param month Месяц, за который нужно получить показания.
     * @return Optional, содержащий показания пользователя за указанный месяц, если они есть, или пустой Optional, если показаний нет.
     */
    public Optional<Map<String, Double>> getReadingsByMonth(User user, Month month) {
        var readingsMap = usersReadings.get(user);
        if (readingsMap != null) {
            return Optional.ofNullable(readingsMap.get(month));
        } else {
            return Optional.empty();
        }
    }
}
