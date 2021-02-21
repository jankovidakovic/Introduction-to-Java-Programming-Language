/**
 * 
 */
package hr.fer.zemris.java.hw11.jnotepadpp.models;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import hr.fer.zemris.java.hw11.jnotepadpp.listeners.SingleDocumentListener;

/**
 * Default single document model implementation.
 * 
 * @author jankovidakovic
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {

	private Path filePath;
	private JTextArea textArea;

	private boolean modified;

	private List<SingleDocumentListener> listeners;

	public DefaultSingleDocumentModel(Path filePath, String textContent) {
		this.filePath = filePath;

		listeners = new ArrayList<SingleDocumentListener>();

		// create an instance of text area
		textArea = new JTextArea(textContent);
		// create modification listener
		textArea.getDocument().addDocumentListener(new DocumentListener() {


			@Override
			public void removeUpdate(DocumentEvent e) {
				updateAndNotify();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateAndNotify();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				return; // DO NOTHING
			}

			private void updateAndNotify() {
				DefaultSingleDocumentModel.this.setModified(true);
			}
		});

	}

	@Override
	public JTextArea getTextComponent() {
		return textArea;
	}

	@Override
	public Path getFilePath() {
		return filePath;
	}

	@Override
	public void setFilePath(Path path) {
		this.filePath = path;
		listeners.forEach(l -> l.documentFilePathUpdated(this));
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		if (modified != this.modified) {
			this.modified = modified;
			listeners.forEach(l -> l.documentModifyStatusUpdated(this));
		}
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(l);

	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int hashCode() {
		return Objects.hash(filePath);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof DefaultSingleDocumentModel)) {
			return false;
		}
		DefaultSingleDocumentModel other = (DefaultSingleDocumentModel) obj;
		return Objects.equals(filePath, other.filePath);
	}

}
