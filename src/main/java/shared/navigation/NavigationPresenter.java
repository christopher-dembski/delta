package shared.navigation;

import java.util.function.Consumer;

public class NavigationPresenter<T> {
    private final NavigationView<T> view;

    public NavigationPresenter(NavigationView<T> view) {
        this.view = view;
    }

    public void navigateTo(T navItemValue) {
        view.selectNavItem(navItemValue);
    }

    public void addNavigationListener(Consumer<T> listener) {
        view.addNavigationListener(listener);
    }
}
