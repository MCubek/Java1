package hr.fer.oprpp1.hw04;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.Collection;

/**
 * @author matej
 * @project hw04-0036516398
 * @created 03/11/2020
 */
public class StudentDatabaseTest {
    @Test
    void testDatabaseFactory() throws FileNotFoundException {
        StudentDatabase database = new StudentDatabase();

        assertEquals(63, database.filter((v) -> {
            return true;
        }).size());
    }

    @Test
    void testFilterOnFactory() throws FileNotFoundException {
        StudentDatabase database = new StudentDatabase();

        IFilter filter = s -> s.getJmbag().equals("0000000015");

        var record = database.filter(filter);
        assertEquals(1, record.size());

        assertEquals("Glavinić Pecotić", record.get(0).getLastName());
    }
}
