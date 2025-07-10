package app;

import profile.view.UserSignUp;
import profile.presenter.UserSignUpPresenter;
import profile.service.ProfileService;
import profile.service.ProfileServiceImplementor;
import profile.repository.UserRepository;
import profile.repository.UserRepositoryImplementor;
import data.MySQLDriver;
import data.MySQLConfig;

public class AppMain {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Create the database driver and config

                // Create the repository and service layers
                UserRepository repository = new UserRepositoryImplementor(driver);
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
