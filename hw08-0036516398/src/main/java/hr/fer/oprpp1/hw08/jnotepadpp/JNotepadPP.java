package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.ispit.zi.zad1.ExamZad01_1;
import hr.fer.oprpp1.hw08.jnotepadpp.ispit.zi.zad1.ExamZad01_2;
import hr.fer.oprpp1.hw08.jnotepadpp.ispit.zi.zad2.StupciComponent;
import hr.fer.oprpp1.hw08.jnotepadpp.ispit.zi.zad3.Zadatak3;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.swing.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.swing.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.local.swing.LocalizableStatusLabel;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Simple file text editor zvan JNotepad++ koji omogućuje editanje više prozora odjednom.
 *
 * @author MatejCubek
 * @project hw08-0036516398
 * @created 28/12/2020
 */
public class JNotepadPP extends JFrame {

    @Serial
    private static final long serialVersionUID = 9077301190283082844L;

    private final MultipleDocumentModel model;

    private final ILocalizationProvider localizationProvider;
    private Action croatianLocalizationAction;
    private Action englishLocalizationAction;
    private Action germanLocalizationAction;

    private Action createNewAction;
    private Action openExistingAction;
    private Action saveAction;
    private Action saveAsAction;
    private Action closeAction;
    private Action cutAction;
    private Action copyAction;
    private Action pasteAction;
    private Action statisticsAction;
    private Action exitAction;

    private Action fileMenuAction;
    private Action languageMenuAction;
    private Action editMenuAction;
    private Action ispitMenuAction;

    private Action changeCaseMenuAction;
    private Action sortMenuAction;

    private LocalizableStatusLabel lengthStatusLabel;
    private LocalizableStatusLabel lineStatusLabel;
    private LocalizableStatusLabel columnStatusLabel;
    private LocalizableStatusLabel selectionStatusLabel;

    private Action toUpperCaseAction;
    private Action toLowerCaseAction;
    private Action invertCaseAction;

    private Action sortAscendingAction;
    private Action sortDescendingAction;
    private Action uniqueAction;

    private Action zadatak11Action;
    private Action zadatak12Action;
    private Action zadatak2Action;
    private Action zadatak3Action;

    private JPanel centerPanel;

    /**
     * Jedini konstuktor koji gradi aplikaciju
     */
    public JNotepadPP() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("JNotepad++");
        setSize(1280, 720);
        setLocationRelativeTo(null);

