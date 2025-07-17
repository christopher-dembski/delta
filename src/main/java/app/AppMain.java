package app;


import javax.swing.*;
import java.awt.*;

public class AppMain extends JFrame {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(AppMain::new);
    }

    public AppMain() {
        this.setTitle("Nutrient App");
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

        mainPanel.add(createPlaceholderView("Profile View"));
        mainPanel.add(createPlaceholderView("Select Profile View"), MenuItem.SELECT_PROFILE.toString());
        mainPanel.add(createPlaceholderView("Edit Profile View"), MenuItem.EDIT_PROFILE.toString());
        mainPanel.add(createPlaceholderView("Create Profile View"), MenuItem.CREATE_PROFILE.toString());
        mainPanel.add(createPlaceholderView("Log Meals View"), MenuItem.LOG_MEAL.toString());
        mainPanel.add(createPlaceholderView("Multiple Meals View"), MenuItem.VIEW_MULTIPLE_MEALS.toString());
        mainPanel.add(createPlaceholderView("Single Meal View"), MenuItem.VIEW_SINGLE_MEAL.toString());
        mainPanel.add(createPlaceholderView("Meal Statistics View"), MenuItem.VIEW_MEAL_STATISTICS.toString());
        mainPanel.add(createPlaceholderView("Explore Swaps View"), MenuItem.EXPLORE_INGREDIENT_SWAPS.toString());

        JSplitPane divider = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menu, mainPanel);
        divider.setDividerLocation(250);
        this.add(divider);

        this.setVisible(true);
    }

    private static JPanel createPlaceholderView(String title) {
        JPanel placeholderView = new JPanel(new BorderLayout());
        placeholderView.add(new JLabel(title, JLabel.CENTER), BorderLayout.CENTER);
        return placeholderView;
    }
}
