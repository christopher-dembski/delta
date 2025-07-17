package app;

import shared.navigation.*;

import javax.swing.*;


public class AppMainPresenter {
    private static AppMainPresenter instance;

    private AppMainView appMainView;
    private NavigationPresenter leftNavPresenter;

    private AppMainPresenter() {
        INavElement<LeftNavItem> leftNavTree = buildLeftNavTree();
        NavigationView<LeftNavItem> leftNav = new NavigationView<>(leftNavTree);
        leftNavPresenter = new NavigationPresenter<>(leftNav);
        appMainView = new AppMainView(leftNav);
        leftNavPresenter.addNavigationListener((leftNavItem) -> {
            JComponent newView = buildView((LeftNavItem) leftNavItem);
            appMainView.renderCard((LeftNavItem) leftNavItem, newView);
        });
    }

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

    private static INavElement<LeftNavItem> buildLeftNavTree() {
        NavSubMenu<LeftNavItem> leftNavRoot = new NavSubMenu<>(LeftNavItem.MENU_ROOT);
        leftNavRoot.addNavElement(buildLeftNavProfileSubMenu());
        leftNavRoot.addNavElement(buildLeftNavMealsSubmenu());
        leftNavRoot.addNavElement(new NavItem<>(LeftNavItem.VIEW_MEAL_STATISTICS));
        leftNavRoot.addNavElement(new NavItem<>(LeftNavItem.EXPLORE_INGREDIENT_SWAPS));
        return leftNavRoot;
    }

    private static INavElement<LeftNavItem> buildLeftNavProfileSubMenu() {
        NavSubMenu<LeftNavItem> profileSubMenu = new NavSubMenu<>(LeftNavItem.CREATE_PROFILE);
        profileSubMenu.addNavElement(new NavItem<>(LeftNavItem.SELECT_PROFILE));
        profileSubMenu.addNavElement(new NavItem<>(LeftNavItem.EDIT_PROFILE));
        profileSubMenu.addNavElement(new NavItem<>(LeftNavItem.CREATE_PROFILE));
        return profileSubMenu;
    }

    private static INavElement<LeftNavItem> buildLeftNavMealsSubmenu() {
        NavSubMenu<LeftNavItem> mealsSubMenu = new NavSubMenu<>(LeftNavItem.MEALS_SUBMENU);
        mealsSubMenu.addNavElement(new NavItem<>(LeftNavItem.LOG_MEAL));
        mealsSubMenu.addNavElement(new NavItem<>(LeftNavItem.VIEW_MULTIPLE_MEALS));
        mealsSubMenu.addNavElement(new NavItem<>(LeftNavItem.VIEW_SINGLE_MEAL));
        return mealsSubMenu;
    }

    public void navigateTo(LeftNavItem leftNavItem) {
        leftNavPresenter.navigateTo(leftNavItem);
    }

    public static AppMainPresenter instance() {
        if (instance == null) {
            instance = new AppMainPresenter();
        }
        return instance;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            AppMainPresenter presenter = AppMainPresenter.instance();
            // TEMP: just for testing the navigateTo method works
            presenter.navigateTo(LeftNavItem.LOG_MEAL);
        });
    }
}
