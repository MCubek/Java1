package hr.fer.oprpp1.hw08.jnotepadpp.local.swing;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProviderBridge;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Swing komponenta koja spaja {@link JFrame} i {@link ILocalizationProvider}
 * te se brine za spajanje i odspajanje poslužitelja lokalizacije kako bi se prailno
 * pobrisao garbage collectorom.
 *
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 29/12/2020
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

    public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
        super(parent);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                connect();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                disconnect();
            }
        });
    }
}
