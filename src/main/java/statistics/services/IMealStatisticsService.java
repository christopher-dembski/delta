package statistics.services;

import meals.models.meal.Meal;
import statistics.models.NutrientStatistics;
import java.util.List;

public interface IMealStatisticsService {
    NutrientStatistics calculateNutrientBreakdown(List<Meal> meals);
} 