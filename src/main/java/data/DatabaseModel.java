package data;

public interface DatabaseModel {
    public Integer getId();
    public String getTableName();
    public DatabaseRecord convertToDatabaseRecord();
}
