package csv;

import data.DatabaseException;

import java.io.IOException;

public interface ICSVImportService {
    void load(String filePath, String collectionName) throws DatabaseException, IOException;
}
