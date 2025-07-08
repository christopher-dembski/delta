package data;

import helpers.DatabaseTestHelpers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.AppBackend;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MySQLDriverTest {
    @BeforeEach
    public void beforeEach() {
        DatabaseTestHelpers.resetTestDatabase();
    }

    @Test
    public void testAlwaysTrue() {
        // test to verify Docker is working
        assertEquals(1 + 1, 2, "Always true.");
    }

    @Test
    public void testSelectAll() throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(new SelectQuery("students"));
        assertEquals(records.size(), 4);
    }
}
