package data;


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
}
