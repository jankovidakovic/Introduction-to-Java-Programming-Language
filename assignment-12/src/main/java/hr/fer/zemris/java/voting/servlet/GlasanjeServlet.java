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
 * Servlet which handles request to the voting page, on which the users can vote
 * in a chosen poll.
 * 
 * @author jankovidakovic
 *
 */
@WebServlet("/servleti/glasanje")
public class GlasanjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// get poll ID
		Long pollID = Long.parseLong(request.getParameter("pollID"));

		// get poll from database
		Poll poll = DAOProvider.getDao().getPollById(pollID);

		List<PollOption> options =
				DAOProvider.getDao().getPollOptionsById(pollID);
		options.sort((a, b) -> a.getId().compareTo(b.getId()));

		request.setAttribute("poll", poll);
		request.setAttribute("options", options);
		request.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp")
				.forward(request, response);
	}

}
