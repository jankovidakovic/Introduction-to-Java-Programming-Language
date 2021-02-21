package hr.fer.zemris.java.voting.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.voting.dao.DAOProvider;
import hr.fer.zemris.java.voting.model.PollOption;

/**
 * Servlet which creates XML representation of the poll results, and writes the
 * XML representation to the response stream.
 * 
 * @author jankovidakovic
 *
 */
@WebServlet("/servleti/glasanje-xls")
public class GlasanjeXls extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Long pollID = Long.parseLong(request.getParameter("pollID"));

		List<PollOption> options = DAOProvider.getDao()
				.getPollOptionsById(pollID);
		options.sort((a, b) -> b.getVotesCount().compareTo(a.getVotesCount()));

		HSSFWorkbook hwb = new HSSFWorkbook();
		
		HSSFSheet sheet = hwb.createSheet("1");
		HSSFRow rowHead = sheet.createRow(0);

		rowHead.createCell(0).setCellValue(request.getParameter("pollElement"));
		rowHead.createCell(0).setCellValue("Votes");

		for (int i = 1; i <= options.size(); i++) {
			HSSFRow row = sheet.createRow(i);
			row.createCell(0).setCellValue(options.get(i-1).getOptionTitle());
			row.createCell(1).setCellValue(options.get(i-1).getVotesCount());
		}

		response.setContentType("application/vnd.ms-excel");

		response.setHeader("Content-Disposition",
				"attachment;filename=\"rezultati.xls\"");

		hwb.write(response.getOutputStream());
		hwb.close();

	}

}
