package validators;

import exceptions.ValidationException;
import interfaces.Validator;

import java.util.Map;

/**
 * Реализация интерфейса Validator для объектов класса Readings.
 * Проверяет, что показания по холодной воде, горячей воде и отоплению не отрицательны.
 */
public class ReadingsValidator implements Validator<Map<String, Double>> {
    /**
     * Проверяет, что все показания не отрицательны.
     * Если любое из значений отрицательно, выбрасывает исключение ValidationException.
     *
     * @param readings Показания, которые должны быть проверены.
     * @throws ValidationException Если любое из значений показаний отрицательно.
     */
    public void validate(Map<String, Double> readings) {
        for (Map.Entry<String, Double> entry : readings.entrySet()) {
            if (entry.getValue() < 0) {
                throw new ValidationException("Значение не может быть отрицательным для " + entry.getKey());
            }
        }
    }
}
