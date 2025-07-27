package statistics.view;

import statistics.model.NutrientSummary;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Concrete view implementation for nutrient breakdown.
 * Contains all UI creation and layout logic.
 */
public class NutrientBreakdownView extends JPanel implements INutrientBreakdownView {
    
    private JPanel chartDisplayPanel;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    // Callbacks set by presenter
    private GenerateChartCallback generateChartCallback;
    private QuickDateCallback quickDateCallback;
    
    public NutrientBreakdownView() {
        initializeUI();
    }
    
    /**
     * Initialize the complete UI layout.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Nutrient Breakdown Analysis"));
        
        // Create date selection panel at the top
        JPanel dateSelectionPanel = createDateSelectionPanel();
        add(dateSelectionPanel, BorderLayout.NORTH);
        
        // Create chart display area
        chartDisplayPanel = new JPanel(new BorderLayout());
        chartDisplayPanel.setPreferredSize(new Dimension(800, 600));
        chartDisplayPanel.setBorder(BorderFactory.createTitledBorder("Chart Display"));
        
        // Show initial message
        showInitialMessage();
        
        add(chartDisplayPanel, BorderLayout.CENTER);
    }
    
    /**
     * Creates the date selection panel with calendar controls.
     */
    private JPanel createDateSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Date Range Selection"));
        
        // Main row with date controls
        JPanel mainRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        // Start Date
        JLabel startLabel = new JLabel("From:");
        startLabel.setFont(startLabel.getFont().deriveFont(Font.BOLD, 11f));
        
        // Default to 7 days ago
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        Date defaultStartDate = cal.getTime();
        
        startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd"));
        startDateSpinner.setValue(defaultStartDate);
        startDateSpinner.setPreferredSize(new Dimension(110, 25));
        
        // End Date  
        JLabel endLabel = new JLabel("To:");
        endLabel.setFont(endLabel.getFont().deriveFont(Font.BOLD, 11f));
        
        Date defaultEndDate = new Date(); // Today
        
        endDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd"));
        endDateSpinner.setValue(defaultEndDate);
        endDateSpinner.setPreferredSize(new Dimension(110, 25));
        
        // Generate Button
        JButton generateButton = new JButton("Generate");
        generateButton.setFont(generateButton.getFont().deriveFont(Font.BOLD, 11f));
        generateButton.setBackground(new Color(70, 130, 180));
        generateButton.setForeground(Color.WHITE);
        generateButton.setPreferredSize(new Dimension(90, 25));
        
        // Quick date buttons
        JButton todayButton = createQuickDateButton("Today", 0);
        JButton last7DaysButton = createQuickDateButton("Last 7 days", -7);
        
