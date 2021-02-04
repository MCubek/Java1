package hr.fer.oprpp1.hw04;

import hr.fer.oprpp1.hw04.StudentRecord;

/**
 * Filter Studenata
 *
 * @author matej
 * @project hw04-0036516398
 * @created 03/11/2020
 */
public interface IFilter {
    boolean accepts(StudentRecord record);
}
