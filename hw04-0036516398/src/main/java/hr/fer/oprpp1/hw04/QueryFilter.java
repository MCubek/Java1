package hr.fer.oprpp1.hw04;

import java.util.List;

/**
 * Implementacije sučelja <code>IFilter</code>
 * koja koristi <code>ConditionalExpression</code> objekte.
 *
 * @author matej
 * @project hw04-0036516398
 * @created 03/11/2020
 */
public class QueryFilter implements IFilter {
    final List<ConditionalExpression> conditionalExpressions;

    public QueryFilter(List<ConditionalExpression> conditionalExpressions) {
        this.conditionalExpressions = conditionalExpressions;
    }

    @Override
    public boolean accepts(StudentRecord record) {

        for (ConditionalExpression expression : conditionalExpressions) {
            IComparisonOperator operator = expression.getOperator();

            if (! operator.satisfied(expression.getStrategy().get(record), expression.getLiteral()))
                return false;

        }

        return true;
    }
}
