package data;

public interface DatabaseModel extends Visitable {
    public Integer getId();
    public DatabaseRecord toDatabaseRecord();
    // TO DO: remove
    public String getTableName();
}
