package app;

import javax.swing.*;
import java.awt.*;

/**
 * Placeholder view to be replaced with actual views as they are built.
 */
public class PlaceholderView extends JPanel {

    /**
     * @param title The String to render in the placeholder view.
     */
    public PlaceholderView(String title) {
        super(new BorderLayout());
        this.add(new JLabel(title, JLabel.CENTER));
    }
}
