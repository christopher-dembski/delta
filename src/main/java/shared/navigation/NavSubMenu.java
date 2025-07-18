package shared.navigation;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a non-leaf element in a navigation menu (i.e. a nested menu).
 *
 * @param <T> The type of element stored in each navigation element.
 *            Typically, an enum used to uniquely identify each navigation item.
 */
public class NavSubMenu<T> implements INavElement<T> {
    private T value;
    private List<INavElement<T>> children;

    /**
     * @param value The unique identifier of the navigation item.
     * @param children The children of the navigation item, representing options within the sub-menu.
     */
    public NavSubMenu(T value, List<INavElement<T>> children) {
        this.value = value;
        this.children = children;
    }

    /**
     * Constructs a navigation sub-menu without any children.
     * @param value The unique identifier of the navigation item.
     */
    public NavSubMenu(T value) {
        this(value, new ArrayList<>());
    }

    /**
     * Adds a child to the sub-menu.
     * @param navElement The navigation element to add as a child.
     */
    public void addNavElement(INavElement<T> navElement) {
        this.children.add(navElement);
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public List<INavElement<T>> getChildren() {
        return children;
    }
}
