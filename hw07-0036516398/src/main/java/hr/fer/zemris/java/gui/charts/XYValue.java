package hr.fer.zemris.java.gui.charts;

/**
 * @author MatejCubek
 * @project hw07-0036516398
 * @created 16/12/2020
 */
public class XYValue {
    private final int x;
    private final int y;

    public XYValue(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XYValue xyValue = (XYValue) o;

        if (x != xyValue.x) return false;
        return y == xyValue.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "XYValue{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
