package data;


import java.util.HashMap;

public class Student implements DatabaseModel {
    private final Integer id;
    private final String name;

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Student(DatabaseRecord record) {
        // TO DO: instantiate from record
        this.name = "Chris";
        this.id = 1;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public DatabaseRecord toDatabaseRecord() {
        HashMap<String, DatabaseValue> values = new HashMap<>();
        values.put("id", new DatabaseValue("id", DatabaseValueType.INTEGER, this.getId()));
        values.put("name", new DatabaseValue("name", DatabaseValueType.VARCHAR, this.getName()));
        return new DatabaseRecord(values);
    }
}
