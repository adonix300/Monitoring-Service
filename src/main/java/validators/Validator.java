package validators;

import exceptions.ValidationException;

/**
 * Интерфейс для валидации данных.
 *
 * @param <T> Тип данных, которые нужно проверить.
 */
public interface Validator<T> {
    /**
     * Проверяет данные на соответствие определенным критериям.
     *
     * @param data Данные для проверки.
     * @throws ValidationException если данные не проходят валидацию.
     */
    void validate(T data) throws ValidationException;
}
