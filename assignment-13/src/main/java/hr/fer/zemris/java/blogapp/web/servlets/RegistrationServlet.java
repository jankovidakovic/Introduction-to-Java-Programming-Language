package hr.fer.zemris.java.blogapp.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.blogapp.dao.DAOProvider;
import hr.fer.zemris.java.blogapp.model.BlogUser;
import hr.fer.zemris.java.blogapp.model.form.RegistrationForm;
import hr.fer.zemris.java.blogapp.web.utils.SessionUtils;

/**
 * Servlet that handles the registration process. On recieving a GET request,
 * forwards the request to the JSP that renders the registration page. On POST
 * request, validates the form and creates a new user if everything is in order.
 * 
 * @author jankovidakovic
 *
 */
@WebServlet("/servleti/register")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Handles the GET request to the registration page.
	 * 
	 * @param  request          HTTP request
	 * @param  response         HTTP response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/pages/register.jsp")
				.forward(request, response);
	}

	/**
	 * Handles the POST request to the registration page, which signals that
	 * someone is trying to register to the blog page.
	 * 
	 * @param  request          HTTP request
	 * @param  response         HTTP response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// model of a registration form
		RegistrationForm registrationForm = new RegistrationForm();
		registrationForm.fillFromHttpRequest(request);
		System.out.println("REGISTRATION FORM:\n" + registrationForm);
		registrationForm.validate();

		if (registrationForm.isValid()) { // form data is valid

			// validate against existing users in the database
			BlogUser existingUser = DAOProvider
					.getDAO()
					.getBlogUserByNick(registrationForm.getNick());
			if (existingUser == null) {
				System.out.println("REGISTRATION FORM VALID.");

				// create new user and fill it with form data
				BlogUser user = new BlogUser();
				registrationForm.fillIntoModel(user);

				System.out.println("CREATED NEW USER:\n" + user);
				// store new user into the database
				DAOProvider.getDAO().insertUser(user);

				// store user information into current session
				SessionUtils.login(request.getSession(), user);

				// forward to the home page JSP for rendering
				response.sendRedirect(
						request.getContextPath() + "/servleti/main");
			} else {
				System.out.println(
						"User with given nickname is already registered.");

				registrationForm
						.putError("nick",
								"User with given nickname already exists! Please choose a different nickname.");
				// set form as attribute so that errors can be displayed
				request.setAttribute("registrationForm", registrationForm);
				request.getRequestDispatcher("/WEB-INF/pages/register.jsp")
						.forward(request, response);
			}

		} else {
			System.out.println("Registration form is invalid.");
			request.setAttribute("registrationForm", registrationForm);
			request.getRequestDispatcher("/WEB-INF/pages/register.jsp")
					.forward(request, response);
		}

	}

}
