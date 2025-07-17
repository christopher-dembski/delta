package app;

public class AppMainPresenter {
    private AppMain view;

    public AppMainPresenter() {
        NavigationView menu = new NavigationView();
        NavigationPresenter navigationPresenter = new NavigationPresenter(menu);
        view = new AppMain(menu);
        navigationPresenter.addNavigationListener(menuItem -> view.renderCard(menuItem));
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(AppMainPresenter::new);
    }
}
