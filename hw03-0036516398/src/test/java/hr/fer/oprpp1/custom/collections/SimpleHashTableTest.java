package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleHashTableTest {
    @Test
    public void testHashTablePutsValues() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        assertEquals(2, testTable.get("Kristina"));
        assertEquals(5, testTable.get("Ivana"));
        assertEquals(5, testTable.size());
    }

    @Test
    public void testHashTableToString() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana

        assertEquals("[Ante=2, Ivana=5, Jasna=2]", testTable.toString());
    }

    @Test
    public void testContainsKey() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        assertTrue(testTable.containsKey("Kristina"));
    }

    @Test
    public void testContainsValue() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        assertTrue(testTable.containsValue(100));
    }

    @Test
    public void testRemoveElement() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        testTable.remove("Ivana");

        assertFalse(testTable.containsKey("Ivana"));
        assertTrue(testTable.containsKey("Jasna"));
    }

    @Test
    public void testRemoveElement2() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        testTable.remove("Ante");

        assertFalse(testTable.containsKey("Ante"));
        assertTrue(testTable.containsKey("Ivana"));
    }

    @Test
    public void testHashtableIteratorInForEach() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        StringBuilder result = new StringBuilder();

        for (var element : testTable) {
            result.append(element.getKey()).append(element.getValue());
        }

        assertEquals("Josip100Ante2Ivana5Jasna2Kristina2", result.toString());
    }

    @Test
    public void testHashtableIteratorRemoveValid() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        var it = testTable.iterator();

        while (it.hasNext()) {
            if (it.next().getKey().equals("Ivana"))
                it.remove();
        }
        assertFalse(testTable.containsKey("Ivana"));
    }

    @Test
    public void testHashtableIteratorNextThrowsException() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);

        var it = testTable.iterator();

        it.next();
        it.next();

        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void testHashtableIteratorConcurrentModificationError() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);

        var it = testTable.iterator();

        it.next();
        testTable.put("Lucija", 2);

        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    public void testHashtableIteratorRemoveCalledTwiceThrowsException() {
        SimpleHashtable<String, Integer> testTable = new SimpleHashtable<>(2);

        testTable.put("Ivana", 2);
        testTable.put("Ante", 2);
        testTable.put("Jasna", 2);
        testTable.put("Kristina", 2);
        testTable.put("Ivana", 5); // overwrites old grade for Ivana
        testTable.put("Josip", 100);

        var it = testTable.iterator();


        assertThrows(IllegalStateException.class, () -> {
            while (it.hasNext()) {
                if (it.next().getKey().equals("Ivana")) {
                    it.remove();
                    it.remove();
                }
            }
        });
    }

    @Test
    public void testHashtableDoubleIterator() {
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
        StringBuilder sb = new StringBuilder();
        for (SimpleHashtable.TableEntry<String, Integer> pair : examMarks) {
            sb.append(String.format("%s => %d\n", pair.getKey(), pair.getValue()));
        }

        assertEquals("""
                Ante => 2
                Ivana => 5
                Jasna => 2
                Kristina => 5
                """,
                sb.toString());


    }
}
