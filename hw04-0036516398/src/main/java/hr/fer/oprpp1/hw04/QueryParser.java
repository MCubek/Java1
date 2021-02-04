package hr.fer.oprpp1.hw04;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sučelje parsera query naredaba
 *
 * @author matej
 * @project hw04-0036516398
 * @created 03/11/2020
 */
public class QueryParser {
    public static final String[] OPERATORS = {"<=", ">=", "!=", "<", ">", "=", "LIKE"};
    private final List<ConditionalExpression> conditions;

    /**
     * Konstruktor koji stvara parser za zadani querry
     *
     * @param query upit
     */
    public QueryParser(String query) {
        conditions = new ArrayList<>();
        parse(query);
    }

    private void parse(String query) {
        query = query.strip();
        if (! query.startsWith("query")) throw new QueryException("Format wrong.");

        if (! operatorsAndsValid(query)) throw new QueryException("AND operator format invalid.");

        Matcher mBeginning = Pattern.compile("\\Aquery\\s+(\\w+)\\s*(LIKE|[<>=!]=?)\\s*\"([\\w*]+)\""
                , Pattern.UNICODE_CHARACTER_CLASS | Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE).matcher(query);

        Matcher mOther = Pattern.compile("and\\s+(\\w+)\\s*(LIKE|[<>=!]=?)\\s*\"([\\w*]++)\""
                , Pattern.UNICODE_CHARACTER_CLASS | Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE).matcher(query);

        if (! mBeginning.find()) throw new QueryException("Expression wrong.");

        String attribute = mBeginning.group(1);
        String operator = mBeginning.group(2);
        String literal = mBeginning.group(3);

        checkValuesAndAddToList(attribute, operator, literal);

        while (mOther.find()) {
            attribute = mOther.group(1);
            operator = mOther.group(2);
            literal = mOther.group(3);
            checkValuesAndAddToList(attribute, operator, literal);
        }
    }

    private boolean operatorsAndsValid(String query) {
        int andCount = (query.length() - query.toLowerCase().replace("and", "").length()) / "and".length();

        int operatorCount = 0;
        String modifiedQuery = query.toUpperCase();
        for (var testOperator : OPERATORS) {
            if (! modifiedQuery.contains(testOperator)) continue;
            String beforeQuery = modifiedQuery;
            modifiedQuery = modifiedQuery.replace(testOperator, "");
            operatorCount += (beforeQuery.length() - modifiedQuery.length()) / testOperator.length();
        }
        return andCount + 1 == operatorCount;
    }

    private void checkValuesAndAddToList(String attribute, String operator, String literal) {
        IFieldValueGetter fieldValueGetter = switch (attribute.toLowerCase()) {
            case ("jmbag") -> FieldValueGetters.JMBAG;
            case ("firstname") -> FieldValueGetters.FIRST_NAME;
            case ("lastname") -> FieldValueGetters.LAST_NAME;
            default -> throw new QueryException("Attribute " + attribute + " does not exist.");
        };

        IComparisonOperator comparisonOperator = switch (operator.toLowerCase()) {
            case ("<") -> ComparisonOperators.LESS;
            case ("<=") -> ComparisonOperators.LESS_OR_EQUALS;
            case ("=") -> ComparisonOperators.EQUALS;
            case (">=") -> ComparisonOperators.GREATER_OR_EQUALS;
            case (">") -> ComparisonOperators.GREATER;
            case ("like") -> ComparisonOperators.LIKE;
            case ("!=") -> ComparisonOperators.NOT_EQUALS;
            default -> throw new QueryException("Operator " + operator + " does not exist.");
        };

        if (comparisonOperator == ComparisonOperators.LIKE && checkInvalidLikeOperator(literal)) {
            System.err.println("Like operator " + literal + " is invalid and is skipped.");
            return;
        }

        conditions.add(new ConditionalExpression(fieldValueGetter, literal, comparisonOperator));
    }

    private boolean checkInvalidLikeOperator(String literal) {
        return literal.matches("\\A.*\\*.*\\*.*\\z");
    }

    /**
     * Metoda vraća <code>true</code> ako je query forme <code>jmbag="xxx"</code>.
     * Mora sadržati samo jedan komparator, jedan atribut jmbag i operator <code>EQUALS</code>.
     *
     * @return <code>true</code> ako je forma isptavna, inace <code>false</code>.
     */
    public boolean isDirectQuery() {
        if (conditions.size() != 1) return false;

        ConditionalExpression conditionalExpression = conditions.get(0);

        return conditionalExpression.getOperator() == ComparisonOperators.EQUALS
                && conditionalExpression.getStrategy() == FieldValueGetters.JMBAG;
    }

    /**
     * Metoda vaća string s jmbagom ako zadovoljava <code>isDirectQuery</code>.
     *
     * @return jmbag iz jednostavnog izraza
     * @see hr.fer.oprpp1.hw04.QueryParser
     */
    public String getQueriedJMBAG() {
        if (! isDirectQuery()) throw new QueryException("Query format wrong for getJmbag call");

        return conditions.get(0).getLiteral();
    }

    /**
     * Za sve upite, ova metoda vraća listu uvjetnih izraza.
     * Direktni izrazi, jednostavni imati će samo jedan element.
     *
     * @return lista uvjetnih izraza
     */
    public List<ConditionalExpression> getQuery() {
        return conditions;
    }

}
