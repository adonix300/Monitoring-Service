package models;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс Readings представляет собой контейнер для хранения показаний различных типов.
 * Каждое показание представлено в виде пары ключ-значение, где ключ - это тип показания, а значение - это числовое значение показания.
 */
public class Readings {
    private final Map<String, Double> readings = new HashMap<>();

    public void add(String type, double value) {
        readings.put(type, value);
    }

    public Map<String, Double> get() {
        return readings;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Показания: ");
        for (Map.Entry<String, Double> entry : readings.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(", ");
        }
        return sb.toString();
    }
}