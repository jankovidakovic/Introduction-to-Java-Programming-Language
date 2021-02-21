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

/**
 * Wervlet which handles requests to the home page.
 * 
 * @author jankovidakovic
 *
 */
@WebServlet("/servleti/index.html")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Fetches the list of defined polls and renders a dynamic web page that
	 * displays the polls as clickable links.
	 * 
	 * @param request  object which represents the http request
	 * @param response object which represents the http response
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List<Poll> polls = DAOProvider.getDao().fetchPolls();
		request.setAttribute("polls", polls);

		// forward to JSP file for rendering
		request.getRequestDispatcher("/WEB-INF/pages/index.jsp")
				.forward(request, response);
	}

}
