package app;

import profile.presenter.UserSignUpPresenter;
import profile.repository.UserRepository;
import profile.repository.UserRepositoryImplementor;
import profile.service.ProfileService;
import profile.service.ProfileServiceImplementor;
import profile.view.UserSignUp;
import shared.AppBackend;

public class AppMain {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Create the repository and service layers
                UserRepository repository = new UserRepositoryImplementor(AppBackend.db());
                ProfileService profileService = new ProfileServiceImplementor(repository);

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
