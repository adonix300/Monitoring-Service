package validators.impl;

import exceptions.ValidationException;
import models.Readings;
import validators.Validator;

import java.util.Map;

/**
 * Реализация интерфейса Validator для объектов класса Readings.
 * Проверяет, что показания не отрицательны и не пусты.
 */
public class ReadingsValidator implements Validator<Readings> {
    /**
     * Проверяет, что все показания не отрицательны и не пусты.
     *
     * @param readings Показания, которые должны быть проверены.
     * @throws ValidationException Если показания пусты или любое из значений показаний отрицательно.
     */
    public void validate(Readings readings) {
        if (readings.get().isEmpty()) {
            throw new ValidationException("Показания не могут быть пустыми");
        }
        for (Map.Entry<String, Double> entry : readings.get().entrySet()) {
            if (entry.getValue() < 0) {
                throw new ValidationException("Значение не может быть отрицательным для " + entry.getKey());
            }
        }
    }
}
