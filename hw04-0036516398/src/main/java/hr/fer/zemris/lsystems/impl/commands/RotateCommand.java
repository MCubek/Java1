package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Metoda koja rotira orijentaciju kornjače za zadani broj stupnjava
 *
 * @author matejC
 */
public class RotateCommand implements Command {
    private static final double DEG_TO_RAD = Math.PI / 180;
    private final double angleRadians;

    /**
     * Konstruktor koji prima kut u stupnjevima i rotira kornjaču na vrhu konteksta
     * @param angle kut u stupnjavima
     */
    public RotateCommand(double angle) {
        this.angleRadians = angle * DEG_TO_RAD;
    }

    @Override
    public void execute(Context context, Painter painter) {
        TurtleState turtleState = context.getCurrentState();
        turtleState.getOrientation().rotate(angleRadians);
    }
}
