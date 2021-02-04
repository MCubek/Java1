package hr.fer.oprpp1.hw01;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;


/**
 * Razred koji predstavlja kompleksan broj
 *
 * @author matej
 */
public class ComplexNumber {
    public static final double EXPONENT = 0.000001;
    private final double real;
    private final double imaginary;

    /**
     * Konstruktor koji stvara kompleksni broj na osnovi realnog i imaginarnog djela
     *
     * @param real      realni dio kompleksnog dio
     * @param imaginary imaginaran dio kompleksnog broja
     */
    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Metoda stvara novi kompleksan broj koji sadrži samo realni dio, a imaginarni je 0
     *
     * @param real realni dio komplesnog broja
     * @return novi kompleksni broj s samo realnim djelom
     */
    public static ComplexNumber fromReal(double real) {
        return new ComplexNumber(real, 0);
    }

    /**
     * Metoda stvara novi kompleksan broj koji sadrži samo imaginarni dio, a realni je 0
     *
     * @param imaginary imaginarni dio komplesnog broja
     * @return novi kompleksni broj s samo imaginarnim djelom
     */
    public static ComplexNumber fromImaginary(double imaginary) {
        return new ComplexNumber(0, imaginary);
    }

    /**
     * Metoda stvara novi komplesan dio iz trigonometrijskog oblika pomoću kuta i modula.
     * Kut kompleksnog broja mora biti u radijanima
     *
     * @param magnitude modul kompleksnog broja
     * @param angle     kut kompleksnog broja u radijanima
     * @return novi kompleksni broj
     */
    public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
        return new ComplexNumber(magnitude * cos(angle), magnitude * sin(angle));
    }

    /**
     * Metoda stvara novi kompleksni broj iz Stringa
     * Npr. (1.2+2.1i), (-2.5), (i), (+22-12i)
     *
     * @param s string koji predstavlja kompleksan broj
     * @return novi kompleksan broj
     */
    public static ComplexNumber parse(String s) {
        String string = s.strip();

        if (string.matches(".*(\\+\\+|--|\\+-|-\\+).*") || ! string.matches("[\\d.i+-]+"))
            throw new IllegalArgumentException("String is doesnt match required form");

        double real = 0.0, imaginary = 0.0;

        Pattern pattern = Pattern.compile("[+|-]?\\d+\\.?\\d*i?");
        Matcher matcher = pattern.matcher(s);
        Pattern patternOnlyI = Pattern.compile("-i|\\+i|^i");
        Matcher matcherOnlyI = patternOnlyI.matcher(s);

        String value;
        int counter = 0;

        while (matcher.find()) {
            counter++;
            value = matcher.group();
            if (value.contains("i")) {
                imaginary = Double.parseDouble(value.replace("i", ""));
            } else {
                real = Double.parseDouble(value);
            }
        }

        while (matcherOnlyI.find()) {
            counter++;
            value = matcherOnlyI.group();
            imaginary = Double.parseDouble(value.replace("i", "1.0"));
        }

        if (counter > 2)
            throw new IllegalArgumentException("String is invalid, contains more then 2 numbers!");

        return new ComplexNumber(real, imaginary);
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    /**
     * Metoda vraća modul kompleksnog broja.
     *
     * @return modul
     */
    public double getMagnitude() {
        return hypot(real, imaginary);
    }

    /**
     * Metoda vraća kut kompleksnog broja u radijanima.
     *
     * @return kut u radijanima
     */
    public double getAngle() {
        double angle;
        if (real == 0 || imaginary == 0) {
            if (imaginary == 0) return real >= 0 ? 0 : PI;
            return imaginary >= 0 ? PI / 2 : 3 * PI / 2;
        }
        angle = atan(imaginary / real);

        if (real < 0)
            angle = angle + PI;

        if (angle < 0)
            angle += 2 * PI;

        return angle;
    }

    /**
     * Metoda zbraja dva kompleksna vroja i vraća novi kompleksni broj.
     *
     * @param c broj s kojim se zbraja kompleksni broj
     * @return novi zbrojeni kompleksni broj
     * @throws NullPointerException ako je predan null
     */
    public ComplexNumber add(ComplexNumber c) {
        if (c == null) throw new NullPointerException("Complex number must not be null!");
        return new ComplexNumber(real + c.real, imaginary + c.imaginary);
    }

    /**
     * Metoda oduzima dva kompleksna vroja i vraća novi kompleksni broj.
     *
     * @param c broj s kojim se oduzima kompleksni broj
     * @return novi oduzeti kompleksni broj
     * @throws NullPointerException ako je predan null
     */
    public ComplexNumber sub(ComplexNumber c) {
        if (c == null) throw new NullPointerException("Complex number must not be null!");
        return new ComplexNumber(real - c.real, imaginary - c.imaginary);
    }

    /**
     * Metoda množi dva kompleksna vroja i vraća novi kompleksni broj.
     *
     * @param c broj s kojim se množi kompleksni broj
     * @return novi pomnoženi kompleksni broj
     * @throws NullPointerException ako je predan null
     */
    public ComplexNumber mul(ComplexNumber c) {
        if (c == null) throw new NullPointerException("Complex number must not be null!");
        //z1*z2=x1*x2-y1*x2+(x1*y2+x2*y1)i
        return new ComplexNumber(real * c.real - imaginary * c.imaginary, real * c.imaginary + imaginary * c.real);
    }

    /**
     * Metoda dijeli dva kompleksna broja i vraća novi kompleksni broj.
     *
     * @param c broj s kojim se dijeli kompleksni broj
     * @return novi podijeljeni kompleksni broj
     * @throws NullPointerException ako je predan null
     */
    public ComplexNumber div(ComplexNumber c) {
        if (c == null) throw new NullPointerException("Complex number must not be null!");
        double divisor = c.real * c.real + c.imaginary * c.imaginary;

        if (abs(divisor) < EXPONENT)
            throw new ArithmeticException("Can't divide with 0");

        double realPart = (real * c.real + imaginary * c.imaginary) / divisor;
        double imaginaryPart = (imaginary * c.real - real * c.imaginary) / divisor;
        return new ComplexNumber(realPart, imaginaryPart);
    }

    /**
     * Metoda računa n-tu potenciju kompleksnog broja i vraća ju kao novi kompleksni broj.
     *
     * @param n potencija kompleksnog broja
     * @return novi kompleksni broj koji je n-ta potencija
     * @throws IllegalArgumentException ako je predan eksponent manji od 0
     */
    public ComplexNumber power(int n) {
        if (n < 0) throw new IllegalArgumentException("Exponent must be >=0!");
        double angle = getAngle();
        double magnitude = getMagnitude();
        return ComplexNumber.fromMagnitudeAndAngle(pow(magnitude, n), n * angle);

    }

    /**
     * Metoda računa n-ti kroijen kompleksnog broja i vraća ga kao novi kompleksni broj.
     *
     * @param n korijen kompleksnog broja
     * @return novi kompleksni broj koji je n-ti korijen
     * @throws IllegalArgumentException ako je predan eksponent manji ili jednak 0
     */
    public ComplexNumber[] root(int n) {
        if (n <= 0) throw new IllegalArgumentException("Root must be > 0!");
        double angle = getAngle();
        double magnitude = getMagnitude();

        ComplexNumber[] array = new ComplexNumber[n];

        for (int i = 0; i < n; i++) {
            array[i] = ComplexNumber.fromMagnitudeAndAngle(pow(magnitude, 1.0 / n), (angle + 2 * i * PI) / n);
        }
        return array;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (real != 0)
            stringBuilder.append(String.format(Locale.ROOT,"%.2f", real));
        if (imaginary != 0) {
            if (imaginary > 0)
                stringBuilder.append("+");
            stringBuilder.append(String.format(Locale.ROOT,"%.2f", imaginary)).append("i");
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComplexNumber that = (ComplexNumber) o;

        if (Math.abs(that.real - real) >= EXPONENT) return false;
        return Math.abs(that.imaginary - imaginary) <= EXPONENT;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(real);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(imaginary);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
