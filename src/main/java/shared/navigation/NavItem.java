package shared.navigation;

import java.util.Collections;
import java.util.List;

public class NavItem<T> implements INavElement<T> {
    private T value;

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
