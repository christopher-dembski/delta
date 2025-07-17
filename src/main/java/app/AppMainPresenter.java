package app;

import shared.NavigationPresenter;
import shared.NavigationView;

public class AppMainPresenter {
    private AppMainView view;

    public AppMainPresenter() {
        NavigationView menu = new NavigationView();
        NavigationPresenter navigationPresenter = new NavigationPresenter(menu);
        view = new AppMainView(menu);
        navigationPresenter.addNavigationListener(menuItem -> view.renderCard(menuItem));
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(AppMainPresenter::new);
    }
}
