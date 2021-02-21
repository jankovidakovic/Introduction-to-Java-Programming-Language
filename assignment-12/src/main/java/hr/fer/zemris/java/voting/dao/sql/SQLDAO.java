package hr.fer.zemris.java.voting.dao.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.voting.dao.DAO;
import hr.fer.zemris.java.voting.dao.DAOException;
import hr.fer.zemris.java.voting.model.Poll;
import hr.fer.zemris.java.voting.model.PollData;
import hr.fer.zemris.java.voting.model.PollOption;


/**
 * Concrete implementation of an interface to the persistence layer, which uses
 * SQL and relational database to store data.
 * 
 * @author jankovidakovic
 *
 */
public class SQLDAO implements DAO {

	@Override
	public List<Poll> fetchPolls() throws DAOException {

		List<Poll> polls = new ArrayList<Poll>();
		Connection con = SQLConnectionProvider.getConnection();

		try (PreparedStatement pst =
				con.prepareStatement("SELECT * FROM Polls ORDER BY id")) {

			try (ResultSet rs = pst.executeQuery()) {
				while (rs != null && rs.next()) {
					Poll poll = new Poll();
					poll.setId(rs.getLong("id"));
					poll.setTitle(rs.getString("title"));
					poll.setMessage(rs.getString("message"));
					polls.add(poll);
				}
			}
		} catch (Exception e) {
			throw new DAOException("Error in getting polls", e);
		}
		return polls;
	}

