package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.nio.file.Path;

/**
 * Sučelje pristavlja model i metode upravljanja jednog dokumenta.
 * Sadrži njegove bitne metapodatke kao što su putanja ukoliko je učitan
 * iz datotečnog sustava, modifikacije i reference na komponente UI layouta.
 *
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 28/12/2020
 */
public interface SingleDocumentModel {
    /**
     * Metoda vraća {@link JTextArea} gdje se prikazuje dokument.
     *
     * @return JTextArea objekt dokumenta.
     */
    JTextArea getTextComponent();

    Path getFilePath();

    void setFilePath(Path path);

    boolean isModified();

    void setModified(boolean modified);

    void addSingleDocumentListener(SingleDocumentListener l);

    void removeSingleDocumentListener(SingleDocumentListener l);
}
