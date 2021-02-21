package hr.fer.zemris.java.voting.model;

/**
 * Model of a poll option which can be voted for in some poll. Consists of its
 * ID (used as a primary key in database), title, link, identifier of a poll
 * which it belongs to, and count of votes.
 * 
 * @author jankovidakovic
 *
 */
public class PollOption {

	private Long id; // primary key in the database
	private String optionTitle; // title of the option
	private String optionLink; // link that represents the option
	private Long pollID; // id of the poll that the option belongs to
	private Long votesCount; // number of times that someone has voted for the
								// option

	/**
	 * Default constructor, creates a completely empty poll option.
	 */
	public PollOption() {

	}

	/**
	 * Creates a new poll option. Only title and link is provided, other
	 * arguments are set when the poll option is stored in the database.
	 * 
	 * @param optionTitle title of the poll options
	 * @param optionLink  link to some web page that represents the poll option.
	 */
	public PollOption(String optionTitle, String optionLink) {
		this.optionTitle = optionTitle;
		this.optionLink = optionLink;
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
	 * @return the optionTitle
	 */
	public String getOptionTitle() {
		return optionTitle;
	}

	/**
	 * @param optionTitle the optionTitle to set
	 */
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	/**
	 * @return the optionLink
	 */
	public String getOptionLink() {
		return optionLink;
	}

	/**
	 * @param optionLink the optionLink to set
	 */
	public void setOptionLink(String optionLink) {
		this.optionLink = optionLink;
	}

	/**
	 * @return the pollID
	 */
	public Long getPollID() {
		return pollID;
	}

	/**
	 * @param pollID the pollID to set
	 */
	public void setPollID(Long pollID) {
		this.pollID = pollID;
	}

	/**
	 * @return the votesCount
	 */
	public Long getVotesCount() {
		return votesCount;
	}

	/**
	 * @param votesCount the votesCount to set
	 */
	public void setVotesCount(Long votesCount) {
		this.votesCount = votesCount;
	}


}
