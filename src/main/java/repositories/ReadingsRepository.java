package repositories;

import models.Readings;
import models.User;

import java.time.Month;
import java.util.Map;
import java.util.Optional;

/**
 * Интерфейс для работы с показаниями пользователей.
 * Позволяет добавлять показания для определенного пользователя и месяца, получать все показания для пользователя,
 * а также получать показания для пользователя за определенный месяц.
 */
public interface ReadingsRepository {

    /**
     * Добавляет показания для указанного пользователя и месяца.
     *
     * @param user     Пользователь, для которого добавляются показания.
     * @param month    Месяц, для которого добавляются показания.
     * @param readings Показания, которые нужно добавить.
     */
    void addReadings(User user, Month month, Readings readings);

    /**
     * Возвращает все показания для указанного пользователя.
     *
     * @param user Пользователь, для которого запрашиваются показания.
     * @return Карта показаний, где ключ - это месяц, а значение - показания за этот месяц.
     * Возвращает пустой Optional, если показания для пользователя отсутствуют.
     */
    Optional<Map<Month, Readings>> getAllReadings(User user);

    /**
     * Возвращает показания для указанного пользователя и месяца.
     *
     * @param user  Пользователь, для которого запрашиваются показания.
     * @param month Месяц, для которого запрашиваются показания.
     * @return Optional, содержащий показания за указанный месяц,
     * или пустой Optional, если показания для пользователя и месяца отсутствуют.
     */
    Optional<Readings> getReadingsByMonth(User user, Month month);
}