package hr.fer.oprpp1.hw08.jnotepadpp.local.swing;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

import javax.swing.*;

/**
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 30/12/2020
 */
public class LocalizableStatusLabel extends JLabel {
    private final String key;
    private final ILocalizationProvider localizationProvider;
    private int value;

    public LocalizableStatusLabel(String key, ILocalizationProvider localizationProvider) {
        this.key = key;
        this.localizationProvider = localizationProvider;
        value = 0;

        localizationProvider.addLocalizationListener(this::updateLabel);
        updateLabel();
    }

    private void updateLabel() {
        super.setText(localizationProvider.getString(key) + ":" + value);
    }

    public void setValue(int value) {
        this.value = value;
        updateLabel();
    }

}
