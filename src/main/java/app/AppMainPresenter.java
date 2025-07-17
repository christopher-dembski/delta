package app;

import shared.NavigationPresenter;
import shared.NavigationView;

public class AppMainPresenter {
    private static AppMainPresenter instance;

    private AppMainView appMainView;

    public AppMainPresenter() {
        NavigationView leftNav = new NavigationView();
        NavigationPresenter leftNavPresenter = new NavigationPresenter(leftNav);
        appMainView = new AppMainView(leftNav);
        leftNavPresenter.addNavigationListener(menuItem -> appMainView.renderCard(menuItem));
    }

    public static AppMainPresenter instance() {
        if (instance == null) {
            instance = new AppMainPresenter();
        }
        return instance;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(AppMainPresenter::instance);
    }
}
