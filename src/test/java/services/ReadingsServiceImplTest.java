package services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import enums.Role;
import exceptions.ValidationException;
import models.Readings;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.ReadingsRepository;
import services.impl.ReadingsServiceImpl;
import validators.Validator;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReadingsServiceImplTest {

    @Mock
    private ReadingsRepository readingsRepository;

    @Mock
    private Validator<Readings> readingsValidator;

    @InjectMocks
    private ReadingsServiceImpl readingsService;

    private User user;
    private Readings readings;

    @BeforeEach
    void setUp() {
        user = new User("testUser", Role.USER);
        readings = new Readings(); // Замените на соответствующую реализацию
        // Настройка readings по необходимости
    }

    @Test
    @DisplayName("Успешное добавление показаний")
    void addReadingsSuccess() {
        when(readingsRepository.getAllReadings(user)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> readingsService.addReadings(user, Month.JANUARY, readings));
        verify(readingsValidator).validate(readings);
        verify(readingsRepository).addReadings(user, Month.JANUARY, readings);
    }

    @Test
    @DisplayName("Добавление показаний для уже существующего месяца")
    void addReadingsForExistingMonth() {
        Map<Month, Readings> existingReadings = new HashMap<>();
        existingReadings.put(Month.JANUARY, readings);

        when(readingsRepository.getAllReadings(user)).thenReturn(Optional.of(existingReadings));

        Exception exception = assertThrows(RuntimeException.class, () -> readingsService.addReadings(user, Month.JANUARY, readings));
        assertTrue(exception.getMessage().contains("За этот месяц уже были поданы показания"));
    }

    @Test
    @DisplayName("Получение всех показаний пользователя")
    void getAllReadingsSuccess() {
        Map<Month, Readings> readingsMap = new HashMap<>();
        readingsMap.put(Month.JANUARY, readings);

        when(readingsRepository.getAllReadings(user)).thenReturn(Optional.of(readingsMap));

        String result = readingsService.getAllReadings(user);
        assertTrue(result.contains(Month.JANUARY.toString()));
        assertTrue(result.contains(readings.toString()));
    }

    @Test
    @DisplayName("Получение показаний пользователя, когда показаний нет")
    void getAllReadingsEmpty() {
        when(readingsRepository.getAllReadings(user)).thenReturn(Optional.empty());

        String result = readingsService.getAllReadings(user);
        assertTrue(result.contains("Показаний не найдено"));
    }

    @Test
    @DisplayName("Получение последних показаний пользователя")
    void getLastReadingsSuccess() {
        Map<Month, Readings> readingsMap = new HashMap<>();
        readingsMap.put(Month.JANUARY, readings);

        when(readingsRepository.getAllReadings(user)).thenReturn(Optional.of(readingsMap));

        Optional<Readings> result = readingsService.getLastReadings(user);
        assertTrue(result.isPresent());
        assertEquals(readings, result.get());
    }

    @Test
    @DisplayName("Получение последних показаний, когда показаний нет")
    void getLastReadingsEmpty() {
        when(readingsRepository.getAllReadings(user)).thenReturn(Optional.empty());

        Optional<Readings> result = readingsService.getLastReadings(user);
        assertFalse(result.isPresent());
    }
    @Test
    @DisplayName("Получение показаний за определенный месяц")
    void getReadingsByMonthSuccess() {
        when(readingsRepository.getReadingsByMonth(user, Month.JANUARY)).thenReturn(Optional.of(readings));

        Optional<Readings> result = readingsService.getReadingsByMonth(user, Month.JANUARY);
        assertTrue(result.isPresent());
        assertEquals(readings, result.get());
    }

    @Test
    @DisplayName("Получение показаний за месяц, когда показаний нет")
    void getReadingsByMonthEmpty() {
        when(readingsRepository.getReadingsByMonth(user, Month.JANUARY)).thenReturn(Optional.empty());

        Optional<Readings> result = readingsService.getReadingsByMonth(user, Month.JANUARY);
        assertFalse(result.isPresent());
    }

}
