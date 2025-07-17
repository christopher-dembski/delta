package shared.navigation;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.function.Consumer;


public class NavigationView<T> extends JPanel {
    private final JTree leftNavTree;

    public NavigationView(INavElement<T> navElement) {
        super(new BorderLayout());
        leftNavTree = new JTree(buildTree(navElement));
        leftNavTree.setRootVisible(false);
        this.add(leftNavTree);
    }

    private DefaultMutableTreeNode buildTree(INavElement<T> navElement) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(navElement.getValue());
        for (INavElement<T> childNavElement: navElement.getChildren()) {
            DefaultMutableTreeNode childNode = buildTree(childNavElement);
            node.add(childNode);
        }
        return node;
    }

    public void addNavigationListener(Consumer<T> listener) {
        leftNavTree.addTreeSelectionListener(treeSelectionEvent -> {
            DefaultMutableTreeNode menuNode =
                    (DefaultMutableTreeNode) treeSelectionEvent.getPath().getLastPathComponent();
            T leftNavItem = (T) menuNode.getUserObject();
            listener.accept(leftNavItem);
        });
    }
}
