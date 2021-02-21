package hr.fer.zemris.java.blogapp.web.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.blogapp.dao.DAOProvider;
import hr.fer.zemris.java.blogapp.model.BlogComment;
import hr.fer.zemris.java.blogapp.model.BlogEntry;
import hr.fer.zemris.java.blogapp.model.BlogUser;
import hr.fer.zemris.java.blogapp.model.form.BlogEntryForm;

/**
 * Servlet that handles everything about blog authors, from displaying their
 * home page, to editing and posting new blog entries, to adding blog comments,
 * and etc.
 * 
 * @author jankovidakovic
 *
 */
@WebServlet("/servleti/author/*")
public class AuthorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Handles the GET request to the author pages. All users can request an
	 * author's home page, which displays all the author's blog entries. If the
	 * author is the one requesting the home page, he can add a new blog entry.
	 * On blog entry viewing, blog entry's author can edit the blog entry, and
	 * all users can post blog comments.
	 * 
	 * @param  request          HTTP request
	 * @param  response         HTTP response
	 * @throws ServletException
	 * @throws IOException
	 * 
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// get information about the requested path
		String pathInfo = request.getPathInfo();
		System.out
				.println("GET REQUEST: " + request.getContextPath() + pathInfo);
		if (pathInfo == null) {
			// render error
		} else if (getCharacterCount(pathInfo, '/') == 1
				&& pathInfo.startsWith("/")) {
			// some author's home page is requested

			// extract requested author's nick
			String authorNick = pathInfo.substring(1);
			if (authorNick == null) {
				System.out.println("NULL AUTHOR");
				request.setAttribute("error",
						"Invalide URL - please specify author");
				request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
						.forward(request, response);
			}
			// fetch author from database
			BlogUser authorUser =
					DAOProvider.getDAO().getBlogUserByNick(authorNick);

			if (authorUser == null) {
				System.out
						.println("REQUEST FOR UNEXISTING AUTHOR: " + pathInfo);
				request.setAttribute("error",
						"Invalid URL - author does not exist.");
				request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
						.forward(request, response);
			}

			// TODO - why is this line needed???? -> provides password hash in
			// request - unsafe
			request.setAttribute("author.nick", authorNick);

			// fetch author's blog entries
			// List<BlogEntry> blogEntries =
			// DAOProvider.getDAO().getBlogEntries(authorUser);
			// can't we do it in author object?
			request.setAttribute("blogEntries", authorUser.getBlogEntries());
			request.getRequestDispatcher("/WEB-INF/pages/author.jsp")
					.forward(request, response);
		} else if (getCharacterCount(pathInfo, '/') == 2
				&& pathInfo.startsWith("/")) {
			if (pathInfo.endsWith("/new")) {
				// requesting to add a new blog entry

				// extract author nickname from URL path
				String authorNick = pathInfo.substring(1,
						pathInfo.length() - "/new".length());

				if (authorNick == null) {
					System.out.println("NULL AUTHOR");
					request.setAttribute("error",
							"Invalid URL - please specify a non-empty author");
					request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
							.forward(request, response);
				}

				// get author user from the database
				BlogUser authorUser =
						DAOProvider.getDAO().getBlogUserByNick(authorNick);

				if (authorUser == null) {
					System.out.println(
							"REQUEST TO ADD BLOG ENTRY FOR UNEXISTING AUTHOR: "
									+ authorNick);
					request.setAttribute("error",
							"Invalid URL - non-existing author.");
					request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
							.forward(request, response);
				}
				// request.setAttribute("author", authorUser);
				request.setAttribute("author.nick", authorNick);

				request.getRequestDispatcher(
						"/WEB-INF/pages/updateBlogEntry.jsp")
						.forward(request, response);

			} else if (pathInfo.endsWith("/edit")) {
				// request for editing an existing blog id

				Long blogEntryID = null;
				try {
					blogEntryID = Long.parseLong(request.getParameter("id"));
				} catch (NumberFormatException e) {
					// render error
					System.out.println("EDIT REQUEST FOR BAD ID");
					request.setAttribute("error",
							"Invalid URL - no blog entry with given ID.");
					request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
							.forward(request, response);
				}

				// fetch blog entry from the database
				BlogEntry blogEntry =
						DAOProvider.getDAO().getBlogEntry(blogEntryID);

				// create and fill blog entry form
				BlogEntryForm blogEntryForm = new BlogEntryForm();
				blogEntryForm.fillFromModel(blogEntry);
				request.setAttribute("blogEntryForm", blogEntryForm);
				request.setAttribute("blogEntry", blogEntry);

				// extract author nick
				String authorNick = pathInfo.substring(1,
						pathInfo.length() - "/edit".length());
				BlogUser authorUser =
						DAOProvider.getDAO().getBlogUserByNick(authorNick);

				if (authorUser == null) {
					System.out
							.println("REQUESTING EDIT FOR NON-EXISTING AUTHOR: "
									+ authorNick);
					request.setAttribute("error",
							"Invalid URL - non-existing author.");
					request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
							.forward(request, response);
				}
				request.setAttribute("author.nick", authorNick);
				request.getRequestDispatcher(
						"/WEB-INF/pages/updateBlogEntry.jsp")
						.forward(request, response);

			} else { // request to see blog entry of some author

				// get blog entry ID from the URL
				Long id = null;
				try {
					id = Long.parseLong(pathInfo.split("/")[2]);
				} catch (NumberFormatException e) {
					System.out.println("BAD BLOG ENTRY ID");
					request.setAttribute("error",
							"Invalid URL - invalid blog entry ID.");
					request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
							.forward(request, response);
				}

				// fetch blog entry from the database
				BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
				if (blogEntry == null) {
					System.out.println("NO BLOG ENTRY WITH ID: " + id);
					request.setAttribute("error",
							"Invalid URL - blog entry with given ID does not exist.");
					request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
							.forward(request, response);
				}
				request.setAttribute("blogEntry", blogEntry);
				// List<BlogComment> blogComments =
				// DAOProvider.getDAO().getBlogComments(blogEntry);

				// fetch blog entry comments
				request.setAttribute("blogComments", blogEntry.getComments());

				// extract author nickanem from the URL
				String authorNick = pathInfo.substring(1, pathInfo.length()
						- "/".length() - id.toString().length());

				if (authorNick == null) {
					System.out.println("NULL AUTHOR");
					request.setAttribute("error",
							"Invalid URL - please specify a non-empty author.");
					request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
							.forward(request, response);
				}
				// fetch author from the database
				BlogUser authorUser =
						DAOProvider.getDAO().getBlogUserByNick(authorNick);

				if (authorUser == null) {
					System.out.println(
							"REQUESTING BLOG ENTRY FOR UNEXISTING AUTHOR: "
									+ authorNick);
					request.setAttribute("error",
							"Invalid URL - non-existing author.");
					request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
							.forward(request, response);
				}
				request.setAttribute("author.nick", authorNick);

				// finally - render blog entry
				request.getRequestDispatcher("/WEB-INF/pages/blogEntry.jsp")
						.forward(request, response);
			}
		} else {
			System.out.println(
					"WRONG URL: " + request.getContextPath() + pathInfo);
			request.setAttribute("error", "Unknown URL requested.");
			request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
					.forward(request, response);
		}
	}

	/**
	 * Handles the POST requests to the author's part of the web application.
	 * Creates new comments, creates and edits blog entries.
	 * 
	 * @param  request          HTTP request
	 * @param  response         HTTP response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// get info about the requested path
		String pathInfo = request.getPathInfo();
		if (pathInfo == null) {
			System.out.println(
					"BAD POST REQUEST: " + request.getContextPath() + pathInfo);
			request.setAttribute("error", "Unknown POST request.");
			request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
					.forward(request, response);
		} else if (getCharacterCount(pathInfo, '/') == 1
				&& pathInfo.startsWith("/")) {
			System.out.println(
					"BAD POST REQUEST: " + request.getContextPath() + pathInfo);
			request.setAttribute("error", "Unknown POST request.");
			request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
					.forward(request, response);

		} else if (getCharacterCount(pathInfo, '/') == 2
				&& pathInfo.startsWith("/")) {
			if (pathInfo.endsWith("/new")) {
				System.out.println("POST REQUEST FOR NEW: "
						+ request.getContextPath() + pathInfo);

				// check if user has authorization
				String authorNick = pathInfo.substring("/"
						.length(),
						pathInfo.length() - "/new".length());

				if (authorNick == null) {
					System.out.println("BAD POST REQUEST: NULL AUTHOR");
					request.setAttribute("error",
							"Invalid URL - please specify a non-empty author.");
					request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
							.forward(request, response);
				}

				// fetch author from the database
				BlogUser authorUser =
						DAOProvider.getDAO().getBlogUserByNick(authorNick);
				if (authorUser == null) {
					System.out.println("BAD POST REQUEST: NON EXISTING AUTHOR: "
							+ authorNick);
					request.setAttribute("error",
							"Invalid URL - non-existing author.");
					request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
							.forward(request, response);
				}

				if (request.getSession().getAttribute("current.user.id")
						== null) {
					request.setAttribute("error",
							"Only logged in users can add blog entries to "
									+ "their blog page. Please log in first.");
					request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
							.forward(request, response);
					// render 401 unauthorized
				} else if (((String) request.getSession()
						.getAttribute("current.user.nick")).equals(
								authorNick)
						== false) {
					System.out.println("UNAUTHORIZED ACCESS FOR USER: "
							+ (String) request.getSession()
									.getAttribute("current.user.nick"));
					request.setAttribute("error",
							"You're not authorized to add a blog entry for the requested author.");
					request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
							.forward(request, response);
				}

				// create and fill blog entry form
				BlogEntryForm blogEntryForm = new BlogEntryForm();
				blogEntryForm.fillFromHttpRequest(request);
				System.out.println("BLOG ENTRY FORM:\n" + blogEntryForm);
				blogEntryForm.validate();

				if (blogEntryForm.isValid()) {

					System.out.println("FORM VALIDATION SUCCESFULL");
					// create new blog entry
					BlogEntry blogEntry = new BlogEntry();
					blogEntryForm.fillIntoModel(blogEntry);
					blogEntry.setCreator(authorUser);

					DAOProvider.getDAO().insertBlogEntry(blogEntry);

					// redirect to author blog page
					response.sendRedirect(request.getContextPath()
							+ "/servleti/author/" + authorNick);
				} else {
					System.out.println("BLOG VALIDATION UNSUCCESSFUL");
					request.setAttribute("blogEntryForm", blogEntryForm);
					request.setAttribute("author.nick", authorNick);

					BlogEntry blogEntry = new BlogEntry();
					blogEntry.setText(blogEntryForm.getText());
					blogEntry.setTitle(blogEntryForm.getTitle());
					blogEntry.setCreator(authorUser);

					request.setAttribute("blogEntry", blogEntry);
					request.getRequestDispatcher(
							"/WEB-INF/pages/updateBlogEntry.jsp")
							.forward(request, response);
				}

			} else if (pathInfo.endsWith("/edit")) {

				// get author nick from URL
				String authorNick = pathInfo.substring(
						1,
						pathInfo.length() - "/edit".length());

				if (authorNick == null) {
					System.out.println("NULL AUTHOR EDIT REQUEST");
					request.setAttribute("error",
							"Invalid URL - please provide a non-empty author.");
					request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
							.forward(request, response);
				}

				// fetch author from the database
				BlogUser authorUser =
						DAOProvider.getDAO().getBlogUserByNick(authorNick);

				if (authorUser == null) {
					System.out.println("NON EXISTING AUTHOR EDIT REQUEST");
					request.setAttribute("error",
							"Invalid URL - non-existing author.");
					request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
							.forward(request, response);
				}
				request.setAttribute("author.nick", authorNick);

				// create blog entry form and fill it from http request
				BlogEntryForm blogEntryForm = new BlogEntryForm();
				blogEntryForm.fillFromHttpRequest(request);
				System.out.println("BLOG ENTRY FORM:\n" + blogEntryForm);
				blogEntryForm.validate();

				if (blogEntryForm.isValid()) {

					// blog entry form data is valid, check against database
					Long id = null;
					try {
						id = Long.parseLong(request.getParameter("id"));
					} catch (NumberFormatException e) {
						System.out.println("BAD BLOG ENTRY ID");
						request.setAttribute("error",
								"Invalid URL - invalid blog entry ID format.");
						request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
								.forward(request, response);
					}

					// fetch blog entry
					BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
					if (blogEntry == null) {
						System.out.println("NON EXISTING BLOG ENTRY");
						request.setAttribute("error",
								"Invalid URL - blog entry with given ID does not exist.");
						request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
								.forward(request, response);
					}
					blogEntryForm.fillIntoModel(blogEntry);
					DAOProvider.getDAO().insertBlogEntry(blogEntry);
					response.sendRedirect(request.getContextPath()
							+ "/servleti/author/" + pathInfo.substring("/"
									.length(),
									pathInfo.length() - "/edit".length()));
				} else {
					request.setAttribute("blogEntryForm", blogEntryForm);
					request.getRequestDispatcher(
							"/WEB-INF/pages/updateBlogEntry.jsp")
							.forward(request, response);
				}
			} else { // posting a comment
				String comment = request.getParameter("comment");
				if (comment == null) {
					System.out.println("NULL COMMENT");
					request.setAttribute("error",
							"You cannot post an empty comment.");
					request.getRequestDispatcher(
							"/WEB-INF/pages/blogEntry.jsp")
							.forward(request, response);
				}

				if (comment.isEmpty()) {
					System.out.println("EMPTY COMMENT");
					request.setAttribute("error",
							"Blog comment cannot be empty!");
					request.getRequestDispatcher("/WEB-INF/pages/blogEntry.jsp")
							.forward(request, response);
				} else {

					// check if user is logged in
					if (request.getSession().getAttribute("current.user.id")
							== null) {
						System.out.println("ANON USERS CANNOT COMMENT");
						request.setAttribute("error",
								"Only logged in users can comment. Please log in first.");
						request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
								.forward(request, response);
					}
					BlogComment blogComment = new BlogComment();
					blogComment.setMessage(comment);
					blogComment
							.setPostedOn(new Date(System.currentTimeMillis()));
					blogComment.setUsersNick((String) request.getSession()
							.getAttribute("current.user.nick"));

					Long id = null;
					try {
						id = Long.parseLong(pathInfo.split("/")[2]);
					} catch (NumberFormatException e) {
						System.out.println("BAD ID");
						request.setAttribute("error",
								"Invalid URL - unknown ID format.");
						request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
								.forward(request, response);
					}

					BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
					if (blogEntry == null) {
						System.out.println("NO BLOG ENTRY FOR GIVEN ID");
						request.setAttribute("error",
								"Invalid URL - blog entry with given ID does not exist.");
						request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
								.forward(request, response);
					}

					blogComment.setBlogEntry(blogEntry);
					DAOProvider.getDAO().insertBlogComment(blogComment);

					request.setAttribute("blogEntry", blogEntry);
					request.setAttribute("blogComments",
							DAOProvider.getDAO().getBlogComments(blogEntry));
					String authorNick = pathInfo.substring(1,
							pathInfo.length() - 1 - id.toString().length());
					if (authorNick == null) {
						System.out.println("NULL AUTHOR");
						request.setAttribute("error",
								"Invalid URL - please specify a non-empty author.");
						request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
								.forward(request, response);
					}
					BlogUser authorUser =
							DAOProvider.getDAO().getBlogUserByNick(authorNick);
					if (authorUser == null) {
						System.out
								.println("NO AUTHOR WITH NICK: " + authorNick);
						request.setAttribute("error",
								"Invalid URL - non-existing author.");
						request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
								.forward(request, response);
					}
					request.setAttribute("author.nick", authorNick);
					request.getRequestDispatcher("/WEB-INF/pages/blogEntry.jsp")
							.forward(request, response);
				}

			}
		} else {
			System.out.println("UNKNOWN POST REQUEST");
			request.setAttribute("error", "Unknown POST request.");
			request.getRequestDispatcher("/WEB-INF/pages/error.jsp")
					.forward(request, response);
		}
	}

	/**
	 * Counts the occurences of the given character in the given string.
	 * 
	 * @param  string    String which is to be searched for characters
	 * @param  character character to be searched within given string
	 * @return           number of times that the given character appears in the
	 *                   given string.
	 */
	private long getCharacterCount(String string, Character character) {
		return string.chars().filter(c -> c == character).count();
	}

}
