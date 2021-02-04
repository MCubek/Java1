package hr.fer.oprpp1.hw04;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author matej
 * @project hw04-0036516398
 * @created 03/11/2020
 */
class ComparisonOperatorsTest {
    @Test
    void testEqualsOperators() {
        assertTrue(ComparisonOperators.EQUALS.satisfied("AbCdE", "AbCdE"));
    }

    @Test
    void testLikeOperators() {
        assertTrue(ComparisonOperators.LIKE.satisfied("Zagreb", "Za*"));
    }
}