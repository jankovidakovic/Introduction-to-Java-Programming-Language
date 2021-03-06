package hr.fer.zemris.java.hw11.jnotepadpp.local;

public interface ILocalizationProvider {

	String getString(String key);

	void addLocalizationListener(ILocalizationListener listener);

	void removeLocalizationListener(ILocalizationListener listener);

	String getCurrentLanguage();

}
