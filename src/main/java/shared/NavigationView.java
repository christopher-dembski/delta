package shared;

import app.LeftNavItem;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.function.Consumer;

public class NavigationView extends JPanel {
    private final JTree leftNavTree;

    public NavigationView() {
        super(new BorderLayout());
        DefaultMutableTreeNode leftNavRoot = new DefaultMutableTreeNode(LeftNavItem.MENU_ROOT);
        DefaultMutableTreeNode profileSubMenu = buildProfilesSubMenu();
        DefaultMutableTreeNode mealsSubMenu = buildMealsSubmenu();
        leftNavRoot.add(profileSubMenu);
        leftNavRoot.add(mealsSubMenu);
        leftNavRoot.add(new DefaultMutableTreeNode(LeftNavItem.VIEW_MEAL_STATISTICS));
        leftNavRoot.add(new DefaultMutableTreeNode(LeftNavItem.EXPLORE_INGREDIENT_SWAPS));
        leftNavTree = new JTree(leftNavRoot);
        leftNavTree.setRootVisible(false);
        this.add(leftNavTree);
    }

    // TO DO: refactor to be recursive with nested list of MenuItems
    // accept this list as param to make completely decouple from main UI

    private static DefaultMutableTreeNode buildProfilesSubMenu() {
        DefaultMutableTreeNode profileSubMenu = new DefaultMutableTreeNode(LeftNavItem.PROFILE_SUBMENU);
        DefaultMutableTreeNode selectProfileMenuItem = new DefaultMutableTreeNode(LeftNavItem.SELECT_PROFILE);
        DefaultMutableTreeNode editProfileMenuItem = new DefaultMutableTreeNode(LeftNavItem.EDIT_PROFILE);
        DefaultMutableTreeNode createProfileMenuItem = new DefaultMutableTreeNode(LeftNavItem.CREATE_PROFILE);
        profileSubMenu.add(selectProfileMenuItem);
        profileSubMenu.add(editProfileMenuItem);
        profileSubMenu.add(createProfileMenuItem);
        return profileSubMenu;
    }

    private static DefaultMutableTreeNode buildMealsSubmenu() {
        DefaultMutableTreeNode mealsSubMenu = new DefaultMutableTreeNode(LeftNavItem.MEALS_SUBMENU);
        DefaultMutableTreeNode logMealMenuItem = new DefaultMutableTreeNode(LeftNavItem.LOG_MEAL);
        DefaultMutableTreeNode viewMultipleMealsMenuItem = new DefaultMutableTreeNode(LeftNavItem.VIEW_MULTIPLE_MEALS);
        DefaultMutableTreeNode viewSingleMealMenuItem = new DefaultMutableTreeNode(LeftNavItem.VIEW_SINGLE_MEAL);
        mealsSubMenu.add(logMealMenuItem);
        mealsSubMenu.add(viewMultipleMealsMenuItem);
        mealsSubMenu.add(viewSingleMealMenuItem);
        return mealsSubMenu;
    }

    public void addNavigationListener(Consumer<LeftNavItem> listener) {
        leftNavTree.addTreeSelectionListener(treeSelectionEvent -> {
            DefaultMutableTreeNode menuNode =
                    (DefaultMutableTreeNode) treeSelectionEvent.getPath().getLastPathComponent();
            LeftNavItem leftNavItem = (LeftNavItem) menuNode.getUserObject();
            listener.accept(leftNavItem);
        });
    }
}
