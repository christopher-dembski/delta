package shared;

import data.IDatabaseDriver;
import data.MySQLDriver;
import data.MySQLConfig;
import data.DatabaseException;

/**
 * Represents the backend of the application.
 * Contains global data for the application.
 */
public class AppBackend {
    /**
     * Name of the environment variable for the app's execution environment.
     */
    public static final String APP_ENV = "APP_ENV";
    /**
     * Constant denoting our CI environment.
     */
    public static final String CI = "CI";
    /**
     * Constant denoting our local test environment.
     */
    public static final String LOCAL_TEST = "LOCAL_TEST";
    /**
     * Constant denoting our local development environment.
     */
    public static final String LOCAL_DEV = "LOCAL_DEV";
    /**
     * The singleton instance of the app backend.
     */
    private static AppBackend instance;
    /**
     * The database driver to use to execute queries.
     */
    private IDatabaseDriver db;

    /**
     * @return The instance representing the backend of the application.
     */
    public static AppBackend instance() {
        if (instance == null) {
            instance = new AppBackend();
        }
        return instance;
    }

    /**
     * Constructs a new instance of the app backend.
     */
    private AppBackend() {
        try {
            db = new MySQLDriver(MySQLConfig.instance());
        } catch (DatabaseException e) {
            // for now, crash on startup if the database connection cannot be established
            // in the future, we should handle this more gracefully in the UI
            System.out.println("Database crashed on startup:\n" + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * @return The database driver used to execute database queries for the app.
     */
    public static IDatabaseDriver db() {
        return instance().db;
    }

    /**
     * @return The current execution environment for the app.
     */
   public static String environment() {
       String environment = System.getenv(AppBackend.APP_ENV);
       // default to local development environment
       if(environment == null) environment = AppBackend.LOCAL_DEV;
       return environment;
   }
}
