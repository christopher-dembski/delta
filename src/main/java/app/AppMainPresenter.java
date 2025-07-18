package app;

import shared.navigation.*;

import javax.swing.*;

/**
 * Presenter for the main UI of the app.
 */
public class AppMainPresenter {
    private static AppMainPresenter instance;

    private AppMainView appMainView;
    private NavigationPresenter<LeftNavItem> leftNavPresenter;

    private AppMainPresenter() {
        INavElement<LeftNavItem> leftNavTree = buildLeftNavTree();
        NavigationView<LeftNavItem> leftNav = new NavigationView<>(leftNavTree);
        leftNavPresenter = new NavigationPresenter<>(leftNav);
        appMainView = new AppMainView(leftNav);
        leftNavPresenter.addNavigationListener((leftNavItem) -> {
            JComponent newView = buildView(leftNavItem);
            appMainView.renderCard(leftNavItem, newView);
        });
    }

    /**
     * Constructs a new view/presenter corresponding to the given navigation item.
     * We construct a new view each time navigation happens and only have one active at a time.
     * Otherwise, the data for the entire app would load at once on initial load.
     * And, we would have to update the data for each view if the user say switched to a new profile.
     * Building a new view each time avoids this complexity and is similar to loading a new page after navigating
     * to a new URL in a web application, where most of the data is re-fetched.
     *
     * @param navItem The navigation item to build the view for.
     * @return The view corresponding to the navigation item wired up to the presenter.
     */
    private JComponent buildView(LeftNavItem navItem) {
        return switch (navItem) {
            case SELECT_PROFILE -> new PlaceholderView("Select Profile View");
            case EDIT_PROFILE -> new PlaceholderView("Edit Profile View");
            case CREATE_PROFILE -> new PlaceholderView("Create Profile View");
            case LOG_MEAL -> new PlaceholderView("Log Meals View");
            case VIEW_MULTIPLE_MEALS -> new PlaceholderView("Multiple Meals View");
            case VIEW_SINGLE_MEAL -> new PlaceholderView("Single Meal View");
            case VIEW_MEAL_STATISTICS -> new PlaceholderView("Meal Statistics View");
            case EXPLORE_INGREDIENT_SWAPS -> new PlaceholderView("Explore Swaps View");
            default -> new PlaceholderView("Unknown View");
        };
    }

    /**
     * @return The tree representing the menu for the left navigation bar.
     */
    private static INavElement<LeftNavItem> buildLeftNavTree() {
        NavSubMenu<LeftNavItem> leftNavRoot = new NavSubMenu<>(LeftNavItem.MENU_ROOT);
        leftNavRoot.addNavElement(buildLeftNavProfileSubMenu());
        leftNavRoot.addNavElement(buildLeftNavMealsSubmenu());
        leftNavRoot.addNavElement(new NavItem<>(LeftNavItem.VIEW_MEAL_STATISTICS));
        leftNavRoot.addNavElement(new NavItem<>(LeftNavItem.EXPLORE_INGREDIENT_SWAPS));
        return leftNavRoot;
    }

    /**
     * @return The profiles submenu for the left navigation bar.
     */
    private static INavElement<LeftNavItem> buildLeftNavProfileSubMenu() {
        NavSubMenu<LeftNavItem> profileSubMenu = new NavSubMenu<>(LeftNavItem.PROFILE_SUBMENU);
        profileSubMenu.addNavElement(new NavItem<>(LeftNavItem.SELECT_PROFILE));
        profileSubMenu.addNavElement(new NavItem<>(LeftNavItem.EDIT_PROFILE));
        profileSubMenu.addNavElement(new NavItem<>(LeftNavItem.CREATE_PROFILE));
        return profileSubMenu;
    }

    /**
     * @return The meals submenu for the left navigation bar.
     */
    private static INavElement<LeftNavItem> buildLeftNavMealsSubmenu() {
        NavSubMenu<LeftNavItem> mealsSubMenu = new NavSubMenu<>(LeftNavItem.MEALS_SUBMENU);
        mealsSubMenu.addNavElement(new NavItem<>(LeftNavItem.LOG_MEAL));
        mealsSubMenu.addNavElement(new NavItem<>(LeftNavItem.VIEW_MULTIPLE_MEALS));
        mealsSubMenu.addNavElement(new NavItem<>(LeftNavItem.VIEW_SINGLE_MEAL));
        return mealsSubMenu;
    }

    /**
     * Navigates to the specified view.
     * This method is useful if you want to force navigation without the user clicking on the navigation menu directly.
     * For example, after creating a new profile and hitting "Submit", the CreateProfilePresenter could call this method
     * to navigate to the LogMealView for example.
     *
     * @param leftNavItem The nav item for the view to navigate to.
     */
    public void navigateTo(LeftNavItem leftNavItem) {
        leftNavPresenter.navigateTo(leftNavItem);
    }

    /**
     * @return The singleton instance controlling the main UI for the app.
     */
    public static AppMainPresenter instance() {
        if (instance == null) {
            instance = new AppMainPresenter();
        }
        return instance;
    }

    /**
     * Runs the application and renders the UI.
     * @param args Command line arguments (unused).
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            AppMainPresenter presenter = AppMainPresenter.instance();
            // TEMP: just for testing the navigateTo method works
            presenter.navigateTo(LeftNavItem.LOG_MEAL);
        });
    }
}
