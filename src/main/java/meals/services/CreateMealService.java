package meals.services;

import data.DatabaseException;
import data.InsertQuery;
import meals.models.MockDataFactory;
import meals.models.meal.Meal;
import shared.AppBackend;
import shared.service_output.ServiceError;
import shared.service_output.ServiceOutput;

import java.util.Collections;
import java.util.List;

public class CreateMealService {
    private static final String DATABASE_ERROR_MESSAGE = "An error occurred when saving the meal to the database";

    public static class CreateMealServiceOutput extends ServiceOutput {
        public CreateMealServiceOutput(List<ServiceError> errors) {
            super(errors);
        }
    }

    private static CreateMealService instance;

    private CreateMealService() {}

    public static CreateMealService instance() {
        if (instance == null) {
            instance = new CreateMealService();
        }
        return instance;
    }

    public CreateMealServiceOutput createMeal(Meal meal) {
        try {
            AppBackend.db().execute(new InsertQuery(Meal.getTableName(), meal));
        } catch (DatabaseException e) {
            ServiceError serviceError = new ServiceError(DATABASE_ERROR_MESSAGE + e.getMessage());
            return new CreateMealServiceOutput(List.of(serviceError));
        }
        // succeeded so return no errors
        return new CreateMealServiceOutput(Collections.emptyList());
    }

    public static void main(String[] args) {
        CreateMealServiceOutput result =
                CreateMealService.instance().createMeal(MockDataFactory.createMockMeal(Meal.MealType.LUNCH));
        if (result.ok()) {
            System.out.println("Inserted meal into database.");
        } else {
            System.out.println(result.errors().getFirst());
        }
    }
}
