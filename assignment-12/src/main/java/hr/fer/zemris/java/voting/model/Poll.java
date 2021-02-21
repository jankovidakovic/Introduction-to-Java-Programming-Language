package hr.fer.zemris.java.voting.model;

/**
 * Model of a poll, which has its id, title, and message.
 * 
 * @author jankovidakovic
 *
 */
public class Poll {

	private Long id; // id of the poll
	private String title; // title of the poll
	private String message; // message

	/**
	 * Creates a blank poll
	 */
	public Poll() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
