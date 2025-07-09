package profile.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import profile.model.Profile;
import profile.model.Sex;
import profile.model.UnitSystem;
import profile.repository.TestUserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileServiceImplementorTest {

    private ProfileServiceImplementor service;
    private TestUserRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new TestUserRepository();
        service = new ProfileServiceImplementor(repository);
    }

    @Test
    public void testAddProfile_Success() {
        Profile profile = new Profile.Builder()
                .name("John Doe")
                .age(25)
                .sex(Sex.MALE)
                .dateOfBirth(LocalDate.of(1998, 1, 1))
                .height(180.0)
                .weight(70.0)
                .unitSystem(UnitSystem.METRIC)
                .build();

        service.add(profile);

        Optional<Profile> found = repository.findById(profile.getId());
        assertTrue(found.isPresent());
        assertEquals(profile, found.get());
    }

    @Test
    public void testAddProfile_NullProfile() {
        assertThrows(NullPointerException.class, () -> {
            service.add(null);
        });
    }

    @Test
    public void testAddProfile_DuplicateProfile() {
        Profile profile = new Profile.Builder()
                .name("John Doe")
                .age(25)
                .sex(Sex.MALE)
                .dateOfBirth(LocalDate.of(1998, 1, 1))
                .height(180.0)
                .weight(70.0)
                .unitSystem(UnitSystem.METRIC)
                .build();

        service.add(profile);
        
        // Try to add the same profile again - should handle gracefully
        assertDoesNotThrow(() -> {
            service.add(profile);
        });
    }

    @Test
    public void testAddProfile_WithDefaults() {
        Profile profile = new Profile.Builder()
                .name("Jane Doe")
                .age(30)
                .sex(Sex.FEMALE)
                .dateOfBirth(LocalDate.of(1993, 5, 15))
                .height(165.0)
                .weight(60.0)
                .build(); // No unitSystem specified - should get default

        service.add(profile);

        Optional<Profile> found = repository.findById(profile.getId());
        assertTrue(found.isPresent());
        // The profile should have the default unit system
        assertEquals(UnitSystem.METRIC, found.get().getUnitSystem());
    }

    @Test
    public void testGetById() {
        Profile profile = new Profile.Builder()
                .name("Test User")
                .age(28)
                .sex(Sex.FEMALE)
                .dateOfBirth(LocalDate.of(1995, 3, 10))
                .height(170.0)
                .weight(65.0)
                .build();

        service.add(profile);
        
        Optional<Profile> found = service.getById(profile.getId());
        assertTrue(found.isPresent());
        assertEquals(profile, found.get());
        
        Optional<Profile> notFound = service.getById("nonexistent");
        assertFalse(notFound.isPresent());
    }

    @Test
    public void testUpdate() {
        Profile original = new Profile.Builder()
                .name("Original Name")
                .age(25)
                .sex(Sex.MALE)
                .dateOfBirth(LocalDate.of(1998, 1, 1))
                .height(175.0)
                .weight(70.0)
                .build();

        service.add(original);
        
        Profile updated = new Profile.Builder()
                .id(original.getId()) // Same ID
                .name("Updated Name")
                .age(26)
                .sex(Sex.MALE)
                .dateOfBirth(LocalDate.of(1998, 1, 1))
                .height(175.0)
                .weight(72.0)
                .build();

        service.update(updated);
        
        Optional<Profile> found = service.getById(original.getId());
        assertTrue(found.isPresent());
        assertEquals("Updated Name", found.get().getName());
        assertEquals(72.0, found.get().getWeight());
    }

    @Test
    public void testListAll() {
        assertTrue(service.listAll().isEmpty());
        
        Profile profile1 = new Profile.Builder()
                .name("User 1")
                .age(25)
                .sex(Sex.MALE)
                .dateOfBirth(LocalDate.of(1998, 1, 1))
                .height(180.0)
                .weight(70.0)
                .build();
                
        Profile profile2 = new Profile.Builder()
                .name("User 2")
                .age(30)
                .sex(Sex.FEMALE)
                .dateOfBirth(LocalDate.of(1993, 5, 15))
                .height(165.0)
                .weight(60.0)
                .build();

        service.add(profile1);
        service.add(profile2);
        
        assertEquals(2, service.listAll().size());
    }

    @Test
    public void testSessionManagement() {
        Profile profile = new Profile.Builder()
                .name("Session User")
                .age(28)
                .sex(Sex.FEMALE)
                .dateOfBirth(LocalDate.of(1995, 3, 10))
                .height(170.0)
                .weight(65.0)
                .build();

        service.add(profile);
        
        // Initially no session
        assertFalse(service.getCurrentSession().isPresent());
        
        // Open session
        Optional<Profile> opened = service.openSession(profile.getId());
        assertTrue(opened.isPresent());
        assertEquals(profile, opened.get());
        
        // Current session should be set
        Optional<Profile> current = service.getCurrentSession();
        assertTrue(current.isPresent());
        assertEquals(profile, current.get());
        
        // Close session
        service.closeSession();
        assertFalse(service.getCurrentSession().isPresent());
    }
}
