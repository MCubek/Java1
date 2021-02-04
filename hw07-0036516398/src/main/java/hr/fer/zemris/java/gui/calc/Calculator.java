package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * Swing aplikacija kalkulatora.
 *
 * @author MatejCubek
 * @version 1.0
 * @project hw07-0036516398
 * @created 15/12/2020
 */
public class Calculator extends JFrame {
    private final CalcModel calcModel;
    private JPanel panel;
    private final LinkedList<Double> queue = new LinkedList<>();
    private Map<JButton, String> normalValues;
    private Map<JButton, String> invertValues;
    private JCheckBox invertCheck;


    /**
     * Defaultni konstruktor.
     */
    public Calculator() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Java Calculator v1.0");
        calcModel = new CalcModelImpl();
        initGUI();
        setLocationRelativeTo(null);
        pack();
    }

    private void initGUI() {
        Container cp = getContentPane();
        panel = new JPanel(new CalcLayout(4));

        initDigits();
        initBasicOperators();
        initSpecialOperators();
        initSpecialCommands();
        initDisplay();

        cp.add(panel);
    }


    private void initDigits() {
        insertDigit("0", new RCPosition(5, 3));
        insertDigit("1", new RCPosition(4, 3));
        insertDigit("2", new RCPosition(4, 4));
        insertDigit("3", new RCPosition(4, 5));
        insertDigit("4", new RCPosition(3, 3));
        insertDigit("5", new RCPosition(3, 4));
        insertDigit("6", new RCPosition(3, 5));
        insertDigit("7", new RCPosition(2, 3));
        insertDigit("8", new RCPosition(2, 4));
        insertDigit("9", new RCPosition(2, 5));
    }

    private void initBasicOperators() {
        createButton("+", new RCPosition(5, 6)).addActionListener(l ->
                executeTwoArgumentCommand(Double::sum));

        createButton("-", new RCPosition(4, 6)).addActionListener(l ->
                executeTwoArgumentCommand((a, b) -> a - b));

        createButton("*", new RCPosition(3, 6)).addActionListener(l ->
                executeTwoArgumentCommand((a, b) -> a * b));

        createButton("/", new RCPosition(2, 6)).addActionListener(l ->
                executeTwoArgumentCommand((a, b) -> a / b));


    }

    private void initSpecialOperators() {
        normalValues = new HashMap<>();
        invertValues = new HashMap<>();

        JButton sin = createButton("sin", new RCPosition(2, 2));
        sin.addActionListener(l -> {
            if (! invertCheck.isSelected())
                executeSpecialOneArgumentCommand(Math::sin);
            else
                executeSpecialOneArgumentCommand(Math::asin);
        });
        normalValues.put(sin, "sin");
        invertValues.put(sin, "arcsin");

        JButton cos = createButton("cos", new RCPosition(3, 2));
        cos.addActionListener(l -> {
            if (! invertCheck.isSelected())
                executeSpecialOneArgumentCommand(Math::cos);
            else
                executeSpecialOneArgumentCommand(Math::acos);
        });
        normalValues.put(cos, "cos");
        invertValues.put(cos, "arccos");

        JButton tan = createButton("tan", new RCPosition(4, 2));
        tan.addActionListener(l -> {
            if (! invertCheck.isSelected())
                executeSpecialOneArgumentCommand(Math::tan);
            else
                executeSpecialOneArgumentCommand(Math::atan);
        });
        normalValues.put(tan, "tan");
        invertValues.put(tan, "arctan");

        JButton ctg = createButton("ctg", new RCPosition(5, 2));
        ctg.addActionListener(l -> {
            if (! invertCheck.isSelected())
                executeSpecialOneArgumentCommand(a -> 1 / Math.tan(a));
            else
                executeSpecialOneArgumentCommand(a -> 1 / Math.atan(a));
        });
        normalValues.put(ctg, "ctg");
        invertValues.put(ctg, "arcctg");

        createButton("1/x", new RCPosition(2, 1)).addActionListener(l ->
                executeSpecialOneArgumentCommand(a -> 1 / a));

        JButton log = createButton("log", new RCPosition(3, 1));
        log.addActionListener(l -> {
            if (! invertCheck.isSelected())
                executeSpecialOneArgumentCommand(Math::log10);
            else
                executeSpecialOneArgumentCommand(a -> Math.pow(10, a));
        });
        normalValues.put(log, "log");
        invertValues.put(log, "10^x");

        JButton ln = createButton("ln", new RCPosition(4, 1));
        ln.addActionListener(l -> {
            if (! invertCheck.isSelected())
                executeSpecialOneArgumentCommand(Math::log);
            else
                executeSpecialOneArgumentCommand(a -> Math.pow(Math.E, a));
        });
        normalValues.put(ln, "ln");
        invertValues.put(ln, "e^x");

        JButton xPowN = createButton("x^n", new RCPosition(5, 1));
        xPowN.addActionListener(l -> {
            if (! invertCheck.isSelected())
                executeTwoArgumentCommand(Math::pow);
            else
                executeTwoArgumentCommand((a, b) -> Math.pow(a, 1 / b));
        });
        normalValues.put(xPowN, "x^n");
        invertValues.put(xPowN, "x^(1/n)");
    }

    private void initSpecialCommands() {
        createButton("=", new RCPosition(1, 6)).addActionListener(l -> {
            double value =
                    calcModel.getPendingBinaryOperation().applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue());
            calcModel.setValue(value);
            calcModel.setPendingBinaryOperation(null);
            calcModel.setActiveOperand(0);
        });

        createButton("+/-", new RCPosition(5, 4)).addActionListener(l ->
                calcModel.swapSign()
        );

        createButton(".", new RCPosition(5, 5)).addActionListener(l ->
                calcModel.insertDecimalPoint()
        );

        createButton("clr", new RCPosition(1, 7)).addActionListener(l ->
                calcModel.clear()
        );

        createButton("reset", new RCPosition(2, 7)).addActionListener(l ->
                calcModel.clearAll()
        );

        createButton("push", new RCPosition(3, 7)).addActionListener(l -> {
                    queue.push(calcModel.getValue());
                    calcModel.clear();
                }
        );

        createButton("pop", new RCPosition(4, 7)).addActionListener(l ->
                calcModel.setValue(queue.pop())
        );

        invertCheck = createCheckBox("Inv", new RCPosition(5, 7));

        invertCheck.addActionListener(l ->
                updateLabelsInverse(invertCheck.isSelected()));
    }

    private void initDisplay() {
        Display display = new Display(calcModel);

        calcModel.addCalcValueListener(display);

        panel.add(display, new RCPosition(1, 1));
    }


    private JButton createButton(String label, RCPosition position) {
        JButton button = new JButton(label);
        button.setBorder(BorderFactory.createLineBorder(new Color(125, 140, 156)));
        button.setBackground(new Color(211, 211, 225));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(button, position);

        return button;
    }

    private JButton insertDigit(String label, RCPosition position) {
        JButton button = createButton(label, position);
        button.setFont(button.getFont().deriveFont(30f));
        button.addActionListener(l -> calcModel.insertDigit(Integer.parseInt(label)));

        return button;
    }

    private JCheckBox createCheckBox(String label, RCPosition position) {
        JCheckBox jCheckBox = new JCheckBox(label);
        panel.add(jCheckBox, position);

        return jCheckBox;
    }

    /**
     * Metoda koja preimenuje sve gumbe s dvije vrijednosti.
     *
     * @param inverse zastavica je li inverse zahtjevan.
     */
    private void updateLabelsInverse(boolean inverse) {
        Map<JButton, String> map = inverse ? invertValues : normalValues;

        map.forEach(AbstractButton::setText);
    }

    /**
     * Obavljanje unarnog opratora nad modelom kalkulatora.
     *
     * @param operator unarni operator.
     */
    private void executeSpecialOneArgumentCommand(DoubleUnaryOperator operator) {
        calcModel.setValue(operator.applyAsDouble(calcModel.getValue()));
    }

    /**
     * Obavljanje binarnog operatora nad modelom kalkulatora.
     *
     * @param operator binarni kalkulator.
     */
    private void executeTwoArgumentCommand(DoubleBinaryOperator operator) {
        if (calcModel.isActiveOperandSet()) {
            double valueBefore = calcModel.getPendingBinaryOperation()
                    .applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue());
            calcModel.setValue(valueBefore);
        }

        calcModel.setActiveOperand(calcModel.getValue());
        calcModel.setPendingBinaryOperation(operator);

        calcModel.freezeScreen();
        calcModel.clear();
        calcModel.unfreezeScreen();
    }

    /**
     * Pokretanje swing aplikacije.
     *
     * @param args nije koristeno.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new Calculator().setVisible(true));
    }

    /**
     * Privatni razred koji modelira Display u kalkulatoru i nasljeduje {@link JLabel}
     * i sučelje {@link CalcValueListener} kako bi dojavile promjene nad brojevima.
     */
    private static class Display extends JLabel implements CalcValueListener {
        CalcModel model;

        /**
         * Konsruktor koji prima model nad kojim se citaju vrijednosti i boolean
         * zastavica je li display zamrznut.
         *
         * @param model CalcModel.
         */
        public Display(CalcModel model) {
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setOpaque(true);
            setBackground(new Color(255, 255, 0));
            setHorizontalAlignment(SwingConstants.RIGHT);
            setFont(getFont().deriveFont(30f));
            this.model = model;
            setText(model.toString());
        }

        @Override
        public void valueChanged(CalcModel model) {
                setText(model.toString());
        }
    }
}
