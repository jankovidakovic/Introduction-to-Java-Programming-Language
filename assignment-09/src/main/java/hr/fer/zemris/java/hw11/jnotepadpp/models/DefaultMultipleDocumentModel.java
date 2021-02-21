/**
 * 
 */
package hr.fer.zemris.java.hw11.jnotepadpp.models;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import hr.fer.zemris.java.hw11.jnotepadpp.listeners.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.listeners.SingleDocumentListener;

/**
 * Default multiple document model implementation. Contains a collection of
 * <code>SingleDocumentModel</code> objects, which can be accessed by their
 * indices. Also contains a reference to current
 * <code>SingleDocumentModel</code>, and support for listeners management.
 * 
 * @author jankovidakovic
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane
		implements MultipleDocumentModel {

	private static final long serialVersionUID = 1L;

	private List<SingleDocumentModel> singleDocs;

	private List<MultipleDocumentListener> listeners;

	private SingleDocumentModel currentDocument;
	private int currentDocumentIndex;

	public DefaultMultipleDocumentModel() {

		singleDocs = new ArrayList<SingleDocumentModel>();
		listeners = new ArrayList<MultipleDocumentListener>();

		currentDocument = null;
		currentDocumentIndex = -1;

		addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				int previousIndex = currentDocumentIndex;
				SingleDocumentModel previousModel = currentDocument;
				
				currentDocumentIndex = getSelectedIndex();
				currentDocument = (currentDocumentIndex == -1 ? null
						: singleDocs.get(currentDocumentIndex));
				if (previousIndex == -1) {
					if (currentDocumentIndex == -1) {
						// impossible
						return;
					} else {
						// tab was added
					}
				} else { // previous tab existed
					if (currentDocumentIndex == -1) {
						//only tab was removed
					} else {

					}
				}
				listeners.forEach(l -> l.currentDocumentChanged(previousModel,
						currentDocument));
			}
		});
	}

	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return singleDocs.iterator();
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel newModel = new DefaultSingleDocumentModel(null, "");
		addDocument(newModel);
		newModel.getTextComponent().setText(""); // for caret update
		return newModel;
	}

	private ImageIcon getImageIcon(String name) {
		try (InputStream is = DefaultMultipleDocumentModel.this.getClass()
				.getClassLoader().getResourceAsStream(name)) {
			byte[] bytes = is.readAllBytes();
			ImageIcon imgIcon = new ImageIcon(bytes);
			Image image = imgIcon.getImage();
			int fontSize = super.getFont().getSize();
			Image imageScaled = image.getScaledInstance(fontSize,
					fontSize,
					java.awt.Image.SCALE_SMOOTH);
			return new ImageIcon(imageScaled);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(DefaultMultipleDocumentModel.this,
					"Cannot update document icon", "Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

	}

	private void addDocument(SingleDocumentModel doc) {

		if (isLoadedInOtherTab(doc.getFilePath(), -1)) {
			super.setSelectedComponent(
					super.getComponentAt(singleDocs.indexOf(doc)));
			return;
		}
		JScrollPane newTabPane = new JScrollPane(doc.getTextComponent());
		singleDocs.add(doc);


		super.addTab(
				(doc.getFilePath() == null ? "(unnamed)"
						: doc.getFilePath().getFileName()
								.toString()),
				getImageIcon("icons/greenDisk.png"), newTabPane);

		super.setSelectedComponent(newTabPane);

		doc.addSingleDocumentListener(new SingleDocumentListener() {

			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				// update icon
				String name;
				if (model.isModified()) {
					name = "icons/redDisk.png";
				} else {
					name = "icons/greenDisk.png";
				}
				DefaultMultipleDocumentModel.this.setIconAt(
						DefaultMultipleDocumentModel.super.indexOfComponent(
								newTabPane),
						getImageIcon(name));
			}

			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				// update title - > check if works?
				DefaultMultipleDocumentModel.this.setTitleAt(
						DefaultMultipleDocumentModel.super.indexOfComponent(
								newTabPane),
						model.getFilePath() == null ? "(unnamed)"
								: model.getFilePath().getFileName().toString());
			}
		});

		listeners.forEach(l -> l.documentAdded(doc));
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDocument;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		if (path == null) {
			throw new IllegalArgumentException("Path must not be null.");
		}
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br =
				Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				sb.append(currentLine).append("\n");
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("Cannot read from given path.");
		}
		SingleDocumentModel loadedModel =
				new DefaultSingleDocumentModel(path, sb.toString());
		addDocument(loadedModel);
		loadedModel.getTextComponent().setText(sb.toString()); // for caret
		loadedModel.setModified(false);
		return loadedModel;
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		Path pathToSave = newPath == null ? model.getFilePath() : newPath;
		if (Files.exists(pathToSave)) {
			if (isLoadedInOtherTab(pathToSave, singleDocs.indexOf(model))) {
				throw new IllegalArgumentException(
						"Cannot save to file that is opened.");
			}
			// else - overwrite
		}
		try (BufferedWriter bw =
				Files.newBufferedWriter(pathToSave, Charset.forName("UTF-8"))) {
			bw.write(model.getTextComponent().getText());

			model.setFilePath(pathToSave); // REE
			model.setModified(false); // saved file is fresh file
			listeners.forEach(l -> l.currentDocumentChanged(model, model));
			// TODO - fix
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Cannot save file", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	private boolean isLoadedInOtherTab(Path path, int index) {
		for (SingleDocumentModel singleDoc : singleDocs) {
			if (singleDoc.getFilePath() != null
					&& singleDoc.getFilePath().equals(path)
					&& singleDocs.indexOf(singleDoc) != index) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		singleDocs.remove(model);
		super.remove(currentDocumentIndex);
		listeners.forEach(l -> l.documentRemoved(model));
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);

	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return singleDocs.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return singleDocs.get(index);
	}

}
