package app;


public enum MenuItem {
    // root
    MENU_ROOT("Menu Root - Not Visible"),
    // profile submenu
    PROFILE_SUBMENU("Profiles"),
    SELECT_PROFILE("Select Profile"),
    EDIT_PROFILE("Edit Profile"),
    CREATE_PROFILE("Create Profile"),
    // meals submenu
    MEALS_SUBMENU("Meals"),
    LOG_MEAL("Log meal"),
    VIEW_MULTIPLE_MEALS("View Multiple Meals"),
    VIEW_SINGLE_MEAL("View Single Meal"),
    // meal statistics
    VIEW_MEAL_STATISTICS("View Meal Stats"),
    // swaps
    EXPLORE_INGREDIENT_SWAPS("Explore Swaps");

    private final String label;

    MenuItem(String label) {
        this.label= label;
    }

    @Override
    public String toString() {
        return label;
    }
}
