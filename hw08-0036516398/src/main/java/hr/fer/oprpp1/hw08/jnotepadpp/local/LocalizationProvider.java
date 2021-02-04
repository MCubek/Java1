package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Klasa singleton obrasca uporabe koja nasljeduje {@link AbstractLocalizationProvider}
 * i bavi se prijevodom svih ključeva u dani jezik.
 *
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 29/12/2020
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

    private static final LocalizationProvider singletonInstance = new LocalizationProvider();
    private String language;
    private ResourceBundle bundle;

    private LocalizationProvider() {
        setLanguage("en");
    }

    public static LocalizationProvider getInstance() {
        return singletonInstance;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
        bundle = ResourceBundle.getBundle(this.getClass().getPackageName() + ".prijevodi", Locale.forLanguageTag(language));
        fire();
    }

    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }
}
