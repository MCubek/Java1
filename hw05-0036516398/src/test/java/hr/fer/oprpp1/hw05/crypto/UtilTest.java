package hr.fer.oprpp1.hw05.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 30/11/2020
 */
class UtilTest {

    @Test
    void hextobyteTest() {
        assertArrayEquals(new byte[]{1, - 82, 34}, Util.hextobyte("01aE22"));
    }

    @Test
    void bytetohexTest() {
        assertEquals("01ae22",Util.bytetohex(new byte[]{1, - 82, 34}));
    }
}