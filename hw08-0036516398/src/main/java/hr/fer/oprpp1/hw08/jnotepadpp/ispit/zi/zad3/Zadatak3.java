package hr.fer.oprpp1.hw08.jnotepadpp.ispit.zi.zad3;

import hr.fer.oprpp1.hw08.jnotepadpp.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.SingleDocumentModel;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 26/01/2021
 */
public class Zadatak3 extends JDialog {
    private final MultipleDocumentModel model;
    private DefaultListModel<String> listModel;
    JList<String> list;

    public Zadatak3(MultipleDocumentModel model) {
        this.model = Objects.requireNonNull(model);
        setModal(false);
        setSize(500, 500);

        intGUI();

        model.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                Zadatak3.this.updateList();
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                Zadatak3.this.updateList();
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {
                Zadatak3.this.updateList();
            }
        });
    }

    private void intGUI() {
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);

        updateList();

        JPanel panel = new JPanel();
        panel.add(new JScrollPane(list));

        container.add(panel, BorderLayout.CENTER);

    }

    private void updateList() {
        listModel.clear();
        for (int i = 0; i < model.getNumberOfDocuments(); i++) {
            var doc = model.getDocument(i);
            if(doc.getFilePath()==null)
                continue;
            listModel.addElement(doc.getFilePath().toAbsolutePath().toString());
            if (doc.equals(model.getCurrentDocument())) {
                list.setSelectedIndex(i);
            }
        }
    }
}
