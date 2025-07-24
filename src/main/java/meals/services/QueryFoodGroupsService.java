package meals.services;

import data.Comparison;
import data.DatabaseException;
import data.IRecord;
import data.SelectQuery;
import meals.models.food.FoodGroup;
import shared.AppBackend;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class QueryFoodGroupsService {
    private static final String QUERY_FOOD_GROUP_SERVICE_ERROR_MESSAGE = "An error occurred when fetching food groups";

    private static final Map<Integer, FoodGroup> cache = new TreeMap<>();

    public static class QueryFoodGroupServiceException extends Exception {
        public QueryFoodGroupServiceException(String errorMessage) {
            super(errorMessage);
        }
    }

    private static QueryFoodGroupsService instance;

    private QueryFoodGroupsService() {}

    public static QueryFoodGroupsService instance() {
        if (instance == null) instance = new QueryFoodGroupsService();
        return instance;
    }

    public List<FoodGroup> fetchAll() throws QueryFoodGroupServiceException {
        // if values are in the cache, then avoid a database call
        if (!cache.isEmpty()) return cache.values().stream().toList();
        // otherwise query the database
        try {
            List<IRecord> records = AppBackend.db().execute(new SelectQuery(FoodGroup.getTableName()));
            List<FoodGroup> foodGroups = records.stream().map(FoodGroup::new).toList();
            // update cache
            for (FoodGroup foodGroup : foodGroups) cache.put(foodGroup.getFoodGroupId(), foodGroup);
            return foodGroups;
        } catch (DatabaseException e) {
            throw new QueryFoodGroupServiceException(QUERY_FOOD_GROUP_SERVICE_ERROR_MESSAGE);
        }
    }

    public FoodGroup findById(int id) throws QueryFoodGroupServiceException {
        // if value in the cache, use the cache
        if (cache.containsKey(id)) return cache.get(id);
        // otherwise, query the database
        try {
            IRecord record = AppBackend.db().execute(
                    new SelectQuery(FoodGroup.getTableName())
                            .filter("id", Comparison.EQUAL, id)
            ).getFirst();
            FoodGroup foodGroup = new FoodGroup(record);
            // update cache
            cache.put(id, foodGroup);
            return foodGroup;
        } catch (DatabaseException e) {
            throw new QueryFoodGroupServiceException(QUERY_FOOD_GROUP_SERVICE_ERROR_MESSAGE);
        }
    }
}
