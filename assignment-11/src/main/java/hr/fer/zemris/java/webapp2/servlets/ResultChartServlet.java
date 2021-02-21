package hr.fer.zemris.java.webapp2.servlets;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Servlet that handles the requests for chart representing the results of the
 * voting. Servlet creates a chart and writes it to response body.
 */
@WebServlet("/glasanje-grafika")
public class ResultChartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// get output stream for response
		OutputStream out = response.getOutputStream();
		try {
			DefaultPieDataset resultDataset = new DefaultPieDataset();

			// read results from file
			String resultsPath = request.getServletContext()
					.getRealPath("WEB-INF/glasanje-rezultati.txt");
			List<String> results = Files.readAllLines(Paths.get(resultsPath));

			// read band data
			String bandsPath = request.getServletContext()
					.getRealPath("WEB-INF/glasanje-definicija.txt");
			List<String> bandsLines = Files.readAllLines(Paths.get(bandsPath));

			// create a data structure to get band name by ID
			Map<Integer, String> getBandNameById =
					new HashMap<Integer, String>();

			bandsLines.forEach(bandString -> {
				// tokens[0] = band ID
				// tokens[1] = band name
				// tokens[2] = band song
				String[] tokens = bandString.split("\\t");
				getBandNameById.put(Integer.parseInt(tokens[0]), tokens[1]);
			});

			// insert data
			results.forEach(result -> {
				// tokens[0] = band id
				// tokens[1] = votes
				String[] tokens = result.split("\\t");
				resultDataset.setValue(
						getBandNameById.get(Integer.parseInt(tokens[0])),
						Integer.parseInt(tokens[1]));
			});

			JFreeChart resultsPieChart = ChartFactory.createPieChart(
					"Results Chart", resultDataset, false, true, false);

			// plot chart
			PiePlot ColorConfigurator = (PiePlot) resultsPieChart.getPlot();
			ColorConfigurator.setLabelGenerator(
					new StandardPieSectionLabelGenerator("{0}:{1}"));
			ColorConfigurator.setLabelBackgroundPaint(new Color(220, 220, 220));

			// set response type
			response.setContentType("image/png");

			// write chart to response
			ChartUtils.writeChartAsPNG(out, resultsPieChart, 640, 480);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			out.close();
		}

	}

}
