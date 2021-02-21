package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Model of an abstract action which name can be localized.
 * 
 * @author jankovidakovic
 *
 */
public abstract class LocalizableAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs the instance of this action, localizing its name using the
	 * provided key
	 * 
	 * @param key key to the name of the action
	 * @param lp  localization provider used to get the localized action name
	 */
	public LocalizableAction(String key, ILocalizationProvider lp) {
		putValue(Action.NAME, lp.getString(key));

		// add listener which will change the name on localization change
		lp.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				putValue(Action.NAME, lp.getString(key));

			}
		});

	}

}
