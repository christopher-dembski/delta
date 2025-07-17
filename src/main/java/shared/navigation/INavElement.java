package shared.navigation;

import java.util.List;

public interface INavElement<T> {
    T getValue();
    List<INavElement<T>> getChildren();
}
