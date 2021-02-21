package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Final model of localization provider that is used directly by the notepad
 * application. It manages the connections of the localization provider to the
 * frame, and ensures eligibility for garbage collection upon frame disposal.
 * 
 * @author jankovidakovic
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Constructs a localization provider which manages the connection of given
	 * provider and given frame.
	 * 
	 * @param localProvider provider that provides the localization data
	 * @param frame         frame which uses the data to localize its content
	 */
	public FormLocalizationProvider(ILocalizationProvider localProvider,
			JFrame frame) {
		super(localProvider);

		// adds the listener which will connect and disconnect the bridge upon
		// window opening/closing
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}

			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}
		});
	}
}
