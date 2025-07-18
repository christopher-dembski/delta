package shared.navigation;

import java.util.Collections;
import java.util.List;

/**
 * Represents a leaf element in a navigation menu (i.e. not a nested menu).
 *
 * @param <T> The type of element stored in each navigation element.
 *            Typically, an enum used to uniquely identify each navigation item.
 */
public class NavItem<T> implements INavElement<T> {
    private final T value;

    /**
     * @param value The unique identifier for this menu item.
     */
    public NavItem(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public List<INavElement<T>> getChildren() {
        return Collections.emptyList();
    }
}
