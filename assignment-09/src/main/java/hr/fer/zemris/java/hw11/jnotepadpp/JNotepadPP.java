package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.listeners.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationListener;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.models.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.models.SingleDocumentModel;

/**
 * Simple and fully-functional notepad application. Supports use of multiple
 * tabs.
 * 
 * @author jankovidakovic
 *
 */
public class JNotepadPP extends JFrame {

	private static final long serialVersionUID = 1L;

	private MultipleDocumentModel model;

	private FormLocalizationProvider flp;

	// actions which will be localized

	private Action newDocumentAction;
	private Action openDocumentAction;
	private Action saveDocumentAction;
	private Action saveAsDocumentAction;
	private Action closeDocumentAction;
	private Action infoAction;
	private Action cutAction;
	private Action copyAction;
	private Action pasteAction;
	private Action exitAction;

	private Action fileMenuAction;
	private Action editMenuAction;
	private Action toolsAction;
	private Action changeCaseAction;
	private Action toUppercaseAction;
	private Action toLowercaseAction;
	private Action invertCaseAction;

	private Action languageMenuAction;

	private Action croatianAction;
	private Action englishAction;
	private Action germanAction;

	private Action sortAction;
	private Action ascendingAction;
	private Action descendingAction;

	private Action uniqueAction;

	private JLabel documentSize;

