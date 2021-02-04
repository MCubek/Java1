package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DictionaryTest {
    @Test
    public void testAddValueAndReturn(){
        Dictionary<String, Integer> dictionary = new Dictionary<>();

        dictionary.put("First", 1);
        dictionary.put("Second", 2);

        assertEquals(1,dictionary.get("First"));
    }

    @Test
    public void testAddValueAndRemove(){
        Dictionary<String, Integer> dictionary = new Dictionary<>();

        dictionary.put("First", 1);
        dictionary.put("Second", 2);

        dictionary.remove("First");
        assertNull(dictionary.get("First"));
    }

    @Test
    public void testGetSize(){
        Dictionary<String, Integer> dictionary = new Dictionary<>();

        dictionary.put("First", 1);
        dictionary.put("Second", 2);

        assertEquals(2,dictionary.size());
    }

    @Test
    public void testEmpty(){
        Dictionary<String, Integer> dictionary = new Dictionary<>();

        assertTrue(dictionary.isEmpty());
    }

    @Test
    public void testIsEmptyAfterClear(){
        Dictionary<String, Integer> dictionary = new Dictionary<>();

        dictionary.put("First", 1);
        dictionary.put("Second", 2);

        dictionary.clear();

        assertTrue(dictionary.isEmpty());
    }

    @Test
    public void testRemoveGetsOldValue(){
        Dictionary<String, Integer> dictionary = new Dictionary<>();

        dictionary.put("First", 1);
        dictionary.put("Second", 2);

        assertEquals(1,dictionary.remove("First"));
    }

    @Test
    public void testPutOverWrites(){
        Dictionary<String, Integer> dictionary = new Dictionary<>();

        dictionary.put("First", 1);
        dictionary.put("Second", 2);

        dictionary.put("First",3);

        assertEquals(3,dictionary.get("First"));
    }

    @Test
    public void testRemove(){
        Dictionary<String, Integer> dictionary = new Dictionary<>();

        dictionary.put("First", 1);
        dictionary.put("Second", 2);

        dictionary.remove("First");

        assertEquals(1,dictionary.size());
    }
}
