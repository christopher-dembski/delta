package meals.services;

import data.Comparison;
import data.DatabaseException;
import data.IRecord;
import data.SelectQuery;
import meals.models.food.Food;
import meals.models.food.FoodGroup;
import meals.models.food.Measure;
import meals.models.nutrient.Nutrient;
import shared.AppBackend;

import java.util.*;

public class QueryFoodsService {
    private static final String QUERY_FOODS_SERVICE_ERROR_MESSAGE = "An error occurred when fetching foods.";

    public static class QueryFoodsServiceException extends Exception {
        public QueryFoodsServiceException(String errorMessage) {
            super(errorMessage);
        }
    }

    private static QueryFoodsService instance;

    private QueryFoodsService() {
    }

    public static QueryFoodsService instance() {
        if (instance == null) instance = new QueryFoodsService();
        return instance;
    }

    public List<Food> fetchAll() throws QueryFoodsServiceException {
        try {
            List<IRecord> records = AppBackend.db().execute(new SelectQuery(Food.getTableName()));
            List<Food> foods = new ArrayList<>();
            for (IRecord record : records) foods.add(buildFoodFromRecord(record));
            return foods;
        } catch (DatabaseException | QueryFoodGroupsService.QueryFoodGroupServiceException |
                 QueryNutrientsService.QueryNutrientServiceException e) {
            throw new QueryFoodsServiceException(QUERY_FOODS_SERVICE_ERROR_MESSAGE);
        }
    }

    public Food findById(int id) throws QueryFoodsServiceException {
        try {
            SelectQuery query = new SelectQuery(Food.getTableName()).filter("id", Comparison.EQUAL, id);
            IRecord record = AppBackend.db().execute(query).getFirst();
            return buildFoodFromRecord(record);
        } catch (DatabaseException | QueryFoodGroupsService.QueryFoodGroupServiceException |
                 QueryNutrientsService.QueryNutrientServiceException e) {
            throw new QueryFoodsServiceException(QUERY_FOODS_SERVICE_ERROR_MESSAGE);
        }
    }

    private static Food buildFoodFromRecord(IRecord food) throws QueryFoodGroupsService.QueryFoodGroupServiceException, DatabaseException, QueryNutrientsService.QueryNutrientServiceException {
        int foodId = (int) food.getValue("id");
        String description = (String) food.getValue("description");
        int foodGroupId = (int) food.getValue("food_group_id");
        FoodGroup foodGroup = QueryFoodGroupsService.instance().findById(foodGroupId);
        Map<Nutrient, Float> nutrientAmounts = QueryNutrientsService.instance().findNutrientQuantitiesForFood(foodId);
        return new Food(foodId, description, foodGroup, nutrientAmounts, getMeasures(foodId));
    }

    private static List<Measure> getMeasures(int foodId) throws DatabaseException {
        SelectQuery query = new SelectQuery(Measure.getConversionFactorsMeasuresTableName())
                .filter("food_id", Comparison.EQUAL, foodId);
        List<IRecord> records = AppBackend.db().execute(query);
        List<Measure> measures = new ArrayList<>();
        for (IRecord measureRecord : records) {
            int measureId = (int) measureRecord.getValue("measure_id");
            String measureName = (String) measureRecord.getValue("measure_name");
            // we are fine with losing precision because the value is constant and a small number of decimal places
            Double conversionFactorValue = (Double) measureRecord.getValue("conversion_factor_value");
            measures.add(new Measure(measureId, measureName, conversionFactorValue.floatValue()));
        }
        return measures;
    }
}
