package shared.navigation;

import java.util.function.Consumer;

/**
 * The presenter controlling a navigation menu.
 * @param <T>
 */
public class NavigationPresenter<T> {
    private final NavigationView<T> view;

    /**
     * @param view The navigation component to control.
     */
    public NavigationPresenter(NavigationView<T> view) {
        this.view = view;
    }

    /**
     * Navigates to the specified navigation item.
     * @param navItemValue The identifier of the nav item to navigate to.
     */
    public void navigateTo(T navItemValue) {
        view.selectNavItem(navItemValue);
    }

    /**
     * Adds a listener to run when a navigation item is selected.
     * @param listener The listener to run when a navigation item is selected.
     */
    public void addNavigationListener(Consumer<T> listener) {
        view.addNavigationListener(listener);
    }
}
