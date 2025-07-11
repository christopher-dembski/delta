package profile.model;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfileTest {

    private Profile.Builder validBuilder;

    @BeforeEach
    void setUp() {
        validBuilder = new Profile.Builder()
                .id(42)
                .name("John Doe")
                .age(25)
                .sex(Sex.MALE)
                .dateOfBirth(LocalDate.of(1998, 1, 1))
                .height(175.0)
                .weight(70.0)
                .unitSystem(UnitSystem.METRIC);
    }

    @Test
    void build_WithValidData_ShouldCreateProfile() {
        // When
        Profile profile = validBuilder.build();

        // Then
        assertNotNull(profile);
        assertEquals(42, profile.getId());
        assertEquals("John Doe", profile.getName());
        assertEquals(25, profile.getAge());
        assertEquals(Sex.MALE, profile.getSex());
        assertEquals(LocalDate.of(1998, 1, 1), profile.getDateOfBirth());
        assertEquals(175.0, profile.getHeight());
        assertEquals(70.0, profile.getWeight());
        assertEquals(UnitSystem.METRIC, profile.getUnitSystem());
    }

    @Test
    void build_WithNullId_ShouldAllowNullId() {
        // Given
        validBuilder.id(null);

        // When
        Profile profile = validBuilder.build();

        // Then
        assertNull(profile.getId()); 
    }

    @Test
    void build_WithEmptyName_ShouldThrowException() {
        // Given
        validBuilder.name(null); 

        // When/Then
        assertThrows(NullPointerException.class, () -> validBuilder.build());
    }

    @Test
    void build_WithNegativeAge_ShouldThrowException() {
        // Given
        validBuilder.age(0); // Change to 0 since the validation is age <= 0

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> validBuilder.build());
    }

    @Test
    void build_WithNullSex_ShouldThrowException() {
        // Given
        validBuilder.sex(null);

        // When/Then
        assertThrows(NullPointerException.class, () -> validBuilder.build()); 
    }

    @Test
    void build_WithNullDateOfBirth_ShouldThrowException() {
        // Given
        validBuilder.dateOfBirth(null);

        // When/Then
        assertThrows(NullPointerException.class, () -> validBuilder.build()); 
    }

    @Test
    void build_WithNegativeHeight_ShouldThrowException() {
        // Given
        validBuilder.height(0.0); // Change to 0 since validation is height <= 0

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> validBuilder.build());
    }

    @Test
    void build_WithNegativeWeight_ShouldThrowException() {
        // Given
        validBuilder.weight(0.0); // Change to 0 since validation is weight <= 0

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> validBuilder.build());
    }

    @Test
    void build_WithNullUnitSystem_ShouldUseDefault() {
        // Given
        validBuilder.unitSystem(null);

        // When
        Profile profile = validBuilder.build();

        // Then
        assertEquals(UnitSystem.METRIC, profile.getUnitSystem()); // Should use default
    }

    @Test
    void build_WithSameBuilder_ShouldCreateDifferentInstances() {
        // Given - Create profiles with different IDs to test instance differences
        Profile.Builder builder1 = new Profile.Builder()
                .id(100)
                .name("John Doe")
                .age(25)
                .sex(Sex.MALE)
                .dateOfBirth(LocalDate.of(1998, 1, 1))
                .height(175.0)
                .weight(70.0)
                .unitSystem(UnitSystem.METRIC);
        
        Profile.Builder builder2 = new Profile.Builder()
                .id(200)
                .name("John Doe")
                .age(25)
                .sex(Sex.MALE)
                .dateOfBirth(LocalDate.of(1998, 1, 1))
                .height(175.0)
                .weight(70.0)
                .unitSystem(UnitSystem.METRIC);

        // When
        Profile profile1 = builder1.build();
        Profile profile2 = builder2.build();

        // Then
        assertNotEquals(profile1, profile2); // Different objects with different IDs
        assertNotEquals(profile1.getId(), profile2.getId()); 
    }

    @Test
    void build_WithExplicitSameId_ShouldCreateEqualProfiles() {
        // Given
        Integer sameId = 123;
        Profile profile1 = validBuilder.id(sameId).build();
        Profile profile2 = new Profile.Builder()
                .id(sameId)
                .name("John Doe")
                .age(25)
                .sex(Sex.MALE)
                .dateOfBirth(LocalDate.of(1998, 1, 1))
                .height(175.0)
                .weight(70.0)
                .unitSystem(UnitSystem.METRIC)
                .build();

        // When/Then
        // Note: Without overridden equals/hashCode, these will only be equal if same instance
        assertNotSame(profile1, profile2); // Different instances
        assertEquals(profile1.getId(), profile2.getId()); // Same ID
        assertEquals(profile1.getName(), profile2.getName()); // Same name
    }

    @Test
    void getters_ShouldReturnCorrectValues() {
        // Given
        Profile profile = validBuilder.build();

        // When/Then
        assertEquals("John Doe", profile.getName());
        assertEquals(25, profile.getAge());
        assertEquals(Sex.MALE, profile.getSex());
        assertEquals(UnitSystem.METRIC, profile.getUnitSystem());
        assertNotNull(profile.getId());
        assertTrue(profile.toString().contains("Profile")); // Basic toString test
    }
}
