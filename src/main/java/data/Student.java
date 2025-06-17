package data;


import java.util.HashMap;

public class Student implements DataAccessObject {
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
        HashMap<String, Object> values = new HashMap<>();
        values.put("id", this.getId());
        values.put("name", this.getName());
        return new DatabaseRecord(values);
    }

    @SuppressWarnings("unused")
    public static Student fromDatabaseRecord(DatabaseRecord record) {
        return new Student(
                (Integer) record.getValue("id"),
                (String) record.getValue("name")
        );
    }
}
