package hr.fer.zemris.java.voting.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Server which redirects the request for the home page to the url that obliges
 * the persistence layer url mapping convention.
 * 
 * @author jankovidakovic
 *
 */
@WebServlet(name = "RedirectToHomeServlet", urlPatterns = { "/index.html" })
public class RedirectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("servleti/index.html");
	}

}
