package hr.fer.zemris.java.webapp2.servlets;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

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
 * Servlet that handles the request for the infographic chart containing
 * information about OS Usage worldwide.
 */
@WebServlet("/reportImage")
public class ReportImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// get output stream for response
		OutputStream out = response.getOutputStream();
        try {
                DefaultPieDataset osUsageDataset = new DefaultPieDataset();

				// set values
				osUsageDataset.setValue("Android", 37.66);
				osUsageDataset.setValue("Windows", 35.94);
				osUsageDataset.setValue("iOS", 15.28);
				osUsageDataset.setValue("OS X", 8.59);
				osUsageDataset.setValue("Unknown", 9);
				osUsageDataset.setValue("Linux", 7.9);
                
				// create pie chart from dataset
				JFreeChart osUsagePieChart = ChartFactory.createPieChart(
						"OS Usage chart", osUsageDataset, false, true, false);

				// create object that plots the chart
                PiePlot ColorConfigurator = (PiePlot) osUsagePieChart.getPlot();
                ColorConfigurator.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}:{1}"));
				ColorConfigurator
						.setLabelBackgroundPaint(new Color(220, 220, 220));

				// set response type
				response.setContentType("image/png");

				// write char to response
                ChartUtils.writeChartAsPNG(out, osUsagePieChart, 640, 480);
        }
        catch (Exception e) {
			System.err.println(e.getMessage());
        }
        finally {
			out.close();
        }

	}

}
