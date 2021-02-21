package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of an abstract localization provider which implements all the methods
 * of the localization provider interface, except the method for getting
 * localized strings, which should be implemented by the classes which extend
 * this class.
 * 
 * @author jankovidakovic
 *
 */
public abstract class AbstractLocalizationProvider
		implements ILocalizationProvider {

	private List<ILocalizationListener> listeners;

	/**
	 * Constructs an abstract localization listener
	 */
	public AbstractLocalizationProvider() {
		listeners = new ArrayList<ILocalizationListener>();
	}

	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		listeners.add(listener);

	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifies all the registered listeners that the localization has changed.
	 */
	public void fire() {
		listeners.forEach(l -> l.localizationChanged());
	}

}
