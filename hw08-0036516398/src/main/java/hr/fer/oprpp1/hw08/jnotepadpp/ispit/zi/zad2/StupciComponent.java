package hr.fer.oprpp1.hw08.jnotepadpp.ispit.zi.zad2;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 26/01/2021
 */
public class StupciComponent extends JComponent {
    private final List<Integer> integerList;

    private final Color unEven = new Color(0, 255, 0);
    private final Color even = new Color(255, 0, 0);

    public StupciComponent(List<Integer> integerList) {
        this.integerList = integerList;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;

        int componentWidth = getWidth();
        int componentHeight = getHeight();

        int numberOfColumns = integerList.size();

        int columnWidth = componentWidth / numberOfColumns;

        int ax, ay, bx, by, cx, cy, dx, dy;

        ax = 0;
        ay = componentHeight;
        bx = columnWidth;
        by = componentHeight;

        int sum = integerList.stream()
                .mapToInt(v->v)
                .sum();

        for (int i = 0; i < numberOfColumns; i++) {
            int number = integerList.get(i);
            int columnHight =(int) ((number*1.0 / sum) * componentHeight);
            cx = bx;
            cy = componentHeight - columnHight;
            dx = ax;
            dy = componentHeight - columnHight;
            graphics2D.setColor(i % 2 == 0 ? unEven : even);
            Polygon pol = new Polygon();
            pol.addPoint(ax, ay);
            pol.addPoint(bx, by);
            pol.addPoint(cx, cy);
            pol.addPoint(dx, dy);
            graphics2D.fillPolygon(pol);

            ax += columnWidth;
            bx += columnWidth;
        }
    }
}
