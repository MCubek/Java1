package hr.fer.zemris.java.gui.layouts.demo;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;

/**
 * @author MatejCubek
 * @project hw07-0036516398
 * @created 15/12/2020
 */
public class DemoFrame2 extends JFrame {
    public DemoFrame2() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initGUI();
        pack();
        setSize(500,500);
    }

    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new CalcLayout(3));
        cp.add(l("1,1"), new RCPosition(1, 1));
        cp.add(l("1,6"), new RCPosition(1, 6));
        cp.add(l("1,7"), new RCPosition(1, 7));
        cp.add(l("2,1"), new RCPosition(2, 1));
        cp.add(l("tekst stvarno najdulji"), new RCPosition(2, 7));
        cp.add(l("tekst kraći"), new RCPosition(4, 2));
        cp.add(l("tekst srednji"), new RCPosition(4, 5));
        cp.add(l("tekst"), new RCPosition(4, 7));
    }

    private JLabel l(String text) {
        JLabel l = new JLabel(text);
        l.setBackground(Color.CYAN);
        l.setOpaque(true);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        return l;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DemoFrame2().setVisible(true);
        });
    }
}
