package swaps;


import org.junit.jupiter.api.BeforeEach;
import swaps.view.*;


import static org.mockito.Mockito.*;

class TestSwapsUI {
    private SwapsPresenter presenter;
    private SwapsView view;

    @BeforeEach
    public void beforeEach() {
         view = spy(new SwapsView());
         presenter = new SwapsPresenter(view);
    }
}
