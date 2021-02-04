package hr.fer.zemris.java.gui.layouts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa koja predstavlja poziciju u {@link CalcLayout}.
 *
 * @author MatejCubek
 * @project hw07-0036516398
 * @created 14/12/2020
 */
public class RCPosition {
    private final int row;
    private final int column;

    public RCPosition(int row, int column) {
        if (row < 1 || column < 1)
            throw new IllegalArgumentException("Arguments must be greater then 1.");
        this.row = row;
        this.column = column;
    }

    /**
     * Metoda parsira string u element RCPosition.
     *
     * @param string ulazni string za parsiranje.
     * @return Novi ovjekt RCPositon s pozicijama.
     * @throws IllegalArgumentException ukoliko nije moguće parsirati.
     */
    public static RCPosition parse(String string) {
        Pattern pattern = Pattern.compile("(\\d+),(\\d+)");
        Matcher m = pattern.matcher(string);

        int row, column;

        if (m.find()) {
            row = Integer.parseInt(m.group(1));
            column = Integer.parseInt(m.group(2));
        } else
            throw new IllegalArgumentException("Unable to parse position " + string + " into RCPosition.");

        return new RCPosition(row, column);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RCPosition that = (RCPosition) o;

        if (row != that.row) return false;
        return column == that.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }

    @Override
    public String toString() {
        return "(" + row +
                "," + column +
                ')';
    }
}
