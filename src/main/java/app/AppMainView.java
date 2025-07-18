package app;


import shared.navigation.NavigationView;

import javax.swing.*;
import java.awt.*;

/**
 * The component for the main UI window of the app. All other components within the app are rendered within it.
 */
public class AppMainView extends JFrame {

    private static final String HEADER_TITLE = "Nutrition App";
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 700;
    private static final int LEFT_NAV_INITIAL_WIDTH = 200;

    private CardLayout mainWindowCardLayout = new CardLayout();
    private JPanel mainWindow = new JPanel(mainWindowCardLayout);
    private NavigationView<LeftNavItem> leftNavView;

    /**
     * @param leftNavView The view for the left navigation menu.
     */
    public AppMainView(NavigationView<LeftNavItem> leftNavView) {
        initLayout();
        initLeftNav(leftNavView);
    }

    /**
     * Initializes the left navigation menu.
     * Helper method to be called in the constructor.
     * @param leftNavView The view for the left navigation menu.
     */
    private void initLeftNav(NavigationView<LeftNavItem> leftNavView) {
        this.leftNavView = leftNavView;
        this.add(this.leftNavView);
        JSplitPane divider = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftNavView, mainWindow);
        divider.setDividerLocation(LEFT_NAV_INITIAL_WIDTH);
        this.add(divider);
    }

    /**
     * Initializes the main layout of the app window.
     * Helper method to be called in the constructor.
     */
    private void initLayout() {
        this.setTitle(HEADER_TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainWindowCardLayout = new CardLayout();
        mainWindow = new JPanel(mainWindowCardLayout);
        this.setVisible(true);
    }

    /**
     * Renders the view for the specified navigation item.
     * @param leftNavItem The navigation menu item to render the view for.
     * @param viewToRender The view to render.
     */
    public void renderCard(LeftNavItem leftNavItem, JComponent viewToRender) {
        // remove the old view, so we don't have a memory leak where views keep getting added but not removed
        // unpredictable results can occur if there are multiple views added to the main window with the same key
        mainWindow.removeAll();
        mainWindow.add(viewToRender, leftNavView.toString());
        mainWindowCardLayout.show(mainWindow, leftNavItem.toString());
        mainWindow.revalidate();
        mainWindow.repaint();
    }
}
