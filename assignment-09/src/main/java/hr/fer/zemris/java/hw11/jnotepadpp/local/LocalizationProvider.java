package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Model of a singleton localization provider which can be used by all the
 * elements of the application.
 * 
 * @author jankovidakovic
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

	// singleton instance
	private static final LocalizationProvider provider =
			new LocalizationProvider();

	private String language;
	private ResourceBundle bundle;

	// private constructor to ensure that only one instance exists
	private LocalizationProvider() {
		language = "en";
		bundle = ResourceBundle.getBundle(
				"hr.fer.zemris.java.hw11.jnotepadpp.local.prijevod",
				Locale.forLanguageTag(language));
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	/**
	 * Returns the singleton instance
	 * 
	 * @return singleton instance
	 */
	public static LocalizationProvider getInstance() {
		return provider;
	}

	/**
	 * Sets the localization language to the given language.
	 * 
	 * @param language new language
	 */
	public void setLanguage(String language) {
		this.language = language;
		bundle = ResourceBundle.getBundle(
				"hr.fer.zemris.java.hw11.jnotepadpp.local.prijevod",
				Locale.forLanguageTag(language));
		fire(); // notify all listeners
	}

	/**
	 * Returns the current language
	 * 
	 * @return current language
	 */
	public String getCurrentLanguage() {
		return language;
	}
}

