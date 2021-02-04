package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

import java.awt.*;

/**
 * Klasa sučelja <code>Command</code>
 * Razrdd boja crtež bojom koju sadrži.
 *
 * @author matejC
 */
public class ColorCommand implements Command {
    private final Color color;

    public ColorCommand(Color color) {
        this.color = color;
    }

    @Override
    public void execute(Context context, Painter painter) {
        TurtleState turtleState = context.getCurrentState();
        turtleState.setColor(color);
    }
}
