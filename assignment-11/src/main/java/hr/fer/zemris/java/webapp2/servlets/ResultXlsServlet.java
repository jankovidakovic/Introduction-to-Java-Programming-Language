package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;
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

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Servlet that handles the request for XML Table containing the information
 * about the voting results. Servlet creates the table and returns it as a file.
 */
@WebServlet("/glasanje-xls")
public class ResultXlsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// read results from file
		String resultsPath = request.getServletContext()
				.getRealPath("WEB-INF/glasanje-rezultati.txt");
		List<String> results = Files.readAllLines(Paths.get(resultsPath));
		results.sort((a, b) -> b.split("\\t")[1].compareTo(a.split("\\t")[1]));
		// read band data
		String bandsPath = request.getServletContext()
				.getRealPath("WEB-INF/glasanje-definicija.txt");
		List<String> bandsLines = Files.readAllLines(Paths.get(bandsPath));

		// create a data structure to get band name by ID
		Map<Integer, String> getBandNameById = new HashMap<Integer, String>();

		bandsLines.forEach(bandString -> {
			// tokens[0] = band ID
			// tokens[1] = band name
			// tokens[2] = band song
			String[] tokens = bandString.split("\\t");
			getBandNameById.put(Integer.parseInt(tokens[0]), tokens[1]);
		});

		// create xml page
		HSSFWorkbook hwb = new HSSFWorkbook();

		HSSFSheet sheet = hwb.createSheet("1");
		HSSFRow rowHead = sheet.createRow(0);
		rowHead.createCell(0).setCellValue("Band");
		rowHead.createCell(1).setCellValue("Votes");

		for (int i = 1; i <= results.size(); i++) {
			HSSFRow row = sheet.createRow(i);
			row.createCell(0).setCellValue(getBandNameById
					.get(Integer.parseInt(results.get(i - 1).split("\\t")[0])));
			row.createCell(1).setCellValue(results.get(i - 1).split("\\t")[1]);
		}

		// set response type
		response.setContentType("application/vnd.ms-excel");

		// set "save as" type
		response.setHeader("Content-Disposition",
				"attachment; filename=\"rezultati.xls\"");

		// write file to response
		hwb.write(response.getOutputStream());
		hwb.close();
	}

}
