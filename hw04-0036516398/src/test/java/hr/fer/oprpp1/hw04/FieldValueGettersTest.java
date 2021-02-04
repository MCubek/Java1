package hr.fer.oprpp1.hw04;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author matej
 * @project hw04-0036516398
 * @created 03/11/2020
 */
class FieldValueGettersTest {
    @Test
    void testFieldGetter() {
        StudentRecord record = new StudentRecord("Hrvoje", "Horvat", "0000001234", 4);
        assertEquals("Horvat", FieldValueGetters.LAST_NAME.get(record));
    }
}