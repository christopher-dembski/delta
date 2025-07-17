package app;

import javax.swing.*;
import java.awt.*;

public class PlaceholderView extends JPanel {

    public PlaceholderView(String title) {
        super(new BorderLayout());
        this.add(new JLabel(title, JLabel.CENTER));
    }
}
