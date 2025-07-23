package meals.models.food;

import data.*;
import shared.AppBackend;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Access Object for NutrientAmountV2 operations.
 */
public class NutrientAmountDAOV2 {
    private final String tableName;

    public NutrientAmountDAOV2() {
        this.tableName = "nutrient_amounts";
    }

    public List<NutrientAmountV2> findAll() throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(new SelectQuery(tableName));
        return records.stream()
                .map(NutrientAmountV2::new)
                .collect(Collectors.toList());
    }

    public NutrientAmountV2 findByFoodAndNutrient(Integer foodId, Integer nutrientId) throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(
                new SelectQuery(tableName)
                        .filter("FoodID", Comparison.EQUAL, foodId)
                        .filter("NutrientID", Comparison.EQUAL, nutrientId)
        );
        return records.isEmpty() ? null : new NutrientAmountV2(records.get(0));
    }

    public List<NutrientAmountV2> findByFood(Integer foodId) throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(
                new SelectQuery(tableName)
                        .filter("FoodID", Comparison.EQUAL, foodId)
        );
        return records.stream()
                .map(NutrientAmountV2::new)
                .collect(Collectors.toList());
    }

    public List<NutrientAmountV2> findByNutrient(Integer nutrientId) throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(
                new SelectQuery(tableName)
                        .filter("NutrientID", Comparison.EQUAL, nutrientId)
        );
        return records.stream()
                .map(NutrientAmountV2::new)
                .collect(Collectors.toList());
    }

    public void save(NutrientAmountV2 nutrientAmount) throws DatabaseException {
        AppBackend.db().execute(new InsertQuery(tableName, nutrientAmount));
    }

    public void update(NutrientAmountV2 nutrientAmount) throws DatabaseException {
        AppBackend.db().execute(new UpdateQuery(tableName, nutrientAmount));
    }

    public void deleteByFoodAndNutrient(Integer foodId, Integer nutrientId) throws DatabaseException {
        AppBackend.db().execute(
                new DeleteQuery(tableName)
                        .filter("FoodID", Comparison.EQUAL, foodId)
                        .filter("NutrientID", Comparison.EQUAL, nutrientId)
        );
    }

    public void deleteByFood(Integer foodId) throws DatabaseException {
        AppBackend.db().execute(
                new DeleteQuery(tableName)
                        .filter("FoodID", Comparison.EQUAL, foodId)
        );
    }

    public void delete(NutrientAmountV2 nutrientAmount) throws DatabaseException {
        deleteByFoodAndNutrient(nutrientAmount.getFoodId(), nutrientAmount.getNutrientId());
    }

    public int count() throws DatabaseException {
        return AppBackend.db().execute(new SelectQuery(tableName)).size();
    }
} 