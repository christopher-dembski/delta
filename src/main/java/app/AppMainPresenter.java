package app;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import meals.ui.LogMealPresenter;
import meals.ui.LogMealView;
import meals.ui.MealListPresenter;
import meals.ui.MealListView;
import profile.presenter.EditProfilePresenter;
import profile.presenter.ProfileSelectorPresenter;
import profile.presenter.UserSignUpPresenter;
import profile.view.EditProfileView;
import profile.view.SignUpView;
import profile.view.SplashView;
import shared.navigation.INavElement;
import shared.navigation.NavItem;
import shared.navigation.NavSubMenu;
import shared.navigation.NavigationPresenter;
import shared.navigation.NavigationView;
import swaps.ui.SwapsPresenter;
import swaps.ui.SwapsView;
import swaps.ui.goals.CreateGoalsView;

/**
 * Presenter for the main UI of the app.
 */
public class AppMainPresenter {
    private static AppMainPresenter instance;

    private final AppMainView appMainView;
    private final NavigationPresenter<LeftNavItem> leftNavPresenter;

    private AppMainPresenter() {
        // navigation tree (menu structure)
        INavElement<LeftNavItem> leftNavTree = buildLeftNavTree();

        // Wire view - presenter
        NavigationView<LeftNavItem> leftNav = new NavigationView<>(leftNavTree);
        leftNavPresenter = new NavigationPresenter<>(leftNav);
        appMainView = new AppMainView(leftNav);

        // listen for navigation events
        leftNavPresenter.addNavigationListener(leftNavItem -> {
            boolean sessionActive = shared.ServiceFactory.getProfileService()
                    .getCurrentSession()
                    .isPresent();

            // block navigation to meal-related tabs when no profile is selected
            if (!sessionActive && isMealTab(leftNavItem)) {
                JOptionPane.showMessageDialog(appMainView,
                        "Please select a profile to access this feature.",
                        "No Active Profile",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // else build and render the requested view
            JComponent newView = buildView(leftNavItem);
            if (newView != null) {
                appMainView.renderCard(leftNavItem, newView);
            }
        });
    }

    /** @return {@code true} if the nav item needs an active profile/session. */
    private boolean isMealTab(LeftNavItem item) {
        return switch (item) {
            case LOG_MEAL,
                    VIEW_MULTIPLE_MEALS,
                    VIEW_MEAL_STATISTICS,
                    EXPLORE_INGREDIENT_SWAPS,
                    VIEW_SINGLE_MEAL ->
                true;
            default -> false;
        };
    }

    /**
     * Constructs a new view/presenter corresponding to the given navigation item.
     * We construct a new view each time navigation happens and only have one active
     * at a time.
     * Otherwise, the data for the entire app would load at once on initial load.
     * And, we would have to update the data for each view if the user say switched
     * to a new profile.
     * Building a new view each time avoids this complexity and is similar to
     * loading a new page after navigating
     * to a new URL in a web application, where most of the data is re-fetched.
     *
     * @param navItem The navigation item to build the view for.
     * @return The view corresponding to the navigation item wired up to the
     *         presenter,
     *         or {@code null} if a submenu heading is clicked or the specified view
     *         does not exist.
     */
    private JComponent buildView(LeftNavItem navItem) {
        boolean sessionActive = shared.ServiceFactory.getProfileService()
                .getCurrentSession()
                .isPresent();

        return switch (navItem) {

            case SELECT_PROFILE -> {
                try {
                    SplashView view = new SplashView();
                    new ProfileSelectorPresenter(view, shared.ServiceFactory.getProfileService())
                            .initialize();
                    yield view;
                } catch (Exception e) {
                    System.err.println("Failed to initialize profile selector: " + e.getMessage());
                    yield new PlaceholderView("Error loading Profile Selection");
                }
            }
            case EDIT_PROFILE -> {
                try {
                    EditProfileView view = new EditProfileView();
                    new EditProfilePresenter(view, shared.ServiceFactory.getProfileService())
                            .initialize();
                    yield view;
                } catch (Exception e) {
                    System.err.println("Failed to initialize edit profile: " + e.getMessage());
                    yield new PlaceholderView("Error loading Edit Profile form");
                }
            }
            case CREATE_PROFILE -> {
                try {
                    SignUpView view = new SignUpView();
                    new UserSignUpPresenter(view, shared.ServiceFactory.getProfileService())
                            .initialize();
                    yield view;
                } catch (Exception e) {
                    System.err.println("Failed to initialize sign up panel: " + e.getMessage());
                    yield new PlaceholderView("Error loading Create Profile form");
                }
            }

            case LOG_MEAL,
                    VIEW_MULTIPLE_MEALS,
                    VIEW_MEAL_STATISTICS,
                    EXPLORE_INGREDIENT_SWAPS -> {
                if (!sessionActive) {
                    yield new PlaceholderView("Please select a profile to access this feature.");
                } else {
                    yield switch (navItem) {
                        case LOG_MEAL -> initializeLogMealView();
                        case VIEW_MULTIPLE_MEALS -> initializeMealListView();
                        case VIEW_MEAL_STATISTICS -> new PlaceholderView("Meal Statistics View");
                        case EXPLORE_INGREDIENT_SWAPS -> initializeSwapsView();
                        // This inner switch is exhaustive, VIEW_SINGLE_MEAL is unreachable here.
                        default -> null;
                    };
                }
            }

            default -> null;
        };
    }

    /**
     * Creates the view to log meals initialized with the corresponding presenter.
     * 
     * @return The view enabling the user to log meals through the UI.
     */
    private LogMealView initializeLogMealView() {
        LogMealView logMealView = new LogMealView();
        new LogMealPresenter(logMealView); // register event listeners
        return logMealView;
    }

    private SwapsView initializeSwapsView() {
        SwapsView swapsView = new SwapsView(new CreateGoalsView());
        new SwapsPresenter(swapsView); // register action listeners
        return swapsView;
    }

    private MealListView initializeMealListView() {
        MealListView mealListView = new MealListView();
        new MealListPresenter(mealListView);
        return mealListView;
    }

    /** @return The tree representing the menu for the left navigation bar. */
    private static INavElement<LeftNavItem> buildLeftNavTree() {
        NavSubMenu<LeftNavItem> leftNavRoot = new NavSubMenu<>(LeftNavItem.MENU_ROOT);
        leftNavRoot.addNavElement(buildLeftNavProfileSubMenu());
        leftNavRoot.addNavElement(buildLeftNavMealsSubmenu());
        leftNavRoot.addNavElement(new NavItem<>(LeftNavItem.VIEW_MEAL_STATISTICS));
        leftNavRoot.addNavElement(new NavItem<>(LeftNavItem.EXPLORE_INGREDIENT_SWAPS));
        return leftNavRoot;
    }

    /** @return The profiles submenu for the left navigation bar. */
    private static INavElement<LeftNavItem> buildLeftNavProfileSubMenu() {
        NavSubMenu<LeftNavItem> profileSubMenu = new NavSubMenu<>(LeftNavItem.PROFILE_SUBMENU);
        profileSubMenu.addNavElement(new NavItem<>(LeftNavItem.SELECT_PROFILE));
        profileSubMenu.addNavElement(new NavItem<>(LeftNavItem.EDIT_PROFILE));
        profileSubMenu.addNavElement(new NavItem<>(LeftNavItem.CREATE_PROFILE));
        return profileSubMenu;
    }

    /** @return The meals submenu for the left navigation bar. */
    private static INavElement<LeftNavItem> buildLeftNavMealsSubmenu() {
        NavSubMenu<LeftNavItem> mealsSubMenu = new NavSubMenu<>(LeftNavItem.MEALS_SUBMENU);
        mealsSubMenu.addNavElement(new NavItem<>(LeftNavItem.LOG_MEAL));
        mealsSubMenu.addNavElement(new NavItem<>(LeftNavItem.VIEW_MULTIPLE_MEALS));
        mealsSubMenu.addNavElement(new NavItem<>(LeftNavItem.VIEW_SINGLE_MEAL));
        return mealsSubMenu;
    }

    /**
     * Navigates to the specified view programmatically.
     * For example, after creating a new profile, the presenter could call
     * {@code navigateTo(LeftNavItem.LOG_MEAL);} to switch views.
     *
     * @param leftNavItem The nav item for the view to navigate to.
     */
    public void navigateTo(LeftNavItem leftNavItem) {
        leftNavPresenter.navigateTo(leftNavItem);
    }

    /** @return The singleton instance controlling the main UI for the app. */
    public static AppMainPresenter instance() {
        if (instance == null) {
            instance = new AppMainPresenter();
        }
        return instance;
    }

    /**
     * Runs the application and renders the UI.
     * 
     * @param args Command line arguments (unused).
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            AppMainPresenter presenter = AppMainPresenter.instance();
            // Start with profile selection
            presenter.navigateTo(LeftNavItem.SELECT_PROFILE);
        });
    }
}
