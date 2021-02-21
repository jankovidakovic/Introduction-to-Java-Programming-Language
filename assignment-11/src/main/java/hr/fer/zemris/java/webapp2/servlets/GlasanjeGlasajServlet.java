package hr.fer.zemris.java.webapp2.servlets;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that handles the action of voting for some band. Servlet saves the
 * vote in the file glasanje-rezultati.txt and then forwards the request to the
 * servlet that handles the display of results.
 */
@WebServlet("/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// get correct path
		String fileName = request
				.getServletContext()
				.getRealPath("/WEB-INF/glasanje-rezultati.txt");

		// create file if necessary
		Path filePath = Paths.get(fileName);
		if (!Files.exists(filePath)) {
			Files.createFile(filePath);
		}

		// update voting data
		int id = Integer.parseInt(request.getParameter("id"));
		List<String> lines = Files.readAllLines(filePath);
		boolean updated = false;

		// find appropriate line to increment vote
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			String[] tokens = line.split("\\t");
			if (Integer.parseInt(tokens[0]) == id) { // found the band
				tokens[1] = Integer.toString(Integer.parseInt(tokens[1]) + 1);
				updated = true;
				line = tokens[0] + "\t" + tokens[1];
				lines.set(i, line);
				break;
			}
		}
		if (!updated) { // no line found
			String newLine = Integer.toString(id) + "\t1";
			lines.add(newLine);
		}

		// clear file and write back the results
		BufferedWriter writer = Files.newBufferedWriter(filePath);
		writer.write(String.join("\n", lines));
		writer.flush();
		writer.close();
		// redirect, no response
		response.sendRedirect(request.getContextPath() + "/glasanje-rezultati");
	}

}
