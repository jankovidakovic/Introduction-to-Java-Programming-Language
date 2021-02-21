package hr.fer.zemris.java.voting.dao.sql;

import java.sql.Connection;

/**
 * Linkage of thread context with the connection to the database. Should be used
 * by every thread which wants to access the persistence layer of the
 * application.
 * 
 * @author jankovidakovic
 *
 */
public class SQLConnectionProvider {

	private static ThreadLocal<Connection> connections = new ThreadLocal<>();
	
	/**
	 * Stores the information about the given connection to the thread's
	 * context, or destroys such information if it's already present.
	 * 
	 * @param con connection to the database which is to be linked with current
	 *            thread
	 */
	public static void setConnection(Connection con) {
		if(con==null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}
	
	/**
	 * Retrieves a valid connection to the database that is rented to the thread
	 * that calls this method.
	 * 
	 * @return connection to the database which the thread can legally use.
	 */
	public static Connection getConnection() {
		return connections.get();
	}
	
}