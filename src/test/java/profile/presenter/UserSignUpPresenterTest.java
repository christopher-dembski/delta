package profile.presenter;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import profile.model.Profile;
import profile.model.Sex;
import profile.model.UnitSystem;
import profile.service.TestProfileService;
import profile.view.ISignUpView;
import profile.view.TestSignUpView;

class UserSignUpPresenterTest {

    private TestSignUpView testView;
    private TestProfileService testService;
    private UserSignUpPresenter presenter;

    @BeforeEach
    void setUp() {
        testView = new TestSignUpView();
        testService = new TestProfileService();
        presenter = new UserSignUpPresenter(testView, testService);
        testView.reset();
        testService.clear();
    }

    @Test
    void constructor_ShouldStoreViewAndService() {
        // Given/When
        UserSignUpPresenter testPresenter = new UserSignUpPresenter(testView, testService);
        
        // Then
        assertNotNull(testPresenter);
    }

    @Test
    void initialize_ShouldSetViewCallback() {
        // When
        presenter.initialize();

        // Then - After initialization, view should have a submit callback
        // We can test this by triggering submit and seeing if it processes
        testView.triggerSubmit();
        
        // Should either succeed or show validation errors (depending on form data)
        assertTrue(testView.isClosed() || testView.getLastError() != null || testService.size() > 0);
    }

    @Test
    void handleFormSubmission_WithValidInput_ShouldCreateProfile() {
        // Given
        presenter.initialize();
        testView.setFormInput(new ISignUpView.RawInput(
            "John Doe",
            "25", 
            "1999-07-01",  
            "175.0",
            "70.0",
            "MALE",
            "METRIC"
        ));

        // When
        testView.triggerSubmit();

        // Then
        assertEquals(1, testService.size());
        assertTrue(testView.isClosed());
        assertNull(testView.getLastError());
    }

    @Test
    void handleFormSubmission_WithEmptyName_ShouldShowError() {
        // Given
        presenter.initialize();
        testView.setFormInput(new ISignUpView.RawInput(
            "",  // Empty name
            "25",
            "1999-07-01",  // Fixed: consistent age/DOB
            "175.0",
            "70.0",
            "MALE",
            "METRIC"
        ));

        // When
        testView.triggerSubmit();

        // Then
        assertEquals(0, testService.size());
        assertFalse(testView.isClosed());
        assertNotNull(testView.getLastError());
        assertTrue(testView.getLastError().contains("Full name is required"));
    }

    @Test
    void handleFormSubmission_WithInvalidAge_ShouldShowError() {
        // Given
        presenter.initialize();
        testView.setFormInput(new ISignUpView.RawInput(
            "John Doe",
            "invalid",  // Invalid age
            "1999-07-01",  // Fixed: consistent DOB
            "175.0", 
            "70.0",
            "MALE",
            "METRIC"
        ));

        // When
        testView.triggerSubmit();

        // Then
        assertEquals(0, testService.size());
        assertFalse(testView.isClosed());
        assertNotNull(testView.getLastError());
        assertTrue(testView.getLastError().contains("Age must be a valid number"));
    }

    @Test
    void handleFormSubmission_WithFutureDateOfBirth_ShouldShowError() {
        // Given
        presenter.initialize();
        testView.setFormInput(new ISignUpView.RawInput(
            "John Doe",
            "25",
            "2030-01-15",  // Future date
            "175.0",
            "70.0",
            "MALE", 
            "METRIC"
        ));

        // When
        testView.triggerSubmit();

        // Then
        assertEquals(0, testService.size());
        assertFalse(testView.isClosed());
        assertNotNull(testView.getLastError());
        assertTrue(testView.getLastError().contains("Date of birth cannot be in the future"));
    }

    @Test
    void handleFormSubmission_WithDuplicateName_ShouldShowError() {
        // Given
        Profile existingProfile = new Profile.Builder()
            .name("John Doe")
            .age(30)
            .sex(Sex.MALE)
            .dateOfBirth(LocalDate.of(1994, 7, 1))  // Fixed: age 30 with consistent DOB
            .height(180.0)
            .weight(75.0)
            .unitSystem(UnitSystem.METRIC)
            .build();
        testService.addProfile(existingProfile);

        presenter.initialize();
        testView.setFormInput(new ISignUpView.RawInput(
            "john doe",  // Same name, different case - should pass validation but fail duplicate check
            "25",
            "1999-07-01",  // Fixed: consistent age/DOB for test input
            "175.0",
            "70.0", 
            "MALE",
            "METRIC"
        ));

        // When
        testView.triggerSubmit();

        // Then
        assertEquals(1, testService.size()); // Only the original profile
        assertFalse(testView.isClosed());
        assertNotNull(testView.getLastError());
        assertTrue(testView.getLastError().contains("already exists"));
    }

    @Test
    void handleFormSubmission_WithServiceException_ShouldShowError() {
        // Given
        presenter.initialize();
        // Set exception to happen when listAll is called (for duplicate check)
        testService.setShouldThrowException(true);
        testService.setExceptionMessage("Database connection failed");
        
        testView.setFormInput(new ISignUpView.RawInput(
            "John Doe",
            "25",
            "1999-07-01",  // Fixed: consistent age/DOB
            "175.0",
            "70.0",
            "MALE",
            "METRIC"
        ));

        // When
        testView.triggerSubmit();

        // Then
        assertEquals(0, testService.size());
        assertFalse(testView.isClosed());
        assertNotNull(testView.getLastError());
        assertTrue(testView.getLastError().contains("error") || testView.getLastError().contains("Database connection failed"));
    }

    @Test
    void handleFormSubmission_WithInvalidHeightForMetric_ShouldShowError() {
        // Given
        presenter.initialize();
        testView.setFormInput(new ISignUpView.RawInput(
            "John Doe",
            "25",
            "1999-07-01",  // Fixed: consistent age/DOB
            "500.0",  // Invalid height for metric (too tall)
            "70.0",
            "MALE",
            "METRIC"
        ));

        // When
        testView.triggerSubmit();

        // Then
        assertEquals(0, testService.size());
        assertFalse(testView.isClosed());
        assertNotNull(testView.getLastError());
        assertTrue(testView.getLastError().contains("Height must be between 30 and 300 cm"));
    }

    @Test
    void handleFormSubmission_WithInvalidHeightForImperial_ShouldShowError() {
        // Given
        presenter.initialize();
        testView.setFormInput(new ISignUpView.RawInput(
            "John Doe",
            "25",
            "1999-07-01",  // Fixed: consistent age/DOB
            "20.0",  // Invalid height for imperial (too tall in feet)
            "150.0",
            "MALE",
            "IMPERIAL"
        ));

        // When
        testView.triggerSubmit();

        // Then
        assertEquals(0, testService.size());
        assertFalse(testView.isClosed());
        assertNotNull(testView.getLastError());
        assertTrue(testView.getLastError().contains("Height must be between 1 and 10 feet"));
    }
}
