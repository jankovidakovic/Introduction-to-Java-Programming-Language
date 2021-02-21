package hr.fer.zemris.java.blogapp.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.blogapp.dao.DAOProvider;
import hr.fer.zemris.java.blogapp.model.BlogUser;
import hr.fer.zemris.java.blogapp.model.form.LoginForm;
import hr.fer.zemris.java.blogapp.web.utils.SessionUtils;

/**
 * Main servlet which handles the home page of the application.
 * 
 * @author jankovidakovic
 *
 */
@WebServlet("/servleti/main/*")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Handles the GET request by forwarding it to the dynamic JSP page that
	 * renders the home page, or if requested, logs the user out of the
	 * application.
	 * 
	 * @param  request          HTTP Request
	 * @param  response         HTTP response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// get information about the requested path
		String pathInfo = request.getPathInfo();
		if (pathInfo != null && pathInfo.equals("/logout")) {

			System.out.println("USER LOGGING OUT: "
					+ request.getSession().getAttribute("current.user.nick"));
			// log current user out of the application
			SessionUtils.logout(request.getSession());

			// redirect to main page
			response.sendRedirect(request.getContextPath() + "/servleti/main");
		} else if (pathInfo == null) {
			request.setAttribute("authors", DAOProvider.getDAO().getAuthors());

			// forward to home page for rendering
			request.getRequestDispatcher("/WEB-INF/pages/index.jsp")
					.forward(request, response);
		} else {
			// render error
		}

	}

	/**
	 * Handles the POST request to the home page, by validating the login form
	 * and logging the user into the blog page if everything is in order.
	 * 
	 * @param  request          HTTP request
	 * @param  response         HTTP response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		LoginForm loginForm = new LoginForm();
		loginForm.fillFromHttpRequest(request);
		System.out.println("ATTEMPTED LOGIN:\n" + loginForm);
		loginForm.validate();

		if (loginForm.isValid()) {
			// static data is valid, database validation is needed
			BlogUser existingUser =
					DAOProvider.getDAO().getBlogUserByNick(loginForm.getNick());
			if (existingUser == null) { // user with provided nick doesnt exist
				loginForm.putError("nick",
						"No user with given nickname exists.");
				System.out.println("Invalid login attempt - non-existing nick");
				request.setAttribute("loginForm", loginForm); // provide login
																// form for
				// rendering purposes
				request.setAttribute("authors",
						DAOProvider.getDAO().getAuthors());
				// forward to JSP for rendering
				request.getRequestDispatcher("/WEB-INF/pages/index.jsp")
						.forward(request, response);
			} else { // user with provided nick exists

				// check password hashes
				BlogUser user = new BlogUser();
				loginForm.fillIntoModel(user);
				if (user.getPasswordHash()
						.equals(existingUser.getPasswordHash())) {
					// correct password - user is logged in
					System.out.println(
							"LOGIN SUCCESS: " + existingUser.getNick());
					SessionUtils.login(request.getSession(), existingUser);
					request.setAttribute("loginForm", loginForm);
					request.setAttribute("authors",
							DAOProvider.getDAO().getAuthors());
					response.sendRedirect(request.getContextPath());
				} else {
					System.out.println(
							"LOGIN FAIL - WRONG PASSWORD FOR "
									+ loginForm.getNick());
					loginForm.putError("password", "Incorrect password");
					request.setAttribute("loginForm", loginForm); // provide
																	// login
																	// form for
					// rendering purposes
					request.setAttribute("authors",
							DAOProvider.getDAO().getAuthors());
					// forward to JSP for rendering
					request.getRequestDispatcher("/WEB-INF/pages/index.jsp")
							.forward(request, response);
				}
			}

		} else {
			System.out.println("LOGIN FAIL - INVALID FORM");
			request.setAttribute("loginForm", loginForm); // provide login form
															// for
			// rendering purposes
			request.setAttribute("authors", DAOProvider.getDAO().getAuthors());
			// forward to JSP for rendering
			request.getRequestDispatcher("/WEB-INF/pages/index.jsp")
					.forward(request, response);
		}


	}

}
