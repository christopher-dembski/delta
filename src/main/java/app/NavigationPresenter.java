package app;

import java.util.function.Consumer;

public class NavigationPresenter {
    private final NavigationView view;

    public NavigationPresenter(NavigationView view) {
        this.view = view;
    }

    public void addNavigationListener(Consumer<MenuItem> listener) {
        view.addNavigationListener(listener);
    }
}
