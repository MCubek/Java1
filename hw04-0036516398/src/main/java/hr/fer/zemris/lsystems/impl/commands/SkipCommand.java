package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Metoda kojojom se mice kornjaca bez da crta po ekranu.
 * Put kojim kornjaca skace moze se skalirati.
 *
 * @author matejC
 */
public class SkipCommand implements Command {
    private double step;

    /**
     * Konstruktor koji stvara klasu s korakom s kojim se mnozi daljina kornjace.
     *
     * @param step faktor s kojim se mnozi broj koliko kornjaca preskace
     */
    public SkipCommand(double step) {
        this.step = step;
    }

    @Override
    public void execute(Context context, Painter painter) {
        TurtleState turtleState = context.getCurrentState();
        Vector2D turtlePosition = turtleState.getCurrentPosition();

        turtlePosition.add(turtleState.getOrientation().scaled(step * turtleState.getDistance()));
    }
}
