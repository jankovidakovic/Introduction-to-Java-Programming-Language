package hr.fer.zemris.java.blogapp.dao;

import hr.fer.zemris.java.blogapp.dao.jpa.JPADAOImpl;

/**
 * Provides clients with DAO for interaction with the persistence layer. Uses
 * singleton design pattern to ensure only one DAO exists for whole application.
 * 
 * @author jankovidakovic
 *
 */
public class DAOProvider {

	// singleton DAO
	private static DAO dao = new JPADAOImpl();

	/**
	 * DAO Provider method
	 * 
	 * @return DAO
	 */
	public static DAO getDAO() {
		return dao;
	}
}
