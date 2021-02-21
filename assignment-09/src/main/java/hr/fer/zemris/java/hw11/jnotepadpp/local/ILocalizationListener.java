package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Model of a listener which can listen for localization changes.
 * 
 * @author jankovidakovic
 *
 */
public interface ILocalizationListener {

	/**
	 * This method should be invoked when the localization changes
	 */
	void localizationChanged();
}