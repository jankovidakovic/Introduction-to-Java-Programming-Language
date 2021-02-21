package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Model of a bridge that is used to connect the localization provider to some
 * object which wants to use its services. Bridge ensures that once the client
 * is destroyed, it is eligible for garbage collection because no more
 * references to the client are stored, which would be the case if the client
 * connected to the localization provider directly, without this bridge.
 * 
 * @author jankovidakovic
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	private boolean connected; // connection status
	private ILocalizationProvider localProvider;

	private String language;

	/**
	 * Initializes the bridge to accept connections to the given provider
	 * 
	 * @param localProvider provider for which this bridge manages connections.
	 */
	public LocalizationProviderBridge(ILocalizationProvider localProvider) {
		super();
		this.localProvider = localProvider;

	}

	@Override
	public String getString(String key) {
		return localProvider.getString(key);
	}

	// listener which is used as a connection of some client to the localization
	// provider.
	private ILocalizationListener listener = new ILocalizationListener() {

		@Override
		public void localizationChanged() {
			fire(); // notify all its listeners
		}
	};

	/**
	 * Disconnects the client from the localization provider
	 */
	public void disconnect() {
		localProvider.removeLocalizationListener(listener);
	}

	/**
	 * Connects the client to the localization provider, if the client was not
	 * previously connected.
	 */
	public void connect() {
		if (connected) {
			throw new IllegalStateException("Already connected.");
		}
		connected = true;
		localProvider.addLocalizationListener(listener);
		language = localProvider.getCurrentLanguage();
	}

	public String getCurrentLanguage() {
		return language;
	}

}
