package statistics;

import javax.swing.*;
import java.awt.*;

/**
 * Quick playground to compare UI panels from different presenters.
 */
public class UIPlayground {
    
    /**
     * Creates error panel similar to SwapComparisonPresenter style.
     */
    public static JPanel createErrorPanel1(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel errorLabel = new JLabel("<html><div style='text-align: center; color: red;'>" + 
                                       message + "</div></html>", JLabel.CENTER);
        panel.add(errorLabel, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createTitledBorder("Error Panel 1 (SwapComparison Style)"));
        return panel;
    }
    
    /**
     * Creates error panel similar to NutrientBreakdownPresenter style.
     */
    public static JPanel createErrorPanel2(String errorMessage) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Error Panel 2 (NutrientBreakdown Style)"));
        
        JLabel errorLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h2>❌ Error</h2>" +
                "<p>" + errorMessage + "</p>" +
                "</div></html>");
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(errorLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates a proposed unified error panel.
     */
    public static JPanel createUnifiedErrorPanel(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Unified Error Panel"));
        
        JLabel errorLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h2 style='color: #d32f2f;'>❌ Error</h2>" +
                "<p style='color: #666; margin: 10px;'>" + message + "</p>" +
                "<p style='color: #999; font-size: 12px;'><i>Please try again or contact support</i></p>" +
                "</div></html>");
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(errorLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates a proposed unified "no data" panel.
     */
    public static JPanel createUnifiedNoDataPanel(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Unified No Data Panel"));
        
        JLabel noDataLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h2 style='color: #1976d2;'>📊 No Data Available</h2>" +
                "<p style='color: #666; margin: 10px;'>" + message + "</p>" +
                "<p style='color: #999; font-size: 12px;'><i>Try selecting a different date range or adding meal data</i></p>" +
                "</div></html>");
        noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noDataLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(noDataLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Main method to display all panels for comparison.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("UI Panel Comparison Playground");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new GridLayout(2, 3, 10, 10));
            
            // Row 1: Error panels
            frame.add(createErrorPanel1("Failed to load data from database"));
            frame.add(createErrorPanel2("Network connection timeout occurred"));
            frame.add(createUnifiedErrorPanel("Unable to process your request"));
            
            // Row 2: Info panels
            frame.add(createInfoPanel("Current Style", "This is how info panels look now"));
            frame.add(createNoDataPanel("No meals found for selected date range"));
            frame.add(createUnifiedNoDataPanel("No meal data available for the specified period"));
            
            frame.setSize(1200, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
            System.out.println("🎨 UI Playground launched!");
            System.out.println("💡 Compare the different panel styles and see which you prefer.");
        });
    }
    
    /**
     * Example info panel for comparison.
     */
    private static JPanel createInfoPanel(String title, String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        
        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h3 style='color: #388e3c;'>ℹ️ Information</h3>" +
                "<p>" + message + "</p>" +
                "</div></html>");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(infoLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Example no data panel from current presenters.
     */
    private static JPanel createNoDataPanel(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Current No Data Panel"));
        
        JLabel noDataLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h2>📊 No Data Available</h2>" +
                "<p>" + message + "</p>" +
                "</div></html>");
        noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(noDataLabel, BorderLayout.CENTER);
        
        return panel;
    }
} 