	@Override
	public Poll getPollById(Long pollID) throws DAOException {

		Connection con = SQLConnectionProvider.getConnection();

		try (PreparedStatement pst =
				con.prepareStatement("SELECT * FROM Polls WHERE id=?")) {
			pst.setLong(1, pollID);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs != null && rs.next()) {
					Poll poll = new Poll();
					poll.setId(rs.getLong("id"));
					poll.setTitle(rs.getString("title"));
					poll.setMessage(rs.getString("message"));
					return poll;
				}
			}
		} catch (Exception e) {
			throw new DAOException("Error in getting poll", e);
		}
		return null;
	}

	@Override
	public List<PollOption> getPollOptionsById(
			Long pollID)
			throws DAOException {
		List<PollOption> options = new ArrayList<PollOption>();
		Connection con = SQLConnectionProvider.getConnection();

		try (PreparedStatement pst = con.prepareStatement(
				"SELECT * FROM PollOptions WHERE pollID = ?")) {
			pst.setLong(1, pollID);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs != null && rs.next()) {
					PollOption option = new PollOption();
					option.setId(rs.getLong("id"));
					option.setOptionTitle(rs.getString("optionTitle"));
					option.setOptionLink(rs.getString("optionLink"));
					option.setPollID(rs.getLong("pollID"));
					option.setVotesCount(rs.getLong("votesCount"));
					options.add(option);
				}
			}
		} catch (Exception e) {
			throw new DAOException("Error in getting poll options.", e);
		}

		return options;
	}

	@Override
	public PollOption incrementVote(Long id) throws DAOException {

		Connection con = SQLConnectionProvider.getConnection();

		try (PreparedStatement pst = con.prepareStatement(
				"UPDATE PollOptions " + "SET votesCount = votesCount + 1 "
						+ "WHERE id = ?")) {
			pst.setLong(1, id);
			pst.executeUpdate();

			try (PreparedStatement getUpdatedTuple = con.prepareStatement(
					"SELECT * FROM PollOptions WHERE id = ?")) {
				getUpdatedTuple.setLong(1, id);
				try (ResultSet rs = getUpdatedTuple.executeQuery()) {
					if (rs != null && rs.next()) {
						PollOption option = new PollOption();
						option.setId(rs.getLong("id"));
						option.setOptionTitle(rs.getString("optionTitle"));
						option.setOptionLink(rs.getString("optionLink"));
						option.setPollID(rs.getLong("pollID"));
						option.setVotesCount(rs.getLong("votesCount"));
						return option;
					}
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			throw new DAOException("Error in getting poll options.", e);
		}
		return null;
	}

	@Override
	public List<PollOption> getPollWinners(Long pollID) throws DAOException {

		List<PollOption> winners = new ArrayList<PollOption>();

		Connection con = SQLConnectionProvider.getConnection();

		try (PreparedStatement pst = con.prepareStatement(
				"SELECT * FROM PollOptions " + "WHERE pollID = ? "
						+ "AND votesCount >= ALL ("
						+ "SELECT votesCount "
						+ "FROM PollOptions AS InnerPoll "
						+ "WHERE InnerPoll.pollID = ?"
						+ ")")) {
			pst.setLong(1, pollID);
			pst.setLong(2, pollID);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs != null && rs.next()) {
					PollOption option = new PollOption();
					option.setId(rs.getLong("id"));
					option.setOptionTitle(rs.getString("optionTitle"));
					option.setOptionLink(rs.getString("optionLink"));
					option.setPollID(rs.getLong("pollID"));
					option.setVotesCount(rs.getLong("votesCount"));
					winners.add(option);
				}
			}
		} catch (Exception e) {
			throw new DAOException("Error in getting poll winners", e);
		}
		return winners;
	}

	@Override
	public boolean checkTableExistence(String tableName) throws DAOException {

		Connection con = SQLConnectionProvider.getConnection();
		try {
			DatabaseMetaData dbmd = con.getMetaData();
			ResultSet rset =
					dbmd.getTables(null, null, tableName.toUpperCase(), null);
			if (rset != null && rset.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new DAOException("Error in checking table existence", e);
		}

	}

	@Override
	public void createPollsTable() throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement("CREATE TABLE Polls ("
					+ "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
					+ "title VARCHAR(150) NOT NULL,"
					+ "message CLOB(2048) NOT NULL)");
			ps.execute();
		} catch (Exception e) {
			throw new DAOException("Error in creating polls table", e);
		}

	}

	@Override
	public void createPollOptionsTable() throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		try {
			PreparedStatement ps =
					con.prepareStatement("CREATE TABLE PollOptions ("
							+ "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
							+ "optionTitle VARCHAR(100) NOT NULL,"
							+ "optionLink VARCHAR(150) NOT NULL,"
							+ "pollID BIGINT," + "votesCount BIGINT,"
							+ "FOREIGN KEY (pollID) REFERENCES Polls(id) )");
			ps.execute();
		} catch (Exception e) {
			throw new DAOException("Error in creating poll options table", e);
		}
	}

	@Override
	public void insertPoll(PollData pollData) throws DAOException {

		Poll poll = pollData.getPoll();
		List<PollOption> options = pollData.getOptions();

		Connection con = SQLConnectionProvider.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(
					"INSERT INTO Polls (title, message) VALUES (?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, poll.getTitle());
			ps.setString(2, poll.getMessage());
			ps.executeUpdate();

			try (ResultSet rset = ps.getGeneratedKeys()) {
				if (rset != null && rset.next()) {
					long pollID = rset.getLong(1);
					// insert all poll options
					for (PollOption option : options) {
						ps = con.prepareStatement("INSERT INTO PollOptions "
								+ "(optionTitle, optionLink, pollID, votesCount) "
								+ "VALUES (?, ?, ?, ?)");
						ps.setString(1, option.getOptionTitle());
						ps.setString(2, option.getOptionLink());
						ps.setLong(3, pollID); // pollID
						ps.setLong(4, 0); // votesCount

						ps.executeUpdate();

					}
				} else {
					throw new DAOException("Error in inserting poll");
				}
			}
		} catch (Exception e) {
			throw new DAOException("Error in inserting poll", e);
		}

	}

	@Override
	public void dropTable(String tableName) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		try {
			PreparedStatement ps =
					con.prepareStatement("DROP TABLE " + tableName);
			ps.execute();
		} catch (Exception e) {
			throw new DAOException("Error in dropping table", e);
		}
	}

}