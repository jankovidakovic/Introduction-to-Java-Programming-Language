package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;
import hr.fer.zemris.java.gui.layouts.CalcLayout;

/**
 * Model of a graphical calculator.
 * 
 * @author jankovidakovic
 *
 */
public class Calculator extends JFrame {

	private CalcModel model;
	private boolean inverse; // TODO - maybe fix

	// TODO - implement actions as strategy pattern

	// performed when the digit is clicked
	private ActionListener onClickOfDigit = a -> {
		DigitButton digit = (DigitButton) a.getSource();
		model.insertDigit(digit.getDigit());
	};

	// performed when the binary operation is clicked
	private ActionListener onClickOfBinaryOperation = a -> {
		BinaryOperationButton op = (BinaryOperationButton) a.getSource();
		if (model.isActiveOperandSet()) {
			// execute the operation
			double left = model.getActiveOperand();
			double right = model.getValue();
			double result = model
					.getPendingBinaryOperation()
					.applyAsDouble(left, right);
			model.setValue(result);
			model.freezeValue(Double.toString(result));
		}

		model.setPendingBinaryOperation(op.getOperation());
		model.setActiveOperand(model.getValue());
		model.freezeValue(Double.toString(model.getValue()));
		model.clear();
	};

	// performed when the unary operation is clicked
	private ActionListener onClickOfUnaryOperation = a -> {
		UnaryOperationButton op = (UnaryOperationButton) a.getSource();
		double value = model.getValue(); // current value
		double result = 0;
		if (inverse) { // apply inverse operation
			result = op.getInverseOperation().applyAsDouble(value);
		} else { // apply normal operation
			result = op.getOperation().applyAsDouble(value);
		}
		model.clear(); // clear the calculator
		model.setValue(result); // set new value
		model.freezeValue(Double.toString(result));
	};

	// performed when the exponentiation button is clicked
	private ActionListener onClickOfExpButton = a -> {
		ExpButton op = (ExpButton) a.getSource();
		if (model.isActiveOperandSet()) {
			// execute the operation
			double left = model.getActiveOperand();
			double right = model.getValue();
			double result = model
					.getPendingBinaryOperation()
					.applyAsDouble(left, right);
			model.setValue(result);
		}

		if (inverse) {
			model.setPendingBinaryOperation(op.getInverseOperation());
		} else {
			model.setPendingBinaryOperation(op.getOperation());
		}

		model.setActiveOperand(Calculator.this.model.getValue());
		model.clear();
	};

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a calculator in its default state, and displays its graphical
	 * interface.
	 */
	public Calculator() {
		super();
		model = new CalcModelImpl();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Java Calculator v1.0");
		initGUI();
		setSize(640, 480);
	}

	/**
	 * Initializes the graphical user interface of the calculator, and lays out
	 * all the components that it has. It uses the custom made CalcLayout layout
	 * manager.
	 */
	private void initGUI() {

		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(5));

		JLabel display = new JLabel("");
		display.setHorizontalAlignment(JLabel.RIGHT);
		display.setFont(display.getFont().deriveFont(30f));
		display.setBackground(Color.YELLOW);
		display.setOpaque(true);
		display.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		model.addCalcValueListener(model -> {
			display.setText(model.toString());
		});

		cp.add(display, "1,1");

