package hr.fer.oprpp1.math;

import java.util.Objects;

/**
 * Metoda dvodimenizonalnog vektora
 *
 * @author matej
 */
public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Metoda doda vektor iz parametra na vektor iz kojeg se poziva
     * @param offset vektor koji se pribraja
     * @throws NullPointerException ako je predan null
     */
    public void add(Vector2D offset) {
        Objects.requireNonNull(offset);
        x += offset.x;
        y += offset.y;
    }

    /**
     * Metoda zbraja dva vektora i vraca novi vektor kao njihov zbroj
     * @param offset vektor koji se pribraja onome nad kojem je pozvan
     * @return novi vektor koji je zbroj
     * @throws NullPointerException ako je predan null
     */
    public Vector2D added(Vector2D offset) {
        Objects.requireNonNull(offset);
        return new Vector2D(x + offset.x, y + offset.y);
    }

    /**
     * Metoda rotira vektor za zadani kut u radijanima
     * @param angle kut u radijanima za koji se rotira
     */
    public void rotate(double angle) {
        double newX, newY;
        newX = x * Math.cos(angle) - y * Math.sin(angle);
        newY = x * Math.sin(angle) + y * Math.cos(angle);
        x = newX;
        y = newY;
    }

    /**
     * Metoda stvara novi vektor sa parametrima zadanog i rotira ga za kut u radijanima
     * @param angle kut za koji se novi vektor rotira
     * @return novi vektor rotiran za zadani kut
     */
    public Vector2D rotated(double angle) {
        double newX, newY;
        newX = x * Math.cos(angle) - y * Math.sin(angle);
        newY = x * Math.sin(angle) + y * Math.cos(angle);
        return new Vector2D(newX, newY);
    }

    /**
     * Metoda skalira zadani vektor za zadani skalar
     * @param scalar broj za koji se skalira vektor
     */
    public void scale(double scalar) {
        x *= scalar;
        y *= scalar;
    }

    /**
     * Metoda stvara novi vektor i skalira ga za zadani skalar
     * @param scalar broj s kojim se skalira vektor
     * @return novi vektor skaliran
     */
    public Vector2D scaled(double scalar) {
        return new Vector2D(x * scalar, y * scalar);
    }

    /**
     * Metoda stvara novi vektor s parametrima zadanog i vraca ga
     * @return novi vektor kao kopija
     */
    public Vector2D copy() {
        return new Vector2D(x, y);
    }
}
