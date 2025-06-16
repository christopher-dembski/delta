package data;

public interface DatabaseModel {
    Integer getId();

    DatabaseRecord toDatabaseRecord();
}
