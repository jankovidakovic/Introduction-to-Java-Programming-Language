package hr.fer.zemris.java.hw11.jnotepadpp.models;

import java.nio.file.Path;

import javax.swing.JTextArea;

import hr.fer.zemris.java.hw11.jnotepadpp.listeners.SingleDocumentListener;

/**
 * Represents a model of single document, having information about file path
 * from which document was loaded, document modification status and reference to
 * Swing component which is used for editing. This model is a subject for its
 * state information and therefore defines appropriate
 * registration/deregistration methods for its listeners.
 * 
 * @author jankovidakovic
 *
 */
public interface SingleDocumentModel {

	/**
	 * Retrieves the text component associated with the model, in which the
	 * document can be edited.
	 * 
	 * @return text component associated with this model
	 */
	JTextArea getTextComponent();

	/**
	 * Returns the path associated with this model.
	 * 
	 * @return File path associated with this model.
	 */
	Path getFilePath();

	/**
	 * Sets the file path of this model to the given path.
	 * 
	 * @param path New path of the document. Must not be <code>null</code>.
	 */
	void setFilePath(Path path);

	/**
	 * Checks whether the document that this model references has been modified
	 * since loading.
	 * 
	 * @return <code>true</code> if this model's document has been modified,
	 *         <code>false</code> otherwise.
	 */
	boolean isModified();

	/**
	 * Sets the modification status of the document that this model references
	 * to the given value.
	 * 
	 * @param modified new modification status
	 */
	void setModified(boolean modified);

	/**
	 * Registers a new listener that listens for changes in document's status.
	 * 
	 * @param l new listener to be added
	 */
	void addSingleDocumentListener(SingleDocumentListener l);

	/**
	 * Unregisters the given listener from this document's listeners.
	 * 
	 * @param l listener to be removed
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);

}

