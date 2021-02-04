package hr.fer.oprpp1.hw04;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Klasa koja predstavalja in-memory bazu studenata
 *
 * @author matej
 * @project hw04-0036516398
 * @created 03/11/2020
 */
public class StudentDatabase {
    private final Map<String, StudentRecord> database;

    public StudentDatabase() throws FileNotFoundException {
        database = new LinkedHashMap<>();
        File factory = new File("src/main/resources/DB/database.txt");

        parseDatabase(factory);

    }

    private void parseDatabase(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String[] lineArray = scanner.nextLine().split("\\s+");

            if (lineArray.length < 4 || lineArray.length > 5) continue;

            String jmbag = lineArray[0];
            int finalGrade;
            String firstName;
            String lastName;

            if (lineArray.length == 4) {
                lastName = lineArray[1];
                firstName = lineArray[2];
                finalGrade = Integer.parseInt(lineArray[3]);
            } else {
                lastName = lineArray[1] + " " + lineArray[2];
                firstName = lineArray[3];
                finalGrade = Integer.parseInt(lineArray[4]);
            }

            if (jmbag.length() != 10) throw new IllegalArgumentException("Invalid JMBAG.");
            if (! (finalGrade >= 1 && finalGrade <= 5))
                throw new IllegalArgumentException("Invalid grade: " + finalGrade);

            database.put(jmbag, new StudentRecord(firstName, lastName, jmbag, finalGrade));
        }

        scanner.close();
    }

    /**
     * Metoda koja vraća studenta s zadanim indeksom.
     * Ukoliko student ne postoji vrati <code>null</code>
     * Kompleksnost je O(1)
     *
     * @param jmbag jmbag studenta
     * @return student s zadanim jmbagom
     */
    public StudentRecord forJMBAG(String jmbag) {
        return database.get(jmbag);
    }

    /**
     * Metoda koja vraća listu studenata koji zadovoljavaju filter.
     * Ukoliko ne postoji studenata koji zadovoljavaju vrati praznu listu.
     *
     * @param filter filter koji propušta studente
     * @return nova lista studenata koji zadovoljavaju filter
     */
    public List<StudentRecord> filter(IFilter filter) {
        return database.values()
                .stream()
                .filter(filter::accepts)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        try {
            StudentDatabase studentDatabase = new StudentDatabase();

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String query = scanner.nextLine();

                if (query.toLowerCase().equals("exit")) break;

                try{
                    QueryParser parser = new QueryParser(query);
                    List<StudentRecord> records = new ArrayList<>();
                    if (parser.isDirectQuery()) {
                        System.out.println("Using index for record retrieval.");
                        StudentRecord student = studentDatabase.forJMBAG(parser.getQueriedJMBAG());
                        if (student != null)
                            records.add(student);
                    } else {
                        records.addAll(studentDatabase.filter(new QueryFilter(parser.getQuery())));
                    }
                    RecordFormatter.format(records).forEach(System.out::println);
                } catch (QueryException queryException){
                    System.err.println(queryException.getMessage());
                }
            }

        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Database file not found.");
        }
    }
}
