package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Defaultna impementacija sučelja {@link SingleDocumentModel}.
 * 
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 28/12/2020
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {

    private final JTextArea textArea;
    private Path path;
    private boolean modified;
    private final List<SingleDocumentListener> documentListenerList;

    public DefaultSingleDocumentModel(Path path, String textContent) {
        this.textArea = new JTextArea(textContent);
        this.path = path;

        documentListenerList = new ArrayList<>();
        modified = false;

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setModified(true);
                notifyModified();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setModified(true);
                notifyModified();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setModified(true);
                notifyModified();
            }
        });
        notifyPathChanged();
    }

    @Override
    public JTextArea getTextComponent() {
        return textArea;
    }

    @Override
    public Path getFilePath() {
        return path;
    }

    @Override
    public void setFilePath(Path path) {
        this.path = Objects.requireNonNull(path);
        modified = false;
        notifyPathChanged();
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
        notifyModified();
    }

    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        documentListenerList.add(l);
    }

    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        documentListenerList.remove(l);
    }

    private void notifyModified() {
        documentListenerList.forEach(l -> l.documentModifyStatusUpdated(this));
    }

    private void notifyPathChanged() {
        documentListenerList.forEach(l -> l.documentFilePathUpdated(this));
    }
}
