package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Sučelje koje omogućuje pretplatu na promjenu lokalizacije
 * i traženje prijevoda.
 *
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 29/12/2020
 */
public interface ILocalizationProvider {
    /**
     * Metoda koja prevodi ključ u trenutni jezik.
     *
     * @param key ključ koji se želi prevesti.
     * @return Prijevod u trenutnom jeziku.
     */
    String getString(String key);

    /**
     * Metoda koja dodaje slušača.
     *
     * @param listener Slušač na promjene jezika.
     */
    void addLocalizationListener(ILocalizationListener listener);

    /**
     * Metoda koja uklanja slušača.
     *
     * @param listener Slušak na promjene jezika koji se miče.
     */
    void removeLocalizationListener(ILocalizationListener listener);
}
