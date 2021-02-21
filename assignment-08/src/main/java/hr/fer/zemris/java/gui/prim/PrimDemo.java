package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program which demonstrates the functionality of PrimListModel. Program
 * creates a simple GUI that demonstrates the capabilities of the model.
 * 
 * @author jankovidakovic
 *
 */
public class PrimDemo extends JFrame {

	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(300, 200);
		initGUI();
	}

	/**
	 * Creates GUI for the demonstration
	 */
	private void initGUI() {

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		PrimListModel model = new PrimListModel();

		JList<Integer> leftList = new JList<>(model);
		JList<Integer> rightList = new JList<>(model);

		JPanel listPanel = new JPanel(new GridLayout(1, 0));

		listPanel.add(new JScrollPane(leftList));
		listPanel.add(new JScrollPane(rightList));

		cp.add(listPanel, BorderLayout.CENTER);

		JButton next = new JButton("sljedeći");
		next.addActionListener(a -> {
			model.next();
		});

		cp.add(next, BorderLayout.PAGE_END);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new PrimDemo();
			frame.setVisible(true);
		});
	}

}
