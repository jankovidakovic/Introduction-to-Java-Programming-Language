package hr.fer.zemris.java.voting.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.voting.dao.DAOProvider;
import hr.fer.zemris.java.voting.model.Poll;
import hr.fer.zemris.java.voting.model.PollOption;

/**
 * Servlet which handles the request to get poll results page.
 * 
 * @author jankovidakovic
 *
 */
@WebServlet("/servleti/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// get poll ID from URL param
		Long pollID = Long.parseLong(request.getParameter("pollID"));

		request.setAttribute("pollID", pollID);
		// get poll options
		List<PollOption> options =
				DAOProvider.getDao().getPollOptionsById(pollID);
		options.sort((a, b) -> b.getVotesCount().compareTo(a.getVotesCount()));

		request.setAttribute("options", options);

		// get current poll winners
		List<PollOption> winners =
				DAOProvider.getDao().getPollWinners(pollID);
		request.setAttribute("winners", winners);

		Poll poll = DAOProvider.getDao().getPollById(pollID);
		if (poll.getTitle().contains("band")) {
			request.setAttribute("pollElement", "Band");
		} else if (poll.getTitle().contains("movie")) {
			request.setAttribute("pollElement", "Movie");
		} else {
			request.setAttribute("pollElement", "Unknown Entity");
		}

		// redirect to rendering
		request.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp")
				.forward(request, response);


	}

}
