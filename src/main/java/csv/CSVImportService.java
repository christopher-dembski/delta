package csv;

import data.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Defines a job that imports the CSV files.
 */
public class CSVImportService implements ICSVImportService {
    /**
     * The database driver to use to import the CSVs.
     */
    private final IDatabaseDriver driver;

    /**
     * The CSV parser.
     */
    private final CSVFormat parser;

    /**
     * @param driver The database driver to use to import the CSVs.
     */
    public CSVImportService(IDatabaseDriver driver) {
        this.driver = driver;
        parser = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();
    }

    /**
     * Imports the CSV files into the database.
     *
     * @param filePath       The path of the CSV file to import.
     * @param collectionName The database collection to insert the values into.
     * @throws DatabaseException Thrown if an error occurs while inserting a value into the database.
     * @throws IOException       Thrown if an error occurs while reading the CSV file.
     */
    public void load(String filePath, String collectionName) throws DatabaseException, IOException {
        Reader reader = new FileReader(filePath);
        Iterable<CSVRecord> records = parser.parse(reader);
        for (CSVRecord record : records) {
            IRecord csvRecordAdapter = new CSVRecordAdapter(record);
            driver.execute(new InsertQuery(collectionName, csvRecordAdapter));
        }
    }

    /**
     * Example script to show teh CSV import functionality.
     *
     * @param args Command line args (unused).
     * @throws DatabaseException Thrown if an error occurs while accessing the database.
     * @throws IOException       Thrown if an error occurs while reading the CSV file.
     */
    public static void main(String[] args) throws DatabaseException, IOException {
        CSVImportService csvImporter = new CSVImportService(new MySQLDriver());
        // example for testing
        csvImporter.load("src/main/java/csv/students.csv", "students");
    }
}
