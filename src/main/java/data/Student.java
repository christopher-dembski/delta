package data;


import java.util.HashMap;

public class Student implements DatabaseModel {
    private static String TABLE_NAME = "students";

    Integer id;
    String name;

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public <T> T accept(Visitor<? extends T> visitor) {
        return visitor.visitStudent(this);
    }

    public DatabaseRecord toDatabaseRecord() {
        HashMap<String, DatabaseValue> values = new HashMap<>();
        values.put("id", new DatabaseValue("id", DatabaseValueType.INTEGER, this.getId()));
        values.put("name", new DatabaseValue("name", DatabaseValueType.VARCHAR, this.getName()));
        return new DatabaseRecord(values);
    }
}
