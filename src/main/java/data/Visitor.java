package data;

public interface Visitor<T> {
    T visitStudent(Student student);
}
