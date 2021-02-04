package hr.fer.zemris.java.gui.layouts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author MatejCubek
 * @project hw07-0036516398
 * @created 15/12/2020
 */
class RCPositionTest {

    @Test
    void parse() {
        assertEquals(new RCPosition(5, 4), RCPosition.parse("5,4"));
        assertEquals(new RCPosition(2, 1), RCPosition.parse("2,1"));
    }
}