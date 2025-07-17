package shared.navigation;

import app.LeftNavItem;

import java.util.function.Consumer;

public class NavigationPresenter {
    private final NavigationView view;

    public NavigationPresenter(NavigationView view) {
        this.view = view;
    }

    public void addNavigationListener(Consumer<LeftNavItem> listener) {
        view.addNavigationListener(listener);
    }
}
