package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.function.BiConsumer;

/**
 * Custom layout manager for the purpose of creating a simple calculator.
 * CalcLayout positions components in a grid of 7 rows and 5 columns. First
 * component (row 1, column 1) always spans across 5 columns, so the maximum
 * number of components that the manager can position is 31.
 * 
 * @author jankovidakovic
 *
 */
public class CalcLayout implements LayoutManager2 {

	private Component[][] grid; // array which stores the components, uses
								// 1-indexing for convenience

	private int margin; // spacing between components

	private int rows = 5; // number of rows
	private int cols = 7; // number of columns

	private Dimension cellPref; // preferred size of cells

	/**
	 * Constructs a default version of the layout, with no margin between
	 * components.
	 */
	public CalcLayout() {
		grid = new Component[rows + 1][cols + 1];
		margin = 0;

		cellPref = new Dimension(0, 0);

	}

	/**
	 * Constructs a layout in which the components will be separated by the
	 * given margin.
	 * 
	 * @param  margin                   spacing between components (in pixels).
	 *                                  Valid if non-negative integer.
	 * @throws IllegalArgumentException if given margin is invalid (negative).
	 */
	public CalcLayout(int margin) throws IllegalArgumentException {
		grid = new Component[rows + 1][cols + 1];
		if (margin < 0) {
			throw new IllegalArgumentException("Margin cannot be negative.");
		}
		this.margin = margin;

		cellPref = new Dimension(0, 0);

	}

	/**
	 * This method is not supported in this layout manager.
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException("Not supported.");

	}

	@Override
	public void removeLayoutComponent(Component comp) {
		for (int i = 1; i <= rows; i++) {
			for (int j = 1; j <= cols; j++) {
				if (i == 1 && j > 1 && j < 6) {
					continue;
				}
				if (grid[i][j] == comp) {
					grid[i][j] = null;
					return;
				}
			}
		}
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(cellPref.width * cols + margin * (cols - 1),
				cellPref.height * rows + margin * (rows - 1));
	}

	/**
	 * Returns the new BiConsumer which is used to calculate some dimensions of
	 * the container which implements this layout.
	 * 
	 * @param  which wanted dimension, can be preferred, minimum or maximum
	 * @return       BiConsumer which can be used to calculate the wanted
	 *               dimension
	 */
	private BiConsumer<Component, DimensionDouble>
			newDimensionConsumer(SizeType which) {

		return new BiConsumer<Component, DimensionDouble>() {

			@Override
			public void accept(Component comp, DimensionDouble dimD) {
				Dimension dim = CalcLayout.getSizeOfType(comp, which);
				if (dim != null) { // has the given dimension
					dimD.height += dim.getHeight();
					dimD.width += dim.getWidth();
				}

			}

		};
	}

	/**
	 * Returns the dimensions of given component, which are of given SizeType.
	 * 
	 * @param  comp Component which dimensions are needed
	 * @param  type type of dimensions (preferred, minimum or maximum)
	 * @return      dimensions of the component that correspond to the given
	 *              type.
	 */
	private static Dimension getSizeOfType(Component comp, SizeType type) {
		return switch (type) {
			case MINIMUM -> comp.getMinimumSize();
			case MAXIMUM -> comp.getMaximumSize();
			case PREFERRED -> comp.getPreferredSize();
		};
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return calculateSize(parent,
				newDimensionConsumer(SizeType.MINIMUM));
	}

	@Override
	public void layoutContainer(Container parent) {

		Dimension dim = parent.getSize();

		// size of one row
		double rowSize = (dim.getHeight() - (rows - 1) * margin) / rows;
		// size of one column
		double colSize = (dim.getWidth() - (cols - 1) * margin) / cols;

		// coordinates for laying out the components
		double x = 0;
		double y = 0;

		for (int i = 1; i <= rows; i++) {
			for (int j = 1; j <= cols; j++) {
				if (i == 1 && j > 1 && j < 6) { // illegal
					x += colSize + (double) margin;
					continue;
				}
				if (grid[i][j] != null) { // exists
					if (i == 1 && j == 1) { // the big one
						grid[i][j].setBounds((int) Math.round(x),
								(int) Math.round(y),
								(int) Math.round(colSize * 5 + margin * 4),
								(int) Math.round(rowSize));
					} else {
						grid[i][j].setBounds((int) Math.round(x),
								(int) Math.round(y), (int) Math.round(colSize),
								(int) Math.round(rowSize));
					}
				}
				x += colSize + (double) margin;
			}
			y += rowSize + (double) margin;
			x = 0; // reset x component
		}
		// TODO - check borders ???
	}

	/**
	 * Adds the specified component to the layout, using the provided constraint.
	 * 
	 * @param  comp                     Component to be added
	 * @param  constraints              constraints on the new component. If it
	 *                                  is an instance of String, it will be
	 *                                  parsed into RCPosition and then the
	 *                                  component will be added to that
	 *                                  position. Also, it can be instance of
	 *                                  RCPosition in which case component is
	 *                                  also added to that position. If obtained
	 *                                  position is invalid, exception is
	 *                                  thrown.
	 * @throws CalcLayoutException      if the layout already contains a
	 *                                  component at the given position, or if
	 *                                  the obtained position is illegal. Legal
	 *                                  positions are all from (1,1) to (7,5),
	 *                                  except (1,2), (1,3), (1,4) and (1,5),
	 *                                  since those are occupied by the
	 *                                  component at the position (1,1).
	 * @throws IllegalArgumentException if the given constraint object is not an
	 *                                  instance of String or RCPosition, or
	 *                                  given String is not parsable into
	 *                                  RCPosition.
	 */
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if (comp == null || constraints == null) {
			throw new NullPointerException("Neither argument can be null.");
		}
		RCPosition pos;
		if (constraints instanceof String) { // Try to parse
			pos = CalcLayout.parse((String) constraints);
			// above line will throw an exception if unable to parse
		} else if (constraints instanceof RCPosition) {
			pos = (RCPosition) constraints;
		} else {
			throw new IllegalArgumentException(
					"Constraint must be an instance of String or RCPosition.");
		}

