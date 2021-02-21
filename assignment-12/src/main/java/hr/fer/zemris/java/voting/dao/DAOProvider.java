package hr.fer.zemris.java.voting.dao;

import hr.fer.zemris.java.voting.dao.sql.SQLDAO;

/**
 * Singleton class which provides the interface for communicating with
 * persistence layer of the web application.
 * 
 * @author jankovidakovic
 *
 */
public class DAOProvider {

	// singleton interface for persistence layer access
	private static DAO dao = new SQLDAO();
	
	/**
	 * Retrieves the singleton object representing the interface to the
	 * persistence layer.
	 * 
	 * @return DAO object
	 */
	public static DAO getDao() {
		return dao;
	}
	
}