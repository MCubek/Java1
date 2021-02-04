package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Slušač na promjene jezika.
 *
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 29/12/2020
 */

public interface ILocalizationListener {
    /**
     * Metoda koja se poziva kada se dogodi promjena
     * u trenutnom jeziku.
     */
    void localizationChanged();
}
