package hr.fer.oprpp1.hw04;

import java.util.LinkedList;
import java.util.List;

/**
 * Razred koji formatira ispis studenata baze podataka
 *
 * @author matej
 * @project hw04-0036516398
 * @created 04/11/2020
 */
public class RecordFormatter {
    public static List<String> format(List<StudentRecord> records) {
        List<String> lines = new LinkedList<>();

        if (records.isEmpty()) {
            lines.add("Records selected: 0");
            return lines;
        }

        int maxFirstNameSize = records.stream()
                .mapToInt(v -> v.getFirstName().length())
                .max().orElse(0);

        int maxLastNameSize = records.stream()
                .mapToInt(v -> v.getLastName().length())
                .max().orElse(0);

        String borders = "+============+=" +
                "=".repeat(maxLastNameSize) +
                "=+=" +
                "=".repeat(maxFirstNameSize) +
                "=+===+";
        lines.add(borders);

        records.forEach(v -> {
            String line = "| " +
                    v.getJmbag() +
                    " | " +
                    v.getLastName() +
                    " ".repeat(maxLastNameSize - v.getLastName().length()) +
                    " | " +
                    v.getFirstName() +
                    " ".repeat(maxFirstNameSize - v.getFirstName().length()) +
                    " | " +
                    v.getFinalGrade() +
                    " |";
            lines.add(line);
        });

        lines.add(borders);
        lines.add("Records selected: " + records.size());
        return lines;
    }
}
