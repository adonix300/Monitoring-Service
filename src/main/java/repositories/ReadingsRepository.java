package repositories;

import models.Readings;
import models.User;

import java.time.Month;
import java.util.Map;
import java.util.Optional;

/**
 * Интерфейс для работы с данными о показаниях.
 */
public interface ReadingsRepository {

    /**
     * Добавляет показания для указанного пользователя и месяца
     *
     * @param user     Пользователь, для которого добавляются показания
     * @param month    Месяц, для которого добавляются показания
     * @param readings Показания
     */
    void addReadings(User user, Month month, Readings readings);

    /**
     * Возвращает все показания для указанного пользователя.
     *
     * @param user Пользователь, для которого запрашиваются показания.
     * @return Карта показаний, где ключ - это тип показания, а значение - само показание.
     * Возвращает пустой Optional, если показания для пользователя отсутствуют.
     */
    Optional<Map<Month, Readings>> getAllReadings(User user);

    /**
     * Возвращает показания для указанного пользователя и месяца.
     *
     * @param user  Пользователь, для которого запрашиваются показания.
     * @param month Месяц, для которого запрашиваются показания.
     * @return Optional, содержащий Map показаний, где ключ - это тип показания, а значение - само показание
     * или пустой Optional, если показания для пользователя и месяца отсутствуют.
     */
    Optional<Readings> getReadingsByMonth(User user, Month month);
}