		if (pos.getRow() == 1 && pos.getColumn() > 1 && pos.getColumn() < 6) {
			throw new CalcLayoutException("Illegal position.");
		}
		if (pos.getRow() < 1 || pos.getRow() > rows || pos.getColumn() < 1
				|| pos.getColumn() > cols) {
			throw new CalcLayoutException("Position out of bounds.");
		}

		if (grid[pos.getRow()][pos.getColumn()] != null) {
			// component already exists at wanted position
			throw new CalcLayoutException(
					"Component at given position already exists!");
		}

		grid[pos.getRow()][pos.getColumn()] = comp;
		
		// update preferred size

		Dimension prefSize = comp.getPreferredSize();

		if (pos.getRow() == 1 && pos.getColumn() == 1) {
			cellPref.width = Math.max(cellPref.width, (int) Math
					.round((double) (prefSize.getWidth()
							- margin * 4) / 5));
		} else {
			cellPref.width = Math.max(cellPref.width,
					(int) Math.round(prefSize.getWidth()));
		}
		
		cellPref.height = Math.max(cellPref.height,
				(int) Math.round(prefSize.getHeight()));
		

	}

	/**
	 * Determines some size of the given container, using its components and a
	 * given BiConsumerObject which performs size calculations based on
	 * components that the container contains.
	 * 
	 * @param  target Target container for which the size is determined
	 * @param  bc     Object that calculates the size of the container based on
	 *                sizes of its children
	 * @return        calculated dimensions of target container
	 */
	private Dimension calculateSize(
			Container target,
			BiConsumer<Component, DimensionDouble> bc) {
		
		// to prevent rounding errors
		DimensionDouble dimD = new DimensionDouble();

		for (int i = 1; i <= rows; i++) {
			for (int j = 1; j <= cols; j++) {
				if (i == 1 && j > 1 && j < 6) { // illegal position
					continue;
				}

				if (grid[i][j] != null) { // component exists
					bc.accept(grid[i][j], dimD); // increase dimensions


					// account for the margins
					if (isCornerCell(i, i)) { // both dimensions only one side
						dimD.height += (double) margin / 2;
						dimD.width += (double) margin / 2;
					} else if (isEdgeRowCell(i, j)) { // only top or bottom
						dimD.height += (double) margin / 2;
						dimD.width += (double) margin;
					} else if (isEdgeColumnCell(i, j)) { // only left or right
						dimD.height += (double) margin;
						dimD.width += (double) margin / 2;
					} else { // all margins
						dimD.height += (double) margin;
						dimD.width += (double) margin;
					}

				}

			}
		}

		return new Dimension((int) Math.round(dimD.height),
				(int) Math.round(dimD.width));
	}

	/**
	 * Determines whether a cell with given row and column is at the edge row
	 * (first or last). Edge row cells are special because they do not have
	 * margins on both the top side and the bottom side of the cell.
	 * 
	 * @param  row row of the cell
	 * @param  col column of the cell
	 * @return     <code>true</code> if the cell is on edge row,
	 *             <code>false</code> otherwise.
	 */
	private boolean isEdgeRowCell(int row, int col) {
		return row == 1 || row == rows;
	}

	/**
	 * Determines whether a cell with given row and column is at the edge column
	 * (first or last). Edge column cells are special because they do not have
	 * margins on both the left side and right side of the cell.
	 * 
	 * @param  row row of the cell
	 * @param  col column of the cell
	 * @return     <code>true</code> if the cell is on edge column,
	 *             <code>false</code> otherwise.
	 */
	private boolean isEdgeColumnCell(int row, int col) {
		return col == 1 || col == cols;
	}

	/**
	 * Determines whether a cell with given row and column is at the corner.
	 * Corner cells are special because they only have one of top and bottom
	 * margin, and one of left and right margin.
	 * 
	 * @param  row row of the cell
	 * @param  col column of the cell
	 * @return     <code>true</code> if the cell is in the corner
	 *             <code>false</code> otherwise.
	 */
	private boolean isCornerCell(int row, int col) {
		return isEdgeRowCell(row, col) && isEdgeColumnCell(row, col);
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return calculateSize(target,
				newDimensionConsumer(SizeType.MAXIMUM));
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
		cellPref.height = 0;
		cellPref.width = 0;

	}


	/**
	 * Parses a given string to produce valid RCPosition object.
	 * 
	 * @param  text                     string to parse
	 * @return                          valid RCPosition object which
	 *                                  corresponds to the given string
	 * @throws IllegalArgumentException if string is not parsable into
	 *                                  RCPosition
	 */
	public static RCPosition parse(String text) {
		String[] tokens = text.split(",");
		if (tokens.length != 2) {
			throw new IllegalArgumentException("Cannot parse.");
		}
		try {
			return new RCPosition(Integer.parseInt(tokens[0].trim()),
					Integer.parseInt(tokens[1].trim()));
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Cannot parse.");
		}
	}

}
