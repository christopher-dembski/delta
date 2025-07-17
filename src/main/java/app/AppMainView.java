package app;


import shared.navigation.NavigationView;

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

    public void addCard(JComponent view, LeftNavItem navItem) {
        mainWindow.add(view, navItem.toString());
    }

    public void renderCard(LeftNavItem leftNavItem) {
        mainWindowCardLayout.show(mainWindow, leftNavItem.toString());
    }
}
