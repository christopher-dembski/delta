package meals.models.food;

import data.*;
import shared.AppBackend;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Access Object for NutrientV2 operations.
 */
public class NutrientDAOV2 {
    private final String tableName;

    public NutrientDAOV2() {
        this.tableName = "nutrients";
    }

    public List<NutrientV2> findAll() throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(new SelectQuery(tableName));
        return records.stream()
                .map(NutrientV2::new)
                .collect(Collectors.toList());
    }

    public NutrientV2 findById(Integer nutrientId) throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(
                new SelectQuery(tableName)
                        .filter("NutrientID", Comparison.EQUAL, nutrientId)
        );
        return records.isEmpty() ? null : new NutrientV2(records.get(0));
    }

    public NutrientV2 findByCode(Integer nutrientCode) throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(
                new SelectQuery(tableName)
                        .filter("NutrientCode", Comparison.EQUAL, nutrientCode)
        );
        return records.isEmpty() ? null : new NutrientV2(records.get(0));
    }

    public NutrientV2 findBySymbol(String nutrientSymbol) throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(
                new SelectQuery(tableName)
                        .filter("NutrientSymbol", Comparison.EQUAL, nutrientSymbol)
        );
        return records.isEmpty() ? null : new NutrientV2(records.get(0));
    }

    public List<NutrientV2> findByUnit(String unit) throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(
                new SelectQuery(tableName)
                        .filter("NutrientUnit", Comparison.EQUAL, unit)
        );
        return records.stream()
                .map(NutrientV2::new)
                .collect(Collectors.toList());
    }

    public void save(NutrientV2 nutrient) throws DatabaseException {
        AppBackend.db().execute(new InsertQuery(tableName, nutrient));
    }

    public void update(NutrientV2 nutrient) throws DatabaseException {
        AppBackend.db().execute(new UpdateQuery(tableName, nutrient));
    }

    public void deleteById(Integer nutrientId) throws DatabaseException {
        AppBackend.db().execute(
                new DeleteQuery(tableName)
                        .filter("NutrientID", Comparison.EQUAL, nutrientId)
        );
    }

    public void delete(NutrientV2 nutrient) throws DatabaseException {
        if (nutrient.getNutrientId() != null) {
            deleteById(nutrient.getNutrientId());
        }
    }

    public int count() throws DatabaseException {
        return AppBackend.db().execute(new SelectQuery(tableName)).size();
    }
} 