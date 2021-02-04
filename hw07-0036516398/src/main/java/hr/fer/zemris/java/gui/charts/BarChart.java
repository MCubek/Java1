package hr.fer.zemris.java.gui.charts;

import java.util.List;
import java.util.Objects;

/**
 * Razred koji sadrzi podatke o prikazu jednoga barCharta.
 *
 * @author MatejCubek
 * @project hw07-0036516398
 * @created 16/12/2020
 */
public class BarChart {
    private final List<XYValue> xyValueList;
    private final String xLabel, yLabel;
    private final int minY, maxY, yGap;

    /**
     * Konstuktor razreda BarChart koji prima sve potrebne podatke.
     *
     * @param xyValueList lista svih vrijednosti.
     * @param xLabel      oznaka x osi.
     * @param yLabel      oznaka y osi.
     * @param minY        mininalna vrijednost prikazanog y.
     * @param maxY        maksimalna vrijednost prikazanog y.
     * @param yGap        razmak izmedu vrijednosti y.
     * @throws NullPointerException ukoliko je predan null.
     */
    public BarChart(List<XYValue> xyValueList, String xLabel, String yLabel, int minY, int maxY, int yGap) {
        this.xyValueList = Objects.requireNonNull(xyValueList);
        this.xLabel = Objects.requireNonNull(xLabel);
        this.yLabel = Objects.requireNonNull(yLabel);
        if (minY < 0) throw new IllegalArgumentException("MinY must be greater than 0.");
        this.minY = minY;
        if (maxY <= minY) throw new IllegalArgumentException("MaxY must be greater than MinY");
        this.maxY = maxY;

        this.yGap = calculateYGap(yGap);
        checkValuesGreaterThenMin();
    }

    private void checkValuesGreaterThenMin() {
        double min = xyValueList.stream()
                .mapToDouble(XYValue::getY)
                .min()
                .orElseThrow();

        if (min < minY) throw new IllegalArgumentException("List has value smaller then minimum.");
    }

    private int calculateYGap(int yGap) {
        int gap = yGap;
        while ((maxY - minY) % gap != 0) {
            gap++;
        }
        return gap;
    }

    public List<XYValue> getXyValueList() {
        return xyValueList;
    }

    public String getxLabel() {
        return xLabel;
    }

    public String getyLabel() {
        return yLabel;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getyGap() {
        return yGap;
    }
}
