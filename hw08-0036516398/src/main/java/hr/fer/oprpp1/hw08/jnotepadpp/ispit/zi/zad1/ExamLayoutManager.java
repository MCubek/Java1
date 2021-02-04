package hr.fer.oprpp1.hw08.jnotepadpp.ispit.zi.zad1;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 26/01/2021
 */
public class ExamLayoutManager implements LayoutManager2 {
    public static final ExamPosition AREA1 = new ExamPosition(1, 1);
    public static final ExamPosition AREA2 = new ExamPosition(2, 1);
    public static final ExamPosition AREA3 = new ExamPosition(2, 2);

    private int percentage;
    private final Map<Component, ExamPosition> componentExamPositionMap;

    public ExamLayoutManager(int percentage) {
        if (percentage < 10 || percentage > 90)
            throw new ExamLayoutException();
        this.percentage = percentage;
        componentExamPositionMap = new HashMap<>();
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
        //TODO
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        Objects.requireNonNull(comp);

        if (constraints instanceof ExamPosition) {
            var position = (ExamPosition) constraints;

            var row = position.getRow();
            var column = position.getColumn();

            if (row > 2 || row < 1) throw new ExamLayoutException("Row has to be 1 or 2 and is " + row);

            if (column > 2 || column < 1) throw new ExamLayoutException("Row has to be 1 or 2 and is " + column);

            if (row == 1 && column == 2)
                throw new ExamLayoutException("Position row=1 and col=2 does not exist in layout.");

            componentExamPositionMap.put(comp, position);
        } else throw new IllegalArgumentException("Constraint invalid! Type:" + constraints.getClass());
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return null;
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {

    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        componentExamPositionMap.remove(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(400,150);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return null;
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Dimension size = parent.getSize();

            int panelWidth = size.width;
            int panelHeight = size.height;

            int heightOne = panelHeight * percentage / 100;
            int widthTwo = panelWidth * percentage / 100;

            for (Map.Entry<Component, ExamPosition> entry : componentExamPositionMap.entrySet()) {
                Component component = entry.getKey();
                ExamPosition position = entry.getValue();

                int width, height, x, y;


                if (position.getRow() == 1) {
                    width = panelWidth;
                    height = heightOne;
                    x = 0;
                    y = 0;
                } else if (position.getRow() == 2 && position.getColumn() == 1) {

                    width = widthTwo;
                    height = panelHeight - heightOne;
                    x = 0;
                    y = heightOne;
                } else if (position.getRow() == 2 && position.getColumn() == 2) {
                    width = panelWidth - widthTwo;
                    height = panelHeight - heightOne;
                    x = widthTwo;
                    y = heightOne;
                }else
                    return;

                component.setBounds(x, y, width, height);
            }
        }
    }
}
