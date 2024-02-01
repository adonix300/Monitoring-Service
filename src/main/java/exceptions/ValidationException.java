package exceptions;

/**
 * Класс ValidationException представляет исключение, которое выбрасывается при ошибке валидации.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
