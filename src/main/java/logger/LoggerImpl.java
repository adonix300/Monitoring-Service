package logger;

import interfaces.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Реализация интерфейса Logger.
 * Использует шаблон проектирования Singleton для обеспечения единственного экземпляра Logger в приложении.
 * Логирует сообщения в файл "logs.log".
 */
public class LoggerImpl implements Logger {

    private static Logger instance = null;
    private FileWriter writer;
    /**
     * Создает экземпляр LoggerImpl и открывает файл "logs.log" для записи.
     * Если файл не может быть открыт, выводит трассировку стека исключения.
     */
    private LoggerImpl(){
        try {
            writer = new FileWriter("logs.log", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Возвращает единственный экземпляр LoggerImpl.
     * Если экземпляр еще не был создан, создает его.
     *
     * @return Единственный экземпляр LoggerImpl.
     */
    public static Logger getInstance(){
        if (instance == null) {
            instance = new LoggerImpl();
        }
        return instance;
    }

    /**
     * Записывает сообщение в файл "logs.log" с текущей датой и временем.
     * Если сообщение не может быть записано, выводит сообщение об ошибке.
     *
     * @param msg Сообщение, которое нужно записать.
     */
    private void log(String msg){
        try {
            writer.append("[")
                    .append(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
                    .append(":")
                    .append(LocalTime.now().format(DateTimeFormatter.ofPattern("HH.mm.ss.nnn")))
                    .append("] ")
                    .append(msg)
                    .append("\n");
            writer.flush();
        } catch (IOException e) {
            System.err.println("Log Error " + msg);
        }
    }
    /**
     * Записывает информационное сообщение в файл "logs.log".
     *
     * @param message Информационное сообщение, которое нужно записать.
     */
    public void info(String message) {
        log("INFO : " + message);
    }
}
