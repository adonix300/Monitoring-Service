package services;

import exceptions.ValidationException;
import interfaces.Logger;
import interfaces.ReadingsService;
import interfaces.Validator;
import logger.LoggerImpl;
import models.User;
import repositories.ReadingsRepositoryImpl;

import java.time.Month;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация интерфейса ReadingsService.
 * Предоставляет методы для работы с показаниями, такие как добавление показаний, вывод всех показаний, получение последних показаний
 * и получение показаний за указанный месяц.
 * Использует ReadingsRepository для хранения данных показаний и Validator для проверки данных показаний.
 */
public class ReadingsServiceImpl implements ReadingsService {
    private final ReadingsRepositoryImpl repository;
    private final Validator<Map<String, Double>> validator;
    private static final Logger logger = LoggerImpl.getInstance();

    public ReadingsServiceImpl(ReadingsRepositoryImpl repository, Validator<Map<String, Double>> validator) {
        this.repository = repository;
        this.validator = validator;
    }

    /**
     * Добавляет показания пользователя в репозиторий.
     * Если за данный месяц уже были поданы показания, выводит сообщение об этом.
     *
     * @param user     Пользователь, который подает показания.
     * @param month    Месяц, за который подаются показания.
     * @param readings Показания, которые нужно добавить.
     * @throws ValidationException Если показания не прошли валидацию.
     */
    public void addReadings(User user, Month month, Map<String, Double> readings) throws ValidationException {
        validator.validate(readings);
        Optional<Map<Month, Map<String, Double>>> allReadings = repository.getAllReadings(user);
        if (allReadings.isPresent() && allReadings.get().containsKey(month)) {
            System.out.println("За этот месяц уже были поданы показания");
        } else {
            repository.addReadings(user, month, readings);
            System.out.println("Данные успешно внесены");
            logger.info("Пользователь " + user.getLogin() + " подал показания за " + month);
        }
    }

    /**
     * Получает все показания пользователя из репозитория и выводит их.
     * Если показания отсутствуют, выводит сообщение об этом.
     *
     * @param user Пользователь, для которого нужно получить показания.
     */
    public void printAllReadings(User user) {
        Optional<Map<Month, Map<String, Double>>> readingsMap = repository.getAllReadings(user);
        if (readingsMap.isPresent()) {
            readingsMap.get().forEach(((month, readings) -> System.out.println(month + ":" + readings)));
            logger.info("Пользователь " + user.getLogin() + " получил историю подачи показаний.");
        } else {
            System.out.println("Показаний не найдено.");
        }
    }

    /**
     * Возвращает последние показания, представленные пользователем.
     * Последние показания определяются как последнее значение в LinkedHashMap показаний, полученных от репозитория.
     * Последним значением будет последнее добавленное чтение.
     *
     * @param user Пользователь, для которого необходимо получить последние показания.
     * @return Optional, содержащий последние показания пользователя, если они есть, или пустой Optional, если показаний нет.
     */
    public Optional<Map<String, Double>> getLastReadings(User user) {

        Optional<Map<Month, Map<String, Double>>> allReadingsMap = repository.getAllReadings(user);
        Optional<Map<String, Double>> lastReadings = Optional.empty();

        if (allReadingsMap.isPresent()) {
            for (var entry : allReadingsMap.get().entrySet()) {
                lastReadings = Optional.ofNullable(entry.getValue());
            }
            logger.info("Пользователь " + user.getLogin() + " получил актуальные показания.");
        } else {
            System.out.println("Вы не подавали никаких показаний");
        }

        return lastReadings;
    }

    /**
     * Возвращает показания пользователя за указанный месяц.
     * Если показания за данный месяц отсутствуют, выводит сообщение об этом.
     *
     * @param user  Пользователь, для которого нужно получить показания.
     * @param month Месяц, за который нужно получить показания.
     * @return Optional, содержащий показания пользователя за указанный месяц, если они есть, или пустой Optional, если показаний нет.
     */
    public Optional<Map<String, Double>> getReadingsByMonth(User user, Month month) {
        Optional<Map<String, Double>> readings = repository.getReadingsByMonth(user, month);
        readings.ifPresentOrElse(
                r -> {
                    logger.info("Пользователь " + user.getLogin() + " получил показания за " + month);
                },
                () -> System.out.println("Вы не подавали показаний за этот месяц.")
        );
        return readings;
    }
}
