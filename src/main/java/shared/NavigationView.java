package shared;

import app.MenuItem;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.function.Consumer;

public class NavigationView extends JPanel {
    private final JTree menuTree;

    public NavigationView() {
        super(new BorderLayout());
        DefaultMutableTreeNode menuRoot = new DefaultMutableTreeNode(app.MenuItem.MENU_ROOT);
        DefaultMutableTreeNode profileSubMenu = buildProfilesSubMenu();
        DefaultMutableTreeNode mealsSubMenu = buildMealsSubmenu();
        menuRoot.add(profileSubMenu);
        menuRoot.add(mealsSubMenu);
        menuRoot.add(new DefaultMutableTreeNode(app.MenuItem.VIEW_MEAL_STATISTICS));
        menuRoot.add(new DefaultMutableTreeNode(app.MenuItem.EXPLORE_INGREDIENT_SWAPS));
        menuTree = new JTree(menuRoot);
        menuTree.setRootVisible(false);
        this.add(menuTree);
    }

    // TO DO: refactor to be recursive with nested list of MenuItems
    // accept this list as param to make completely decouple from main UI

    private static DefaultMutableTreeNode buildProfilesSubMenu() {
        DefaultMutableTreeNode profileSubMenu = new DefaultMutableTreeNode(app.MenuItem.PROFILE_SUBMENU);
        DefaultMutableTreeNode selectProfileMenuItem = new DefaultMutableTreeNode(app.MenuItem.SELECT_PROFILE);
        DefaultMutableTreeNode editProfileMenuItem = new DefaultMutableTreeNode(app.MenuItem.EDIT_PROFILE);
        DefaultMutableTreeNode createProfileMenuItem = new DefaultMutableTreeNode(app.MenuItem.CREATE_PROFILE);
        profileSubMenu.add(selectProfileMenuItem);
        profileSubMenu.add(editProfileMenuItem);
        profileSubMenu.add(createProfileMenuItem);
        return profileSubMenu;
    }

    private static DefaultMutableTreeNode buildMealsSubmenu() {
        DefaultMutableTreeNode mealsSubMenu = new DefaultMutableTreeNode(app.MenuItem.MEALS_SUBMENU);
        DefaultMutableTreeNode logMealMenuItem = new DefaultMutableTreeNode(app.MenuItem.LOG_MEAL);
        DefaultMutableTreeNode viewMultipleMealsMenuItem = new DefaultMutableTreeNode(app.MenuItem.VIEW_MULTIPLE_MEALS);
        DefaultMutableTreeNode viewSingleMealMenuItem = new DefaultMutableTreeNode(app.MenuItem.VIEW_SINGLE_MEAL);
        mealsSubMenu.add(logMealMenuItem);
        mealsSubMenu.add(viewMultipleMealsMenuItem);
        mealsSubMenu.add(viewSingleMealMenuItem);
        return mealsSubMenu;
    }

    public void addNavigationListener(Consumer<app.MenuItem> listener) {
        menuTree.addTreeSelectionListener(treeSelectionEvent -> {
            DefaultMutableTreeNode menuNode =
                    (DefaultMutableTreeNode) treeSelectionEvent.getPath().getLastPathComponent();
            app.MenuItem menuItem = (MenuItem) menuNode.getUserObject();
            listener.accept(menuItem);
        });
    }
}
