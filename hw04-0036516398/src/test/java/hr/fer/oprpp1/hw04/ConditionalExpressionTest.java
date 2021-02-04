package hr.fer.oprpp1.hw04;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author matej
 * @project hw04-0036516398
 * @created 03/11/2020
 */
class ConditionalExpressionTest {
    @Test
    void testNullElementsThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ConditionalExpression(null, null, null));
    }

    @Test
    void testLikeOperatorWorking() {
        assertTrue(ComparisonOperators.LIKE.satisfied("Zagreb","Za*b"));
    }

    @Test
    void testLessThanOperator() {
        assertTrue(ComparisonOperators.LESS.satisfied("4", "5"));
    }
}