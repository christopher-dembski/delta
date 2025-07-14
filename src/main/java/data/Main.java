package data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    /**
     * Example script demonstrating usage of the MySQLDriver.
     *
     * @param args Command line Args (unused).
     */
    public static void main(String[] args) throws DatabaseException {
        // Instantiate Driver
        IDatabaseDriver driver = new MySQLDriver(MySQLConfig.instance());
        // Table names are stored in the MySQL config,
        // but the students table is just an example table and is not stored in the config
        String studentsTable = "students";

        // SELECT
        List<IRecord> students = driver.execute(
                new SelectQuery(studentsTable)
                        .filter("id", Comparison.LESS_THAN, 3)
        );
        System.out.printf("Students with ids less than 3: %s%n", students);

        // UPDATE
        Map<String, Object> chrisData = new HashMap<>();
        chrisData.put("id", 1);
        chrisData.put("name", "Kris");
        IRecord chris = new Record(chrisData);
        driver.execute(new UpdateQuery(studentsTable, chris));
        chris = driver.execute(
                new SelectQuery(studentsTable)
                        .filter("id", Comparison.EQUAL, 1)
        ).getFirst();
        System.out.printf("Student after updating name to 'Kris': %s%n", chris);

        // DELETE
        System.out.printf(
                "Number of records before deleting record: %d%n",
                driver.execute(new SelectQuery(studentsTable)).size()
        );
        driver.execute(
                new DeleteQuery(studentsTable)
                        .filter("id", Comparison.EQUAL, 1)
        );
        System.out.printf(
                "Number of students after deleting record: %d%n",
                driver.execute(new SelectQuery(studentsTable)).size()
        );

        // INSERT
        chrisData = new HashMap<>();
        chrisData.put("id", 1);
        chrisData.put("name", "Chris");
        chris = new Record(chrisData);
        driver.execute(new InsertQuery(studentsTable, chris));
        System.out.printf(
                "Number of students after inserting record: %d%n",
                driver.execute(new SelectQuery(studentsTable)).size()
        );
    }
}
