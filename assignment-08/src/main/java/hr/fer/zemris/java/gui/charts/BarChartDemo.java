package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program which demonstrates the functionality of BarChartComponent. Program
 * loads the chart data from a file, path to which should be given as a
 * command-line argument. Program parses the data and if it is in valid format,
 * produces a graphical chart representing the data.
 * 
 * @author jankovidakovic
 *
 */
public class BarChartDemo extends JFrame {

	private static final long serialVersionUID = 1L;

	private BarChart chartData; // data for drawing
	private String fileName; // file which contains the data

	/**
	 * Constructs a new instance of BarChartDemo, which will be used to draw the
	 * chart from given data, which was read from given filename
	 * 
	 * @param chartData data used to draw the chart
	 * @param fileName  file name from which the data has been extracted
	 */
	public BarChartDemo(BarChart chartData, String fileName) {
		super();
		this.chartData = chartData;
		this.fileName = fileName;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Java Chart v1.0");
		initGUI();
		pack();
	}

	/**
	 * Initializes the graphical user interface to display the chart along with
	 * information about the file that contains the chart data.
	 */
	private void initGUI() {

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		JLabel filenameLabel = new JLabel(fileName);
		filenameLabel.setHorizontalAlignment(JLabel.CENTER);
		filenameLabel.setPreferredSize(new Dimension(640, 20));
		filenameLabel.setOpaque(true);
		filenameLabel.setBackground(Color.WHITE);
		filenameLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		cp.add(filenameLabel, BorderLayout.PAGE_START);

		JComponent chart = new BarChartComponent(chartData);
		chart.setPreferredSize(new Dimension(640, 480));

		cp.add(chart, BorderLayout.CENTER);


	}

	public static void main(String[] args)
			throws InvocationTargetException, InterruptedException {

		if (args.length != 1) {
			System.out.println("Program takes one argument, "
					+ "path to the file containing chart data.");
			System.exit(1);
		}
		String arg = args[0];
		try {
			Path path = Paths.get(arg);
			if (!Files.exists(path)) {
				System.out.println("Invalid path, file does not exist.");
				System.exit(1);
			}
			Scanner sc = new Scanner(path);
			String xDesc = sc.nextLine();
			String yDesc = sc.nextLine();
			String[] entries = sc.nextLine().split("\\s+");
			List<XYValue> values = new ArrayList<XYValue>();
			for (int i = 0; i < entries.length; i++) {
				String[] params = entries[i].split(",");
				if (params.length != 2) {
					System.out.println("Invalid file format.");
					System.exit(1);
				}
				try {
					int x = Integer.parseInt(params[0]);
					int y = Integer.parseInt(params[1]);
					values.add(new XYValue(x, y));
				} catch (NumberFormatException e) {
					System.out.println("Invalid file format.");
					System.exit(1);
				}
			}
			int yMin = sc.nextInt();
			int yMax = sc.nextInt();
			int ySpacing = sc.nextInt();

			SwingUtilities.invokeAndWait(() -> {
				JFrame chartDemo = new BarChartDemo(new BarChart(values, xDesc,
						yDesc, yMin, yMax, ySpacing),
						path.toAbsolutePath().toString());
				chartDemo.setVisible(true);
			});
			sc.close();

		} catch (IOException ex) {
			System.out.println("An error occured while trying to read a file.");
			System.exit(1);
		}
	}

}
