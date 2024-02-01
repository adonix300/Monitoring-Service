package logger;

/**
 * Интерфейс логгера
 */
public interface Logger {
    /**
     * Логирует поступающую информацию.
     *
     * @param message Сообщение, которое необходимо залогировать.
     */
    void info(String message);
}
