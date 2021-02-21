package hr.fer.zemris.java.hw11.jnotepadpp.listeners;

import hr.fer.zemris.java.hw11.jnotepadpp.models.SingleDocumentModel;

/**
 * Listener that listens to the changes in some multiple document model.
 * 
 * @author jankovidakovic
 *
 */
public interface MultipleDocumentListener {

	/**
	 * Used to process the case in which the current document was changed in
	 * some way(changed to some other tab, edited, loaded, saved etc.). Previous
	 * model and current model can be <code>null</code>, but not both at the
	 * same time.
	 * 
	 * @param previousModel single document that precedes the current model in
	 *                      the multiple document model.
	 * @param currentModel  current model, the one that was changed
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel,
			SingleDocumentModel currentModel);

	/**
	 * Used to process the case in which a new single document was added to the
	 * multiple document model.
	 * 
	 * @param model newly added document
	 */
	void documentAdded(SingleDocumentModel model);

	/**
	 * Used to process the case in which a single document model was removed
	 * from the multiple document model.
	 * 
	 * @param model model that was removed.
	 */
	void documentRemoved(SingleDocumentModel model);

}
