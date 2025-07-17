package app;

import shared.NavigationPresenter;
import shared.NavigationView;

public class AppMainPresenter {
    private AppMainView appMainView;

    public AppMainPresenter() {
        NavigationView leftNav = new NavigationView();
        NavigationPresenter leftNavPresenter = new NavigationPresenter(leftNav);
        appMainView = new AppMainView(leftNav);
        leftNavPresenter.addNavigationListener(menuItem -> appMainView.renderCard(menuItem));
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(AppMainPresenter::new);
    }
}
