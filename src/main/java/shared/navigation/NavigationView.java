package shared.navigation;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Enumeration;
import java.util.function.Consumer;

/**
 * A vertical navigation bar that supports nested menu items.
 *
 * @param <T> The type of value to store for each navigation item.
 *            Typically, an enum uniquely identifying each navigation item.
 */
public class NavigationView<T> extends JPanel {
    private final JTree navTree;

    /**
     * @param rootNavElement The root of the tree representing the navigation menu.
     *                       The root itself is not shown, but each of its descendants are rendered in the menu.
     */
    public NavigationView(INavElement<T> rootNavElement) {
        super(new BorderLayout());
        navTree = new JTree(buildTree(rootNavElement));
        navTree.setRootVisible(false);
        this.add(navTree);
    }

    /**
     * Builds a tree of DefaultMutableTreeNode instances recursively.
     * The DefaultMutableTreeNode class is the component representing the nested navigation elements in the UI.
     *
     * @param navElement The root of the navigation item tree to render.
     * @return The DefaultMutableTreeNode representing the nested navigation items that can be rendered in the UI.
     */
    private DefaultMutableTreeNode buildTree(INavElement<T> navElement) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(navElement.getValue());
        for (INavElement<T> childNavElement : navElement.getChildren()) {
            DefaultMutableTreeNode childNode = buildTree(childNavElement);
            node.add(childNode);
        }
        return node;
    }

    /**
     * Selects the navigation item with the specified value.
     * This method is used for forcing navigation when the user does not click the UI directly.
     * @param targetValue The value identifying the navigation item to select.
     */
    public void selectNavItem(T targetValue) {
        DefaultMutableTreeNode node = findNode(targetValue);
        if (node != null) {
            navTree.setSelectionPath(new TreePath(node.getPath()));
        }
    }

    /**
     * Performs a breadth first search to find the specified node.
     * @param targetValue The value identifying the node.
     * @return The specified node, or null if it is not found.
     */
    private DefaultMutableTreeNode findNode(T targetValue) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) navTree.getModel().getRoot();
        Enumeration<TreeNode> nodes = root.breadthFirstEnumeration();
        while (nodes.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes.nextElement();
            if (node.getUserObject().equals(targetValue)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Registers a listener to run when a navigation item is clicked.
     * @param listener The listener to run when a navigation item is clicked.
     */
    public void addNavigationListener(Consumer<T> listener) {
        navTree.addTreeSelectionListener(treeSelectionEvent -> {
            DefaultMutableTreeNode selectedNode =
                    (DefaultMutableTreeNode) treeSelectionEvent.getPath().getLastPathComponent();
            T leftNavItem = (T) selectedNode.getUserObject();
            listener.accept(leftNavItem);
        });
    }
}
