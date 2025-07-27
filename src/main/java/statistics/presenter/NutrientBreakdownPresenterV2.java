package statistics.presenter;

import statistics.model.NutrientSummary;
import statistics.service.IStatisticsService;
import statistics.view.INutrientBreakdownView;
import meals.models.meal.Meal;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Presenter for nutrient breakdown functionality.
 * Follows MVP pattern - coordinates between view and service layers.
 * Clean separation: no UI code, no business logic, just coordination.
 */
public class NutrientBreakdownPresenterV2 {
    
    private final INutrientBreakdownView view;
    private final IStatisticsService statisticsService;
    
    /**
     * Constructor following dependency injection pattern like profile presenters.
     */
    public NutrientBreakdownPresenterV2(INutrientBreakdownView view, IStatisticsService statisticsService) {
        this.view = view;
        this.statisticsService = statisticsService;
    }
    
    /**
     * Initialize the presenter and wire up view callbacks.
     * Similar to EditProfilePresenter.initialize()
     */
    public void initialize() {
        // Wire up view callbacks
        view.setOnGenerateChart(this::handleGenerateChart);
        view.setOnQuickDateSelection(this::handleQuickDateSelection);
        
        // Set default date range (last 7 days)
        setDefaultDateRange();
    }
    
    /**
     * Handle generate chart request from view.
     * This is the main business coordination method.
     */
    private void handleGenerateChart(Date startDate, Date endDate) {
        // Show loading state
        view.showLoading();
        
        // Perform calculation in background thread to avoid UI blocking
        SwingWorker<NutrientSummary, Void> worker = new SwingWorker<NutrientSummary, Void>() {
            @Override
            protected NutrientSummary doInBackground() throws Exception {
                // Fetch meals through service
                List<Meal> meals = statisticsService.getMealsByDateRange(startDate, endDate);
                
                if (meals.isEmpty()) {
                    return null; // Will trigger no data message
                }
                
                // Calculate nutrient breakdown through service
                return statisticsService.calculateNutrientBreakdown(meals);
            }
            
            @Override
            protected void done() {
                try {
                    NutrientSummary summary = get();
                    
                    if (summary == null || summary.isEmpty()) {
                        view.showNoData("No meals found for the specified date range.");
                    } else {
                        view.showNutrientChart(summary);
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showError("Failed to generate nutrient breakdown: " + e.getMessage());
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Handle quick date selection from view.
     */
    private void handleQuickDateSelection(String dateType, int daysBack) {
        Calendar cal = Calendar.getInstance();
        Date endDate = new Date();
        
        if ("Today".equals(dateType)) {
            // Today only: start and end are the same
            view.setDateRange(endDate, endDate);
        } else if (daysBack == 0) {
            // This month
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date startDate = cal.getTime();
            view.setDateRange(startDate, endDate);
        } else {
            // Days back
            cal.add(Calendar.DAY_OF_MONTH, daysBack);
            Date startDate = cal.getTime();
            view.setDateRange(startDate, endDate);
        }
    }
    
    /**
     * Set default date range in the view.
     */
    private void setDefaultDateRange() {
        Calendar cal = Calendar.getInstance();
        Date endDate = new Date();
        
        cal.add(Calendar.DAY_OF_MONTH, -7);
        Date startDate = cal.getTime();
        
        view.setDateRange(startDate, endDate);
    }
    
    /**
     * Public method for programmatic chart generation.
     * Can be called by other parts of the application.
     */
    public void generateChartForDateRange(Date startDate, Date endDate) {
        view.setDateRange(startDate, endDate);
        handleGenerateChart(startDate, endDate);
    }
    
    /**
     * Public method to refresh with current date selection.
     * Useful for data refresh scenarios.
     */
    public void refreshChart() {
        INutrientBreakdownView.DateRange currentRange = view.getSelectedDateRange();
        handleGenerateChart(currentRange.startDate(), currentRange.endDate());
    }
} 