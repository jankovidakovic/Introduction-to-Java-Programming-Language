package hr.fer.zemris.java.blogapp.model.form;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.blogapp.model.BlogEntry;

/**
 * Form representing a blog entry. It can be a new entry that is being inserted,
 * or an existing one that is being edited.
 * 
 * @author jankovidakovic
 *
 */
public class BlogEntryForm {

	private String title; // title of the blog entry
	private String text; // text of the blog entry

	// map of errors
	Map<String, String> errors = new HashMap<String, String>();

	/**
	 * Fetches the error for the given form element
	 * 
	 * @param  entry element of the form which error is requested
	 * @return       error associated with the given element, or
	 *               <code>null</code> if form element contains no error.
	 */
	public String getError(String entry) {
		return errors.get(entry);
	}

	/**
	 * Checks whether the form data is valid (form doesn't contain any errors.)
	 * 
	 * @return <code>true</code> if the form data is valid, <code>false</code>
	 *         otherwise.
	 */
	public boolean isValid() {
		return errors.isEmpty();
	}

	/**
	 * Chechs whether the given form element is properly formatted, or contains
	 * an error.
	 * 
	 * @param  entry form element for which the validity is being checked
	 * @return       <code>true</code> if the given form element is valid,
	 *               </code>false</code> if it is not.
	 */
	public boolean hasError(String entry) {
		return errors.containsKey(entry);
	}

	/**
	 * Fills the form data from the given HTTP request parameters
	 * 
	 * @param req HTTP request which contains form data in its parameters.
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		this.title = getProcessedEntry(req.getParameter("title"));
		this.text = getProcessedEntry(req.getParameter("text"));

	}

	/**
	 * Fills the form data from the given model of a blog entry. Should be used
	 * when some blog entry is being edited, and therefore its data needs to be
	 * fetched from the database and filled in the form before rendering.
	 * 
	 * @param blogEntry blog entry which data is used to fill the form.
	 */
	public void fillFromModel(BlogEntry blogEntry) {
		this.title = blogEntry.getTitle();
		this.text = blogEntry.getText();

	}

	/**
	 * Fills the form data INTO the given blog entry model. If the form data was
	 * not valid when this method was called, it will cause an exception.
	 * 
	 * @param  blogEntry             blog entry to fill with the form data
	 * @throws IllegalStateException if the form data is not valid.
	 */
	public void fillIntoModel(BlogEntry blogEntry) {
		if (!isValid()) {
			throw new IllegalStateException("Form data is not valid!");
		}
		blogEntry.setTitle(title);
		blogEntry.setText(text);
		blogEntry.setLastModifiedAt(new Date(System.currentTimeMillis()));
		if (blogEntry.getCreatedAt() == null) {
			blogEntry.setCreatedAt(new Date(System.currentTimeMillis()));
		}
	}

	/**
	 * Validates the form data.
	 */
	public void validate() {
		errors.clear();
		if (title.isEmpty()) {
			errors.put("title", "Title cannot be empty!");
		}
		if (text.isEmpty()) {
			errors.put("text", "Text cannot be empty!");
		}
	}

	/**
	 * Prepares the HTTP request parameter so that it can be properly inserted
	 * into the form
	 * 
	 * @param  entry HTTP request parameter to be trimmed and prepared for form
	 *               insertion
	 * @return       formatted HTTP request parameter representing some form
	 *               entry.
	 */
	private String getProcessedEntry(String entry) {
		if (entry == null) {
			return "";
		} else {
			return entry.trim();
		}
	}

	@Override
	public String toString() {
		return "{\n\ttitle: " + title + ",\n\ttext: " + text + "\n}";
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
