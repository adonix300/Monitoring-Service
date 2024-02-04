package validators;

import exceptions.ValidationException;
import models.Readings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import validators.impl.ReadingsValidator;

import static org.junit.jupiter.api.Assertions.*;

public class ReadingsValidatorTest {
    private ReadingsValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new ReadingsValidator();
    }

    @Test
    @DisplayName("Проверка валидации показаний с корректными данными")
    public void testValidateWithValidReadings() {
        Readings readings = new Readings();
        readings.add("heating", 100.0);
        readings.add("coldWater", 200.0);
        readings.add("hotWater", 300.0);

        assertDoesNotThrow(() -> validator.validate(readings));
    }

    @Test
    @DisplayName("Проверка валидации показаний с пустыми данными")
    public void testValidateWithEmptyReadings() {
        Readings readings = new Readings();

        assertThrows(ValidationException.class, () -> validator.validate(readings));
    }

    @Test
    @DisplayName("Проверка валидации показаний с отрицательными значениями")
    public void testValidateWithNegativeValues() {
        Readings readings = new Readings();
        readings.add("heating", -1.0);

        assertThrows(ValidationException.class, () -> validator.validate(readings));
    }
}