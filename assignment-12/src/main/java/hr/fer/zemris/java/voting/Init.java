package hr.fer.zemris.java.voting;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import hr.fer.zemris.java.voting.dao.DAO;
import hr.fer.zemris.java.voting.dao.DAOProvider;
import hr.fer.zemris.java.voting.dao.sql.SQLConnectionProvider;
import hr.fer.zemris.java.voting.model.Poll;

/**
 * Initializer which connects to the database upon application startup, and
 * performs all the neccessary queries on the database so that it is ready for
 * application deployment.
 * 
 * @author jankovidakovic
 *
 */
@WebListener
public class Init implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		// load properties
		Properties config = new Properties();
		try {
			String realPath = sce.getServletContext()
					.getRealPath("/WEB-INF/dbsettings.properties");
			config.load(Files.newInputStream(Paths.get(realPath)));
		} catch (IOException e) { // no such file
			System.out.println("Error: missing database config file!");
			System.exit(-1);
		}

		// url for connecting to the database
		String connectionURL = "jdbc:derby://" + config.getProperty("host")
				+ ":" + config.getProperty("port") + "/" + "votingDB";

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("An error occurred while initializing "
					+ "the database connection pool", e1);
		}

		cpds.setJdbcUrl(connectionURL);

		cpds.setUser(config.getProperty("user"));
		cpds.setPassword(config.getProperty("password"));
		cpds.setInitialPoolSize(5);
		cpds.setMinPoolSize(5);
		cpds.setAcquireIncrement(5);
		cpds.setMaxPoolSize(20);

		// create connection for DAO
		try {
			SQLConnectionProvider.setConnection(cpds.getConnection());
		} catch (SQLException e) {
			System.out.println("Unable to connect to the database!");
			System.exit(-1);
		}

		// perform all neccesary operations on the database to set it up
		setUpDatabase();

		// set attribute which the filter will use to provide connections to
		// every client that wishes to use the persistence layer
		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);

	}

	/**
	 * Sets up the database to be deploy-ready.
	 */
	private void setUpDatabase() {

		// get DAO for database
		DAO dao = DAOProvider.getDao();

		// check table existence
		if (dao.checkTableExistence("Polls")) { // exists
			// check poll options existence
			if (dao.checkTableExistence("PollOptions")) {
				// both tables exist, check poll content
				List<Poll> polls = dao.fetchPolls();
				if (polls.size() == 0) {
					dao.dropTable("Poll");
					dao.dropTable("PollOptions");
					deployFromScratch(dao);
				} else if (polls.size() == 1) {
					// check which poll needs to be inserted
					if (polls.get(0).getTitle().contains("band")) {
						dao.insertPoll(PollDataFactory.newMoviePoll());
					} else {
						dao.insertPoll(PollDataFactory.newBandPoll());
					}
				} else {
					// there are enough polls, do nothing
				}
			} else { // poll options does not exist
				dao.dropTable("Polls");
				deployFromScratch(dao);
			}
		} else { // polls table does ont exist
			if (dao.checkTableExistence("PollOptions")) {
				dao.dropTable("PollOptions");
			}

			deployFromScratch(dao);
		}
	}

	/**
	 * Deploys the needed tables from scratch, using the provided interface to
	 * communicate with the persistence layer. Polls and PollOptions tables are
	 * created and filled with data.
	 * 
	 * @param dao interface for interaction with the persistence layer
	 */
	private void deployFromScratch(DAO dao) {
		dao.createPollsTable();
		dao.createPollOptionsTable();
		dao.insertPoll(PollDataFactory.newBandPoll());
		dao.insertPoll(PollDataFactory.newMoviePoll());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce
				.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}