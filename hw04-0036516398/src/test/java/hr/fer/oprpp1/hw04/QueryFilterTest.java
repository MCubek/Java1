package hr.fer.oprpp1.hw04;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 08/11/2020
 */
class QueryFilterTest {
    @Test
    void testQueryFilter() {
        List<ConditionalExpression> conditionalExpressionList = new ArrayList<>();

        ConditionalExpression ce1 = new ConditionalExpression(FieldValueGetters.JMBAG, "0000000012", ComparisonOperators.EQUALS);
        ConditionalExpression ce2 = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Franković", ComparisonOperators.EQUALS);

        conditionalExpressionList.add(ce1);
        conditionalExpressionList.add(ce2);

        QueryFilter queryFilter = new QueryFilter(conditionalExpressionList);
        try {
            StudentDatabase studentDatabase = new StudentDatabase();

            var studentList = studentDatabase.filter(queryFilter);

            assertEquals(1, studentList.size());
            assertEquals("Hrvoje",studentList.get(0).getFirstName());

        } catch (FileNotFoundException ignored) {
            throw new RuntimeException();
        }


    }
}