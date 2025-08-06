package swaps.ui.swap_statistics;

import java.util.List;

import meals.models.meal.Meal;
import swaps.models.SwapWithMealContext;

/**
 * Parameter object to encapsulate meal comparison data with goals.
 * Refactors the long parameter list in updateMealListComparisonWithGoals method.
 */
public class MealComparisonWithGoalsData {
    private final List<Meal> beforeSwapMeals;
    private final List<Meal> afterSwapMeals;
    private final List<String> goalNutrientNames;
    private final SwapWithMealContext selectedSwap;
    
    public MealComparisonWithGoalsData(List<Meal> beforeSwapMeals, 
                                     List<Meal> afterSwapMeals, 
                                     List<String> goalNutrientNames, 
                                     SwapWithMealContext selectedSwap) {
        this.beforeSwapMeals = beforeSwapMeals;
        this.afterSwapMeals = afterSwapMeals;
        this.goalNutrientNames = goalNutrientNames;
        this.selectedSwap = selectedSwap;
    }
    
    public List<Meal> getBeforeSwapMeals() { return beforeSwapMeals; }
    public List<Meal> getAfterSwapMeals() { return afterSwapMeals; }
    public List<String> getGoalNutrientNames() { return goalNutrientNames; }
    public SwapWithMealContext getSelectedSwap() { return selectedSwap; }
    
    public boolean isValid() {
        return beforeSwapMeals != null && afterSwapMeals != null;
    }
}
