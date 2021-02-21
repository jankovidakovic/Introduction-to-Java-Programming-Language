package hr.fer.zemris.java.webapp2.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.webapp2.models.Band;

/**
 * Servlet that handles the requests to vote for some band.
 */
@WebServlet("/glasanje")
public class GlasanjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// get correct path
		String fileName = request
				.getServletContext()
				.getRealPath("/WEB-INF/glasanje-definicija.txt");

		Path filePath = Paths.get(fileName);
		BufferedReader reader = Files.newBufferedReader(filePath);

		// read bands into list
		List<Band> bands = new ArrayList<Band>();
		reader.lines().forEach(line -> {
			String[] tokens = line.split("\\t");
			bands.add(new Band(Integer.parseInt(tokens[0]), tokens[1],
					tokens[2]));
		});

		// sort by ID
		bands.sort(Comparator.comparingInt(Band::getId));

		// insert to request parameters
		request.setAttribute("bands", bands);
		// forward
		request.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp")
				.forward(request, response);
	}

}
