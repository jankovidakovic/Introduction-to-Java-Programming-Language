package hr.fer.zemris.java.gui.layouts;

/**
 * Position for the CalcLayout manager. Defined by its two read-only properties,
 * row and column.
 * 
 * @author jankovidakovic
 *
 */
public class RCPosition {

	private final int row; // row in the layout
	private final int column; // column in the layout

	/**
	 * Constructs a position with given row and column
	 * 
	 * @param row
	 * @param column
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * Returns the row of the position
	 * 
	 * @return row of the position
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns the column of the position
	 * 
	 * @return column of the position
	 */
	public int getColumn() {
		return column;
	}
}
