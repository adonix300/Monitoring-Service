package services;

import enums.Role;
import interfaces.Validator;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repositories.ReadingsRepositoryImpl;

import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReadingsServiceImplTest {
    @Mock
    private ReadingsRepositoryImpl repository;
    @Mock
    private Validator<Map<String, Double>> validator;
    @InjectMocks
    private ReadingsServiceImpl readingsService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddReadings() {
        User user = new User("testLogin", "testPassword", Role.USER);
        Map<String, Double> readings = Map.of("heating", 100.0, "hotWater", 200.0, "coldWater", 300.0);
        when(repository.getAllReadings(user)).thenReturn(Optional.of(new LinkedHashMap<>()));
        assertDoesNotThrow(() -> readingsService.addReadings(user, Month.JANUARY, readings));
        verify(repository, times(1)).addReadings(user, Month.JANUARY, readings);
    }

    @Test
    public void testPrintAllReadings() {
        User user = new User("testLogin", "testPassword", Role.USER);
        Map<Month, Map<String, Double>> readingsMap = new LinkedHashMap<>();
        when(repository.getAllReadings(user)).thenReturn(Optional.of(readingsMap));
        assertDoesNotThrow(() -> readingsService.printAllReadings(user));
    }

    @Test
    public void testGetLastReadings() {
        User user = new User("testLogin", "testPassword", Role.USER);
        Map<Month, Map<String, Double>> readingsMap = new LinkedHashMap<>();
        Map<String, Double> readings = Map.of("heating", 100.0, "hotWater", 200.0, "coldWater", 300.0);
        readingsMap.put(Month.JANUARY, readings);
        when(repository.getAllReadings(user)).thenReturn(Optional.of(readingsMap));
        Optional<Map<String, Double>> result = readingsService.getLastReadings(user);
        assertTrue(result.isPresent());
        assertEquals(readings, result.get());
    }

    @Test
    public void testGetReadingsByMonth() {
        User user = new User("testLogin", "testPassword", Role.USER);
        Map<String, Double> readings = Map.of("heating", 100.0, "hotWater", 200.0, "coldWater", 300.0);
        when(repository.getReadingsByMonth(user, Month.JANUARY)).thenReturn(Optional.of(readings));
        Optional<Map<String, Double>> result = readingsService.getReadingsByMonth(user, Month.JANUARY);
        assertTrue(result.isPresent());
        assertEquals(readings, result.get());
    }
}

