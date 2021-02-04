package hr.fer.oprpp1.hw08.jnotepadpp.local.swing;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

import javax.swing.*;
import java.io.Serial;

/**
 * Omotač klase {@link AbstractAction} koji se brine o lokalizaciji naziva i opisa.
 *
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 29/12/2020
 */
public abstract class LocalizableAction extends AbstractAction {
    @Serial
    private static final long serialVersionUID = - 3346234381358290599L;

    private final String key;
    private final ILocalizationProvider localizationProvider;

    /**
     * Jedini konstruktor koji stvara abstraktnu akciju
     * @param key ključ vrijednosti akcije
     * @param localizationProvider poslužitelj lokalizacije
     */
    public LocalizableAction(String key, ILocalizationProvider localizationProvider) {
        this.key = key;
        this.localizationProvider = localizationProvider;

        updateLocalizationValues();

        localizationProvider.addLocalizationListener(this::updateLocalizationValues);
    }

    private void updateLocalizationValues() {
        putValue(NAME, localizationProvider.getString(key));
        //putValue(SHORT_DESCRIPTION, localizationProvider.getString(key + "shortDescription"));
    }
}
