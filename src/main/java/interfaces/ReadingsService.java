package interfaces;

import models.User;

import java.time.Month;
import java.util.Map;
import java.util.Optional;

public interface ReadingsService {
    void addReadings(User user, Month month, Map<String, Double> readings);

    void printAllReadings(User user);

    Optional<Map<String, Double>> getLastReadings(User user);

    Optional<Map<String, Double>> getReadingsByMonth(User user, Month month);
}
