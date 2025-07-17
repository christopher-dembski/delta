package app;


import shared.NavigationView;

import javax.swing.*;
import java.awt.*;

public class AppMainView extends JFrame {

    private static final String HEADER_TITLE = "Nutrition App";
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 700;

    private CardLayout mainWindowCardLayout = new CardLayout();
    private JPanel mainWindow = new JPanel(mainWindowCardLayout);
    private NavigationView leftNavView;

    public AppMainView(NavigationView leftNavView) {
        initLeftNav(leftNavView);
        initLayout();
        initDivider();
        initMainWindowCards();
    }

    private void initLeftNav(NavigationView leftNavView) {
        this.leftNavView = leftNavView;
        this.add(this.leftNavView);
    }

    private void initLayout() {
        this.setTitle(HEADER_TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainWindowCardLayout = new CardLayout();
        mainWindow = new JPanel(mainWindowCardLayout);
    }

    private void initDivider() {
        JSplitPane divider = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftNavView, mainWindow);
        divider.setDividerLocation(250);
        this.add(divider);
        this.setVisible(true);
    }

    private void initMainWindowCards() {
        mainWindow.add(new PlaceholderView("Select Profile View"), LeftNavItem.SELECT_PROFILE.toString());
        mainWindow.add(new PlaceholderView("Edit Profile View"), LeftNavItem.EDIT_PROFILE.toString());
        mainWindow.add(new PlaceholderView("Create Profile View"), LeftNavItem.CREATE_PROFILE.toString());
        mainWindow.add(new PlaceholderView("Log Meals View"), LeftNavItem.LOG_MEAL.toString());
        mainWindow.add(new PlaceholderView("Multiple Meals View"), LeftNavItem.VIEW_MULTIPLE_MEALS.toString());
        mainWindow.add(new PlaceholderView("Single Meal View"), LeftNavItem.VIEW_SINGLE_MEAL.toString());
        mainWindow.add(new PlaceholderView("Meal Statistics View"), LeftNavItem.VIEW_MEAL_STATISTICS.toString());
        mainWindow.add(new PlaceholderView("Explore Swaps View"), LeftNavItem.EXPLORE_INGREDIENT_SWAPS.toString());
    }

    public void renderCard(LeftNavItem leftNavItem) {
        mainWindowCardLayout.show(mainWindow, leftNavItem.toString());
    }
}
