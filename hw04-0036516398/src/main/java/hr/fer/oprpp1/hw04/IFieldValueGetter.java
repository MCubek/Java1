package hr.fer.oprpp1.hw04;

import hr.fer.oprpp1.hw04.StudentRecord;

/**
 * Sučelje za dobivanje vrijednosti studenata iz baze
 *
 * @author matej
 * @project hw04-0036516398
 * @created 03/11/2020
 */
public interface IFieldValueGetter {
    String get(StudentRecord record);
}
