package repositories;

import enums.Role;
import models.Readings;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repositories.impl.ReadingsRepositoryImpl;

import java.time.Month;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ReadingsRepositoryImplTest {
    private User user;
    private Readings readings;
    private ReadingsRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new ReadingsRepositoryImpl();
        user = new User("testLogin", "testPassword", Role.USER);
        readings = new Readings();
    }

    @Test
    @DisplayName("Проверка добавления показаний")
    public void testAddReadings() {
        repository.addReadings(user, Month.JANUARY, readings);

        Optional<Map<Month, Readings>> result = repository.getAllReadings(user);

        assertTrue(result.isPresent());
        assertTrue(result.get().containsKey(Month.JANUARY));
        assertEquals(readings, result.get().get(Month.JANUARY));
    }

    @Test
    @DisplayName("Проверка получения всех показаний, когда их нет")
    public void testGetAllReadingsWhenNonePresent() {
        Optional<Map<Month, Readings>> result = repository.getAllReadings(user);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Проверка получения показаний по месяцам")
    public void testGetReadingsByMonth() {
        repository.addReadings(user, Month.JANUARY, readings);

        Optional<Readings> result = repository.getReadingsByMonth(user, Month.JANUARY);

        assertTrue(result.isPresent());
        assertEquals(readings, result.get());
    }

    @Test
    @DisplayName("Проверка получения показаний по месяцам, когда их нет")
    public void testGetReadingsByMonthWhenNonePresent() {
        Optional<Readings> result = repository.getReadingsByMonth(user, Month.JANUARY);

        assertFalse(result.isPresent());
    }
}
