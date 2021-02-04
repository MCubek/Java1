package hr.fer.oprpp1.hw04;

/**
 * Implementacije sučelja <code>IFieldValueGetter</code> za našu
 * bazu podataka.
 *
 * @author matej
 * @project hw04-0036516398
 * @created 03/11/2020
 */
public class FieldValueGetters {
    public static final IFieldValueGetter FIRST_NAME = StudentRecord::getFirstName;

    public static final IFieldValueGetter LAST_NAME = StudentRecord::getLastName;

    public static final IFieldValueGetter JMBAG = StudentRecord::getJmbag;
}
