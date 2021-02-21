package hr.fer.zemris.java.voting.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.voting.dao.DAOProvider;
import hr.fer.zemris.java.voting.model.PollOption;

/**
 * Handles requests which are made after a vote has been made on some poll.
 * 
 * @author jankovidakovic
 *
 */
@WebServlet("/servleti/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Handles GET request to the vote url.
	 * 
	 * @param  request          model of an HTTP request
	 * @param  response         model of an HTTP response
	 * @throws ServletException if anything goes wrong during request handling,
	 *                          regarding the back-end servlet side
	 * @throws IOException      if anything goes wrong durign request handling,
	 *                          regarding the persistence layer.
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// TODO - implement error page rendering if wrong ID is given
		Long id = Long.parseLong(request.getParameter("id"));

		PollOption option = DAOProvider.getDao().incrementVote(id);

		response.sendRedirect(request.getContextPath()
				+ "/servleti/glasanje-rezultati?pollID=" + option.getPollID());

	}

}
