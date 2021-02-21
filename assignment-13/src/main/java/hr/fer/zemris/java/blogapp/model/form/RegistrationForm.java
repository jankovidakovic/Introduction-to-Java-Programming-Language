package hr.fer.zemris.java.blogapp.model.form;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.blogapp.model.BlogUser;
import hr.fer.zemris.java.blogapp.security.SecurityManager;

/**
 * Model of a registration form which must be filled by the users that wish to
 * create a new account. IMPORTANT: Validity of the form is determined ONLY BY
 * THE DATA IN THE FORM, which means that any validation which has to do with
 * the persistence layer, such as checking nickname uniqueness, should be done
 * by the client which uses this form to register new users.
 * 
 * @author jankovidakovic
 *
 */
public class RegistrationForm {

	private String firstName; // first name of the user
	private String lastName; // last name of the user
	private String email; // email
	private String nick; // nick used for loggin in
	private String passwordHash; // password

	private String getProcessedEntry(String entry) {
		if (entry == null) {
			return "";
		} else {
			return entry.trim();
		}
	}

	// errors map, keys are attribute names, and values are errors linked to the
	// specific attributes.
	Map<String, String> errors = new HashMap<String, String>();

	/**
	 * Fetches the error tied with the given entry.
	 * 
	 * @param  entry entry for which the error is requested
	 * @return       error message, if such exists, <code>null</code> otherwise.
	 */
	public String getError(String entry) {
		return errors.get(entry);
	}

	/**
	 * Checks whether the registration form in its current state stores valid
	 * information for registering a new user.
	 * 
	 * @return <code>true</code> if the form data is valid, </code>false</code>
	 *         otherwise.
	 */
	public boolean isValid() {
		return errors.isEmpty();
	}

	/**
	 * Cheks whether form contains an error for the given form entry.
	 * 
	 * @param  entry entry for which the error is requested
	 * @return       </code>true</code> if the form contains an error for the
	 *               given entry </code>false</code> otherwise.
	 */
	public boolean hasError(String entry) {
		return errors.containsKey(entry);
	}

	/**
	 * Inserts a given error for the given entry into the form. This method is
	 * intended to be used when the client needs to do additional validation
	 * before rendering the form result, such as some database validation.
	 * 
	 * @param entry entry to put an error for
	 * @param error error to put
	 */
	public void putError(String entry, String error) {
		errors.put(entry, error);
	}

	/**
	 * Fills the form from the parameters of the given HTTP request.
	 * 
	 * @param req HTTP request which contains in its parameters the form data.
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		this.firstName = getProcessedEntry(req.getParameter("firstName"));
		this.lastName = getProcessedEntry(req.getParameter("lastName"));
		this.email = getProcessedEntry(req.getParameter("email"));
		this.nick = getProcessedEntry(req.getParameter("nick"));
		if (getProcessedEntry(req.getParameter("password")).isEmpty()) {
			errors.put("password", "Password cannot be empty!");
			this.passwordHash = "";
		} else {
			this.passwordHash = SecurityManager.getPasswordHash(
					getProcessedEntry(req.getParameter("password")), "SHA-1");
		}

	}

	/**
	 * Fils the form from given model of a user.
	 * 
	 * @param user user which data is to be copied into the form.
	 */
	public void fillFromModel(BlogUser user) {
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.nick = user.getNick();
		this.passwordHash = user.getPasswordHash();
	}

	/**
	 * Fills the form data into the provided model. This method should be used
	 * only when the form data is valid, otherwise the operation will not be
	 * permitted.
	 * 
	 * @param  user                  user to fill in with the form data
	 * @throws IllegalStateException if the form is not valid
	 */
	public void fillIntoModel(BlogUser user) {
		if (!isValid()) {
			throw new IllegalStateException("Form data is not valid!");
		}
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setNick(nick);
		user.setPasswordHash(passwordHash);
	}

	/**
	 * Validates the form data, only checking the data as it is, and not
	 * validating it in any context related to the persistence layer or any such
	 * thing.
	 */
	public void validate() {
		errors.clear();
		if (firstName.isEmpty()) {
			errors.put("firstName", "First name is required!");
		}
		if (lastName.isEmpty()) {
			errors.put("lastName", "Last name is required!");
		}

		if (email.isEmpty()) {
			errors.put("email", "E-mail is required!");
		}
		if (email.length() < 3 || email.indexOf('@') == -1
				|| email.indexOf('@') == 0
				|| email.indexOf('@') == email.length() - 1) {
			errors.put("email", "E-mail is not properly formatted!");
		}

		if (passwordHash.isEmpty()) {
			errors.put("password", "Password cannot be empty!");
		}

	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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

}
