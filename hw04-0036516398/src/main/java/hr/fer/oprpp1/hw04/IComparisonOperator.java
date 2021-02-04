package hr.fer.oprpp1.hw04;

/**
 * Sučelje komparacije query naredaba naše baze podataka
 *
 * @author matej
 * @project hw04-0036516398
 * @created 03/11/2020
 */
@FunctionalInterface
public interface IComparisonOperator {
    boolean satisfied(String value1, String value2);
}