        //Exit operacija kada zelimo zatvoriti prozor x-om
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitProcedure();
            }
        });

        this.localizationProvider
                = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

        this.model = new DefaultMultipleDocumentModel(localizationProvider, this);

        initGUI();

        CaretListener caretListener = l -> updateStatusBar();

        //Postavljanje slusaca na Caret kada stvaramo i mijenjamo dokument koji azurira status bar.
        model.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                updateStatusBar();
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                model.getTextComponent().addCaretListener(caretListener);
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {
                model.getTextComponent().removeCaretListener(caretListener);
            }
        });
    }

    /**
     * Metoda generiranje UI elemenata
     */
    private void initGUI() {
        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());
        centerPanel = new JPanel(new BorderLayout());

        centerPanel.add((DefaultMultipleDocumentModel) model, BorderLayout.CENTER);

        createActions();
        configureMnemonicsOnActions();
        createMenu();
        createToolbar();
        createStatusBar();

        container.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Metoda koja stvara sve akcije.
     */
    private void createActions() {
        createNewAction = new LocalizableAction("new", localizationProvider) {
            @Serial
            private static final long serialVersionUID = 7537574392251198908L;

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibilityOfActions(true);

                model.createNewDocument();
            }
        };
        createNewAction.setEnabled(true);

        openExistingAction = new LocalizableAction("open", localizationProvider) {
            @Serial
            private static final long serialVersionUID = 8133125205867736112L;

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibilityOfActions(true);

                Path path = getFromFileChooser("openDialog", FileChooserOptions.OPEN);

                if (path == null) return;

                model.loadDocument(path);
            }
        };
        openExistingAction.setEnabled(true);

        saveAction = new LocalizableAction("save", localizationProvider) {
            @Serial
            private static final long serialVersionUID = 3073251773365819531L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrentDocument().getFilePath() == null) {
                    saveAsAction.actionPerformed(e);
                    return;
                }

                //Null znaci trenutni path
                model.saveDocument(model.getCurrentDocument(), null);
            }
        };
        saveAction.setEnabled(false);

        saveAsAction = new LocalizableAction("saveAs", localizationProvider) {
            @Serial
            private static final long serialVersionUID = - 8081958461087599759L;

            @Override
            public void actionPerformed(ActionEvent e) {
                Path path = getFromFileChooser("saveAsDialog", FileChooserOptions.SAVE);

                if (path == null) return;

                //Vec postoji datoteka pa mozda treba prepisati
                if (Files.exists(path)
                        && askConfirmationDialog("documentExistsDialog") != JOptionPane.YES_OPTION) return;

                model.saveDocument(model.getCurrentDocument(), path);
            }
        };
        saveAsAction.setEnabled(false);

        closeAction = new LocalizableAction("close", localizationProvider) {
            @Serial
            private static final long serialVersionUID = - 1694728537338560025L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrentDocument().isModified()) {
                    var dialog = askConfirmationDialog("saveBeforeExitDialog");
                    switch (dialog) {
                        case JOptionPane.YES_OPTION -> {
                            documentSaveProcedure(model.getCurrentDocument());
                            model.closeDocument(model.getCurrentDocument());
                        }
                        case JOptionPane.NO_OPTION -> model.closeDocument(model.getCurrentDocument());
                    }
                } else {
                    model.closeDocument(model.getCurrentDocument());
                }

                if (model.getNumberOfDocuments() == 0)
                    setVisibilityOfActions(false);

                updateStatusBar();
            }
        };
        closeAction.setEnabled(false);

        cutAction = new LocalizableAction("cut", localizationProvider) {
            @Serial
            private static final long serialVersionUID = 8382956242481145975L;

            @Override
            public void actionPerformed(ActionEvent e) {
                new DefaultEditorKit.CutAction().actionPerformed(e);
            }
        };
        cutAction.setEnabled(false);

        copyAction = new LocalizableAction("copy", localizationProvider) {
            @Serial
            private static final long serialVersionUID = - 8986872012865598081L;

            @Override
            public void actionPerformed(ActionEvent e) {
                new DefaultEditorKit.CopyAction().actionPerformed(e);
            }
        };
        copyAction.setEnabled(false);

        pasteAction = new LocalizableAction("paste", localizationProvider) {
            @Serial
            private static final long serialVersionUID = 274574867965634217L;

            @Override
            public void actionPerformed(ActionEvent e) {
                new DefaultEditorKit.PasteAction().actionPerformed(e);
            }
        };
        pasteAction.setEnabled(false);

        statisticsAction = new LocalizableAction("statistics", localizationProvider) {
            @Serial
            private static final long serialVersionUID = 5805758135105175260L;

            @Override
            public void actionPerformed(ActionEvent e) {
                String statistics = getStatisticsFromText(
                        model.getCurrentDocument().getTextComponent().getText());

                showMessageDialog("statisticsDialog", statistics, MessageDialogOption.INFORMATION);
            }
        };
        statisticsAction.setEnabled(false);

        exitAction = new LocalizableAction("exit", localizationProvider) {
            @Serial
            private static final long serialVersionUID = 3531660497952718094L;

            @Override
            public void actionPerformed(ActionEvent e) {
                exitProcedure();
            }
        };
        exitAction.setEnabled(true);


        englishLocalizationAction = new LocalizableAction("en", localizationProvider) {
            @Serial
            private static final long serialVersionUID = 1663587913230274637L;

            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("en");
            }
        };

        croatianLocalizationAction = new LocalizableAction("hr", localizationProvider) {
            @Serial
            private static final long serialVersionUID = 1663587913230274637L;

            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("hr");
            }
        };

        germanLocalizationAction = new LocalizableAction("de", localizationProvider) {
            @Serial
            private static final long serialVersionUID = 1828997840926072183L;

            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("de");
            }
        };

        fileMenuAction = new LocalizableAction("fileMenu", localizationProvider) {
            @Serial
            private static final long serialVersionUID = 8771743876343925771L;

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };

        languageMenuAction = new LocalizableAction("languageMenu", localizationProvider) {
            @Serial
            private static final long serialVersionUID = 3762686769883445719L;

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };

        editMenuAction = new LocalizableAction("editMenu", localizationProvider) {
            @Serial
            private static final long serialVersionUID = 4855832740373417701L;

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };

        ispitMenuAction = new LocalizableAction("ispitMenu", localizationProvider) {
            @Serial
            private static final long serialVersionUID = - 6053812194410557790L;

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };

        changeCaseMenuAction = new LocalizableAction("changeCaseMenu", localizationProvider) {
            @Serial
            private static final long serialVersionUID = - 6053812194410557790L;

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };

        sortMenuAction = new LocalizableAction("sortMenu", localizationProvider) {
            @Serial
            private static final long serialVersionUID = - 5088237419920614236L;

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };


        toUpperCaseAction = new LocalizableAction("toUpper", localizationProvider) {
            @Serial
            private static final long serialVersionUID = - 6053812194410557790L;

            @Override
            public void actionPerformed(ActionEvent e) {
                performFunctionOnSelectionChars(Character::toUpperCase);
            }
        };
        toUpperCaseAction.setEnabled(false);

        toLowerCaseAction = new LocalizableAction("toLower", localizationProvider) {
            @Serial
            private static final long serialVersionUID = - 5088237419920614236L;

            @Override
            public void actionPerformed(ActionEvent e) {
                performFunctionOnSelectionChars(Character::toLowerCase);
            }
        };
        toLowerCaseAction.setEnabled(false);

        invertCaseAction = new LocalizableAction("invertCase", localizationProvider) {
            @Serial
            private static final long serialVersionUID = 7997847228524243782L;

            @Override
            public void actionPerformed(ActionEvent e) {
                performFunctionOnSelectionChars(c -> {
                    if (Character.isUpperCase(c))
                        return Character.toLowerCase(c);

                    return Character.toUpperCase(c);
                });
            }
        };
        invertCaseAction.setEnabled(false);

        sortAscendingAction = new LocalizableAction("sortAscending", localizationProvider) {
            @Serial
            private static final long serialVersionUID = - 5630920456054544101L;

            @Override
            public void actionPerformed(ActionEvent e) {
                performSortOnSelectedLines(getHRComparator());
            }
        };
        sortAscendingAction.setEnabled(false);

        sortDescendingAction = new LocalizableAction("sortDescending", localizationProvider) {
            @Serial
            private static final long serialVersionUID = - 1908889936644576121L;

            @Override
            public void actionPerformed(ActionEvent e) {
                performSortOnSelectedLines(getHRComparator().reversed());
            }
        };
        sortDescendingAction.setEnabled(false);

        uniqueAction = new LocalizableAction("unique", localizationProvider) {
            @Serial
            private static final long serialVersionUID = - 3849381600154350404L;

            @Override
            public void actionPerformed(ActionEvent e) {
                removeDuplicatesFromSelectedLines();
            }
        };
        uniqueAction.setEnabled(false);

        zadatak11Action = new LocalizableAction("zad1",localizationProvider) {
            @Serial
            private static final long serialVersionUID = - 4907926140021309753L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new ExamZad01_1();
                dialog.setVisible(true);
            }
        };
        zadatak11Action.setEnabled(true);

        zadatak12Action = new LocalizableAction("zad12",localizationProvider) {
            @Serial
            private static final long serialVersionUID = - 4907926140021309753L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new ExamZad01_2();
                dialog.setVisible(true);
            }
        };
        zadatak12Action.setEnabled(true);

        zadatak2Action = new LocalizableAction("zad2",localizationProvider) {
            @Serial
            private static final long serialVersionUID = - 5323091128477680268L;

            @Override
            public void actionPerformed(ActionEvent e) {
                var list = new ArrayList<Integer>();

                for (int i = 0; i < model.getNumberOfDocuments(); i++) {
                    var doc = model.getDocument(i);
                    list.add(doc.getTextComponent().getText().length());
                }

                JDialog dialog = new JDialog();
                dialog.add(new StupciComponent(list));
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                dialog.setSize(500,500);
                dialog.setVisible(true);
            }
        };
        zadatak2Action.setEnabled(true);

        zadatak3Action = new LocalizableAction("zad3",localizationProvider) {
            @Serial
            private static final long serialVersionUID = - 3419897773462935251L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new Zadatak3(model);
                dialog.setVisible(true);
            }
        };
        zadatak3Action.setEnabled(false);
    }

    /**
     * Metoda koja konfigurira mnemonike i keystroke za pozvati akcije.
     */
    private void configureMnemonicsOnActions() {
        createNewAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
        createNewAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);

        openExistingAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
        openExistingAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);

        saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        saveAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

        saveAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
        saveAsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);

        closeAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
        closeAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);

        cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);

        pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);

        statisticsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
        statisticsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);

        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
        exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);

        englishLocalizationAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control E"));
        englishLocalizationAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);

        croatianLocalizationAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control H"));
        croatianLocalizationAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_H);

        germanLocalizationAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control G"));
        germanLocalizationAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_G);

        toUpperCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U"));
        toUpperCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);

        toLowerCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Z"));
        toLowerCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Z);

        invertCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
        invertCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);

        sortAscendingAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));
        sortAscendingAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);

        sortDescendingAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control D"));
        sortDescendingAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);

        uniqueAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control M"));
        uniqueAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_M);
    }

    /**
     * Metoda koja generira izbornik na vrhu ekrana.
     */
    @SuppressWarnings("DuplicatedCode")
    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu(fileMenuAction);
        fileMenu.add(new JMenuItem(createNewAction));
        fileMenu.add(new JMenuItem(openExistingAction));
        fileMenu.add(new JMenuItem(saveAction));
        fileMenu.add(new JMenuItem(saveAsAction));
        fileMenu.add(new JMenuItem(closeAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(statisticsAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(exitAction));

        JMenu languageMenu = new JMenu(languageMenuAction);
        languageMenu.add(new JMenuItem(englishLocalizationAction));
        languageMenu.add(new JMenuItem(croatianLocalizationAction));
        languageMenu.add(new JMenuItem(germanLocalizationAction));

        JMenu editMenu = new JMenu(editMenuAction);
        editMenu.add(new JMenuItem(cutAction));
        editMenu.add(new JMenuItem(copyAction));
        editMenu.add(new JMenuItem(pasteAction));

        JMenu caseMenu = new JMenu(changeCaseMenuAction);
        caseMenu.add(new JMenuItem(toUpperCaseAction));
        caseMenu.add(new JMenuItem(toLowerCaseAction));
        caseMenu.add(new JMenuItem(invertCaseAction));

        JMenu sortMenu = new JMenu(sortMenuAction);
        sortMenu.add(new JMenuItem(sortAscendingAction));
        sortMenu.add(new JMenuItem(sortDescendingAction));
        sortMenu.addSeparator();
        sortMenu.add(new JMenuItem(uniqueAction));

        JMenu ispitMenu = new JMenu(ispitMenuAction);
        ispitMenu.add(zadatak11Action);
        ispitMenu.add(zadatak12Action);
        ispitMenu.add(zadatak2Action);
        ispitMenu.add(zadatak3Action);


        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(caseMenu);
        menuBar.add(sortMenu);
        menuBar.add(languageMenu);
        menuBar.add(ispitMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Metoda koja generira toolbar koji se po potrebi moze pomicati.
     */
    @SuppressWarnings("DuplicatedCode")
    private void createToolbar() {
        JToolBar toolBar = new JToolBar();

        JButton createNewButton = new JButton(createNewAction);
        toolBar.add(createNewButton);

        JButton openExistingButton = new JButton(openExistingAction);
        toolBar.add(openExistingButton);

        JButton saveButton = new JButton(saveAction);
        toolBar.add(saveButton);

        JButton saveAsButton = new JButton(saveAsAction);
        toolBar.add(saveAsButton);

        JButton closeButton = new JButton(closeAction);
        toolBar.add(closeButton);

        JButton exitButton = toolBar.add(exitAction);
        toolBar.add(exitButton);

        toolBar.addSeparator();

        JButton cutButton = new JButton(cutAction);
        toolBar.add(cutButton);

        JButton copyButton = new JButton(copyAction);
        toolBar.add(copyButton);

        JButton pasteButton = new JButton(pasteAction);
        toolBar.add(pasteButton);

        toolBar.addSeparator();

        JButton toUpperButton = new JButton(toUpperCaseAction);
        toolBar.add(toUpperButton);

        JButton toLowerButton = new JButton(toLowerCaseAction);
        toolBar.add(toLowerButton);

        JButton invertCaseButton = new JButton(invertCaseAction);
        toolBar.add(invertCaseButton);

        toolBar.addSeparator();

        JButton sortAscendingButton = new JButton(sortAscendingAction);
        toolBar.add(sortAscendingButton);

        JButton sortDescendingButton = new JButton(sortDescendingAction);
        toolBar.add(sortDescendingButton);

        JButton uniqueButton = new JButton(uniqueAction);
        toolBar.add(uniqueButton);

        toolBar.addSeparator();

        JButton statisticsButton = new JButton(statisticsAction);
        toolBar.add(statisticsButton);

        toolBar.setFloatable(true);
        this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }

    /**
     * Metoda koja generira status bar.
     */
    private void createStatusBar() {
        JPanel outherBorderPanel = new JPanel(new BorderLayout());
        outherBorderPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEmptyBorder(1, 8, 1, 8));

        lengthStatusLabel = new LocalizableStatusLabel("length", localizationProvider);

        statusBar.add(lengthStatusLabel, BorderLayout.WEST);

        JPanel subPanel = new JPanel();

        lineStatusLabel = new LocalizableStatusLabel("line", localizationProvider);
        subPanel.add(lineStatusLabel);
        columnStatusLabel = new LocalizableStatusLabel("column", localizationProvider);
        subPanel.add(columnStatusLabel);
        selectionStatusLabel = new LocalizableStatusLabel("select", localizationProvider);
        subPanel.add(selectionStatusLabel);

        statusBar.add(subPanel, BorderLayout.CENTER);

        JLabel timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        initTimeLabel(timeLabel);

        statusBar.add(timeLabel, BorderLayout.EAST);

        outherBorderPanel.add(statusBar, BorderLayout.CENTER);
        centerPanel.add(outherBorderPanel, BorderLayout.PAGE_END);
    }

    /**
     * Metoda koja inicijalizira sat u status baru.
     *
     * @param timeLabel Labela sata koju se azurira vremenom.
     */
    private void initTimeLabel(JLabel timeLabel) {
        new Timer(1000,
                e -> timeLabel.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()))
        ).start();
    }

    /**
     * Metoda koja azurira statistiku dokumenta za zadani tekst(dokument).
     *
     * @param text cijeli teskt dokumenta.
     * @return String sa lokaliziranom porukom statistike.
     */
    private String getStatisticsFromText(String text) {
        String intro = localizationProvider.getString("introStats");
        String chars = localizationProvider.getString("charsStats");
        String nonBlank = localizationProvider.getString("nonBlankStats");
        String lines = localizationProvider.getString("linesStats");

        int charNumber = text.length();
        int nonBlankCharNumber = text.replaceAll("\\s+", "").length();
        int lineNumber = text.split("\r\n|\r|\n").length;

        return String.format("%s %d %s, %d %s %d %s."
                , intro, charNumber, chars, nonBlankCharNumber
                , nonBlank, lineNumber, lines);
    }

    /**
     * Metoda koja poziva {@link JFileChooser} kojim se odabire path koji vrati.
     * Metoda prima opciju pozica li se za spremanje ili citanje.
     *
     * @param dialogKey lokalizacijski kljuc imena.
     * @param options   opcija je li za spremanje ili citanje.
     * @return Path koji je korisnik izabrao.
     */
    private Path getFromFileChooser(String dialogKey, FileChooserOptions options) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(localizationProvider.getString(dialogKey));

        var result = switch (options) {
            case OPEN -> fileChooser.showOpenDialog(JNotepadPP.this);
            case SAVE -> fileChooser.showSaveDialog(JNotepadPP.this);
        };
        if (result != JFileChooser.APPROVE_OPTION)
            return null;

        return fileChooser.getSelectedFile().toPath();
    }

    /**
     * Metoda poziva {@link JOptionPane} <code>OptionDialog</code> koji korisnika pita izbor i metoda
     * vraća vrijednost {@link JOptionPane} sa rezultatom.
     * Metoda je također lokalizirana sa dialogKey, yesDialog, noDialog, cancelDialog i
     * warningDialog.
     *
     * @param dialogKey kluc lokalizacije kojim se zadaje poruka dialoga.
     * @return {@link JOptionPane} vrijednost s rezultatom.
     */
    private int askConfirmationDialog(String dialogKey) {
        String yes = localizationProvider.getString("yesDialog");
        String no = localizationProvider.getString("noDialog");
        String cancel = localizationProvider.getString("cancelDialog");

        String message = localizationProvider.getString(dialogKey);
        String warning = localizationProvider.getString("warningDialog");

        String[] options = new String[]{yes, no, cancel};

        return JOptionPane.showOptionDialog(this, message, warning,
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, null);
    }

    /**
     * Metoda prikazuje <code>MessageDialog</code> iz {@link JOptionPane} koji korisniku prikazuje poruku.
     * Metoda je također lokalizirana s <code>dialogKey</code>.
     *
     * @param dialogKey ključ lokalizacije
     * @param message   Poruka koja se prikazuje korisniku.
     * @param option    {@link MessageDialogOption} enumeracija za tip poruke.
     */
    private void showMessageDialog(String dialogKey, String message, MessageDialogOption option) {
        String title = localizationProvider.getString(dialogKey);

        int type = switch (option) {
            case INFORMATION -> JOptionPane.INFORMATION_MESSAGE;
            case WARNING -> JOptionPane.WARNING_MESSAGE;
            case ERROR -> JOptionPane.ERROR_MESSAGE;
        };

        //noinspection MagicConstant
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    /**
     * Metoda koja sprema dokument ali prije nego sto ga spremi provjeri i po potrebi
     * pita za lokaciju spremanja ako one ne postoji.
     *
     * @param documentModel model Dokumenta.
     */
    private void documentSaveProcedure(SingleDocumentModel documentModel) {
        Path path = documentModel.getFilePath();
        if (path == null) {
            path = getFromFileChooser("saveAsDialog", FileChooserOptions.SAVE);
            if (path == null) return;
        }
        model.saveDocument(documentModel, path);
    }

    /**
     * Metoda za izlazak iz aplikacije koja za sve otvorene i nespremljene dokumente ispituje
     * korisnika želi li ih spremiti i spremi ih ukoliko on želi.
     * Nakon toga metoda izaže iz GUI aplikacije.
     */
    private void exitProcedure() {
        for (SingleDocumentModel document : model) {
            if (document.isModified()) {
                switch (askConfirmationDialog("saveBeforeExitDialog")) {
                    case JOptionPane.YES_OPTION:
                        documentSaveProcedure(document);
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        return;
                    default:
                }

            }
        }
        dispose();
    }

    /**
     * Metoda ažurira statusBar  i sve njegove vrijednosti o trenutnom stanju dokumenta.
     */
    private void updateStatusBar() {
        int length = 0;
        int line = 0;
        int column = 0;
        int selected = 0;

        if (model.getCurrentDocument() != null) {
            var textArea = model.getCurrentDocument().getTextComponent();
            length = textArea.getText().length();
            line = getCurrentLine(textArea) + 1;
            column = getCurrentColumn(textArea) + 1;
            selected = getCurrentSelected(textArea);
        }

        setVisibilityOfSpecialActions(selected > 0);

        lengthStatusLabel.setValue(length);
        lineStatusLabel.setValue(line);
        columnStatusLabel.setValue(column);
        selectionStatusLabel.setValue(selected);
    }

    /**
     * Metoda računa i vraća broj trenutno selektiranih znakova.
     *
     * @param text prikazano polje dokumenta.
     * @return broj selektiranih znakova.
     */
    private int getCurrentSelected(JTextComponent text) {
        return Math.abs(text.getCaret().getDot() - text.getCaret().getMark());
    }

    private int getCurrentColumn(JTextComponent text) {
        var doc = text.getDocument();
        var root = doc.getDefaultRootElement();
        return text.getCaretPosition() - root.getElement(getCurrentLine(text)).getStartOffset();
        //return text.getLineOfOffset(text.getCaretPosition());
    }

    private int getCurrentLine(JTextComponent text) {
        var doc = text.getDocument();
        var root = doc.getDefaultRootElement();
        return root.getElementIndex(text.getCaretPosition());
        //return text.getCaretPosition() - text.getLineStartOffset(getCurrentLine(text));
    }


    private void setVisibilityOfActions(boolean visibility) {
        saveAction.setEnabled(visibility);
        saveAsAction.setEnabled(visibility);
        closeAction.setEnabled(visibility);

        pasteAction.setEnabled(visibility);
        statisticsAction.setEnabled(visibility);
    }

    private void setVisibilityOfSpecialActions(boolean visibility) {
        cutAction.setEnabled(visibility);
        copyAction.setEnabled(visibility);

        toUpperCaseAction.setEnabled(visibility);
        toLowerCaseAction.setEnabled(visibility);
        invertCaseAction.setEnabled(visibility);

        sortAscendingAction.setEnabled(visibility);
        sortDescendingAction.setEnabled(visibility);
        uniqueAction.setEnabled(visibility);

        zadatak3Action.setEnabled(visibility);
    }

    private Comparator<Object> getHRComparator() {
        Locale hr = new Locale("hr");
        return Collator.getInstance(hr);
    }

    /**
     * Metoda sortira trenutno selektirane linije sa zadanim komparatorom.
     *
     * @param comparator Komparator za selektirajne redaka.
     */
    private void performSortOnSelectedLines(Comparator<Object> comparator) {
        try {
            var lines = getSelectedText().split("\r\n|\r|\n");
            Arrays.sort(lines, comparator);

            String result = String.join("\n", lines);

            replaceSelectedText(result);
        } catch (BadLocationException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Metoda miče duplikate iz selektiranih linija.
     */
    private void removeDuplicatesFromSelectedLines() {
        try {
            var result = Arrays.stream(getSelectedText().split("\r\n|\r|\n"))
                    .distinct()
                    .collect(Collectors.joining("\n"));

            replaceSelectedText(result);
        } catch (BadLocationException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Metoda izvodi zadanu funkciju nad svakim znakom unutar selektiranog dijela dokumenta.
     *
     * @param function Funkcija koja se izvodi nad znakovima.
     */
    private void performFunctionOnSelectionChars(Function<Character, Character> function) {
        Locale.setDefault(new Locale("hr"));
        try {
            String text = getSelectedText();

            char[] charArray = text.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                charArray[i] = function.apply(charArray[i]);
            }

            replaceSelectedText(String.valueOf(charArray));
        } catch (BadLocationException e) {
            System.err.println(e.getMessage());
        }

    }

    /**
     * Metoda koja traži i u Stringu vraća trenutno selektirani tekst u dokumentu.
     *
     * @return Selektirani teskt
     * @throws BadLocationException Ukoliko ne može doći do teksta baci exception.
     */
    @SuppressWarnings("DuplicatedCode")
    private String getSelectedText() throws BadLocationException {
        var document = model.getCurrentDocument().getTextComponent().getDocument();
        var caret = model.getCurrentDocument().getTextComponent().getCaret();
        int lower = Math.min(caret.getDot(), caret.getMark());
        int upper = Math.max(caret.getDot(), caret.getMark());
        int offset = upper - lower;

        return document.getText(lower, offset);
    }

    /**
     * Metoda koja mijenja selektirani tekst u dokumentu sa tekstom iz argumenta.
     *
     * @param text Tekst kojim se mijenja selektirani tekst.
     * @throws BadLocationException Ukoliko ne može doći do teksta.
     */
    @SuppressWarnings("DuplicatedCode")
    private void replaceSelectedText(String text) throws BadLocationException {
        var document = model.getCurrentDocument().getTextComponent().getDocument();
        var caret = model.getCurrentDocument().getTextComponent().getCaret();
        int lower = Math.min(caret.getDot(), caret.getMark());
        int upper = Math.max(caret.getDot(), caret.getMark());
        int offset = upper - lower;

        document.remove(lower, offset);
        document.insertString(lower, text, null);
    }

    /**
     * Enumeracija za opcije {@link JFileChooser} dijaloga.
     */
    private enum FileChooserOptions {
        OPEN,
        SAVE
    }

    /**
     * Enumeracija za opcije {@link JOptionPane} <code>messageDialog</code>.
     */
    private enum MessageDialogOption {
        INFORMATION,
        WARNING,
        ERROR
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
    }
}
