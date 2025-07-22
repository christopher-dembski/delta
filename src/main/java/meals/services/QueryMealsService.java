package meals.services;


import data.Comparison;
import data.DatabaseException;
import data.IRecord;
import data.SelectQuery;
import meals.models.MockDataFactory;
import meals.models.food.Food;
import meals.models.food.Measure;
import meals.models.meal.Meal;
import meals.models.meal.MealItem;
import shared.AppBackend;
import shared.service_output.ServiceError;
import shared.service_output.ServiceOutput;
import shared.utils.DateToString;

import java.util.*;

public class QueryMealsService {
    private static final String DATABASE_EXCEPTION_MESSAGE = "An error occurred when fetching meals from the database.";
    private static QueryMealsService instance;

    public static class QueryMealsServiceOutput extends ServiceOutput {
        private final List<Meal> meals;

        public QueryMealsServiceOutput(List<Meal> meals, List<ServiceError> errors) {
            super(errors);
            this.meals = meals;
        }

        public List<Meal> getMeals() {
            return meals;
        }
    }

    public static QueryMealsService instance() {
        if (instance == null) {
            instance = new QueryMealsService();
        }
        return instance;
    }

    private QueryMealsService() {}

    public QueryMealsServiceOutput getMealsByDate(Date fromDate, Date toDate) {
        try {
            List<IRecord> mealRecords = AppBackend.db().execute(
                    new SelectQuery(Meal.getTableName())
                            .filter("created_on", Comparison.GREATER_EQUAL, DateToString.call(fromDate))
                            .filter("created_on", Comparison.LESS_EQUAL, DateToString.call(toDate))
            );
            List<Meal> meals = new ArrayList<>();
            for (IRecord mealRecord: mealRecords) {
                meals.add(buildMealForRecord(mealRecord));
            }
            return new QueryMealsServiceOutput(meals, Collections.emptyList());
        } catch (DatabaseException e) {
            List<ServiceError> errors = List.of(new ServiceError(DATABASE_EXCEPTION_MESSAGE + e.getMessage()));
            return new QueryMealsServiceOutput(Collections.emptyList(), errors);
        }
    }

    private static Meal buildMealForRecord(IRecord mealRecord) throws DatabaseException {
        Integer id = (Integer) mealRecord.getValue("id");
        Meal.MealType mealType = Meal.MealType.fromString((String) mealRecord.getValue("meal_type"));
        Date createdAt = (Date) mealRecord.getValue("created_on");
        List<MealItem> mealItems = buildMealItemsForMeal(mealRecord);
        return new Meal(id, mealType,mealItems, createdAt);
    }

    private static List<MealItem> buildMealItemsForMeal(IRecord mealRecord) throws DatabaseException {
        List<IRecord> mealItemRecords = AppBackend.db().execute(new SelectQuery(MealItem.getTableName()));
        List<MealItem> mealItems = new ArrayList<>();
        for (IRecord mealItemRecord: mealItemRecords) {
            mealItems.add(buildMealItemForRecord(mealItemRecord));
        }
        return mealItems;
    }

    private static MealItem buildMealItemForRecord(IRecord mealItemRecord) {
        Integer id = (Integer) mealItemRecord.getValue("id");
        // TO DO: query using food service
        Food food = MockDataFactory.getRandomFood();
        Float quantity = (Float) mealItemRecord.getValue("quantity");
        // TO DO: query using service
        Measure measure = new Measure(1, "100 Grams", 1.00f);
        return new MealItem(id, food, quantity, measure);
    }

    public static void main(String[] args) {
        QueryMealsServiceOutput result = QueryMealsService.instance().getMealsByDate(new Date(), new Date());
        if (result.ok()) {
            for (Meal meal: result.meals) {
                System.out.println(meal);
                for (MealItem mealItem: meal.getMealItems()) {
                    System.out.println(mealItem);
                }
            }
        }
        else {
            System.out.println(result.errors());
        }
    }
}
