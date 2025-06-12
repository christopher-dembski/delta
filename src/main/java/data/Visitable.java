package data;

public interface Visitable {
    <T> T accept(Visitor<? extends T> visitor);
}
