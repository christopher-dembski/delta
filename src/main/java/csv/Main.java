package csv;

import data.DatabaseException;
import data.MySQLConfig;
import data.MySQLDriver;

import java.io.IOException;

public class Main {
    /**
     * Example script to show teh CSV import functionality.
     *
     * @param args Command line args (unused).
     * @throws DatabaseException Thrown if an error occurs while accessing the database.
     * @throws IOException       Thrown if an error occurs while reading the CSV file.
     */
    public static void main(String[] args) throws DatabaseException, IOException {
        CSVImportService csvImporter = new CSVImportService(new MySQLDriver(MySQLConfig.instance()));
        // example for testing
        csvImporter.load("src/main/java/csv/students.csv", "students");
    }
}
