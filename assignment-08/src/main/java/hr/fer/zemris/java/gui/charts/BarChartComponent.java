package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.List;

import javax.swing.JComponent;

/**
 * Simple chart component which is used to display charts of some data.
 * 
 * @author jankovidakovic
 *
 */
public class BarChartComponent extends JComponent {

	private static final long serialVersionUID = 1L;

	private BarChart chartData; // data used to draw a chart

	/**
	 * Constructs and repaints a chart component using the given chart data.
	 * 
	 * @param chartData data used to draw the chart
	 */
	public BarChartComponent(BarChart chartData) {
		this.chartData = chartData;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {

		Dimension dim = getSize();
		Rectangle r = new Rectangle(getX(), getY(), dim.width, dim.height);

		FontMetrics fm = g.getFontMetrics();

		int xDescHeight = 2 * fm.getHeight(); // height of x axis description
		// height of the chart
		int chartHeight = r.height - 10 - 10 - 10 - xDescHeight;

		// rectangle for the y axis description
		Rectangle yDescRect = new Rectangle(getX(), getY()
				+ 10,
				2 * fm.getHeight(), chartHeight);

		// draw y description
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI / 2);
		g2d.setTransform(at);

		drawCenteredString(g2d, chartData
				.getYDesc(),
				// need lower left corner because of the translation
				new Rectangle(-getY() - chartHeight - 10, getX(),
						chartHeight,
						2 * fm.getHeight()));

		at.rotate(Math.PI / 2); // return the graphics
		g2d.setTransform(at); // back to normal

		int maximumYNumberWidth = // maximum length of numbers on y axis
				fm.stringWidth(Integer.toString(chartData.getYMax()));
		int rightEdge = // position of the edge of numbers at the y axis
				getX() + 2 * fm.getHeight() + 10 + maximumYNumberWidth;

		// now we can calculate the chart width dynamically
		int chartWidth = r.width - yDescRect.width - maximumYNumberWidth - 30;

		// spacing between numbers on the y axis
		int YNumbersSpacing = chartHeight * chartData.getYSpacing()
				/ (chartData.getYMax() - chartData.getYMin());

		// draw the numbers
		for (int i = chartData.getYMin(); i <= chartData.getYMax();
				i += chartData.getYSpacing()) {

			String numberToDraw = Integer.toString(i);
			// find the coordinates
			int xcor = rightEdge - fm.stringWidth(numberToDraw);
			int ycor = getY() + 10 + chartHeight + 9
					- ((i - chartData.getYMin()) / chartData
							.getYSpacing())
							* YNumbersSpacing;
			g.drawString(numberToDraw, xcor, ycor);
		}

		// draw description of the x axis

		// rectangle inside of which the description of x axis will be drawn
		Rectangle xDescRect =
				new Rectangle(yDescRect.width + 20 + maximumYNumberWidth,
						getY() + r.height - 2 * fm.getHeight(), chartWidth,
						2 * fm.getHeight());

		drawCenteredString(g, chartData.getXDesc(), xDescRect);

		// Draw numbers on the x axis
		int entries = chartData.getValues().size();

		// width of the number rectangle
		int xNumberWidth = chartWidth / entries;
		// height of the number rectangle
		int xNumberHeight = 2 * fm.getHeight();

		int xcor = 20 + yDescRect.width + maximumYNumberWidth;
		int ycor = getY() + yDescRect.height + fm.getHeight();

		// rectangle which is used to draw centered numbers
		Rectangle xEntryRectangle =
				new Rectangle(xcor, ycor, xNumberWidth, xNumberHeight);

		for (XYValue values : chartData.getValues()) {
			drawCenteredString(g, Integer.toString(values.getX()),
					xEntryRectangle);
			xEntryRectangle.x += xNumberWidth;
		}

		// draw grid lines

		xcor = 20 + yDescRect.width + maximumYNumberWidth;

		// horizontal lines
		int yCorIncrement = chartHeight * chartData.getYSpacing()
				/ (chartData.getYMax() - chartData.getYMin());
		for (int i = chartData.getYMin(); i <= chartData.getYMax();
				i += chartData.getYSpacing()) {
			g.setColor(Color.DARK_GRAY);
			g.drawLine(xcor - 5, ycor, xcor, ycor);
			g.setColor(Color.ORANGE);
			g.drawLine(xcor, ycor, xcor + chartWidth + 5, ycor);
			ycor -= yCorIncrement;
		}

		// vertical lines
		ycor = getY() + yDescRect.height + fm.getHeight();
		for (int i = 0; i < entries; i++) {
			xcor += xNumberWidth;
			g.setColor(Color.DARK_GRAY);
			g.drawLine(xcor, ycor + 5, xcor, ycor);
			g.setColor(Color.ORANGE);
			g.drawLine(xcor, ycor, xcor, ycor - chartHeight - 5);
		}

		// graph data
		int[] xcors = new int[entries];
		int[] ycors = new int[entries];
		int[] heights = new int[entries];

		List<XYValue> values = chartData.getValues();
		for (int i = 0; i < values.size(); i++) {
			xcors[i] = getX() + 20 + yDescRect.width
					+ maximumYNumberWidth
					+ xNumberWidth * i;
			ycors[i] = getY() + 10 + chartHeight + 5 - values.get(i)
					.getY()
					* yCorIncrement / chartData.getYSpacing();
			heights[i] = yCorIncrement * values.get(i).getY()
					/ chartData.getYSpacing();
		}

		for (int i = 0; i < values.size(); i++) {
			g.setColor(new Color(255, 128, 0));
			g.fill3DRect(xcors[i], ycors[i], xNumberWidth, heights[i], true);

			g.setColor(Color.WHITE);
			g.draw3DRect(xcors[i], ycors[i], xNumberWidth, heights[i], true);

		}

		// draw grid boundaries with arrows

		xcor = getX() + 20 + yDescRect.width + maximumYNumberWidth;
		ycor = getY() + yDescRect.height + fm.getHeight();
		g.setColor(Color.DARK_GRAY);
		g.drawLine(xcor, ycor + 5, xcor, ycor - chartHeight - 5);

		Polygon verticalArrow = new Polygon();

		verticalArrow.addPoint(xcor, ycor - chartHeight - 5);
		verticalArrow.addPoint(xcor - 5, ycor - chartHeight);
		verticalArrow.addPoint(xcor + 5, ycor - chartHeight);

		g.fillPolygon(verticalArrow);

		g.drawLine(xcor - 5, ycor, xcor + xNumberWidth * entries + 5, ycor);

		Polygon horizontalArrow = new Polygon();

		horizontalArrow.addPoint(xcor + xNumberWidth * entries + 5, ycor);
		horizontalArrow.addPoint(xcor + xNumberWidth * entries, ycor - 5);
		horizontalArrow.addPoint(xcor + xNumberWidth * entries, ycor + 5);

		g.fillPolygon(horizontalArrow);

	}

	/**
	 * Draws a given string in such a way that it is centered in the given
	 * rectangle.
	 * 
	 * @param g    Graphics object used for drawing
	 * @param text text to be drawn
	 * @param rect rectangle to be centered inside of
	 */
	private void drawCenteredString(Graphics g, String text, Rectangle rect) {

		FontMetrics metrics = g.getFontMetrics();

		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		int y = rect.y + (rect.height - metrics.getHeight()) / 2
				+ metrics.getAscent();

		g.drawString(text, x, y);
	}
}
