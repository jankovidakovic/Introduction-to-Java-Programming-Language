package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.webapp2.models.Band;
import hr.fer.zemris.java.webapp2.models.BandResult;

/**
 * Servlet that handles the requests to display information about the results of
 * the voting.
 */
@WebServlet("/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// get correct path to results
		String resultFileName = request
				.getServletContext()
				.getRealPath("/WEB-INF/glasanje-rezultati.txt");

		// make file if needed, otherwise only read
		Path resultFilePath = Paths.get(resultFileName);
		if (Files.exists(resultFilePath) == false) {
			Files.createFile(resultFilePath); // create file
			System.out.println("Created glasanje-rezultati.txt");
		}

		// read lines from file (if it was just created, list will be empty)
		List<String> resultLines = Files.readAllLines(resultFilePath);

		// map for mapping the results
		// key = bandID, value = votes
		Map<Integer, Integer> resultsFromFile = new HashMap<Integer, Integer>();

		resultLines.forEach(line -> {
			String[] tokens = line.split("\\t"); // split line
			// tokens[0] is band ID, tokens[1] is votes
			resultsFromFile.put(Integer.parseInt(tokens[0]),
					Integer.parseInt(tokens[1]));
		});

		// open band definitions
		String bandFileName = request.getServletContext()
				.getRealPath("WEB-INF/glasanje-definicija.txt");
		Path bandFilePath = Paths.get(bandFileName);
		// read bands into map
		// key = band, value = how much votes they got
		Map<Band, Integer> resultsMap = new HashMap<Band, Integer>();

		// preparation list for bands
		List<String> bandLines = Files.readAllLines(bandFilePath);
		// bands will be parsed and put into bands list
		List<Band> bands = new ArrayList<Band>();


		bandLines.forEach(line -> {

			// tokens[0] = band ID, tokens[1] = name, tokens[2] = song
			String[] tokens = line.split("\\t");
			// create band from tokens
			Band band =
					new Band(Integer.parseInt(tokens[0]), tokens[1], tokens[2]);

			bands.add(band); // add band
			// add to result map
			// use getDefault because maybe not all bands were defined in voting
			// result
			resultsMap.put(band, resultsFromFile.getOrDefault(band.getId(), 0));
		});

		// collect map to list which will be passed to jsp file
		List<BandResult> resultsToPass = resultsMap.entrySet().stream()
				.map(entry -> new BandResult(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());

		// sort descending
		resultsToPass.sort((b1, b2) -> b2.compareTo(b1));

		// insert into request
		request.setAttribute("results", resultsToPass);

		// extract the maximum votes
		int maxVotes = resultsToPass.get(0).getVotes();

		// create list of bands with most votes
		List<Band> victors = resultsToPass.stream()
				.filter(result -> result.getVotes() == maxVotes)
				.map(bandresult -> bandresult
						.getBand())
				.collect(Collectors.toList());

		// insert into request
		request.setAttribute("victors", victors);

		// forward to JSP for rendering the page
		request.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp")
				.forward(request, response);
	}

}
