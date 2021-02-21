package hr.fer.zemris.java.hw11.jnotepadpp.models;

import java.nio.file.Path;

import hr.fer.zemris.java.hw11.jnotepadpp.listeners.MultipleDocumentListener;

/**
 * Represents a model capable of holding zero, one or more documents. Model
 * understands the concept of current document, which is the one being shown to
 * the user and the one on which the user works. Model is also a subject for its
 * state information, and defines appropriate registration/unregistration
 * methods for the listeners.
 * 
 * @author jankovidakovic
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {

	/**
	 * Creates a new document that the user can work on.
	 * 
	 * @return Reference to the newly created document
	 */
	SingleDocumentModel createNewDocument();

	/**
	 * Returns the current document, which is the one that the user is currently
	 * working on (active tab).
	 * 
	 * @return reference fo the current document.
	 */
	SingleDocumentModel getCurrentDocument();

	/**
	 * Loads a document located at given path into the editor.
	 * 
	 * @param  path path to the document that will be loaded. Must not be
	 *              <code>null</code>
	 * @return      Reference to the loaded document
	 */
	SingleDocumentModel loadDocument(Path path);

	/**
	 * Saves the given document to the location represented by the given path.
	 * 
	 * @param model   Model of a document to be saved
	 * @param newPath path at which the document will be saved. If
	 *                <code>null</code>, document should be saved using path
	 *                from which it was loaded. Otherwise, the new path should
	 *                be used and after saving is completed, document's path
	 *                should be updated to <code>newPath</code>. If the new path
	 *                is also a path of some other existing single document,
	 *                method must fail and tell the user that the specified file
	 *                is already opened.
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);

	/**
	 * Removes the given document from the editor. Does not do any checks on the
	 * document's status.
	 * 
	 * @param model Document to be removed
	 */
	void closeDocument(SingleDocumentModel model);

	/**
	 * Registers a new listener that listens to the change of model's status.
	 * 
	 * @param l new listener to be added
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Unregisters the given listener from this model.
	 * 
	 * @param l Listener to be removed
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Returns the number of documents that the model knows about.
	 * 
	 * @return Number of <code>SingleDocumentModel</code> instances that this
	 *         model knows about.
	 */
	int getNumberOfDocuments();

	/**
	 * Returns the single document stored internally at given index.
	 * 
	 * @param  index index of wanted document
	 * @return       instance of <code>SingleDocumentModel</code> associated
	 *               with the given index.
	 */
	SingleDocumentModel getDocument(int index);

}
