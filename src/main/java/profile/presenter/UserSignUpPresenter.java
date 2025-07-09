package profile.presenter;

import profile.view.ISignUpView;
import profile.service.ProfileService;
import profile.model.Profile;
import profile.model.Sex;
import profile.model.UnitSystem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class UserSignUpPresenter {
    private final ISignUpView view;
    private final ProfileService profileService;

    public UserSignUpPresenter(ISignUpView view, ProfileService profileService) {
        this.view = view;
        this.profileService = profileService;
    }

    /**
     * Initialize the presenter and set up view callbacks
     */
    public void initialize() {
        // Set the callback for when the form is submitted
        view.setOnSubmit(this::handleFormSubmission);
    }

    /**
     * Handle form submission from the view
     */
    private void handleFormSubmission() {
        try {
            // Get form data from view
            ISignUpView.RawInput rawInput = view.getFormInput();

            // Validate and convert the raw input
            Profile profile = validateAndCreateProfile(rawInput);

            // Save the profile using the service
            profileService.add(profile);

            // Show success message
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "Profile created successfully for " + profile.getName() + "!",
                "Success",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

            // Close the signup window
            view.close();

        } catch (IllegalArgumentException e) {
            // Show validation error to user
            view.showError(e.getMessage());
        } catch (Exception e) {
            // Show generic error for unexpected issues
            view.showError("An unexpected error occurred: " + e.getMessage());
            System.err.println("Error in form submission: " + e.getMessage());
        }
    }

    /**
     * Validate raw input and create a Profile object
     */
    private Profile validateAndCreateProfile(ISignUpView.RawInput rawInput) throws IllegalArgumentException {
        // Validate required fields
        if (rawInput.fullName() == null || rawInput.fullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }

        if (rawInput.age() == null || rawInput.age().trim().isEmpty()) {
            throw new IllegalArgumentException("Age is required");
        }

        if (rawInput.dob() == null || rawInput.dob().trim().isEmpty()) {
            throw new IllegalArgumentException("Date of birth is required (YYYY-MM-DD format)");
        }

        if (rawInput.height() == null || rawInput.height().trim().isEmpty()) {
            throw new IllegalArgumentException("Height is required");
        }

        if (rawInput.weight() == null || rawInput.weight().trim().isEmpty()) {
            throw new IllegalArgumentException("Weight is required");
        }

        // Parse and validate age
        int age;
        try {
            age = Integer.parseInt(rawInput.age().trim());
            if (age < 0 || age > 150) {
                throw new IllegalArgumentException("Age must be between 0 and 150");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Age must be a valid number");
        }

        // Parse and validate date of birth
        LocalDate dateOfBirth;
        try {
            // Debug logging to see what we're actually receiving
            System.out.println("DEBUG: Raw DOB input received: '" + rawInput.dob() + "'");
            System.out.println("DEBUG: DOB length: " + rawInput.dob().length());
            System.out.println("DEBUG: DOB chars: " + java.util.Arrays.toString(rawInput.dob().toCharArray()));

            String dobString = rawInput.dob().trim();

            // Handle different date formats we might receive
            if (dobString.length() == 8 && dobString.matches("\\d{8}")) {
                // Format: YYYYMMDD (no dashes) - add dashes
                dobString = dobString.substring(0, 4) + "-" +
                           dobString.substring(4, 6) + "-" +
                           dobString.substring(6, 8);
                System.out.println("DEBUG: Converted to: '" + dobString + "'");
            }

            dateOfBirth = LocalDate.parse(dobString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (dateOfBirth.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Date of birth cannot be in the future");
            }
        } catch (DateTimeParseException e) {
            System.out.println("DEBUG: DateTimeParseException occurred: " + e.getMessage());
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD");
        }

        // Parse and validate height
        double height;
        try {
            height = Double.parseDouble(rawInput.height().trim());
            if (height <= 0) {
                throw new IllegalArgumentException("Height must be a positive number");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Height must be a valid number");
        }

        // Parse and validate weight
        double weight;
        try {
            weight = Double.parseDouble(rawInput.weight().trim());
            if (weight <= 0) {
                throw new IllegalArgumentException("Weight must be a positive number");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Weight must be a valid number");
        }

        // Parse sex enum
        Sex sex;
        try {
            sex = Sex.valueOf(rawInput.sex().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid sex selection");
        }

        // Parse unit system enum
        UnitSystem unitSystem;
        try {
            unitSystem = UnitSystem.valueOf(rawInput.unitSystem().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid unit system selection");
        }

        // Create and return the profile using Builder pattern
        String profileId = UUID.randomUUID().toString();
        return new Profile.Builder()
                .id(profileId)
                .name(rawInput.fullName().trim())
                .age(age)
                .sex(sex)
                .dateOfBirth(dateOfBirth)
                .height(height)
                .weight(weight)
                .unitSystem(unitSystem)
                .build();
    }
}
