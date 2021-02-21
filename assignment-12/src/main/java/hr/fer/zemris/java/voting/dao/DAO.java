package hr.fer.zemris.java.voting.dao;

import java.util.List;

import hr.fer.zemris.java.voting.model.Poll;
import hr.fer.zemris.java.voting.model.PollData;
import hr.fer.zemris.java.voting.model.PollOption;

/**
 * Interface which can be used to communicate with the persistence layer
 * 
 * @author jankovidakovic
 *
 */
public interface DAO {
	
	/**
	 * Fetches all the defined polls in the database, and returns them as a
	 * list. Order of the polls is not specified.
	 * 
	 * @return              list of all polls stored in the database
	 * @throws DAOException if anything goes wrong
	 */
	List<Poll> fetchPolls() throws DAOException;

	/**
	 * Retrieves poll from the database by its ID.
	 * 
	 * @param  pollID       id of the requested poll
	 * @return              poll
	 * @throws DAOException if anything goes wrong
	 */
	Poll getPollById(Long pollID) throws DAOException;

	/**
	 * Gets all the available voting options of the poll with given ID.
	 * 
	 * @param  pollID       id of the poll which options are requested
	 * @return              list of poll options
	 * @throws DAOException if anything goes wrong
	 */
	List<PollOption> getPollOptionsById(Long pollID)
			throws DAOException;

	/**
	 * Increments the vote count of the poll option with given ID.
	 * 
	 * @param  id           ID of the poll option which vote is to be
	 *                      incremented
	 * @return              object which represents the poll option after its
	 *                      vote has been successfully incremented
	 * @throws DAOException if anything goes wrong
	 */
	PollOption incrementVote(Long id) throws DAOException;

	/**
	 * Gets the winner of the poll with given ID. Winners are poll options with
	 * most votes out of all options of the given poll.
	 * 
	 * @param  pollID       ID of poll which winners are requested
	 * @return              list of poll options that have the most number of
	 *                      votes
	 * @throws DAOException if anything goes wrong
	 */
	List<PollOption> getPollWinners(Long pollID) throws DAOException;

	/**
	 * Checks whether a table with the given name exists in the database.
	 * 
	 * @param  tableName    name of the table to be checked
	 * @return              <code>true</code> if table exists in the database,
	 *                      <code>false</code> otherwise.
	 * @throws DAOException if anything goes wrong
	 */
	boolean checkTableExistence(String tableName) throws DAOException;

	/**
	 * Creates a table which is able to store polls.
	 * 
	 * @throws DAOException if anything goes wrong
	 */
	void createPollsTable() throws DAOException;

	/**
	 * Creates a table which is able to store poll options.
	 * 
	 * @throws DAOException if anything goes wrong.
	 */
	void createPollOptionsTable() throws DAOException;

	/**
	 * Inserts poll with the given data into the database.
	 * 
	 * @param  pollData     data of the poll to be inserted.
	 * @throws DAOException if anything goes wrong
	 */
	void insertPoll(PollData pollData) throws DAOException;

	/**
	 * Drops table with the given name from the database. Checking whether a
	 * table exists is not guaranteed, so that should be responsibility of a
	 * client that uses this method.
	 * 
	 * @param  tableName    name of the table to be dropped
	 * @throws DAOException if anything goes wrong(e.g. if table doesn't exists)
	 */
	void dropTable(String tableName) throws DAOException;

}