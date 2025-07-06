package app;

import profile.view.UserSignUp;

public class AppMain {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new UserSignUp().setVisible(true);
        });
    }
}
