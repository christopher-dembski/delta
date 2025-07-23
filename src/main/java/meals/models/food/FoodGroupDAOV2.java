package meals.models.food;

import data.*;
import shared.AppBackend;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Access Object for FoodGroupV2 operations.
 */
public class FoodGroupDAOV2 {
    private final String tableName;

    public FoodGroupDAOV2() {
        this.tableName = "food_groups";
    }

    public List<FoodGroupV2> findAll() throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(new SelectQuery(tableName));
        return records.stream()
                .map(FoodGroupV2::new)
                .collect(Collectors.toList());
    }

    public FoodGroupV2 findById(Integer foodGroupId) throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(
                new SelectQuery(tableName)
                        .filter("FoodGroupID", Comparison.EQUAL, foodGroupId)
        );
        return records.isEmpty() ? null : new FoodGroupV2(records.get(0));
    }

    public FoodGroupV2 findByCode(Integer foodGroupCode) throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(
                new SelectQuery(tableName)
                        .filter("FoodGroupCode", Comparison.EQUAL, foodGroupCode)
        );
        return records.isEmpty() ? null : new FoodGroupV2(records.get(0));
    }

    public List<FoodGroupV2> findByName(String name) throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(
                new SelectQuery(tableName)
                        .filter("FoodGroupName", Comparison.EQUAL, name)
        );
        return records.stream()
                .map(FoodGroupV2::new)
                .collect(Collectors.toList());
    }

    public void save(FoodGroupV2 foodGroup) throws DatabaseException {
        AppBackend.db().execute(new InsertQuery(tableName, foodGroup));
    }

    public void update(FoodGroupV2 foodGroup) throws DatabaseException {
        AppBackend.db().execute(new UpdateQuery(tableName, foodGroup));
    }

    public void deleteById(Integer foodGroupId) throws DatabaseException {
        AppBackend.db().execute(
                new DeleteQuery(tableName)
                        .filter("FoodGroupID", Comparison.EQUAL, foodGroupId)
        );
    }

    public void delete(FoodGroupV2 foodGroup) throws DatabaseException {
        if (foodGroup.getFoodGroupId() != null) {
            deleteById(foodGroup.getFoodGroupId());
        }
    }

    public int count() throws DatabaseException {
        return AppBackend.db().execute(new SelectQuery(tableName)).size();
    }
} 