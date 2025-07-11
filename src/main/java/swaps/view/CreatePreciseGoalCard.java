package swaps.view;

import javax.swing.*;

public class CreatePreciseGoalCard extends JPanel {
    protected CreatePreciseGoalCard(int goalNumber) {
        this.add(new JLabel("Create Goal %d (Precise)".formatted(goalNumber)));
    }
}