        // Add action listener to generate button
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (generateChartCallback != null) {
                    Date startDate = (Date) startDateSpinner.getValue();
                    Date endDate = (Date) endDateSpinner.getValue();
                    generateChartCallback.onGenerateChart(startDate, endDate);
                }
            }
        });
        
        // Add components to main row
        mainRow.add(startLabel);
        mainRow.add(startDateSpinner);
        mainRow.add(endLabel);
        mainRow.add(endDateSpinner);
        mainRow.add(generateButton);
        mainRow.add(todayButton);
        mainRow.add(last7DaysButton);
        
        panel.add(mainRow, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Creates quick date selection buttons.
     */
    private JButton createQuickDateButton(String text, int daysBack) {
        JButton button = new JButton(text);
        button.setFont(button.getFont().deriveFont(10f));
        button.setPreferredSize(new Dimension(90, 25));
        
        button.addActionListener(e -> {
            if (quickDateCallback != null) {
                quickDateCallback.onQuickDateSelected(text, daysBack);
            }
        });
        return button;
    }
    
    @Override
    public void setOnGenerateChart(GenerateChartCallback callback) {
        this.generateChartCallback = callback;
    }
    
    @Override
    public void setOnQuickDateSelection(QuickDateCallback callback) {
        this.quickDateCallback = callback;
    }
    
    @Override
    public void showNutrientChart(NutrientSummary summary) {
        JPanel visualizationPanel = createVisualization(summary);
        
        chartDisplayPanel.removeAll();
        chartDisplayPanel.add(visualizationPanel, BorderLayout.CENTER);
        chartDisplayPanel.revalidate();
        chartDisplayPanel.repaint();
    }
    
    @Override
    public void showError(String message) {
        JPanel errorPanel = createErrorPanel(message);
        
        chartDisplayPanel.removeAll();
        chartDisplayPanel.add(errorPanel, BorderLayout.CENTER);
        chartDisplayPanel.revalidate();
        chartDisplayPanel.repaint();
    }
    
    @Override
    public void showNoData(String message) {
        JPanel noDataPanel = createNoDataPanel(message);
        
        chartDisplayPanel.removeAll();
        chartDisplayPanel.add(noDataPanel, BorderLayout.CENTER);
        chartDisplayPanel.revalidate();
        chartDisplayPanel.repaint();
    }
    
    @Override
    public void showLoading() {
        JPanel loadingPanel = new JPanel(new BorderLayout());
        JLabel loadingLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h2>⏳ Processing...</h2>" +
                "<p>Calculating nutrient breakdown...</p>" +
                "</div></html>");
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadingPanel.add(loadingLabel, BorderLayout.CENTER);
        
        chartDisplayPanel.removeAll();
        chartDisplayPanel.add(loadingPanel, BorderLayout.CENTER);
        chartDisplayPanel.revalidate();
        chartDisplayPanel.repaint();
    }
    
    @Override
    public DateRange getSelectedDateRange() {
        Date startDate = (Date) startDateSpinner.getValue();
        Date endDate = (Date) endDateSpinner.getValue();
        return new DateRange(startDate, endDate);
    }
    
    @Override
    public void setDateRange(Date startDate, Date endDate) {
        startDateSpinner.setValue(startDate);
        endDateSpinner.setValue(endDate);
    }
    
    /**
     * Shows initial message when no chart is displayed.
     */
    private void showInitialMessage() {
        JLabel initialLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h2>📊 Nutrient Breakdown</h2>" +
                "<p>Select a date range above and click 'Generate Chart' to view nutrient analysis.</p>" +
                "</div></html>");
        initialLabel.setHorizontalAlignment(SwingConstants.CENTER);
        chartDisplayPanel.add(initialLabel, BorderLayout.CENTER);
    }
    
    /**
     * Creates the main visualization panel with pie chart and summary.
     */
    private JPanel createVisualization(NutrientSummary summary) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Nutrient Breakdown for Selected Meals"));
        
        // Create pie chart
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Double> entry : summary.getNutrientPercentages().entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        
        JFreeChart chart = ChartFactory.createPieChart(
                "Nutrient Distribution by Weight",
                dataset,
                true, true, false
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 500));
        
        mainPanel.add(chartPanel, BorderLayout.CENTER);
        mainPanel.add(createSummaryPanel(summary), BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Creates a summary panel showing top nutrients and meal information.
     */
    private JPanel createSummaryPanel(NutrientSummary summary) {
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.X_AXIS));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
        summaryPanel.setPreferredSize(new Dimension(600, 90));
        
        // Show top 4 nutrients by weight
        List<Map.Entry<String, Double>> topNutrients = summary.getNutrientTotals().entrySet().stream()
                .filter(entry -> !entry.getKey().equals("Other nutrients"))
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(4)
                .collect(Collectors.toList());
        
        for (Map.Entry<String, Double> entry : topNutrients) {
            String nutrientName = entry.getKey();
            double percentage = summary.getNutrientPercentages().get(nutrientName);
            double weight = entry.getValue();
            
            JLabel label = new JLabel(String.format(
                    "<html><b>%s</b><br/>%.1f%% (%.3fg)</html>", 
                    nutrientName, percentage, weight
            ));
            label.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
            summaryPanel.add(label);
        }
        
        summaryPanel.add(Box.createHorizontalStrut(20));
        JLabel mealCountLabel = new JLabel("Total Meals: " + summary.getTotalMeals());
        mealCountLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        summaryPanel.add(mealCountLabel);
        
        return summaryPanel;
    }
    
    /**
     * Creates an error panel when something goes wrong.
     */
    private JPanel createErrorPanel(String errorMessage) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Error"));
        
        JLabel errorLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h2>❌ Error</h2>" +
                "<p>" + errorMessage + "</p>" +
                "</div></html>");
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(errorLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates a panel when no data is available.
     */
    private JPanel createNoDataPanel(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("No Data"));
        
        JLabel noDataLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h2>📊 No Data Available</h2>" +
                "<p>" + message + "</p>" +
                "<p>Please select a different date range or add some meal data.</p>" +
                "</div></html>");
        noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(noDataLabel, BorderLayout.CENTER);
        
        return panel;
    }
} 