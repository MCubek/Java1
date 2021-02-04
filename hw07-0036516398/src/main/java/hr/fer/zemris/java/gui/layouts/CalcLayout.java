package hr.fer.zemris.java.gui.layouts;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Swing layout za potrebe kaklulatora.
 * Implementira {@link LayoutManager2}.
 *
 * @author MatejCubek
 * @project hw07-0036516398
 * @created 14/12/2020
 */
public class CalcLayout implements LayoutManager2 {
    private final int gap;
    private final int ROW_NUMBER = 5;
    private final int COLUMN_NUMBER = 7;
    private final Map<Component, RCPosition> componentRCPositionMap;

    public CalcLayout(int gap) {
        if (gap < 0) throw new CalcLayoutException("Gap can't be a negative number.");
        this.gap = gap;
        this.componentRCPositionMap = new HashMap<>();
    }

    public CalcLayout() {
        this(0);
    }


    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        Objects.requireNonNull(comp);

        RCPosition rcPosition;

        if (constraints instanceof RCPosition)
            rcPosition = (RCPosition) constraints;
        else if (constraints instanceof String) {
            rcPosition = RCPosition.parse((String) constraints);
        } else throw new IllegalArgumentException("Constraint " + constraints.toString() + " is not valid.");

        if (rcPosition.getRow() > ROW_NUMBER || rcPosition.getColumn() > COLUMN_NUMBER)
            throw new CalcLayoutException("Positions are out of range for calculator layout.");

        if (rcPosition.getRow() == 1 && (rcPosition.getColumn() >= 2 && rcPosition.getColumn() <= 5))
            throw new CalcLayoutException("Position " + rcPosition + " does not exist in first row.");

        if (componentRCPositionMap.containsValue(rcPosition))
            throw new CalcLayoutException("Component with same position already exists.");

        componentRCPositionMap.put(comp, rcPosition);
    }

    @Override
    public void layoutContainer(Container parent) {
        RCPosition firstPosition = new RCPosition(1, 1);

        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            Dimension size = parent.getSize();
            int componentCount = parent.getComponentCount();


            if (componentCount == 0) return;

            int paneWidth = size.width - (insets.left + insets.right);
            int paneHeight = size.height - (insets.top + insets.bottom);

            int cellWidth = (paneWidth - (COLUMN_NUMBER - 1) * gap) / COLUMN_NUMBER;
            int cellHeight = (paneHeight - (ROW_NUMBER - 1) * gap) / ROW_NUMBER;

            int widthDifference = paneWidth - (cellWidth * COLUMN_NUMBER + (COLUMN_NUMBER - 1) * gap);
            int heightDifference = paneHeight - (cellHeight * ROW_NUMBER + (ROW_NUMBER - 1) * gap);

            for (Map.Entry<Component, RCPosition> entry : componentRCPositionMap.entrySet()) {
                Component component = entry.getKey();
                RCPosition rcPosition = entry.getValue();

                int width, height, x, y;

                width = cellWidth;
                height = cellHeight;

                if (rcPosition.equals(firstPosition)) {
                    width = 5 * cellWidth + 4 * gap;

                    //Popunjavanje prvog
                    for (int i = 2; i <= 5; i++) {
                        if (addExtraPixelWidth(widthDifference, i)) width++;
                    }
                }

                int column = rcPosition.getColumn();
                int row = rcPosition.getRow();

                if (addExtraPixelWidth(widthDifference, column))
                    width++;

                if (addExtraPixelHeight(heightDifference, row))
                    height++;

                x = calculateXPosition(widthDifference, cellWidth, insets.left, gap, column);
                y = calculateYPosition(heightDifference, cellHeight, insets.top, gap, row);

                component.setBounds(x, y, width, height);
            }
        }
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    /**
     * Not used by this class.
     *
     * @param target the name of the container
     */
    @Override
    public void invalidateLayout(Container target) {

    }

    /**
     * Not used by this class.
     *
     * @param name the name of the component
     * @param comp the component
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        componentRCPositionMap.remove(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return calculateLayoutSize(parent, CalculateLayoutSizeOption.PREFERRED);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return calculateLayoutSize(target, CalculateLayoutSizeOption.MAX);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return calculateLayoutSize(parent, CalculateLayoutSizeOption.MIN);
    }

    /**
     * Metoda računanja dimenzija layouta
     *
     * @param parent                    container roditelj
     * @param calculateLayoutSizeOption enum {@link CalculateLayoutSizeOption} kako bi se koja informacija je potrebna
     * @return nova dimenzija
     */
    private Dimension calculateLayoutSize(Container parent, CalculateLayoutSizeOption calculateLayoutSizeOption) {
        int width = 0;
        int height = 0;
        Insets insets = parent.getInsets();
        RCPosition firstPosition = new RCPosition(1, 1);

        for (Map.Entry<Component, RCPosition> entry : componentRCPositionMap.entrySet()) {
            Component component = entry.getKey();

            Dimension dimension = switch (calculateLayoutSizeOption) {
                case MAX -> component.getMaximumSize();
                case MIN -> component.getMinimumSize();
                case PREFERRED -> component.getPreferredSize();
            };

            height = Math.max(height, dimension.height);

            if (! entry.getValue().equals(firstPosition))
                width = Math.max(width, dimension.width);
            else
                width = Math.max(width, (dimension.width - 4 * gap) / 5);
        }

        return new Dimension(COLUMN_NUMBER * width + insets.left + insets.right + (COLUMN_NUMBER - 1) * gap,
                ROW_NUMBER * height + insets.top + insets.bottom + (ROW_NUMBER - 1) * gap);
    }

    private enum CalculateLayoutSizeOption {
        PREFERRED,
        MIN,
        MAX
    }

    private static boolean addExtraPixelWidth(int differenceWidth, int column) {
        var fillPriorityColumn = Arrays.asList(4, 2, 6, 3, 5, 1, 7);

        return fillPriorityColumn.subList(0, differenceWidth).contains(column);
    }

    private static boolean addExtraPixelHeight(int differenceHeight, int row) {
        var fillPriorityRow = Arrays.asList(3, 1, 5, 2, 4);

        return fillPriorityRow.subList(0, differenceHeight).contains(row);
    }

    private static int calculateXPosition(int differenceWidth, int width, int insetsLeft, int gap, int column) {
        int x = insetsLeft;

        for (int i = 1; i < column; i++) {
            x += width;
            if (addExtraPixelWidth(differenceWidth, i)) x++;
            x += gap;
        }

        return x;
    }

    private static int calculateYPosition(int differenceHeight, int height, int insetsTop, int gap, int row) {
        int y = insetsTop;

        for (int i = 1; i < row; i++) {
            y += height;
            if (addExtraPixelHeight(differenceHeight, i)) y++;
            y += gap;
        }

        return y;
    }

}
