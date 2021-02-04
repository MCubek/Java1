package hr.fer.zemris.java.gui.layouts;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author MatejCubek
 * @project hw07-0036516398
 * @created 15/12/2020
 */
class CalcLayoutTest {
    @Test
    void testUnit1() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
        p.add(l1, new RCPosition(2,2));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();

        assertEquals(152, dim.width);
        assertEquals(158, dim.height);
    }

    @Test
    void testUnit2() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
        p.add(l1, new RCPosition(1,1));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();

        assertEquals(152, dim.width);
        assertEquals(158, dim.height);
    }

    @Test
    void testIllegalArgumentInConstraint() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));

        p.add(l1, new RCPosition(1,1));
        assertThrows(CalcLayoutException.class, () -> p.add(l2, new RCPosition(6, 2)));
        assertThrows(CalcLayoutException.class, () -> p.add(l2, new RCPosition(2, 8)));

        assertThrows(CalcLayoutException.class, () -> p.add(l2, new RCPosition(1, 2)));

        assertThrows(CalcLayoutException.class, () -> p.add(l2, new RCPosition(1, 1)));

    }
}