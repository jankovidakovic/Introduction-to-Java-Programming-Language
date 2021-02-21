package hr.fer.zemris.java.blogapp.model.form;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

import hr.fer.zemris.java.blogapp.model.BlogUser;
import hr.fer.zemris.java.blogapp.security.SecurityManager;

/**
 * Model of a form which takes for input user credentials for logging into the
 * blog application
 * 
 * @author jankovidakovic
 *
 */
public class LoginForm {

	private String nick; // unique nickname
	private String passwordHash; // password hash

	// errors map, keys are attribute names, and values are errors linked to the
	// specific attributes.
	Map<String, String> errors = new HashMap<String, String>();

	public String getError(String entry) {
		return errors.get(entry);
	}

	public void putError(String entry, String error) {
		errors.put(entry, error);
	}

	public boolean isValid() {
		return errors.isEmpty();
	}

	public boolean hasError(String entry) {
		return errors.containsKey(entry);
	}

	public void fillFromHttpRequest(HttpServletRequest req) {
		this.nick = getProcessedEntry(req.getParameter("nick"));
		this.passwordHash = SecurityManager.getPasswordHash(
				getProcessedEntry(req.getParameter("password")), "SHA-1");
	}

	public void fillFromModel(BlogUser user) {
		this.nick = user.getNick();
		this.passwordHash = user.getPasswordHash(); // TODO - password or hash?
	}

	/**
	 * Fills the data about the user into the provided model. Expects the form
	 * data to be valid.
	 * 
	 * @param  user                  Model of a blog user which will be filled
	 *                               with user data
	 * @throws IllegalStateException if the method was called on an invalid form
	 */
	public void fillIntoModel(BlogUser user) {
		if (!isValid()) {
			throw new IllegalStateException("Form data is not valid!");
		}
		user.setNick(nick);
		user.setPasswordHash(passwordHash);

	}

	public void validate() {
		errors.clear();
		if (nick.isEmpty()) {
			errors.put("nick", "Nickname is required!");
			System.out.println("no nickname");
		}
		if (passwordHash.isEmpty()) {
			errors.put("password", "Password is required!");
			System.out.println("no password");
		}

		return;

	}

	/**
	 * Processes a form entry to obtain its <code>String</code> representation.
	 * 
	 * @param  entry form entry to process
	 * @return       <code>String</code> representation of the form entry
	 */
	private String getProcessedEntry(String entry) {
		if (entry == null) { // empty
			return "";
		} else {
			return entry.trim(); // trim excess spaces
		}
	}

	/**
	 * @return the nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return the passwordHash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * @param passwordHash the passwordHash to set
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
