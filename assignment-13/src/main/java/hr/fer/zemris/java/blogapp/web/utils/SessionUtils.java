package hr.fer.zemris.java.blogapp.web.utils;

import javax.servlet.http.HttpSession;

import hr.fer.zemris.java.blogapp.model.BlogUser;

/**
 * Utility class for managing web application sessions
 * 
 * @author jankovidakovic
 *
 */
public class SessionUtils {

	/**
	 * Merges the given blog user into the session, associating the user with
	 * the current session and logging him into the application.
	 * 
	 * @param session  session into which the user will be merged
	 * @param blogUser user to be merged into session
	 */
	public static void login(HttpSession session, BlogUser blogUser) {
		session.setAttribute("current.user.id", blogUser.getId());
		session.setAttribute("current.user.firstName", blogUser.getFirstName());
		session.setAttribute("current.user.lastName", blogUser.getLastName());
		session.setAttribute("current.user.nick", blogUser.getNick());
	}

	public static void logout(HttpSession session) {
		session.invalidate();
	}
}
