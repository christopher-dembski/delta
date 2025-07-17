package shared.navigation;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Enumeration;
import java.util.function.Consumer;


public class NavigationView<T> extends JPanel {
    private final JTree navTree;

    public NavigationView(INavElement<T> navElement) {
        super(new BorderLayout());
        navTree = new JTree(buildTree(navElement));
        navTree.setRootVisible(false);
        this.add(navTree);
    }

    private DefaultMutableTreeNode buildTree(INavElement<T> navElement) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(navElement.getValue());
        for (INavElement<T> childNavElement: navElement.getChildren()) {
            DefaultMutableTreeNode childNode = buildTree(childNavElement);
            node.add(childNode);
        }
        return node;
    }

    public void selectNavItem(T targetValue) {
        DefaultMutableTreeNode node = findNode(targetValue);
        if (node != null) {
            navTree.setSelectionPath(new TreePath(node.getPath()));
        }
    }

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

    public void addNavigationListener(Consumer<T> listener) {
        navTree.addTreeSelectionListener(treeSelectionEvent -> {
            DefaultMutableTreeNode menuNode =
                    (DefaultMutableTreeNode) treeSelectionEvent.getPath().getLastPathComponent();
            T leftNavItem = (T) menuNode.getUserObject();
            listener.accept(leftNavItem);
        });
    }
}
