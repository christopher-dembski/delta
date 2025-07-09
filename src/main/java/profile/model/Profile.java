package profile.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable domain object representing a user profile.
 *
 *  Use the nested Builder to create instances
 *     Profile p = new Profile.Builder()
 *                     .name("Ada Lovelace")
 *                     .age(28)
 *                     .sex(Sex.FEMALE)
 *                     .dateOfBirth(LocalDate.of(1815, 12, 10))
 *                     .height(165)
 *                     .weight(55)
 *                     .unitSystem(UnitSystem.METRIC)
 *                     .build();
 */
public final class Profile {

    private final String     id;
    private final String     name;
    private final int        age;
    private final Sex        sex;
    private final LocalDate  dateOfBirth;
    private final double     height;
    private final double     weight;
    private final UnitSystem unitSystem;

    private Profile(Builder b) {
        this.id          = b.id;
        this.name        = b.name;
        this.age         = b.age;
        this.sex         = b.sex;
        this.dateOfBirth = b.dateOfBirth;
        this.height      = b.height;
        this.weight      = b.weight;
        this.unitSystem  = b.unitSystem;
    }

    // ----- getters ---------------------------------------------------------
    public String     getId()          { return id; }
    public String     getName()        { return name; }
    public int        getAge()         { return age; }
    public Sex        getSex()         { return sex; }
    public LocalDate  getDateOfBirth() { return dateOfBirth; }
    public double     getHeight()      { return height; }
    public double     getWeight()      { return weight; }
    public UnitSystem getUnitSystem()  { return unitSystem; }

 

    @Override
    public String toString() {
        return "Profile{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", age=" + age +
               ", sex=" + sex +
               ", dateOfBirth=" + dateOfBirth +
               ", height=" + height +
               ", weight=" + weight +
               ", unitSystem=" + unitSystem +
               '}';
    }

    // ----- builder ---------------------------------------------------------
    public static class Builder {
        private String     id          = UUID.randomUUID().toString();
        private String     name;
        private int        age;
        private Sex        sex;
        private LocalDate  dateOfBirth;
        private double     height;
        private double     weight;
        private UnitSystem unitSystem  = UnitSystem.METRIC; // default

        public Builder id(String id)                   { this.id = (id != null) ? id : UUID.randomUUID().toString(); return this; }
        public Builder name(String name)               { this.name = name; return this; }
        public Builder age(int age)                    { this.age = age; return this; }
        public Builder sex(Sex sex)                    { this.sex = sex; return this; }
        public Builder dateOfBirth(LocalDate dob)      { this.dateOfBirth = dob; return this; }
        public Builder height(double height)           { this.height = height; return this; }
        public Builder weight(double weight)           { this.weight = weight; return this; }
        public Builder unitSystem(UnitSystem unit)     { this.unitSystem = (unit != null) ? unit : UnitSystem.METRIC; return this; }

        /** Builds a validated, immutable Profile */
        public Profile build() {
            Objects.requireNonNull(name,        "name");
            Objects.requireNonNull(sex,         "sex");
            Objects.requireNonNull(dateOfBirth, "dateOfBirth");
            if (age <= 0)                       throw new IllegalArgumentException("Age must be > 0");
            if (height <= 0 || weight <= 0)     throw new IllegalArgumentException("Height/weight must be > 0");
            return new Profile(this);
        }
    }
}
