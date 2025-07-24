package meals.services;

import data.Comparison;
import data.DatabaseException;
import data.IRecord;
import data.SelectQuery;
import meals.models.nutrient.Nutrient;
import shared.AppBackend;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class QueryNutrientsService {
    private static final String NUTRIENT_SERVICE_ERROR_MESSAGE = "An error occurred when fetching nutrients";

    private static final Map<Integer, Nutrient> cache = new TreeMap<>();

    public static class QueryNutrientServiceException extends Exception {
        public QueryNutrientServiceException(String errorMessage) {
            super(errorMessage);
        }
    }

    private static QueryNutrientsService instance;

    private QueryNutrientsService() {
    }

    public static QueryNutrientsService instance() {
        if (instance == null) instance = new QueryNutrientsService();
        return instance;
    }

    public List<Nutrient> fetchAll() throws QueryNutrientServiceException {
        // if values are in the cache, then avoid a database call
        if (!cache.isEmpty()) return cache.values().stream().toList();
        // otherwise query the database
        try {
            List<IRecord> records = AppBackend.db().execute(new SelectQuery(Nutrient.getTableName()));
            List<Nutrient> nutrients = records.stream().map(Nutrient::new).toList();
            // update cache
            for (Nutrient nutrient : nutrients) cache.put(nutrient.getNutrientId(), nutrient);
            return nutrients;
        } catch (DatabaseException e) {
            throw new QueryNutrientServiceException(NUTRIENT_SERVICE_ERROR_MESSAGE);
        }
    }

    public Nutrient findById(int id) throws QueryNutrientServiceException {
        // if value in the cache, use the cache
        if (cache.containsKey(id)) return cache.get(id);
        // otherwise, query the database
        try {
            IRecord record = AppBackend.db().execute(
                    new SelectQuery(Nutrient.getTableName())
                            .filter("id", Comparison.EQUAL, id)
            ).getFirst();
            Nutrient nutrient = new Nutrient(record);
            // update cache
            cache.put(id, nutrient);
            return nutrient;
        } catch (DatabaseException e) {
            throw new QueryNutrientServiceException(NUTRIENT_SERVICE_ERROR_MESSAGE);
        }
    }
}
