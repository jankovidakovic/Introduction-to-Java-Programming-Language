package hr.fer.zemris.java.voting;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.voting.model.Poll;
import hr.fer.zemris.java.voting.model.PollData;
import hr.fer.zemris.java.voting.model.PollOption;

/**
 * Used to create and retrieve default poll data, which is used to start up the
 * blank web application.
 * 
 * @author jankovidakovic
 *
 */
public class PollDataFactory {

	/**
	 * Returns the data used to create the favorite band poll.
	 * 
	 * @return data for band poll
	 */
	public static PollData newBandPoll() {

		Poll poll = new Poll();
		poll.setTitle("Favorite band voting");
		poll.setMessage(
				"From the following bands, " + "which one is your favorite? "
						+ "Click on the link to vote!");

		List<PollOption> options = new ArrayList<>();

		options.add(new PollOption("The Beatles",
				"https://www.youtube.com/watch?v=z9ypq6_5bsg"));
		options.add(new PollOption("The Platters",
				"https://www.youtube.com/watch?v=H2di83WAOhU"));
		options.add(new PollOption("The Beach Boys",
				"https://www.youtube.com/watch?v=2s4slliAtQU"));
		options.add(new PollOption("The Four Seasons",
				"https://www.youtube.com/watch?v=y8yvnqHmFds"));
		options.add(new PollOption("The Marcels",
				"https://www.youtube.com/watch?v=qoi3TH59ZEs"));
		options.add(new PollOption("The Mamas And The Papas",
				"https://www.youtube.com/watch?v=N-aK6JnyFmk"));
		options.add(new PollOption("The Everly Brothers",
				"https://www.youtube.com/watch?v=tbU3zdAgiX8"));

		return new PollData(poll, options);
	}

	/**
	 * Returns the data which is used to create favorite movie poll.
	 * 
	 * @return favorite movie poll data
	 */
	public static PollData newMoviePoll() {

		Poll poll = new Poll();
		poll.setTitle("Favorite movie voting");
		poll.setMessage(
				"From the following movies, " + "which one is your favorite? "
						+ "Click on the link to vote!");

		List<PollOption> options = new ArrayList<>();

		options.add(new PollOption("Pulp Fiction",
				"https://www.youtube.com/watch?v=s7EdQ4FqbhY"));
		options.add(new PollOption("The Shawshank Redemption",
				"https://www.youtube.com/watch?v=6hB3S9bIaco"));
		options.add(new PollOption("The Prestige",
				"https://www.youtube.com/watch?v=ijXruSzfGEc"));
		options.add(new PollOption("The Godfather",
				"https://www.youtube.com/watch?v=sY1S34973zA"));
		options.add(new PollOption("Fight Club",
				"https://www.youtube.com/watch?v=qtRKdVHc-cE"));

		return new PollData(poll, options);
	}
}
