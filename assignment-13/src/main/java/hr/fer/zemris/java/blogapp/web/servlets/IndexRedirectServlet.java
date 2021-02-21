package hr.fer.zemris.java.blogapp.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Responsible for redirecting users from the root URL to the URL of the home
 * page.
 * 
 * @author jankovidakovic
 *
 */
@WebServlet(urlPatterns = { "/", "/index.jsp" })
public class IndexRedirectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Redirects users from the root URL to the /servleti/main, which is the URL
	 * of the home page.
	 * 
	 * @param  request          HTTP request
	 * @param  response         HTTP response
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect(request.getContextPath() + "/servleti/main");
	}



}
