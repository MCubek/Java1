package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionTest {
    @Test
    public void testAddAllSatisfying() {
        Collection<Integer> col1 = new LinkedListIndexedCollection<>();
        Collection<Integer> col2 = new ArrayIndexedCollection<>();
        col1.add(2);
        col1.add(3);
        col1.add(4);
        col1.add(5);
        col1.add(6);
        col2.add(12);
        col2.addAllSatisfying(col1, v -> v % 2 == 0);

        StringBuilder sb = new StringBuilder();
        col2.forEach(sb::append);

        assertEquals("12246", sb.toString());
    }
}
