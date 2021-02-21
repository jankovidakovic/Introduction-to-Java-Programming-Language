package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that handles an attempt to set the background color to some different
 * value. Servlet takes the color as a request argument and copies it into
 * session parameter, so that all pages can access it.
 * 
 * @author jankovidakovic
 *
 */
@WebServlet("/setcolor")
public class SetColorServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// get request parameter
		String color = (String) req.getParameter("color");
		if (color == null) {
			color = "white";
		}

		// make session parameter
		req.getSession().setAttribute("pickedBgCol", color);

		// forward to home page
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
}
