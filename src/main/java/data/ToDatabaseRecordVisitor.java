package data;

import java.util.HashMap;

public class ToDatabaseRecordVisitor implements Visitor<DatabaseRecord> {
    static ToDatabaseRecordVisitor instance;

    public static ToDatabaseRecordVisitor instance() {
        if (instance == null) {
            instance = new ToDatabaseRecordVisitor();
        }
        return instance;
    }

    @Override
    public DatabaseRecord visitStudent(Student student) {
        HashMap<String, DatabaseValue> values = new HashMap<>();
        values.put("id", new DatabaseValue("id", DatabaseValueType.INTEGER, student.getId()));
        values.put("name", new DatabaseValue("name", DatabaseValueType.VARCHAR, student.getName()));
        return new DatabaseRecord(values);
    }
}
