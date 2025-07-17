package app;


import javax.swing.*;
import java.awt.*;

public class AppMain extends JFrame {

    private static final String TITLE = "Nutrition App";

    public AppMain() {
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);

        CardLayout mainCardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(mainCardLayout);

        NavigationView menu = new NavigationView();
        NavigationPresenter navigationPresenter = new NavigationPresenter(menu);
        navigationPresenter.addNavigationListener(menuItem -> {
            mainCardLayout.show(mainPanel, menuItem.toString());
        });
        JScrollPane menuScrolling = new JScrollPane(menu);
        this.add(menuScrolling);

        mainPanel.add(new PlaceholderView("Select Profile View"), MenuItem.SELECT_PROFILE.toString());
        mainPanel.add(new PlaceholderView("Edit Profile View"), MenuItem.EDIT_PROFILE.toString());
        mainPanel.add(new PlaceholderView("Create Profile View"), MenuItem.CREATE_PROFILE.toString());
        mainPanel.add(new PlaceholderView("Log Meals View"), MenuItem.LOG_MEAL.toString());
        mainPanel.add(new PlaceholderView("Multiple Meals View"), MenuItem.VIEW_MULTIPLE_MEALS.toString());
        mainPanel.add(new PlaceholderView("Single Meal View"), MenuItem.VIEW_SINGLE_MEAL.toString());
        mainPanel.add(new PlaceholderView("Meal Statistics View"), MenuItem.VIEW_MEAL_STATISTICS.toString());
        mainPanel.add(new PlaceholderView("Explore Swaps View"), MenuItem.EXPLORE_INGREDIENT_SWAPS.toString());

        JSplitPane divider = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menu, mainPanel);
        divider.setDividerLocation(250);
        this.add(divider);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(AppMain::new);
    }
}
