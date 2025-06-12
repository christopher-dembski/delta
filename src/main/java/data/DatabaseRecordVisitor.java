package data;

import java.util.ArrayList;
import java.util.List;

public class DatabaseRecordVisitor implements Visitor {
    static DatabaseRecordVisitor instance;

    public static DatabaseRecordVisitor instance() {
        if (instance == null) {
            instance = new DatabaseRecordVisitor();
        }
        return instance;
    }

    @Override
    public DatabaseRecord visitStudent(Student student) {
        List<DatabaseValue> values = new ArrayList<>();
        values.add(new DatabaseValue("id", DatabaseValueType.INTEGER, student.getId()));
        values.add(new DatabaseValue("name", DatabaseValueType.VARCHAR, student.getName()));
        return new DatabaseRecord(values);
    }
}
