package app;

import shared.navigation.*;


public class AppMainPresenter {
    private static AppMainPresenter instance;

    private AppMainView appMainView;
    private NavigationPresenter leftNavPresenter;

    private AppMainPresenter() {
        initLeftNav();
        initCards();
    }

    private void initLeftNav() {
        INavElement<LeftNavItem> leftNavTree = buildLeftNavTree();
        NavigationView<LeftNavItem> leftNav = new NavigationView<>(leftNavTree);
        leftNavPresenter = new NavigationPresenter<>(leftNav);
        appMainView = new AppMainView(leftNav);
        leftNavPresenter.addNavigationListener((leftNavItem) -> {
            appMainView.renderCard((LeftNavItem) leftNavItem);
        });
    }

    private void initCards() {
        appMainView.addCard(new PlaceholderView("Select Profile View"), LeftNavItem.SELECT_PROFILE);
        appMainView.addCard(new PlaceholderView("Edit Profile View"), LeftNavItem.EDIT_PROFILE);
        appMainView.addCard(new PlaceholderView("Create Profile View"), LeftNavItem.CREATE_PROFILE);
        appMainView.addCard(new PlaceholderView("Log Meals View"), LeftNavItem.LOG_MEAL);
        appMainView.addCard(new PlaceholderView("Multiple Meals View"), LeftNavItem.VIEW_MULTIPLE_MEALS);
        appMainView.addCard(new PlaceholderView("Single Meal View"), LeftNavItem.VIEW_SINGLE_MEAL);
        appMainView.addCard(new PlaceholderView("Meal Statistics View"), LeftNavItem.VIEW_MEAL_STATISTICS);
        appMainView.addCard(new PlaceholderView("Explore Swaps View"), LeftNavItem.EXPLORE_INGREDIENT_SWAPS);
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
            AppMainPresenter.instance();
            // TEMP: just for testing the navigateTo method works
            instance().navigateTo(LeftNavItem.LOG_MEAL);
        });
    }
}
