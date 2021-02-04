package hr.fer.oprpp1.hw04;

/**
 * Klasa statickih metoda komparatora vrijednosti
 *
 * @author matej
 * @project hw04-0036516398
 * @created 03/11/2020
 */
public class ComparisonOperators {
    public static final IComparisonOperator LESS = (value1, value2) -> value1.compareTo(value2) < 0;

    public static final IComparisonOperator LESS_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) <= 0;

    public static final IComparisonOperator GREATER = (value1, value2) -> value1.compareTo(value2) > 0;

    public static final IComparisonOperator GREATER_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) >= 0;

    public static final IComparisonOperator EQUALS = String::equals;

    public static final IComparisonOperator NOT_EQUALS = (value1, value2) -> ! value1.equals(value2);

    public static final IComparisonOperator LIKE = (value1, value2) -> value1.matches(value2.replace("*", ".*"));

}
