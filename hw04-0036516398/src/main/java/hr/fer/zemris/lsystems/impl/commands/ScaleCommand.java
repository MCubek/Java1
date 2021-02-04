package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Metoda skalira kornjaču s vrha stoga na kontekstu
 * za zadani skalator.
 *
 * @author matejC
 */
public class ScaleCommand implements Command {
    private final double factor;

    /**
     * Konstrktor koji prima faktor s kojim se mnozi kornjaca s vrha stoga.
     * @param factor faktor s kojim se mnozi kornjaca
     */
    public ScaleCommand(double factor) {
        this.factor = factor;
    }

    @Override
    public void execute(Context context, Painter painter) {
        TurtleState turtleState = context.getCurrentState();
        turtleState.setDistance(turtleState.getDistance() * factor);
    }
}
