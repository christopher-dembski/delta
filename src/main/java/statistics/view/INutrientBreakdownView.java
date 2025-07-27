package statistics.view;

import statistics.model.NutrientSummary;
import java.util.Date;

/**
 * View interface for nutrient breakdown functionality.
 * Follows MVP pattern - defines contract between presenter and view.
 */
public interface INutrientBreakdownView {

    /** Register handler fired when user clicks "Generate Chart" button. */
    void setOnGenerateChart(GenerateChartCallback callback);
    
    /** Register handler fired when user selects quick date ranges. */
    void setOnQuickDateSelection(QuickDateCallback callback);
    
    /** Display the nutrient breakdown chart with data. */
    void showNutrientChart(NutrientSummary summary);
    
    /** Show error message to user. */
    void showError(String message);
    
    /** Show no data message to user. */
    void showNoData(String message);
    
    /** Show loading state while processing. */
    void showLoading();
    
    /** Get the currently selected date range from UI controls. */
    DateRange getSelectedDateRange();
    
    /** Set the date range in the UI controls. */
    void setDateRange(Date startDate, Date endDate);
    
    /** Callback interface for generate chart action. */
    @FunctionalInterface
    interface GenerateChartCallback {
        void onGenerateChart(Date startDate, Date endDate);
    }
    
    /** Callback interface for quick date selection. */
    @FunctionalInterface  
    interface QuickDateCallback {
        void onQuickDateSelected(String dateType, int daysBack);
    }
    
    /** Simple holder for date range data. */
    record DateRange(Date startDate, Date endDate) {}
} 