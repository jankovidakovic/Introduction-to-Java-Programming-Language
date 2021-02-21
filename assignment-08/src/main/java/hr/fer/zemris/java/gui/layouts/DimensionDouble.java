package hr.fer.zemris.java.gui.layouts;

/**
 * Class that holds the GUI window's dimension in double precision. It is used
 * for calculating the preferred, maximum and minimum size of the window, to
 * prevent rounding errors.
 * 
 * @author jankovidakovic
 *
 */
public class DimensionDouble {

	public double width;
	public double height;

	public DimensionDouble() {
		width = 0;
		height = 0;
	}

	public DimensionDouble(double width, double height) {
		this.width = width;
		this.height = height;
	}

}
