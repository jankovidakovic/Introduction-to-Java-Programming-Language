package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Object that holds the relevant information about bar chart. Can be used to
 * draw said chart using a GUI and BarChartComponent.
 * 
 * @author jankovidakovic
 *
 */
public class BarChart {

	private List<XYValue> values; // values that the chart plots
	private String xDesc; // description along the x axis
	private String yDesc; // description along the y axis
	private int yMin; // minimum value of numbers at y axis
	private int yMax; // maximum value of numbers at y axis
	private int ySpacing; // spacing between numbers

	/**
	 * Constructs a bar chart objects with all the given parameters.
	 * 
	 * @param values
	 * @param xDesc
	 * @param yDesc
	 * @param yMin
	 * @param yMax
	 * @param ySpacing
	 */
	public BarChart(List<XYValue> values, String xDesc, String yDesc, int yMin,
			int yMax, int ySpacing) {
		this.values = values;
		this.xDesc = xDesc;
		this.yDesc = yDesc;
		if (yMin < 0) {
			throw new IllegalArgumentException(
					"Minimum y size should be at least zero.");
		}
		this.yMin = yMin;

		if (yMax <= yMin) {
			throw new IllegalArgumentException(
					"Maximum y size should be greater than minimum y size.");
		}
		this.yMax = yMax;

		this.ySpacing = ySpacing;
		while ((this.yMax - yMin) % ySpacing != 0) {
			this.yMax++;
		}

		for (XYValue value : values) {
			if (value.getY() < yMin) {
				throw new IllegalArgumentException(
						"Invalid y value in data list.");
			}
		}
	}

	/**
	 * @return the values
	 */
	public List<XYValue> getValues() {
		return values;
	}

	/**
	 * @return the xDesc
	 */
	public String getXDesc() {
		return xDesc;
	}

	/**
	 * @return the yDesc
	 */
	public String getYDesc() {
		return yDesc;
	}

	/**
	 * @return the yMin
	 */
	public int getYMin() {
		return yMin;
	}

	/**
	 * @return the yMax
	 */
	public int getYMax() {
		return yMax;
	}

	/**
	 * @return the ySpacing
	 */
	public int getYSpacing() {
		return ySpacing;
	}

}
