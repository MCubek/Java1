package hr.fer.oprpp1.hw08.jnotepadpp.ispit.zi.zad1;

/**
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 26/01/2021
 */
public class ExamPosition {
    private final int row;
    private final int column;

    public ExamPosition(int row, int column) {
        if (column < 1 || row < 1) throw new IllegalArgumentException();
        this.row = row;
        this.column = column;
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

        ExamPosition that = (ExamPosition) o;

        if (row != that.row) return false;
        return column == that.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }
}
