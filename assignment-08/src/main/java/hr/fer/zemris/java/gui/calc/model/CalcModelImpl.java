package hr.fer.zemris.java.gui.calc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

/**
 * Implementation of a calculator model that is used as a back-end structure of
 * a simple GUI calculator.
 * 
 * @author jankovidakovic
 *
 */
public class CalcModelImpl implements CalcModel {

	private boolean editable; // flag that denotes whether the model is editable
	private boolean positive; // flag for positivity of entered number
	private String enteredDigits; // String that consists of entered digits
	private double enteredValue; // value of entered digits
	private String frozenValue; // frozen value

	private String activeOperand; // active operand

	private DoubleBinaryOperator pendingOperation; // pending operation

	private final List<CalcValueListener> listeners; // listen for the value change

	/**
	 * Constructs a calculator in its default state.
	 */
	public CalcModelImpl() {
		editable = true;
		positive = true;
		enteredDigits = "";
		enteredValue = 0;
		frozenValue = null;

		activeOperand = null;
		pendingOperation = null;

		listeners = new ArrayList<>();
	}

	@Override
	public void addCalcValueListener(CalcValueListener l) {
		listeners.add(l);

	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(l);

	}

	private void notifyListeners() {
		for (CalcValueListener l : listeners) {
			l.valueChanged(this);
		}
	}

	@Override
	public double getValue() {
		return enteredValue;
	}

	@Override
	public void setValue(double value) {
		enteredValue = value;
		enteredDigits = Double.toString(value); // parse value
		if (enteredDigits.startsWith("-")) { // remove leading minus
			enteredDigits = enteredDigits.substring(1);
			positive = false;
		}

		editable = false;

		notifyListeners();

	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void clear() {
		enteredDigits = "";
		enteredValue = 0;
		editable = true;
		positive = true;

		notifyListeners();

	}

	@Override
	public void clearAll() {
		clear();
		activeOperand = null;
		pendingOperation = null;
		frozenValue = null;
		notifyListeners();

	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if (!isEditable()) {
			throw new CalculatorInputException();
		}
		frozenValue = null;
		positive ^= true;
		enteredValue *= -1;

		notifyListeners();
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if (!isEditable()) {
			throw new CalculatorInputException();
		}
		if (enteredDigits.contains(".")) {
			throw new CalculatorInputException("Dot already inserted.");
		}
		if (enteredDigits.isEmpty()) {
			throw new CalculatorInputException("No digits.");
		}
		frozenValue = null;
		enteredDigits += ".";

	}

	@Override
	public void insertDigit(int digit)
			throws CalculatorInputException, IllegalArgumentException {
		if (!isEditable()) {
			throw new CalculatorInputException();
		}
		if (digit < 0 || digit > 9) {
			throw new IllegalArgumentException("Single digit expected");
		}
		frozenValue = null;
		if (digit == 0 && enteredDigits.equals("0")) {
			return; // ignore multiple leading zeroes
		}
		String newString = enteredDigits + digit;
		if (Double.parseDouble(newString) > Double.MAX_VALUE) {
			throw new CalculatorInputException("Too big");
		}
		if (enteredDigits.equals("0")) {
			enteredDigits = Integer.toString(digit); // delete leading zero
		} else {
			enteredDigits += Integer.toString(digit);
		}
		enteredValue = Double.parseDouble(enteredDigits);

		if (!positive) {
			enteredValue *= -1;
		}

		notifyListeners();
	}

	@Override
	public boolean isActiveOperandSet() {
		return activeOperand != null;
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if (!isActiveOperandSet()) {
			throw new IllegalStateException("No active operand.");
		}
		return Double.parseDouble(activeOperand);
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = Double.toString(activeOperand);
	}

	@Override
	public void clearActiveOperand() {
		activeOperand = null;
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		pendingOperation = op;

	}

	@Override
	public String toString() {
		if (hasFrozenValue()) {
			return frozenValue;
		}
		if (enteredDigits.isEmpty()) {
			return positive ? "0" : "-0";
		}
		return positive ? enteredDigits : "-" + enteredDigits;
	}

	/**
	 * Stores the given string into the frozen value, which is displayed when
	 * the calculator is not editable.
	 * 
	 * @param value Value to be displayed when the calculator is not editable
	 */
	@Override
	public void freezeValue(String value) {
		this.frozenValue = value;
	}

	/**
	 * Checks whether or not a calculator has frozen value stored.
	 * 
	 * @return <code>true</code> if frozen value is stored, <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean hasFrozenValue() {
		return frozenValue != null;
	}

}
