package models;

import java.util.HashMap;
import java.util.Map;

public class Readings {
    private final Map<String, Double> readings = new HashMap<>();

    public void addReading(String type, double value) {
        readings.put(type, value);
    }

    public Double getReading(String type) {
        return readings.get(type);
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