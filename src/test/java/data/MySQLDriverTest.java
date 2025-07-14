package data;

import org.junit.jupiter.api.Test;
import shared.AppBackend;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MySQLDriverTest {
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

    @Test
    public void testFuzzySearch() throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(
                new SelectQuery("students")
                        .filter("name", Comparison.FUZZY_SEARCH, "hri")
        );
        assertEquals(1, records.size());
        assertEquals("Chris", records.get(0).getValue("name"));
    }

    @Test
    public void testOrderByAndLimit() throws DatabaseException {
        List<IRecord> records = AppBackend.db().execute(
                new SelectQuery("students")
                        .sort("name", Order.ASCENDING)
                        .limit(2)
        );
        assertEquals(2, records.size());
        assertEquals("Abdullah", records.get(0).getValue("name"));
    }
}
