package csv;

import data.IRecord;
import org.apache.commons.csv.CSVRecord;

import java.util.Collection;

/**
 * Adapts the interface of the CSVRecord class from the Apache commons-csv library
 * to enable saving an instance of CSVRecord to the database.
 */
public class CSVRecordAdapter implements IRecord {
    /**
     * The CSVRecord to adapt.
     */
    private final CSVRecord csvRecord;

    /**
     * @param csvRecord The CSVRecord to adapt.
     */
    protected CSVRecordAdapter(CSVRecord csvRecord) {
        this.csvRecord = csvRecord;
    }

    @Override
    public Object getValue(String field) {
        return csvRecord.get(field);
    }

    @Override
    public Collection<String> fieldNames() {
        return csvRecord.toMap().keySet();
    }
}
