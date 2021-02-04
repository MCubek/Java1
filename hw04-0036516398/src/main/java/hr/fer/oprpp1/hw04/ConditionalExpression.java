package hr.fer.oprpp1.hw04;

import java.util.Objects;

/**
 * Klasa koja predstavlja tri elementa.
 * Strategiju, literlal i operator.
 *
 * @author matej
 * @project hw04-0036516398
 * @created 03/11/2020
 */
public class ConditionalExpression {
    private final IFieldValueGetter strategy;
    private final String literal;
    private final IComparisonOperator operator;

    public ConditionalExpression(IFieldValueGetter strategy, String literal, IComparisonOperator operator) {
        this.strategy = Objects.requireNonNull(strategy);
        this.literal = Objects.requireNonNull(literal);
        this.operator = Objects.requireNonNull(operator);
    }

    public IFieldValueGetter getStrategy() {
        return strategy;
    }

    public String getLiteral() {
        return literal;
    }

    public IComparisonOperator getOperator() {
        return operator;
    }
}
