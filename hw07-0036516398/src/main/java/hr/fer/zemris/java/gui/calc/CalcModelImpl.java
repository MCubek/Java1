package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import javax.xml.stream.events.Characters;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

/**
 * Implementacija {@link CalcModel} za jednostavan kalkulator.
 *
 * @author MatejCubek
 * @project hw07-0036516398
 * @created 15/12/2020
 */
public class CalcModelImpl implements CalcModel {
    private final List<CalcValueListener> calcValueListeners;
    private boolean editable;
    private boolean positive;
    private String inputDigits;
    private double inputValue;

    private double activeOperand;
    private DoubleBinaryOperator pendingOperation;

    private boolean frozen;

    /**
     * Defaultni konstuktor koji stvara razred.
     */
    public CalcModelImpl() {
        this.calcValueListeners = new ArrayList<>();
        editable = true;
        positive = true;
        inputDigits = "";
        inputValue = 0;
        frozen = false;
    }

    @Override
    public void addCalcValueListener(CalcValueListener l) {
        calcValueListeners.add(l);
    }

    @Override
    public void removeCalcValueListener(CalcValueListener l) {
        calcValueListeners.remove(l);
    }

    @Override
    public double getValue() {
        return inputValue;
    }

    @Override
    public void setValue(double value) {
        this.inputValue = value;
        this.inputDigits = String.valueOf(Math.abs(value));
        this.positive = value >= 0;
        this.editable = false;

        informListeners();
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void clear() {
        this.inputDigits = "";
        this.inputValue = 0;
        this.positive = true;
        this.editable = true;

        informListeners();
    }

    @Override
    public void clearAll() {
        clear();
        this.activeOperand = 0;
        this.pendingOperation = null;
    }

    @Override
    public void swapSign() throws CalculatorInputException {
        if (! editable) throw new CalculatorInputException("Swap sign se mora primijenti na editable.");

        positive = ! positive;

        inputValue *= - 1;

        informListeners();
    }

    @Override
    public void insertDecimalPoint() throws CalculatorInputException {
        if (! editable) throw new CalculatorInputException("Insert decimal point se mora primijenti na editable.");
        if (inputDigits.isBlank()) throw new CalculatorInputException("Swap sign se ne može primijenit na prazno.");
        if (inputDigits.contains(".")) throw new CalculatorInputException("Broj već sadrži decimalnu točku");

        inputDigits += ".";

        informListeners();
    }

    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
        if (! isEditable()) throw new CalculatorInputException("Insert digit se mora primijeniti na editable.");

        try {
            String newValueString = inputDigits + digit;
            double newValueDouble = Double.parseDouble(newValueString);

            if (! Double.isFinite(newValueDouble))
                throw new CalculatorInputException("Broj prekoracuje oranicenja doubla.");

            //Dopusti samo nule ispred tocke
            if (newValueString.length() > 1 &&
                    newValueString.charAt(0) == '0' &&
                    Character.isDigit(newValueString.charAt(1)))
                newValueString = newValueString.substring(1);

            inputDigits = newValueString;

            inputValue = newValueDouble;
            if (! positive) inputValue *= - 1;
        } catch (NumberFormatException e) {
            throw new CalculatorInputException();
        }

        informListeners();
    }

    @Override
    public boolean isActiveOperandSet() {
        return activeOperand != 0;
    }

    @Override
    public double getActiveOperand() throws IllegalStateException {
        if (! isActiveOperandSet()) throw new IllegalStateException("Active operand nije postavljen.");
        return activeOperand;
    }

    @Override
    public void setActiveOperand(double activeOperand) {
        this.activeOperand = activeOperand;
    }

    @Override
    public void clearActiveOperand() {
        this.activeOperand = 0;
    }

    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        return this.pendingOperation;
    }

    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
        this.pendingOperation = op;
    }

    @Override
    public void freezeScreen() {
        frozen = true;
    }

    @Override
    public void unfreezeScreen() {
        frozen = false;
    }

    @Override
    public String toString() {
        if (! inputDigits.isBlank())
            return (positive ? "" : "-") + inputDigits;
        else
            return (positive ? "" : "-") + "0";
    }

    /**
     * Privatna metoda koja obavjestava sve listenere o promjeni.
     */
    private void informListeners() {
        if (! frozen)
            calcValueListeners.forEach(l -> l.valueChanged(this));
    }
}