		cp.add(new SimpleButton("=", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Calculator.this.model.isActiveOperandSet()) {
					double left = model.getActiveOperand();
					double right = model.getValue();
					double result = model
							.getPendingBinaryOperation()
									.applyAsDouble(left, right);
					model.clearAll(); // reset everything
					model.setValue(result); // show result
				}
			}

		}), "1,6");

		cp.add(new SimpleButton("clr", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.clear();
			}

		}), "1,7");

		cp.add(new UnaryOperationButton("1/x", x -> 1 / x, x -> 1 / x,
				onClickOfUnaryOperation), "2,1");

		UnaryOperationButton sine =
				new UnaryOperationButton("sin", Math::sin, Math::asin,
						onClickOfUnaryOperation);
		cp.add(sine, "2,2");

		cp.add(new DigitButton(7, onClickOfDigit), "2,3");
		cp.add(new DigitButton(8, onClickOfDigit), "2,4");
		cp.add(new DigitButton(9, onClickOfDigit), "2,5");

		cp.add(new BinaryOperationButton("/", (a, b) -> a / b,
				onClickOfBinaryOperation), "2,6");
		cp.add(new SimpleButton("reset", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.clearAll();
			}

		}), "2,7");

		UnaryOperationButton logarithm = new UnaryOperationButton("log",
				Math::log10, x -> Math.pow(10, x), onClickOfUnaryOperation);
		cp.add(logarithm, "3,1");

		UnaryOperationButton cosine =
				new UnaryOperationButton("cos", Math::cos, Math::acos,
						onClickOfUnaryOperation);
		cp.add(cosine, "3,2");

		cp.add(new DigitButton(4, onClickOfDigit), "3,3");
		cp.add(new DigitButton(5, onClickOfDigit), "3,4");
		cp.add(new DigitButton(6, onClickOfDigit), "3,5");

		cp.add(new BinaryOperationButton("*", (a, b) -> a * b,
				onClickOfBinaryOperation), "3,6");

		cp.add(new SimpleButton("push", null), "3,7");

		UnaryOperationButton naturalLog =
				new UnaryOperationButton("ln", Math::log, Math::exp,
						onClickOfUnaryOperation);
		cp.add(naturalLog, "4,1");

		UnaryOperationButton tangent =
				new UnaryOperationButton("tan", Math::tan, Math::atan,
						onClickOfUnaryOperation);
		cp.add(tangent, "4,2");

		cp.add(new DigitButton(1, onClickOfDigit), "4,3");
		cp.add(new DigitButton(2, onClickOfDigit), "4,4");
		cp.add(new DigitButton(3, onClickOfDigit), "4,5");

		cp.add(new BinaryOperationButton("-", (a, b) -> a - b,
				onClickOfBinaryOperation), "4,6");
		cp.add(new SimpleButton("pop", null), "4,7");

		ExpButton exponentiation =
				new ExpButton("x^n", Math::pow, (x, n) -> Math.pow(x, 1 / n),
						onClickOfExpButton);
		cp.add(exponentiation, "5,1");

		UnaryOperationButton cotangent = new UnaryOperationButton("ctg",
				x -> 1 / Math.tan(x), x -> Math.PI / 2 - 1 / Math.atan(x),
				onClickOfUnaryOperation);
		cp.add(cotangent, "5,2");

		cp.add(new DigitButton(0, onClickOfDigit), "5,3");
		cp.add(new SimpleButton("+/-", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.swapSign();
			}

		}), "5,4");
		cp.add(new SimpleButton(".", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.insertDecimalPoint();
			}

		}), "5,5");
		cp.add(new BinaryOperationButton("+", (a, b) -> a + b,
				onClickOfBinaryOperation), "5,6");

		JCheckBox inv = new JCheckBox("Inv");

		inv.addActionListener(a -> {
			inverse ^= true;
			if (inverse) {
				sine.setText("arcsin");
				cosine.setText("arccos");
				tangent.setText("arctan");
				cotangent.setText("arcctg");

				naturalLog.setText("e^x");
				logarithm.setText("10^x");

				exponentiation.setText("x^(1/n)");

			} else {
				sine.setText("sin");
				cosine.setText("cos");
				tangent.setText("tan");
				cotangent.setText("ctg");

				naturalLog.setText("ln");
				logarithm.setText("log");

				exponentiation.setText("x^n");
			}

		});
		cp.add(inv, "5,7");
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Calculator calc = new Calculator();
			calc.setVisible(true);
		});

	}

}
