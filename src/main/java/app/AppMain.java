package app;

import profile.presenter.UserSignUpPresenter;
import profile.service.IProfileService;
import profile.view.UserSignUp;
import shared.ServiceFactory;

public class AppMain {
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
