package services;

import enums.Role;
import exceptions.ValidationException;
import models.Readings;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repositories.ReadingsRepository;
import services.impl.ReadingsServiceImpl;
import validators.Validator;

import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReadingsServiceImplTest {
    @Mock
    private ReadingsRepository repository;
    @Mock
    private Validator<Readings> validator;
    @InjectMocks
    private ReadingsServiceImpl service;
    private User user;
    private Readings readings;
    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        user = new User("testLogin", "testPassword", Role.USER);
        readings = new Readings();
        readings.add("heating", 100.0);
        readings.add("hotWater", 100.0);
        readings.add("coldWater", 100.0);
    }

    @AfterEach
    public void closeResources() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("Проверка добавления показаний, когда их еще нет")
    public void testAddReadings() throws ValidationException {
        when(repository.getAllReadings(user)).thenReturn(Optional.empty());
        service.addReadings(user, Month.JANUARY, readings);

        verify(validator, times(1)).validate(readings);
        verify(repository, times(1)).addReadings(user, Month.JANUARY, readings);
    }

    @Test
    @DisplayName("Проверка добавления показаний, когда они уже есть")
    public void testAddReadingsWhenAlreadyPresent() throws ValidationException {
        Map<Month, Readings> readingsMap = new LinkedHashMap<>();
        readingsMap.put(Month.JANUARY, readings);
        when(repository.getAllReadings(user)).thenReturn(Optional.of(readingsMap));

        service.addReadings(user, Month.JANUARY, readings);

        verify(validator, times(1)).validate(readings);
        verify(repository, never()).addReadings(user, Month.JANUARY, readings);
    }

    @Test
    @DisplayName("Проверка получения всех показаний")
    public void testGetAllReadings() {
        Map<Month, Readings> readingsMap = new LinkedHashMap<>();
        readingsMap.put(Month.JANUARY, readings);
        readingsMap.put(Month.FEBRUARY, readings);
        when(repository.getAllReadings(user)).thenReturn(Optional.of(readingsMap));

        String result = service.getAllReadings(user);

        assertTrue(result.contains(Month.JANUARY.toString()));
        assertTrue(result.contains(Month.FEBRUARY.toString()));
        assertTrue(result.contains(readings.toString()));
    }

    @Test
    @DisplayName("Проверка получения всех показаний, когда их нет")
    public void testGetAllReadingsWhenNonePresent() {
        when(repository.getAllReadings(user)).thenReturn(Optional.empty());

        String result = service.getAllReadings(user);

        assertEquals("Показаний не найдено.", result);
    }

    @Test
    @DisplayName("Проверка получения последних показаний")
    public void testGetLastReadings() {
        Map<Month, Readings> readingsMap = new LinkedHashMap<>();
        readingsMap.put(Month.JANUARY, readings);
        when(repository.getAllReadings(user)).thenReturn(Optional.of(readingsMap));

        Optional<Readings> result = service.getLastReadings(user);

        assertTrue(result.isPresent());
        assertEquals(readings, result.get());
    }

    @Test
    @DisplayName("Проверка получения последних показаний, когда их нет")
    public void testGetLastReadingsWhenNonePresent() {
        when(repository.getAllReadings(user)).thenReturn(Optional.empty());

        Optional<Readings> result = service.getLastReadings(user);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Проверка получения показаний по месяцам")
    public void testGetReadingsByMonth() {
        when(repository.getReadingsByMonth(user, Month.JANUARY)).thenReturn(Optional.of(readings));

        Optional<Readings> result = service.getReadingsByMonth(user, Month.JANUARY);

        assertTrue(result.isPresent());
        assertEquals(readings, result.get());
    }

    @Test
    @DisplayName("Проверка получения показаний по месяцам, когда их нет")
    public void testGetReadingsByMonthWhenNonePresent() {
        when(repository.getReadingsByMonth(user, Month.JANUARY)).thenReturn(Optional.empty());

        Optional<Readings> result = service.getReadingsByMonth(user, Month.JANUARY);

        assertFalse(result.isPresent());
    }
}

