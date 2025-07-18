package shared.navigation;

import java.util.List;

/**
 * Interface representing an element in a navigation bar.
 *
 * @param <T> The type of element stored in each navigation element.
 *            Typically, an enum used to uniquely identify each navigation item.
 */
public interface INavElement<T> {
    /**
     * @return The identifier of this navigation item.
     */
    T getValue();

    /**
     * @return The child nodes for this navigation item (for nested menus).
     */
    List<INavElement<T>> getChildren();
}
