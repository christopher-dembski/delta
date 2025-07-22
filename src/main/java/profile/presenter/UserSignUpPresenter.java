package profile.presenter;

import profile.model.Profile;
import profile.service.IProfileService;
import profile.view.ISignUpView;
import profile.view.UserSignUp;
import shared.ServiceFactory;

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
        view.setOnSubmit(this::handleFormSubmission);
    }


    private void handleFormSubmission() {
        try {
            // Extract form data from view
            ISignUpView.RawInput rawInput = view.getFormInput();
            
            // Delegate user creation to service
            Profile createdProfile = profileService.createUser(rawInput);
            
            // show success message through the view
            view.showSuccess("Profile created successfully for " + createdProfile.getName() + "!");
            
            // Close the signup window
            view.close();
            
        } catch (IProfileService.ValidationException e) {
            // Show validation error to user
            view.showError(e.getMessage());
        } catch (IProfileService.DuplicateUserException e) {
            // Show duplicate user error
            view.showError(e.getMessage());
        } catch (Exception e) {
            // generic error for unexpected issues
            view.showError("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Test main method for creating a profile.
     * @param args Command line args (unused).
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Get service instances from factory
                IProfileService profileService = ServiceFactory.getProfileService();

                // Create the view
                UserSignUp view = new UserSignUp();

                // Create and initialize the presenter
                UserSignUpPresenter presenter = new UserSignUpPresenter(view, profileService);
                presenter.initialize();

                // Show the view
                view.setVisible(true);
            } catch (Exception e) {
                System.err.println("Failed to initialize application: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
