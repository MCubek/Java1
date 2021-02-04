package hr.fer.oprpp1.hw08.jnotepadpp;

/**
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 28/12/2020
 */
public interface SingleDocumentListener {
    void documentModifyStatusUpdated(SingleDocumentModel model);
    void documentFilePathUpdated(SingleDocumentModel model);
}
