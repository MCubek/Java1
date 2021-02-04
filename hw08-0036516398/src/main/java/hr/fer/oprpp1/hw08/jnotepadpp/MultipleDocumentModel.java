package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;

/**
 * Sučelje koje propisuje metode za upravljanje
 * nula ili više dokumenata.
 * Također sučelje omogućuje koncept trenutnog dokumenta.
 *
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 28/12/2020
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
    SingleDocumentModel createNewDocument();
    SingleDocumentModel getCurrentDocument();
    SingleDocumentModel loadDocument(Path path);
    void saveDocument(SingleDocumentModel model, Path newPath);
    void closeDocument(SingleDocumentModel model);
    void addMultipleDocumentListener(MultipleDocumentListener l);
    void removeMultipleDocumentListener(MultipleDocumentListener l);
    int getNumberOfDocuments();
    SingleDocumentModel getDocument(int index);
}
