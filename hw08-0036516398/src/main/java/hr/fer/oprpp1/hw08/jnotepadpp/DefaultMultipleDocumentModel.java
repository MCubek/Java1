package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Klasa koja implementira sučelje {@link MultipleDocumentModel} i nasljeđuje
 * {@link JTabbedPane} te se brine za njihovo prikazivanje unutar aplikacije.
 *
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 28/12/2020
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
    @Serial
    private static final long serialVersionUID = - 1714837088850457492L;

    private static final String notSavedIcon = "icons/redFloppy.png";
    private static final String savedIcon = "icons/greenFloppy.png";

    private final List<SingleDocumentModel> singleDocumentModels;
    private SingleDocumentModel currentDocument;

    private final List<MultipleDocumentListener> multipleDocumentListenerList;

    private final ILocalizationProvider localizationProvider;
    private final JFrame frame;

    public DefaultMultipleDocumentModel(ILocalizationProvider localizationProvider, JFrame frame) {
        this.localizationProvider = localizationProvider;
        this.frame = frame;
        singleDocumentModels = new ArrayList<>();
        multipleDocumentListenerList = new ArrayList<>();

        localizationProvider.addLocalizationListener(new ILocalizationListener() {
            @Override
            public void localizationChanged() {
                updateWindowTitle();
                updateTabTitlesAndIcons();
            }
        });

        addChangeListener(e -> {
            if (getSelectedIndex() >= 0) {
                var previous = currentDocument;
                currentDocument = singleDocumentModels.get(getSelectedIndex());

                notifyListenersDocumentChanged(previous, currentDocument);

                updateWindowTitle();
            }
        });
    }

    @Override
    public SingleDocumentModel createNewDocument() {
        var prevDocument = currentDocument;
        currentDocument = createDocumentModel(null, "");

        notifyListenersDocumentAdded(currentDocument);
        notifyListenersDocumentChanged(prevDocument, currentDocument);
        return currentDocument;
    }

    @Override
    public SingleDocumentModel getCurrentDocument() {
        return currentDocument;
    }

    @Override
    public SingleDocumentModel loadDocument(Path path) {
        if (! Files.isReadable(path)) throw new IllegalArgumentException("Path is not readable.");

        SingleDocumentModel prevDocument = currentDocument;

        var existingDocument = singleDocumentModels.stream()
                .filter(doc -> {
                    Path docPath = doc.getFilePath();
                    return docPath != null && docPath.equals(path);
                })
                .findAny();

        if (existingDocument.isPresent()) {
            currentDocument = existingDocument.get();
            setSelectedIndex(singleDocumentModels.indexOf(currentDocument));

            notifyListenersDocumentChanged(prevDocument, currentDocument);
            currentDocument.addSingleDocumentListener(singleDocumentListener);

            return currentDocument;
        }

        try {
            var document = createDocumentModel(path, Files.readString(path));

            currentDocument = document;

            notifyListenersDocumentChanged(prevDocument, document);
            notifyListenersDocumentAdded(document);

            document.addSingleDocumentListener(singleDocumentListener);

            return document;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) {
        Path path = newPath == null ? model.getFilePath() : newPath;

        if (singleDocumentModels.stream()
                .filter(doc -> model != doc)
                .map(SingleDocumentModel::getFilePath)
                .anyMatch(path::equals))
            throw new IllegalArgumentException("Path already exists.");

        try {
            Files.writeString(path, model.getTextComponent().getText());

            model.setFilePath(path);
        } catch (IOException e) {
            System.err.println("Error while saving Document.");
        }
    }

    @Override
    public void closeDocument(SingleDocumentModel model) {
        removeTabAt(singleDocumentModels.indexOf(model));
        singleDocumentModels.remove(model);

        int lastIndex = singleDocumentModels.size() - 1;

        if (singleDocumentModels.size() >= 1) {
            currentDocument = singleDocumentModels.get(lastIndex);
        } else currentDocument = null;

        setSelectedIndex(lastIndex);

        notifyListenersDocumentRemoved(model);
        model.removeSingleDocumentListener(singleDocumentListener);
        updateWindowTitle();
    }

    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        multipleDocumentListenerList.add(l);
    }

    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        multipleDocumentListenerList.remove(l);
    }

    @Override
    public int getNumberOfDocuments() {
        return singleDocumentModels.size();
    }

    @Override
    public SingleDocumentModel getDocument(int index) {
        return singleDocumentModels.get(Objects.checkIndex(index, singleDocumentModels.size()));
    }

    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return singleDocumentModels.iterator();
    }

    private void notifyListenersDocumentChanged(SingleDocumentModel previousDocument, SingleDocumentModel currentDocument) {
        if (previousDocument == null && currentDocument == null)
            throw new IllegalArgumentException("Both previous and current documents can't be null.");
        multipleDocumentListenerList.forEach(l -> l.currentDocumentChanged(previousDocument, currentDocument));
    }

    private void notifyListenersDocumentAdded(SingleDocumentModel document) {
        multipleDocumentListenerList.forEach(l -> l.documentAdded(document));
    }

    private void notifyListenersDocumentRemoved(SingleDocumentModel document) {
        multipleDocumentListenerList.forEach(l -> l.documentRemoved(document));
    }

    private SingleDocumentModel createDocumentModel(Path path, String textContent) {
        SingleDocumentModel documentModel = new DefaultSingleDocumentModel(path, textContent);
        singleDocumentModels.add(documentModel);

        documentModel.addSingleDocumentListener(singleDocumentListener);

        createDocumentTab(documentModel);

        return documentModel;
    }

    private void createDocumentTab(SingleDocumentModel singleDocumentModel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(1, 3, 1, 3));
        panel.add(new JScrollPane(singleDocumentModel.getTextComponent()), BorderLayout.CENTER);

        Path filepath = singleDocumentModel.getFilePath();


        String newDocument = localizationProvider.getString("unnamed");

        addTab(filepath == null ? String.format("(%s)", newDocument) : filepath.getFileName().toString(), null, panel,
                filepath == null ? String.format("(%s)", newDocument) : filepath.toAbsolutePath().toString());

        setSelectedComponent(panel);

        setIconAt(singleDocumentModels.indexOf(singleDocumentModel), getImageIcon(singleDocumentModel.isModified() ? notSavedIcon : savedIcon));
    }

    private ImageIcon getImageIcon(String path) {
        InputStream inputStream = this.getClass().getResourceAsStream(path);

        if (inputStream == null) throw new IllegalArgumentException("Path does not exist.");

        try {
            byte[] bytes = inputStream.readAllBytes();
            inputStream.close();

            return new ImageIcon(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateWindowTitle() {
        String newDocument = localizationProvider.getString("unnamed");

        if (currentDocument == null) {
            frame.setTitle("JNotepad++");
            return;
        }

        Path path = currentDocument.getFilePath();
        frame.setTitle((path == null ? String.format("(%s)", newDocument) : path.toAbsolutePath().toString()) + " - JNotepad++");
    }

    private void updateTabTitlesAndIcons() {
        String newDocument = localizationProvider.getString("unnamed");

        singleDocumentModels
                .forEach(model -> {
                    int index = singleDocumentModels.indexOf(model);
                    if (model.getFilePath() == null) {
                        setTitleAt(index, String.format("(%s)", newDocument));
                        setToolTipTextAt(index, String.format("(%s)", newDocument));
                    } else {
                        setTitleAt(index, model.getFilePath().getFileName().toString());
                        setToolTipTextAt(index, model.getFilePath().toAbsolutePath().toString());
                    }
                    setIconAt(index, getImageIcon(model.isModified() ? notSavedIcon : savedIcon));
                });
    }

    private final SingleDocumentListener singleDocumentListener = new SingleDocumentListener() {
        @Override
        public void documentModifyStatusUpdated(SingleDocumentModel model) {
            var index = singleDocumentModels.indexOf(model);

            setIconAt(index, getImageIcon(model.isModified() ? notSavedIcon : savedIcon));

            String title = getTitleAt(index);
            setTitleAt(index, title + (model.isModified() && ! title.contains("*") ? "*" : ""));
        }

        @Override
        public void documentFilePathUpdated(SingleDocumentModel model) {

            updateTabTitlesAndIcons();
            updateWindowTitle();
        }
    };
}
