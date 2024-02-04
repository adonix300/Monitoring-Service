package logger;

/**
 * Интерфейс логгера
 */
public interface Logger {
    /**
     * Сохраняет поступающую информацию.
     *
     * @param message Сообщение, которое необходимо сохранить.
     */
    void info(String message);

    /**
     * Сохраняет поступающие предупреждения.
     *
     * @param message Сообщение, которое необходимо сохранить.
     */
    void warn(String message);

    /**
     * Сохраняет поступающие ошибки.
     *
     * @param message Сообщение, которое необходимо сохранить.
     */
    void error(String message);
}
