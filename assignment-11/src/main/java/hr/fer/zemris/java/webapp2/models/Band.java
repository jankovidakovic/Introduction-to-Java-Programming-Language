package hr.fer.zemris.java.webapp2.models;

import java.util.Objects;

/**
 * Model of a band which is defined by its ID, name, and most famous song
 * 
 * @author jankovidakovic
 *
 */
public class Band {

	private final int id; // unique ID
	private final String name; // name of the band
	private final String songUrl; // some famous song

	/**
	 * Creates a new band with the given parameters
	 * 
	 * @param id      band id
	 * @param name    band name
	 * @param songUrl band representative song
	 */
	public Band(int id, String name, String songUrl) {
		super();
		this.id = id;
		this.name = name;
		this.songUrl = songUrl;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the songUrl
	 */
	public String getSongUrl() {
		return songUrl;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, songUrl);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Band)) {
			return false;
		}
		Band other = (Band) obj;
		return id == other.id && Objects.equals(name, other.name)
				&& Objects.equals(songUrl, other.songUrl);
	}

}
