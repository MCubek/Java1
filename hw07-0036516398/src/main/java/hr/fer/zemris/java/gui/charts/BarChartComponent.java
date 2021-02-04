package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Comparator;
import java.util.Objects;

/**
 * BarChart kompomenta koja prikazuje pomocu Swinga barChart.
 *
 * @author MatejCubek
 * @project hw07-0036516398
 * @created 16/12/2020
 */
public class BarChartComponent extends JComponent {
    private final BarChart barChart;

    private final int descriptionAndValuesGap = 10;
    private final int valuesAndAxisGap = 10;
    private final int axisExtra = 6;
    private final int barSpace = 2;

    private final Color axisColor = new Color(164, 164, 164);
    private final Color gridColor = new Color(224, 170, 76);
    private final Color barColor = new Color(244, 119, 72);
    private final Font defaultFont = new Font("Arial", Font.PLAIN, 12);
    private final Font boldFont = new Font("Arial", Font.BOLD, 12);

    private final Insets insets;
    private FontMetrics fontMetrics;

    private int fontHeight;
    private int numberWidth;
    private int componentWidth;
    private int componentHeight;
    private int x0, y0;

    /**
     * Konstruktor koji prima BarChart objekt.
     *
     * @param barChart barchart.
     * @throws NullPointerException ukoliko je predan null.
     */
    public BarChartComponent(BarChart barChart) {
        this.barChart = Objects.requireNonNull(barChart);

        this.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        this.insets = getInsets();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;

        graphics2D.setFont(defaultFont);

        calculateFontValues(graphics2D);
        calculateValues();


        paintLabels(graphics2D);
        paintGrid(graphics2D);
    }

    private void calculateFontValues(Graphics2D graphics2D) {
        fontMetrics = graphics2D.getFontMetrics();

        //Velicina slova od dna do vrha
        fontHeight = fontMetrics.getHeight();

        //Sirina najveceg broja, onog na vrhu charta
        numberWidth = fontMetrics.stringWidth(String.valueOf(barChart.getMaxY()));
    }

    private void calculateValues() {
        componentWidth = getWidth();
        componentHeight = getHeight();

        x0 = insets.left + fontHeight + descriptionAndValuesGap + numberWidth + valuesAndAxisGap;
        y0 = componentHeight - (fontHeight * 2 + descriptionAndValuesGap + valuesAndAxisGap + insets.bottom);
    }

    private void paintLabels(Graphics2D graphics2D) {
        String xLabel = barChart.getxLabel();
        String yLabel = barChart.getyLabel();

        //Oznaka x osi
        //Računa se sredina crtljivog polja
        graphics2D.drawString(xLabel,
                (x0 + componentWidth - insets.right - (axisExtra * 2)) / 2 - fontMetrics.stringWidth(xLabel) / 2,
                componentHeight - insets.bottom);

        AffineTransform saved = graphics2D.getTransform();
        AffineTransform rotated = AffineTransform.getQuadrantRotateInstance(3);
        graphics2D.setTransform(rotated);

        //Oznaka y osi
        //Računa se sredina crtljivog polja
        graphics2D.drawString(yLabel, - (insets.top + (axisExtra * 2) + y0) / 2 - fontMetrics.stringWidth(yLabel) / 2,
                x0 - valuesAndAxisGap - descriptionAndValuesGap - numberWidth);

        graphics2D.setTransform(saved);
    }

    private void paintGrid(Graphics2D graphics2D) {
        int minY = barChart.getMinY();
        int maxY = barChart.getMaxY();
        int gapY = barChart.getyGap();

        int steps = (maxY - minY) / gapY;

        var list = barChart.getXyValueList();
        list.sort(Comparator.comparing(XYValue::getX));

        int[] verticalLines = populateLineArray(list.size(),
                x0, componentWidth - insets.right - (axisExtra * 2));
        int[] horizontalLines = populateLineArray(steps, y0, insets.top + (axisExtra * 2));


        //Axis and grid
        {
            //Vertical lines
            for (int line : verticalLines) {
                if (line == x0) graphics2D.setColor(axisColor);
                else graphics2D.setColor(gridColor);

                graphics2D.drawLine(line, y0, line, insets.top + axisExtra);

                graphics2D.setColor(axisColor);
                graphics2D.drawLine(line, y0 + axisExtra, line, y0);
            }
            //Horizontal lines (
            for (int line : horizontalLines) {
                if (line == y0) graphics2D.setColor(axisColor);
                else graphics2D.setColor(gridColor);

                graphics2D.drawLine(x0, line, componentWidth - insets.right - axisExtra, line);

                graphics2D.setColor(axisColor);
                graphics2D.drawLine(x0 - axisExtra, line, x0, line);
            }

        }

        //Arrows
        {
            Polygon arrow1 = new Polygon();
            arrow1.addPoint(componentWidth - insets.right, y0);
            arrow1.addPoint(componentWidth - insets.right - axisExtra, y0 + axisExtra);
            arrow1.addPoint(componentWidth - insets.right - axisExtra, y0 - axisExtra);

            Polygon arrow2 = new Polygon();
            arrow2.addPoint(x0, insets.top);
            arrow2.addPoint(x0 + axisExtra, insets.top + axisExtra);
            arrow2.addPoint(x0 - axisExtra, insets.top + axisExtra);

            graphics2D.fillPolygon(arrow1);
            graphics2D.fillPolygon(arrow2);
        }

        //Bars
        {
            graphics2D.setColor(barColor);
            for (int i = 0; i < list.size(); i++) {
                Polygon bar = new Polygon();
                int height = list.get(i).getY() / gapY;

                bar.addPoint(verticalLines[i] + barSpace, horizontalLines[0] - barSpace);
                bar.addPoint(verticalLines[i + 1] - barSpace, horizontalLines[0] - barSpace);
                bar.addPoint(verticalLines[i + 1] - barSpace, horizontalLines[height] + barSpace);
                bar.addPoint(verticalLines[i] + barSpace, horizontalLines[height] + barSpace);

                graphics2D.fillPolygon(bar);
            }
        }

        // X axis symbols
        {
            graphics2D.setColor(Color.BLACK);
            graphics2D.setFont(boldFont);

            int y = y0 + valuesAndAxisGap + fontHeight;

            for (int i = 0; i < list.size(); i++) {
                int center = (verticalLines[i] + verticalLines[i + 1]) / 2;
                String value = String.valueOf(list.get(i).getX());

                int x = center - fontMetrics.stringWidth(value) / 2;

                graphics2D.drawString(value, x, y);
            }
        }

        //Y axis symbols
        {
            for (int i = 0; i <= steps; i++) {
                String value = String.valueOf(i * barChart.getyGap());
                int y = horizontalLines[i] + fontHeight / 3;
                int x = x0 - valuesAndAxisGap - fontMetrics.stringWidth(value);

                graphics2D.drawString(value, x, y);
            }
        }
    }

    private int[] populateLineArray(int size, int start, int end) {
        var array = new int[size + 1];
        int step = (end - start) / size;
        for (int i = 0; i <= size; i++) {
            if (i == 0)
                array[i] = start;
            else if (i == size)
                array[i] = end;
            else
                array[i] = start + step * i;
        }
        return array;
    }

}
