package hr.fer.zemris.java.hw11.jnotepadpp.listeners;

import hr.fer.zemris.java.hw11.jnotepadpp.models.SingleDocumentModel;

/**
 * Listener that listens for changes in some single document model.
 * 
 * @author jankovidakovic
 *
 */
public interface SingleDocumentListener {

	/**
	 * Processes the case in which the document's modification status was
	 * updated(document was edited, or the status has been manually changed.)
	 * 
	 * @param model model which status was updated
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);

	/**
	 * Processes the case in which the document's file path has been updated(the
	 * document was saved to the new location, or path default path was changed
	 * manually)
	 * 
	 * @param model model which path has been updated
	 */
	void documentFilePathUpdated(SingleDocumentModel model);

}
