package helpers;

import data.MySQLConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTestHelpers {
    private static final String MYSQL_PASSWORD_WARNING =
            "mysql: [Warning] Using a password on the command line interface can be insecure.";
    private static final String RELATIVE_PATH_DATA_PLATFORM = "src/main/java/data/";

    public static void resetTestDatabase() {
        try {
            int exitCode = executeMySQLScript(RELATIVE_PATH_DATA_PLATFORM + "create-test-database.sql");
            if (exitCode != 0) fail("An error occurred when recreating the test database.");
            exitCode = executeMySQLScript(RELATIVE_PATH_DATA_PLATFORM + "create-tables.sql");
            if (exitCode != 0) fail("An error occurred when re-creating the test database tables.");
            exitCode = executeMySQLScript(RELATIVE_PATH_DATA_PLATFORM + "seed-database.sql");
            if (exitCode != 0) fail("An error occurred when seeding the test database.");
        } catch (IOException | InterruptedException e) {
            fail(e);
        }
    }

    private static int executeMySQLScript(String relativePath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Path path = FileSystems
                .getDefault()
                .getPath(relativePath)
                .toAbsolutePath();
        MySQLConfig config = MySQLConfig.instance();
        String serviceAccount = config.getServiceAccount();
        String password = config.getServiceAccountPassword();
        String database = config.getDatabaseName();
        String host = config.getHostName();
        processBuilder.command("mysql", "-u", serviceAccount, "-p" + password, "-h", host, database);
        processBuilder.redirectInput(new File(path.toString()));
        return execute(processBuilder);
    }

    private static int execute(ProcessBuilder processBuilder) throws IOException, InterruptedException {
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        printToStandardOutput(process);
        return process.waitFor();  // return exit code
    }

    private static void printToStandardOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals(MYSQL_PASSWORD_WARNING))
                continue;
            System.out.println(line);
        }
    }
}
