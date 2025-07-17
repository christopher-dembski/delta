package shared.navigation;

import java.util.ArrayList;
import java.util.List;

public class NavSubMenu<T> implements INavElement<T> {
    private T value;
    private List<INavElement<T>> children;

    public NavSubMenu(T value, List<INavElement<T>> children) {
        this.value = value;
        this.children = children;
    }

    public NavSubMenu(T value) {
        this(value, new ArrayList<>());
    }

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
