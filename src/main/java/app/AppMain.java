package app;


import javax.swing.*;
import java.awt.*;

public class AppMain extends JFrame {

    private static final String HEADER_TITLE = "Nutrition App";
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 700;

    private CardLayout mainCardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(mainCardLayout);
    private NavigationView menu;

    public AppMain(NavigationView navigationView) {
        menu = navigationView;
        this.add(menu);
        initLayout();
        initDivider();
        initCardPanels();
    }

    private void initLayout() {
        this.setTitle(HEADER_TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainCardLayout = new CardLayout();
        mainPanel = new JPanel(mainCardLayout);
    }

    private void initDivider() {
        JSplitPane divider = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menu, mainPanel);
        divider.setDividerLocation(250);
        this.add(divider);
        this.setVisible(true);
    }

    private void initCardPanels() {
        mainPanel.add(new PlaceholderView("Select Profile View"), MenuItem.SELECT_PROFILE.toString());
        mainPanel.add(new PlaceholderView("Edit Profile View"), MenuItem.EDIT_PROFILE.toString());
        mainPanel.add(new PlaceholderView("Create Profile View"), MenuItem.CREATE_PROFILE.toString());
        mainPanel.add(new PlaceholderView("Log Meals View"), MenuItem.LOG_MEAL.toString());
        mainPanel.add(new PlaceholderView("Multiple Meals View"), MenuItem.VIEW_MULTIPLE_MEALS.toString());
        mainPanel.add(new PlaceholderView("Single Meal View"), MenuItem.VIEW_SINGLE_MEAL.toString());
        mainPanel.add(new PlaceholderView("Meal Statistics View"), MenuItem.VIEW_MEAL_STATISTICS.toString());
        mainPanel.add(new PlaceholderView("Explore Swaps View"), MenuItem.EXPLORE_INGREDIENT_SWAPS.toString());
    }

    public void renderCard(MenuItem menuItem) {
        mainCardLayout.show(mainPanel, menuItem.toString());
    }
}
