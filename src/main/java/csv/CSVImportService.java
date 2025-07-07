package csv;

import data.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


/**
 * Defines a job that imports the CSV files.
 */
public class CSVImportService implements ICSVImportService {
    /**
     * Default number of records to batch before inserting them into the database.
     */
    private static final int DEFAULT_BATCH_SIZE = 100;

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
        Iterable<CSVRecord> rows = parser.parse(reader);
        List<IRecord> batch = new ArrayList<>();
        for (CSVRecord row : rows) {
            batch.add(new CSVRecordAdapter(row));
            if (batch.size() == DEFAULT_BATCH_SIZE) {
                driver.execute(new InsertQuery(collectionName, batch));
                batch.clear();
            }
        }
        if (!batch.isEmpty()) {
            driver.execute(new InsertQuery(collectionName, batch));
        }
    }
}
