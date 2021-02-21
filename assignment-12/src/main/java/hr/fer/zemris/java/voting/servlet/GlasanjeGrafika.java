package hr.fer.zemris.java.voting.servlet;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

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

import hr.fer.zemris.java.voting.dao.DAOProvider;
import hr.fer.zemris.java.voting.model.PollOption;

/**
 * Servlet which creates a PNG representation of the poll results as a pie
 * chart, and writes the file to the response.
 * 
 * @author jankovidakovic
 *
 */
@WebServlet("/servleti/glasanje-grafika")
public class GlasanjeGrafika extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try (OutputStream out = response.getOutputStream()) {
			DefaultPieDataset dataset = new DefaultPieDataset();
			
			Long pollID = Long.parseLong(request.getParameter("pollID"));

			// fetch data
			List<PollOption> options =
					DAOProvider.getDao().getPollOptionsById(pollID).stream()
							.filter(option -> option.getVotesCount() != 0)
							.sorted((a, b) -> b.getVotesCount()
									.compareTo(a.getVotesCount()))
							.collect(Collectors.toList());

			// fill dataset
			options.forEach(option -> dataset.setValue(option.getOptionTitle(),
					option.getVotesCount()));

			JFreeChart pieChart = ChartFactory.createPieChart("Poll results",
					dataset, false, true, false);

			PiePlot colorConfig = (PiePlot) pieChart.getPlot();
			colorConfig.setLabelGenerator(
					new StandardPieSectionLabelGenerator("{0}:{1}"));

			colorConfig.setLabelBackgroundPaint(new Color(220, 220, 220));

			response.setContentType("image/png");

			ChartUtils.writeChartAsPNG(out, pieChart, 640, 480);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
