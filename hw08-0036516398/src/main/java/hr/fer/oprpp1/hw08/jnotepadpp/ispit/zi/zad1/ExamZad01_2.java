package hr.fer.oprpp1.hw08.jnotepadpp.ispit.zi.zad1;

import javax.swing.*;
import java.awt.*;

public class ExamZad01_2 extends JDialog {
    public ExamZad01_2() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        initGUI();
        pack();
    }

    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 10, 90, 30);

        cp.add(slider, BorderLayout.PAGE_START);

        ExamLayoutManager exlm = new ExamLayoutManager(20);

        slider.addChangeListener(l -> {
            var sl = (JSlider) l.getSource();
            exlm.setPercentage(sl.getValue());
        });

        JPanel panelMain = new JPanel();
        panelMain.setLayout(exlm);
        panelMain.add(makeLabel("Ovo je tekst za područje 1.", Color.RED), ExamLayoutManager.AREA1);
        panelMain.add(makeLabel("Područje 2.", Color.GREEN), ExamLayoutManager.AREA2);
        panelMain.add(makeLabel("Područje 3.", Color.YELLOW), ExamLayoutManager.AREA3);

        cp.add(panelMain, BorderLayout.CENTER);
    }

    private Component makeLabel(String txt, Color col) {
        JLabel lab = new JLabel(txt);
        lab.setOpaque(true);
        lab.setBackground(col);
        return lab;
    }
}