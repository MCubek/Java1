package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstraktna implementacija klase {@link ILocalizationProvider} koja implementira
 * prijavljivanje listenera i njohovo uklanjanje te obavijestavanje svim njima
 * o promjeni.
 * Klasa ne impelementira metodu <code>getString</code>.
 *
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 29/12/2020
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
    private final List<ILocalizationListener> listeners;

    public AbstractLocalizationProvider() {
        listeners = new ArrayList<>();
    }

    @Override
    public void addLocalizationListener(ILocalizationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLocalizationListener(ILocalizationListener listener) {
        listeners.remove(listener);
    }

    /**
     * Metoda obaviještava svakog listenera da se je dogodila promjena.
     */
    public void fire() {
        listeners.forEach(ILocalizationListener::localizationChanged);
    }
}
