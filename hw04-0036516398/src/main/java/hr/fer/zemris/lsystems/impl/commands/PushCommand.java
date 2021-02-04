package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Metoda koja stavlja kornjaču na vrh stoga kontektsa
 *
 * @author matejC
 */
public class PushCommand implements Command {
    @Override
    public void execute(Context context, Painter painter) {
        context.pushState(context.getCurrentState().copyOf());
    }
}
