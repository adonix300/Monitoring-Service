package services.impl;

import exceptions.ValidationException;
import logger.Logger;
import logger.impl.LoggerImpl;
import models.Readings;
import models.User;
import repositories.ReadingsRepository;
import services.ReadingsService;
import validators.Validator;

import java.time.Month;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация интерфейса ReadingsService.
 * Использует ReadingsRepository для хранения данных показаний и Validator для проверки данных показаний.
 */
public class ReadingsServiceImpl implements ReadingsService {
    private final ReadingsRepository repository;
    private final Validator<Readings> validator;
    private static final Logger logger = LoggerImpl.getInstance();

    public ReadingsServiceImpl(ReadingsRepository repository, Validator<Readings> validator) {
        this.repository = repository;
        this.validator = validator;
    }

    /**
     * {@inheritDoc}
     * Если за данный месяц уже были поданы показания, выводит сообщение об этом.
     *
     * @throws ValidationException Если показания не прошли валидацию.
     */
    public void addReadings(User user, Month month, Readings readings) throws ValidationException {
        validator.validate(readings);
        Optional<Map<Month, Readings>> allReadings = repository.getAllReadings(user);
        if (allReadings.isPresent() && allReadings.get().containsKey(month)) {
            System.out.println("За этот месяц уже были поданы показания");
        } else {
            repository.addReadings(user, month, readings);
            System.out.println("Данные успешно внесены");
            logger.info("Пользователь " + user.getLogin() + " подал показания за " + month);
        }
    }


    /**
     * {@inheritDoc}
     * Если показания отсутствуют, выводит сообщение об этом.
     */
    public String getAllReadings(User user) {
        StringBuilder sb = new StringBuilder();
        Optional<Map<Month, Readings>> readingsMap = repository.getAllReadings(user);
        if (readingsMap.isPresent()) {
            readingsMap.get().forEach(((month, readings) -> sb.append(month).append(":").append(readings).append("\n")));
            logger.info("Пользователь " + user.getLogin() + " получил историю подачи показаний.");
        } else {
            sb.append("Показаний не найдено.\n");
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     * Последние показания определяются, как последнее значение в LinkedHashMap показаний, полученных от репозитория.
     */
    public Optional<Readings> getLastReadings(User user) {

        Optional<Map<Month, Readings>> allReadingsMap = repository.getAllReadings(user);
        Optional<Readings> lastReadings = Optional.empty();

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
     * {@inheritDoc}
     * Если показания за данный месяц отсутствуют, выводит сообщение об этом.
     */
    public Optional<Readings> getReadingsByMonth(User user, Month month) {
        Optional<Readings> readings = repository.getReadingsByMonth(user, month);
        readings.ifPresentOrElse(
                r -> {
                    logger.info("Пользователь " + user.getLogin() + " получил показания за " + month);
                },
                () -> System.out.println("Вы не подавали показаний за этот месяц.")
        );
        return readings;
    }
}
