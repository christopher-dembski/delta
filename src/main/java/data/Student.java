package data;


import java.util.HashMap;

public class Student implements DatabaseModel {
    private final Integer id;
    private final String name;

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    public static Student fromDatabaseRecord(DatabaseRecord record) {
        HashMap<String, DatabaseValue> values = record.getValues();
        // TO DO: simplify database record object and move parsing database value from String into MySQL driver
        return new Student(
                Integer.parseInt((String) values.get("id").getObject()),
                (String) values.get("name").getObject()
        );
    }
}
