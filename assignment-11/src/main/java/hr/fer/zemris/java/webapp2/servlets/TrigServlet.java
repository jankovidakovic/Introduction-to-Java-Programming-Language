package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.webapp2.models.TrigEntry;

/**
 * Servlet that handles request for displaying trigonometric calculations on
 * some range of numbers. Servlet performs all the neccessary calculations and
 * passes the data to the page that renders it.
 * 
 * @author jankovidakovic
 *
 */
@WebServlet("/trigonometric")
public class TrigServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Integer a = null; // beginning of the range
		Integer b = null; // end of the range

		// parse a
		try {
			a = Integer.parseInt(req.getParameter("a"));
		} catch (NumberFormatException e) {
			a = 0;
		}
		
		// parse b
		try {
			b = Integer.parseInt(req.getParameter("b"));
		} catch (NumberFormatException e) {
			b = 360;
		}

		if (b < a) { // swap
			int tmp = a;
			a = b;
			b = tmp;
		}
		if (b > a + 720) { // reduce
			b = a + 720;
		}


		List<TrigEntry> trigEntries = new ArrayList<TrigEntry>();

		// generate list of values
		for (int i = a; i <= b; i++) {
			trigEntries.add(new TrigEntry(i));
		}

		req.setAttribute("trigEntries", trigEntries);

		req.setAttribute("size", trigEntries.size());

		req.setAttribute("a", a);
		req.setAttribute("b", b);

		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp")
				.forward(req, resp);

	}
}
