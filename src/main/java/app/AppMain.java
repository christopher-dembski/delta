package app;


import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
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

        JTree menu = buildMenu();
        menu.addTreeSelectionListener(e -> {
            String menuItemId = e.getPath().getLastPathComponent().toString();
            mainCardLayout.show(mainPanel, menuItemId);
        });
        JScrollPane menuScrolling = new JScrollPane(menu);
        this.add(menuScrolling);

        mainPanel.add(createPlaceholderView("Profile View"), "Profile");
        mainPanel.add(createPlaceholderView("Select Profile View"), "Select Profile");
        mainPanel.add(createPlaceholderView("Edit Profile View"), "Edit Profile");
        mainPanel.add(createPlaceholderView("Create Profile View"), "Create Profile");

        mainPanel.add(createPlaceholderView("Meals View"), "Meals");

        mainPanel.add(createPlaceholderView("Swaps View"), "Swaps");

        JSplitPane divider = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menu, mainPanel);
        divider.setDividerLocation(200);
        this.add(divider);

        this.setVisible(true);
    }

    private static JTree buildMenu() {
        DefaultMutableTreeNode menuRoot = new DefaultMutableTreeNode("Menu");

        DefaultMutableTreeNode profileSubMenu = getProfileSubMenu();
        DefaultMutableTreeNode mealsSubmenu = new DefaultMutableTreeNode("Meals");
        DefaultMutableTreeNode swaps = new DefaultMutableTreeNode("Swaps");

        menuRoot.add(profileSubMenu);
        menuRoot.add(mealsSubmenu);
        menuRoot.add(swaps);

        JTree menuTree = new JTree(menuRoot);
        menuTree.setRootVisible(false);

        return menuTree;
    }

    private JPanel navigate(String menuItemId) {
        return switch (menuItemId) {
            case "Select Profile" -> createPlaceholderView("Select Profile View");
            case "Edit Profile" -> createPlaceholderView("Edit Profile View");
            case "Create Profile" -> createPlaceholderView("Create Profile View");
            case "Meals" -> createPlaceholderView("Meals View");
            case "Swaps" -> createPlaceholderView("Swaps View");
            default -> null;
        };
    }

    private static DefaultMutableTreeNode getProfileSubMenu() {
        DefaultMutableTreeNode profileSubMenu = new DefaultMutableTreeNode("Profile");
        DefaultMutableTreeNode selectProfileMenuItem = new DefaultMutableTreeNode("Select Profile");
        DefaultMutableTreeNode editProfileMenuItem = new DefaultMutableTreeNode("Edit Profile");
        DefaultMutableTreeNode createProfileMenuItem = new DefaultMutableTreeNode("Create Profile");
        profileSubMenu.add(selectProfileMenuItem);
        profileSubMenu.add(editProfileMenuItem);
        profileSubMenu.add(createProfileMenuItem);
        return profileSubMenu;
    }

    private static JPanel createPlaceholderView(String title) {
        JPanel placeholderView = new JPanel(new BorderLayout());
        placeholderView.add(new JLabel(title, JLabel.CENTER), BorderLayout.CENTER);
        return placeholderView;
    }
}
