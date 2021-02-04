package hr.fer.oprpp1.hw04;

import java.util.Objects;

/**
 * Klasa koja predstavlja osobu, studenta
 *
 * @author matejC
 * @project hw04-0036516398
 * @created 03/11/2020
 */
public class StudentRecord {
    private final String firstName;
    private final String lastName;
    private final String jmbag;
    private final int finalGrade;

    public StudentRecord(String firstName, String lastName, String jmbag, int finalGrade) {
        this.firstName = Objects.requireNonNull(firstName);
        this.lastName = Objects.requireNonNull(lastName);
        this.jmbag = Objects.requireNonNull(jmbag);
        this.finalGrade = finalGrade;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getJmbag() {
        return jmbag;
    }

    public int getFinalGrade() {
        return finalGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentRecord that = (StudentRecord) o;

        return jmbag.equals(that.jmbag);
    }

    @Override
    public int hashCode() {
        return jmbag.hashCode();
    }
}
