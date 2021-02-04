package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Klasa koja izbacuje zadnju kornjaču s kontektsa
 *
 * @author matejC
 */
public class PopCommand implements Command {
    @Override
    public void execute(Context context, Painter painter) {
        context.popState();
    }
}
