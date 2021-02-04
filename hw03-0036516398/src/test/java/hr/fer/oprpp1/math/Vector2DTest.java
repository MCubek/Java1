package hr.fer.oprpp1.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2DTest {
    @Test
    public void testVectorCreate() {
        var vector = new Vector2D(2.2, 10);

        assertEquals(2.2, vector.getX());
        assertEquals(10, vector.getY());
    }

    @Test
    public void testVectorAdd() {
        var vector = new Vector2D(2.2, 10);
        vector.add(new Vector2D(1.1, 100));

        assertTrue(3.3 - Math.abs(vector.getX()) < 1E-8);
        assertTrue(110 - Math.abs(vector.getY()) < 1E-8);
    }

    @Test
    public void testVectorAdded() {
        var vector = new Vector2D(2.2, 10);
        var newVector = vector.added(new Vector2D(1.1, 100));

        assertTrue(2.2 - Math.abs(vector.getX()) < 1E-8);
        assertTrue(10 - Math.abs(vector.getY()) < 1E-8);

        assertTrue(3.3 - Math.abs(newVector.getX()) < 1E-8);
        assertTrue(110 - Math.abs(newVector.getY()) < 1E-8);
    }

    @Test
    public void testVectorRotate() {
        var vector = new Vector2D(2.2, 0);
        vector.rotate(Math.PI / 2);

        assertTrue(Math.abs(vector.getX()) < 1E-8);
        assertTrue(2.2 - Math.abs(vector.getY()) < 1E-8);
    }

    @Test
    public void testVectorRotate2() {
        var vector = new Vector2D(2.2, 2.2);
        vector.rotate(Math.PI);

        assertTrue(2.2 - Math.abs(vector.getX()) < 1E-8);
        assertTrue(2.2 - Math.abs(vector.getY()) < 1E-8);
    }

    @Test
    public void testVectorRotated() {
        var vector = new Vector2D(2.2, 2.2);
        var newVector = vector.rotated(Math.PI);

        assertNotSame(newVector, vector);
        assertTrue(2.2 - Math.abs(newVector.getX()) < 1E-8);
        assertTrue(2.2 - Math.abs(newVector.getY()) < 1E-8);
    }

    @Test
    public void testVectorCopy() {
        var vector = new Vector2D(2.2, 2.2);
        var newVector = vector.copy();

        assertNotSame(newVector, vector);
    }

    @Test
    public void testVectorScale() {
        var vector = new Vector2D(2.2, 2.2);
        vector.scale(2);

        assertTrue(4.4 - Math.abs(vector.getX()) < 1E-8);
        assertTrue(4.4 - Math.abs(vector.getY()) < 1E-8);
    }

    @Test
    public void testVectorScaled() {
        var vector = new Vector2D(2.2, 2.2);
        var newVector = vector.scaled(2);

        assertNotSame(newVector, vector);
        assertTrue(4.4 - Math.abs(newVector.getX()) < 1E-8);
        assertTrue(4.4 - Math.abs(newVector.getY()) < 1E-8);
    }
}
