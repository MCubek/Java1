package hr.fer.zemris.lsystems.impl;

import hr.fer.oprpp1.math.Vector2D;

import java.awt.*;
import java.util.Objects;

/**
 * Razred kornjače na ekranu.
 * Korjnača ima poziciju, orijentaciju, boju i duljinu skoka.
 *
 * @author matejC
 */
public class TurtleState {
    private Vector2D currentPosition;
    private Vector2D orientation;
    private Color color;
    private double distance;

    /**
     * Konstruktor kornjače.
     * Stvara novu kornjaču s upisanim parametrima.
     *
     * @param currentPosition ternutna pozicija kornjace kao vector2d
     * @param orientation     orijentacija kornjace
     * @param color           boja koju korjnaca stvara
     * @param distance        daljina koju kornjaca preskace
     */
    public TurtleState(Vector2D currentPosition, Vector2D orientation, Color color, double distance) {
        this.currentPosition = Objects.requireNonNull(currentPosition);
        this.orientation = Objects.requireNonNull(orientation);
        this.color = Objects.requireNonNull(color);
        this.distance = distance;
    }

    /**
     * Metoda koja stvara novu kornjaču s istim atributima stare i
     * vraća njenu referencu.
     *
     * @return nova kornjača s istim atributima stare
     */
    public TurtleState copyOf() {
        return new TurtleState(currentPosition.copy(), orientation.copy(), color, distance);
    }

    public Vector2D getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Vector2D currentPosition) {
        this.currentPosition = Objects.requireNonNull(currentPosition);
    }

    public Vector2D getOrientation() {
        return orientation;
    }

    public void setOrientation(Vector2D orientation) {
        this.orientation = Objects.requireNonNull(orientation);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = Objects.requireNonNull(color);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
