package hr.fer.zemris.java.webapp2.models;

/**
 * Model of a voting result, which contains information about the band and the
 * number of votes they got.
 * 
 * @author jankovidakovic
 *
 */
public class BandResult implements Comparable<BandResult> {

	private final Band band; // band
	private final Integer votes; // votes

	/**
	 * Creates a new band result that means the given band has earned the given
	 * number of votes.
	 * 
	 * @param band  band in question
	 * @param votes votes that they acquired
	 */
	public BandResult(Band band, Integer votes) {
		this.band = band;
		this.votes = votes;
	}

	/**
	 * @return the band
	 */
	public Band getBand() {
		return band;
	}

	/**
	 * @return the votes
	 */
	public Integer getVotes() {
		return votes;
	}

	@Override
	public int compareTo(BandResult o) {
		return votes.compareTo(o.getVotes());
	}

}
