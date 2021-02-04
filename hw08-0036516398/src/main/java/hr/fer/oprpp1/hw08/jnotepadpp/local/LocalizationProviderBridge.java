package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Dekorator {@link LocalizationProvider} koji nam omogućuje pravilno upravljanje memorijom.
 *
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 29/12/2020
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
    private boolean connected;
    private final ILocalizationListener listener;
    private final ILocalizationProvider parent;

    public LocalizationProviderBridge(ILocalizationProvider parent) {
        this.parent = parent;
        this.listener = new ILocalizationListener() {
            @Override
            public void localizationChanged() {
                fire();
            }
        };
    }

    /**
     * Spoji se na LocalizationProvider
     */
    public void connect() {
        if (connected) return;

        parent.addLocalizationListener(listener);
        connected = true;
    }

    /**
     * Odspoji se sa LocalizationProvidera
     */
    public void disconnect() {
        parent.removeLocalizationListener(listener);
        connected = false;
    }

    @Override
    public String getString(String key) {
        return parent.getString(key);
    }
}
