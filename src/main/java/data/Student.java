package data;

import java.util.ArrayList;
import java.util.List;

public class Student implements DatabaseModel {
    private static String TABLE_NAME = "students";

    Integer id;
    String name;

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public DatabaseRecord convertToDatabaseRecord() {
        List<DatabaseValue> values = new ArrayList<>();
        values.add(new DatabaseValue("id", DatabaseValueType.INTEGER, 1));
        values.add(new DatabaseValue("name", DatabaseValueType.VARCHAR, "Chris"));
        return new DatabaseRecord(values);
    }
}
