package interfaces;

import models.User;

import java.time.Month;
import java.util.Map;
import java.util.Optional;

public interface ReadingsRepository {
    void addReadings(User user, Month month, Map<String, Double> readings);

    Optional<Map<Month, Map<String, Double>>> getAllReadings(User user);

    Optional<Map<String, Double>> getReadingsByMonth(User user, Month month);
}
