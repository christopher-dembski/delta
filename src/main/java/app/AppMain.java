package app;

import profile.presenter.UserSignUpPresenter;
import profile.repository.IUserRepository;
import profile.repository.UserRepositoryImplementor;
import profile.service.IProfileService;
import profile.service.ProfileServiceImplementor;
import profile.view.UserSignUp;
import shared.AppBackend;

public class AppMain {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Create the repository and service layers
                IUserRepository repository = new UserRepositoryImplementor(AppBackend.db());
                IProfileService profileService = new ProfileServiceImplementor(repository);

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
