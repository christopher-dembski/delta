package profile.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class UnitSystemTest {

    @Test
    void metricToString_ShouldReturnCorrectDisplayName() {
        // When
        String result = UnitSystem.METRIC.toString();

        // Then
        assertEquals("Metric (cm, kg)", result);
    }

    @Test
    void imperialToString_ShouldReturnCorrectDisplayName() {
        // When
        String result = UnitSystem.IMPERIAL.toString();

        // Then
        assertEquals("Imperial (ft, lb)", result);
    }

    @Test
    void valueOf_WithValidNames_ShouldReturnCorrectEnums() {
        // When/Then
        assertEquals(UnitSystem.METRIC, UnitSystem.valueOf("METRIC"));
        assertEquals(UnitSystem.IMPERIAL, UnitSystem.valueOf("IMPERIAL"));
    }

    @Test
    void valueOf_WithInvalidName_ShouldThrowException() {
        // When/Then
        assertThrows(IllegalArgumentException.class, () -> UnitSystem.valueOf("INVALID"));
    }

    @Test
    void values_ShouldReturnAllEnumValues() {
        // When
        UnitSystem[] values = UnitSystem.values();

        // Then
        assertEquals(2, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(UnitSystem.METRIC));
        assertTrue(java.util.Arrays.asList(values).contains(UnitSystem.IMPERIAL));
    }
}
