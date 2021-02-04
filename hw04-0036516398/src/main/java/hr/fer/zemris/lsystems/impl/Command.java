package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * Sučelje komandi grafičkog sučelja
 *
 * @author matejC
 */
@FunctionalInterface
public interface Command {
    /**
     * Metoda koja izvodi zadanu radnju nad kontekstom kornjača.
     * Kako bi izvela radnju koristi <code>Painter</code>
     *
     * @param context kontekst kornjača
     * @param painter objekt koji izvodi crtanje po grafičkom sučelju
     */
    void execute(Context context, Painter painter);
}
