package hr.fer.zemris.java.voting.model;

import java.util.List;

/**
 * Poll data which stores poll and its options under one object.
 * 
 * @author jankovidakovic
 *
 */
public class PollData {

	private Poll poll; // poll
	private List<PollOption> options; // poll options

	/**
	 * Constructs a poll data.
	 * 
	 * @param poll
	 * @param options
	 */
	public PollData(Poll poll, List<PollOption> options) {
		this.poll = poll;
		this.options = options;
	}

	/**
	 * @return the poll
	 */
	public Poll getPoll() {
		return poll;
	}

	/**
	 * @param poll the poll to set
	 */
	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	/**
	 * @return the options
	 */
	public List<PollOption> getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(List<PollOption> options) {
		this.options = options;
	}

}
