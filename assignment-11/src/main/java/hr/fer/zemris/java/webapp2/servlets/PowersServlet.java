package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Servlet that handles the request for an XML Table containig some range of
 * numbers and their powers, each power on its own sheet.
 */
@WebServlet("powers")
public class PowersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try { // parse parameters
			int a = Integer.parseInt(request.getParameter("a"));
			int b = Integer.parseInt(request.getParameter("b"));
			int n = Integer.parseInt(request.getParameter("n"));
			if (a < -100 || a > 100 || b < -100 || b > 100 || n < 1 || n > 5) {
				// error
				request.getRequestDispatcher("WEB-INF/pages/error.jsp")
						.forward(request, response);
			}
			
			//create xml page
			HSSFWorkbook hwb=new HSSFWorkbook();

			for (int i = 1; i <= n; i++) {
				// Create sheet
				HSSFSheet sheet = hwb.createSheet(Integer.toString(i));
				// create header row
				HSSFRow rowhead = sheet.createRow(0);
				rowhead.createCell(0).setCellValue("Number");
				rowhead.createCell(1).setCellValue("Power");
				// fill rows with numbers and their powers
				for (int j = a; j <= b; j++) {
					HSSFRow numRow = sheet.createRow(j - a + 1);
					numRow.createCell(0).setCellValue(j);
					numRow.createCell(1).setCellValue(Math.pow(j, i));
				}
			}

			response.setContentType("application/vnd.ms-excel");
			response.setHeader(
					"Content-Disposition",
					"attachment; filename=\"tablica.xls\"");

			hwb.write(response.getOutputStream());
			hwb.close();
		} catch (NumberFormatException e) { // couldnt parse parameters
			request.getRequestDispatcher("WEB-INF/pages/error.jsp")
					.forward(request, response);
		}

	}

}
