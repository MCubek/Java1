package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;


/**
 * Razred koji crta pomocu kornjače s vrha konteksta
 *
 * @author matejC
 */
public class DrawCommand implements Command {
    private final double step;

    /**
     * Konstruktor s parametrom koji skalira pomak kornjače
     *
     * @param step broj koji slakira pomak kornjače
     */
    public DrawCommand(double step) {
        this.step = step;
    }

    @Override
    public void execute(Context context, Painter painter) {
        TurtleState turtleState = context.getCurrentState();
        Vector2D turtlePosition = turtleState.getCurrentPosition();

        Vector2D startPosition = turtlePosition.copy();

        turtlePosition.add(turtleState.getOrientation().scaled(step * turtleState.getDistance()));

        painter.drawLine(startPosition.getX(), startPosition.getY()
                , turtlePosition.getX(), turtlePosition.getY()
                , turtleState.getColor(), 1f);
    }
}
