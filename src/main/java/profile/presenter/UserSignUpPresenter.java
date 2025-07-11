package profile.presenter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import profile.model.Profile;
import profile.model.Sex;
import profile.model.UnitSystem;
import profile.service.IProfileService;
import profile.view.ISignUpView;

public class UserSignUpPresenter {
    private final ISignUpView view;
    private final IProfileService profileService;

    public UserSignUpPresenter(ISignUpView view, IProfileService profileService) {
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
            // First, validate UI input before processing
            if (!validateUIInput()) {
                return; // Stop submission if validation fails
            }
            
            // Get form data from view
            ISignUpView.RawInput rawInput = view.getFormInput();

            // Validate and convert the raw input
            Profile profile = validateAndCreateProfile(rawInput);

            // Check if a profile with the same full name already exists
            List<Profile> existingProfiles = profileService.listAll();
            boolean nameExists = existingProfiles.stream()
                    .anyMatch(p -> p.getName().equalsIgnoreCase(profile.getName()));
            
            if (nameExists) {
                view.showError("A profile with the name '" + profile.getName() + "' already exists. Please use a different name.");
                return;
            }

            // Save the profile using the service
            profileService.add(profile);

            // Show success message (only if not in headless mode)
            if (!java.awt.GraphicsEnvironment.isHeadless()) {
                javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Profile created successfully for " + profile.getName() + "!",
                    "Success",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
            }

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
            String dobString = rawInput.dob().trim();

            // Handle different date formats we might receive
            if (dobString.length() == 8 && dobString.matches("\\d{8}")) {
                // Format: YYYYMMDD (no dashes) - add dashes
                dobString = dobString.substring(0, 4) + "-" +
                           dobString.substring(4, 6) + "-" +
                           dobString.substring(6, 8);
            }

            dateOfBirth = LocalDate.parse(dobString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (dateOfBirth.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Date of birth cannot be in the future");
            }
        } catch (DateTimeParseException e) {
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
        return new Profile.Builder()
                .name(rawInput.fullName().trim())
                .age(age)
                .sex(sex)
                .dateOfBirth(dateOfBirth)
                .height(height)
                .weight(weight)
                .unitSystem(unitSystem)
                .build();
    }

    /**
     * Validate UI input before allowing form submission
     */
    private boolean validateUIInput() {
        ISignUpView.RawInput rawInput = view.getFormInput();
        
        // Check for empty or null fields
        if (rawInput.fullName() == null || rawInput.fullName().trim().isEmpty()) {
            view.showError("Full name is required");
            return false;
        }
        
        if (rawInput.fullName().trim().length() < 2) {
            view.showError("Full name must be at least 2 characters long");
            return false;
        }
        
        if (rawInput.age() == null || rawInput.age().trim().isEmpty()) {
            view.showError("Age is required");
            return false;
        }
        
        // Validate age format and range
        int age;
        try {
            age = Integer.parseInt(rawInput.age().trim());
            if (age < 0 || age > 150) {
                view.showError("Age must be between 0 and 150");
                return false;
            }
        } catch (NumberFormatException e) {
            view.showError("Age must be a valid number");
            return false;
        }
        
        if (rawInput.dob() == null || rawInput.dob().trim().isEmpty()) {
            view.showError("Date of birth is required");
            return false;
        }
        
        // Validate date format
        String dobString = rawInput.dob().trim();
        if (dobString.length() < 8 || dobString.length() > 10) {
            view.showError("Date of birth must be in YYYY-MM-DD format");
            return false;
        }
        
        // Basic date format validation
        if (!dobString.matches("\\d{4}-\\d{2}-\\d{2}") && !dobString.matches("\\d{8}")) {
            view.showError("Date of birth must be in YYYY-MM-DD format");
            return false;
        }
        
        // Additional date range validation
        try {
            String formattedDob = dobString;
            if (dobString.length() == 8 && dobString.matches("\\d{8}")) {
                formattedDob = dobString.substring(0, 4) + "-" +
                              dobString.substring(4, 6) + "-" +
                              dobString.substring(6, 8);
            }
            
            java.time.LocalDate dateOfBirth = java.time.LocalDate.parse(formattedDob, 
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            if (dateOfBirth.isAfter(java.time.LocalDate.now())) {
                view.showError("Date of birth cannot be in the future");
                return false;
            }
            
            if (dateOfBirth.isBefore(java.time.LocalDate.of(1900, 1, 1))) {
                view.showError("Date of birth cannot be before 1900");
                return false;
            }
            
            // Check age consistency with date of birth
            int calculatedAge = java.time.Period.between(dateOfBirth, java.time.LocalDate.now()).getYears();
            if (Math.abs(calculatedAge - age) > 1) {
                view.showError("Age (" + age + ") doesn't match date of birth (calculated age: " + calculatedAge + ")");
                return false;
            }
        } catch (java.time.format.DateTimeParseException e) {
            view.showError("Invalid date format. Please use YYYY-MM-DD");
            return false;
        }
        
        if (rawInput.height() == null || rawInput.height().trim().isEmpty()) {
            view.showError("Height is required");
            return false;
        }
        
        // Validate height format and range based on unit system
        try {
            double height = Double.parseDouble(rawInput.height().trim());
            if (height <= 0) {
                view.showError("Height must be a positive number");
                return false;
            }
            
            // Unit-specific validation
            String unitSystem = rawInput.unitSystem();
            if ("METRIC".equalsIgnoreCase(unitSystem)) {
                // Height in centimeters
                if (height < 30 || height > 300) {
                    view.showError("Height must be between 30 and 300 cm");
                    return false;
                }
            } else if ("IMPERIAL".equalsIgnoreCase(unitSystem)) {
                // Height in feet (can be decimal like 5.75)
                if (height < 1 || height > 10) {
                    view.showError("Height must be between 1 and 10 feet");
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            view.showError("Height must be a valid number");
            return false;
        }
        
        if (rawInput.weight() == null || rawInput.weight().trim().isEmpty()) {
            view.showError("Weight is required");
            return false;
        }
        
        // Validate weight format and range based on unit system
        try {
            double weight = Double.parseDouble(rawInput.weight().trim());
            if (weight <= 0) {
                view.showError("Weight must be a positive number");
                return false;
            }
            
            // Unit-specific validation
            String unitSystem = rawInput.unitSystem();
            if ("METRIC".equalsIgnoreCase(unitSystem)) {
                // Weight in kilograms
                if (weight < 0.5 || weight > 1000) {
                    view.showError("Weight must be between 0.5 and 1000 kg");
                    return false;
                }
            } else if ("IMPERIAL".equalsIgnoreCase(unitSystem)) {
                // Weight in pounds
                if (weight < 1 || weight > 2200) {
                    view.showError("Weight must be between 1 and 2200 pounds");
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            view.showError("Weight must be a valid number");
            return false;
        }
        
        // Sex and unit system should be valid since they're from combo boxes
        if (rawInput.sex() == null || rawInput.sex().trim().isEmpty()) {
            view.showError("Sex selection is required");
            return false;
        }
        
        if (rawInput.unitSystem() == null || rawInput.unitSystem().trim().isEmpty()) {
            view.showError("Unit system selection is required");
            return false;
        }
        
        return true;
    }
}
