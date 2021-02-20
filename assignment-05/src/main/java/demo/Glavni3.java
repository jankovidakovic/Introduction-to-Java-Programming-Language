package demo;

import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Demonstrates the functionality of <code>LSystemBuilderImpl</code>.
 * Builder is configured from the config file, which is chosen at runtime.
 * Example config files are provided in the project content, under the path
 * ./lib/examples
 * @author jankovidakovic
 *
 */
public class Glavni3 {

	public static void main(String[] args) {
		
		LSystemViewer.showLSystem(LSystemBuilderImpl::new);

	}

}
