package repositories;

import exceptions.ValidationException;
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
     * Добавляет показания для пользователя и месяца.
     *
     * @param user Пользователь, для которого добавляются показания.
     * @param month Месяц, для которого добавляются показания.
     * @param readings Объект Readings, содержащий показания.
     * @throws ValidationException если произошла ошибка при доступе к базе данных или пользователь не найден.
     */
    void addReadings(User user, Month month, Readings readings) throws ValidationException;

    /**
     * Получает все показания пользователя.
     *
     * @param user Пользователь, показания которого необходимо получить.
     * @return Optional, содержащий карту показаний по месяцам, или пустой Optional, если показания не найдены.
     * @throws ValidationException если произошла ошибка при доступе к базе данных.
     */
    Optional<Map<Month, Readings>> getAllReadings(User user) throws ValidationException;

    /**
     * Получает показания пользователя за указанный месяц.
     *
     * @param user Пользователь, показания которого необходимо получить.
     * @param month Месяц, для которого необходимо получить показания.
     * @return Optional, содержащий объект Readings с показаниями за месяц, или пустой Optional, если показания не найдены.
     * @throws ValidationException если произошла ошибка при доступе к базе данных.
     */
    Optional<Readings> getReadingsByMonth(User user, Month month) throws ValidationException;
}