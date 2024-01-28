package repositories;

import enums.Role;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReadingsRepositoryImplTest {
    private ReadingsRepositoryImpl readingsRepository;

    @BeforeEach
    public void init() {
        readingsRepository = new ReadingsRepositoryImpl();
    }

    @Test
    public void testAddReadings() {
        User user = new User("testLogin", "testPassword", Role.USER);
        Map<String, Double> readings = Map.of("heating", 100.0, "hotWater", 200.0, "coldWater", 300.0);
        readingsRepository.addReadings(user, Month.JANUARY, readings);
        Optional<Map<Month, Map<String, Double>>> result = readingsRepository.getAllReadings(user);
        assertTrue(result.isPresent());
        assertEquals(readings, result.get().get(Month.JANUARY));
    }

    @Test
    public void testGetAllReadings() {
        User user = new User("testLogin", "testPassword", Role.USER);
        Map<String, Double> readings = Map.of("heating", 100.0, "hotWater", 200.0, "coldWater", 300.0);
        readingsRepository.addReadings(user, Month.JANUARY, readings);
        Optional<Map<Month, Map<String, Double>>> result = readingsRepository.getAllReadings(user);
        assertTrue(result.isPresent());
        assertEquals(readings, result.get().get(Month.JANUARY));
    }

    @Test
    public void testGetReadingsByMonth() {
        User user = new User("testLogin", "testPassword", Role.USER);
        Map<String, Double> readings = Map.of("heating", 100.0, "hotWater", 200.0, "coldWater", 300.0);
        readingsRepository.addReadings(user, Month.JANUARY, readings);
        Optional<Map<String, Double>> result = readingsRepository.getReadingsByMonth(user, Month.JANUARY);
        assertTrue(result.isPresent());
        assertEquals(readings, result.get());
    }
}