	/**
	 * Constructs a new instance of notepad with GUI and backend.
	 */
	public JNotepadPP() {


		flp = new FormLocalizationProvider(LocalizationProvider.getInstance(),
				this);

		// change action descriptions on localization change
		flp.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				newDocumentAction.putValue(Action.SHORT_DESCRIPTION,
						flp.getString("newDesc"));
				openDocumentAction.putValue(Action.SHORT_DESCRIPTION,
						flp.getString("openDesc"));
				saveDocumentAction.putValue(Action.SHORT_DESCRIPTION,
						flp.getString("saveDesc"));
				saveAsDocumentAction.putValue(Action.SHORT_DESCRIPTION,
						flp.getString("saveAsDesc"));
				closeDocumentAction.putValue(Action.SHORT_DESCRIPTION,
						flp.getString("closeDesc"));
				exitAction.putValue(Action.SHORT_DESCRIPTION,
						flp.getString("exitDesc"));
				cutAction.putValue(Action.SHORT_DESCRIPTION,
						flp.getString("cutDesc"));
				copyAction.putValue(Action.SHORT_DESCRIPTION,
						flp.getString("copyDesc"));
				pasteAction.putValue(Action.SHORT_DESCRIPTION,
						flp.getString("pasteDesc"));
				infoAction.putValue(Action.SHORT_DESCRIPTION,
						flp.getString("infoDesc"));

				String[] docSizeTokens = documentSize.getText().split(":");
				documentSize.setText(
						flp.getString("length") + ":" + docSizeTokens[1]);
			}
		});
		 

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocation(0, 0);
		setSize(800, 600);

		setTitle("JNotepad++");
		initGUI();
	}

	/**
	 * updates the editor status bar according to the given components.
	 * 
	 * @param textArea     text area of which the status will be displayed
	 * @param documentSize label which displays the document size
	 * @param caretInfo    label which displays the caret info
	 */
	private void updateStatusBar(JTextArea textArea, JLabel documentSize,
			JLabel caretInfo) {
		try {
			int line = 1;
			int column = 1;
			int selected = Math.abs(textArea.getCaret().getDot()
					- textArea.getCaret().getMark());
			int caret = textArea.getCaretPosition();
			line = textArea.getLineOfOffset(caret);

			if (selected > 0) {
				changeCaseAction.setEnabled(true);
				sortAction.setEnabled(true);
				uniqueAction.setEnabled(true);
			} else {
				changeCaseAction.setEnabled(false);
				sortAction.setEnabled(false);
				uniqueAction.setEnabled(false);
			}

			column = caret - textArea.getLineStartOffset(line);
			line++; // 1-indexing
			column++;// 1-indexing

			documentSize.setText(flp.getString("length") + ": "
					+ textArea.getText().length());

			caretInfo
					.setText(
					"Ln:" + line + "  Col:" + column + "  Sel:" + selected);

		} catch (BadLocationException ex) {
			// TODO - implement something
		}
	}

	/**
	 * Initializes the graphical interface of the application
	 */
	private void initGUI() {

		model = new DefaultMultipleDocumentModel(); // tabbedPane
		JPanel statusBar = new JPanel(new GridLayout(1, 3));
		documentSize = new JLabel(flp.getString("length") + ": 0");
		JLabel caretInfo = new JLabel("Ln:1 Col:1 Sel:0");

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		JLabel dateTime =
				new JLabel(dateFormat.format(Calendar.getInstance().getTime()));
		int updateIntervalMs = 100;

		// new thread which will update time
		new Timer(updateIntervalMs, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dateTime.setText(
						dateFormat.format(Calendar.getInstance().getTime()));
			}
		}).start();

		statusBar.add(documentSize);
		statusBar.add(caretInfo);
		statusBar.add(dateTime);

		// add dynamic changing of the status bar
		CaretListener caretListener = new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				JTextArea textArea = (JTextArea) e.getSource();
				updateStatusBar(textArea, documentSize, caretInfo);

			}
		};

		// add listener which will ensure dynamic status bar changing depending
		// on the selected tab
		model.addMultipleDocumentListener(new MultipleDocumentListener() {

			// added -> register new listener
			// removed -> unregister listener

			@Override
			public void documentRemoved(SingleDocumentModel model) {
				model.getTextComponent().removeCaretListener(caretListener);
			}

			@Override
			public void documentAdded(SingleDocumentModel model) {
				model.getTextComponent().addCaretListener(caretListener);
			}

			@Override
			public void currentDocumentChanged(
					SingleDocumentModel previousModel,
					SingleDocumentModel currentModel) {

				if (previousModel == null) {
					if (currentModel == null) {
						throw new IllegalStateException("Error");
					} else {
						// new document was added
						JTextArea textArea = currentModel.getTextComponent();
						updateStatusBar(textArea, documentSize, caretInfo);
					}
				} else {
					if (currentModel == null) { // document was removed
						setTitle("JNotepad++");
					} else {
						setTitle((currentModel.getFilePath() == null
								? "(unnamed)" : currentModel.getFilePath())
								+ " - JNotepad++");
						JTextArea textArea = currentModel.getTextComponent();
						updateStatusBar(textArea, documentSize, caretInfo);
					}
				}
			}
		});

		// set layouts
		this.getContentPane().setLayout(new BorderLayout());
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add((JTabbedPane) model, BorderLayout.CENTER);
		centerPanel.add(statusBar, BorderLayout.PAGE_END);

		this.getContentPane().add(centerPanel, BorderLayout.CENTER);

		// create GUI elements
		createActions();
		createMenus();
		createToolbars();

		// ensure checking for unsaved documents upon exit
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				processAttemptedExit();
			}
		});

	}

	/**
	 * Processes the attempt to exit the application, checking for any unsaved
	 * documents.
	 */
	private void processAttemptedExit() {

		for (SingleDocumentModel singleDoc : model) {
			if (singleDoc.isModified()) {

				// TODO - localization?
				int option = JOptionPane.showConfirmDialog(JNotepadPP.this,
						"Save unmodified document?");
				if (option == 0) {
					saveDocumentAction.actionPerformed(null);

				} else if (option == 2) {
					return;
				}
			}
		}
		dispose();
		System.exit(0); // TODO - check ?
	}

	/**
	 * Changes the case of the selected part of the given text area, using the
	 * provided change function.
	 * 
	 * @param textArea       text area in which the case of some text will be
	 *                       changed.
	 * @param changeFunction function used to transform the selected text.
	 */

	private void changeCase(
			JTextArea textArea,
			Function<String, String> changeFunction) {
		Document doc = textArea.getDocument();
		int len = Math.abs(
				textArea
				.getCaret().getDot()
						- textArea.getCaret()
						.getMark());
		int offset = 0;
		if (len != 0) {
			offset = Math.min(
					textArea.getCaret()
							.getDot(),
					textArea.getCaret()
							.getMark());
		} else {
			len = doc.getLength();
		}
		try {
			String text = doc.getText(offset, len);
			text = changeFunction.apply(text);
			doc.remove(offset, len);
			doc.insertString(offset, text, null);
		} catch (BadLocationException ex) {
			// TODO - implement something
		}
	}

	/**
	 * Changes the selected lines from given text area, applying the given
	 * function.
	 * 
	 * @param textArea       text area from which the selected lines will be
	 *                       changed
	 * @param changeFunction change function which will be applied to the
	 *                       selected lines.
	 */
	private void changeSelectedLines(
			JTextArea textArea,
			Function<String, String> changeFunction) {

		Document doc = textArea.getDocument();

		int selStart = textArea.getSelectionStart();
		int selEnd = textArea.getSelectionEnd();

		try {
			int selStartL = textArea.getLineOfOffset(selStart);
			int selEndL = textArea.getLineOfOffset(selEnd);

			int selStartLStart = textArea.getLineStartOffset(selStartL);
			int selEndLEnd = textArea.getLineEndOffset(selEndL);

			int len = selEndLEnd - selStartLStart;
			int offset = selStartLStart;

			String text = textArea.getText(offset, len);
			System.out.println(text);

			text = changeFunction.apply(text);
			text += "\n";
			doc.remove(offset, len);
			doc.insertString(offset, text, null);

		} catch (BadLocationException ex) {

		}
	}

	/**
	 * Creates actions in the GUI environment.
	 */
	private void createActions() {

		newDocumentAction = new LocalizableAction("new", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				model.createNewDocument();
			}
		};

		openDocumentAction = new LocalizableAction("open", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(JNotepadPP.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					Path path = chooser.getSelectedFile().toPath();
					model.loadDocument(path);

				}

			}
		};

		saveDocumentAction = new LocalizableAction("save", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

				if (model.getCurrentDocument().getFilePath() == null) {
					saveAsDocumentAction.actionPerformed(e);
				} else {
					model.saveDocument(model.getCurrentDocument(),
							model.getCurrentDocument().getFilePath());
				}

			}
		};

		saveAsDocumentAction = new LocalizableAction("saveAs", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showSaveDialog(JNotepadPP.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					Path savePath = chooser.getSelectedFile().toPath();
					if (Files.exists(savePath)) {
						int option =
								JOptionPane.showConfirmDialog(JNotepadPP.this,
										"File already exists. Overwrite?");
						// 0=yes, 1=no, 2=cancel
						if (option != 0) {
							return;
						}
					}
					model.saveDocument(model.getCurrentDocument(), savePath);
					JOptionPane.showMessageDialog(JNotepadPP.this,
							"File saved successfully.", "Info",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};

		closeDocumentAction = new LocalizableAction("close", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				model.closeDocument(model.getCurrentDocument());
			}
		};

		exitAction = new LocalizableAction("exit", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

				processAttemptedExit();
			}
		};

		cutAction = new LocalizableAction("cut", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				new DefaultEditorKit.CutAction().actionPerformed(e);

			}
		};

		copyAction = new LocalizableAction("copy", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				new DefaultEditorKit.CopyAction().actionPerformed(e);

			}
		};

		pasteAction = new LocalizableAction("paste", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				new DefaultEditorKit.PasteAction().actionPerformed(e);

			}
		};

		infoAction = new LocalizableAction("info", flp) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				long numberOfChars = model.getCurrentDocument()
						.getTextComponent().getText().chars().count();
				long nonWhitespaceChars = model.getCurrentDocument()
						.getTextComponent().getText().chars()
						.filter(c -> !(Character.isWhitespace(c))).count();
				long lines = model.getCurrentDocument().getTextComponent()
						.getText().lines().count();

				// TODO - localize
				JOptionPane.showMessageDialog(JNotepadPP.this,
						"Characters: " + numberOfChars
								+ "\nNon-whitespace characters: "
								+ nonWhitespaceChars + "\nLines: " + lines,
						"Statistical information",
						JOptionPane.INFORMATION_MESSAGE);

			}
		};

		fileMenuAction = new LocalizableAction("file", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		};

		editMenuAction = new LocalizableAction("edit", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		};

		languageMenuAction = new LocalizableAction("language", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		};

		croatianAction = new LocalizableAction("croatian", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("hr");
			}
		};

		englishAction = new LocalizableAction("english", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("en");
			}
		};

		germanAction = new LocalizableAction("german", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("de");
			}
		};

		toolsAction = new LocalizableAction("tools", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		};

		changeCaseAction = new LocalizableAction("changeCase", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		};

		toUppercaseAction = new LocalizableAction("toUppercase", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

				JTextArea textArea =
						model.getCurrentDocument().getTextComponent();
				changeCase(textArea, text -> {
					char[] chars = text.toCharArray();
					for (int i = 0; i < chars.length; i++) {
						char c = chars[i];
						if (Character.isLowerCase(c)) {
							chars[i] = Character.toUpperCase(c);
						}
					}
					return new String(chars);
				});
			}
		};

		toLowercaseAction = new LocalizableAction("toLowercase", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextArea textArea =
						model.getCurrentDocument().getTextComponent();
				changeCase(textArea, text -> {
					char[] chars = text.toCharArray();
					for (int i = 0; i < chars.length; i++) {
						char c = chars[i];
						if (Character.isUpperCase(c)) {
							chars[i] = Character.toLowerCase(c);
						}
					}
					return new String(chars);
				});
			}
		};

		invertCaseAction = new LocalizableAction("invertCase", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextArea textArea =
						model.getCurrentDocument().getTextComponent();
				changeCase(textArea, text -> {
					char[] chars = text.toCharArray();
					for (int i = 0; i < chars.length; i++) {
						char c = chars[i];
						if (Character.isLowerCase(c)) {
							chars[i] = Character.toUpperCase(c);
						} else if (Character.isUpperCase(c)) {
							chars[i] = Character.toLowerCase(c);
						}
					}
					return new String(chars);
				});

			}
		};

		sortAction = new LocalizableAction("sort", flp) {


			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		};

		ascendingAction = new LocalizableAction("ascending", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextArea textArea =
						model.getCurrentDocument().getTextComponent();
				changeSelectedLines(textArea, text -> {
					Locale locale = new Locale(flp.getCurrentLanguage());
					return Arrays.stream(text.split("\n"))
							.sorted(Collator.getInstance(locale))
							.collect(Collectors.joining("\n"));
				});

			}
		};

		descendingAction = new LocalizableAction("descending", flp) {


			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

				JTextArea textArea =
						model.getCurrentDocument().getTextComponent();
				changeSelectedLines(textArea, text -> {
					Locale locale = new Locale(flp.getCurrentLanguage());
					return Arrays.stream(text.split("\n"))
							.sorted(Collator.getInstance(locale)
									.reversed())
							.collect(Collectors.joining("\n"));
				});

			}
		};

		uniqueAction = new LocalizableAction("unique", flp) {


			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {

				JTextArea textArea =
						model.getCurrentDocument().getTextComponent();
				changeSelectedLines(textArea, text -> {
					return Arrays.stream(text.split("\n"))
							.distinct()
							.collect(Collectors.joining("\n"));
				});

			}
		};

		// disable actions which work only with selected text
		changeCaseAction.setEnabled(false);
		sortAction.setEnabled(false);
		uniqueAction.setEnabled(false);

		// define default action properties
		newDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control N"));
		newDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		newDocumentAction.putValue(Action.SHORT_DESCRIPTION,
				flp.getString("newDesc"));

		openDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		openDocumentAction.putValue(Action.SHORT_DESCRIPTION,
				flp.getString("openDesc"));

		saveDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveDocumentAction.putValue(Action.SHORT_DESCRIPTION,
				flp.getString("saveDesc"));

		saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control A"));
		saveAsDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		saveAsDocumentAction.putValue(Action.SHORT_DESCRIPTION,
				flp.getString("saveAsDesc"));

		closeDocumentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control W"));
		closeDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);
		closeDocumentAction.putValue(Action.SHORT_DESCRIPTION,
				flp.getString("closeDesc"));

		exitAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("alt F4"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		exitAction.putValue(Action.SHORT_DESCRIPTION,
				flp.getString("exitDesc"));

		cutAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control X"));
		cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
		cutAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("cutDesc"));

		copyAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control C"));
		copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		copyAction.putValue(Action.SHORT_DESCRIPTION,
				flp.getString("copyDesc"));

		pasteAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control V"));
		pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
		pasteAction.putValue(Action.SHORT_DESCRIPTION,
				flp.getString("pasteDesc"));

		infoAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control I"));
		infoAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
		infoAction.putValue(Action.SHORT_DESCRIPTION,
				flp.getString("infoDesc"));

	}

	// creates menus in the graphical user interface
	private void createMenus() {

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(fileMenuAction);

		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(newDocumentAction));
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveAsDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(closeDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exitAction));

		JMenu editMenu = new JMenu(editMenuAction);
		menuBar.add(editMenu);

		editMenu.add(new JMenuItem(cutAction));
		editMenu.add(new JMenuItem(copyAction));
		editMenu.add(new JMenuItem(pasteAction));
		editMenu.addSeparator();
		editMenu.add(new JMenuItem(infoAction));

		JMenu languageMenu = new JMenu(languageMenuAction);
		menuBar.add(languageMenu);

		languageMenu.add(new JMenuItem(croatianAction));
		languageMenu.add(new JMenuItem(englishAction));
		languageMenu.add(new JMenuItem(germanAction));

		JMenu toolsMenu = new JMenu(toolsAction);
		menuBar.add(toolsMenu);
		JMenu changeCaseMenu = new JMenu(changeCaseAction);
		toolsMenu.add(changeCaseMenu);

		changeCaseMenu.add(new JMenuItem(toUppercaseAction));
		changeCaseMenu.add(new JMenuItem(toLowercaseAction));
		changeCaseMenu.add(new JMenuItem(invertCaseAction));

		JMenu sortMenu = new JMenu(sortAction);
		toolsMenu.add(sortMenu);
		sortMenu.add(new JMenuItem(ascendingAction));
		sortMenu.add(new JMenuItem(descendingAction));

		toolsMenu.add(new JMenuItem(uniqueAction));

		this.setJMenuBar(menuBar);

	}

	// creates the editor toolbar
	private void createToolbars() {

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(true);

		toolBar.add(new JButton(newDocumentAction));
		toolBar.add(new JButton(openDocumentAction));
		toolBar.addSeparator();

		toolBar.add(new JButton(saveDocumentAction));
		toolBar.add(new JButton(saveAsDocumentAction));
		toolBar.addSeparator();

		toolBar.add(new JButton(closeDocumentAction));
		toolBar.addSeparator();

		toolBar.add(new JButton(cutAction));
		toolBar.add(new JButton(copyAction));
		toolBar.add(new JButton(pasteAction));
		toolBar.addSeparator();

		toolBar.add(new JButton(infoAction));

		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}

	/**
	 * Main method that starts the application
	 * 
	 * @param args command line arguments. None expected.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// set default language
				LocalizationProvider.getInstance().setLanguage("en");
				new JNotepadPP().setVisible(true);
			}
		});
	}
}
