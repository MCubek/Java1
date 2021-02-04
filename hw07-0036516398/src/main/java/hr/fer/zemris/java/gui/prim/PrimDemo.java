package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Program koji se pokreće iz naredbenog retka bez ikakvih argumenata.
 * Po pokretanju otvara prozor u kojem se prikazuju dvije liste
 * (jednako visoke, jednako široke, rastegnute preko čitave površine prozora izuzev donjeg ruba).
 *
 * @author MatejCubek
 * @project hw07-0036516398
 * @created 16/12/2020
 */
public class PrimDemo extends JFrame {

    /**
     * Defaultni konstruktor.
     */
    public PrimDemo() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("PrimDemo");
        setSize(500, 500);

        initGUI();
    }

    private void initGUI() {
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        PrimListModel primListModel = new PrimListModel();
        JList<Integer> list1 = new JList<>(primListModel);
        JList<Integer> list2 = new JList<>(primListModel);

        JPanel panel = new JPanel(new GridLayout(1, 0));

        panel.add(new JScrollPane(list1));
        panel.add(new JScrollPane(list2));

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(l -> primListModel.next());

        container.add(panel, BorderLayout.CENTER);
        container.add(nextButton, BorderLayout.PAGE_END);
    }

    /**
     * Ugnijezdena klasa koja predstavlja model prim brojeva.
     */
    private static class PrimListModel implements ListModel<Integer> {
        private final LinkedList<Integer> list;
        private final List<ListDataListener> listDataListeners;

        /**
         * Defaultni konstruktor.
         */
        public PrimListModel() {
            list = new LinkedList<>();
            list.add(1);

            listDataListeners = new ArrayList<>();
        }

        /**
         * Metoda koja u model dodaje idući prim broj.
         */
        public void next() {
            int num = list.getLast() + 1;
            while (true) {
                boolean valid = true;
                for (int i = 2; i <= Math.sqrt(num); i++) {
                    if (num % i == 0) {
                        valid = false;
                        break;
                    }
                }
                if (valid) break;
                num++;
            }

            list.add(num);
            informListenersAddedElementToEnd();
        }

        private void informListenersAddedElementToEnd() {
            ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, list.size() - 1, list.size() - 1);

            listDataListeners.forEach(l -> l.contentsChanged(e));
        }


        @Override
        public int getSize() {
            return list.size();
        }

        @Override
        public Integer getElementAt(int index) {
            Objects.checkIndex(index, list.size());
            return list.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            listDataListeners.add(l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            listDataListeners.remove(l);
        }
    }

    /**
     * Main metoda koja pokreće {@link PrimDemo}.
     *
     * @param args Nije korišteno.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PrimDemo().setVisible(true));
    }

}
