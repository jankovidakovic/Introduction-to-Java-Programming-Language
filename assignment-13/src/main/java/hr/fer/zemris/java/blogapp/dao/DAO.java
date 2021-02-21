package hr.fer.zemris.java.blogapp.dao;

import java.util.List;

import hr.fer.zemris.java.blogapp.model.BlogComment;
import hr.fer.zemris.java.blogapp.model.BlogEntry;
import hr.fer.zemris.java.blogapp.model.BlogUser;

/**
 * Interface used to interact with the persistence layer. Interaction can be
 * done by any of the methods defined here, and there should exist some concrete
 * implementation of this interface that implements the methods to work as
 * described here.
 * 
 * @author jankovidakovic
 *
 */
public interface DAO {

	/**
	 * Retrieves the blog entry by ID.
	 * 
	 * @param  id           id of the blog entry
	 * @return              retrieved blog entry
	 * @throws DAOException if something goes wrong during the retrieval
	 */
	BlogEntry getBlogEntry(Long id) throws DAOException;

	/**
	 * Fetches an existing user by its nickname, which is unique for every user
	 * and therefore can be used as a key.
	 * 
	 * @param  nick         nickname of the user
	 * @return              user modelled by the <code>BlogUser</code> class, if
	 *                      such exists, or </code>null</code> if no user with
	 *                      given nickname was found.
	 * @throws DAOException if something goes wrong in the persistence layer.
	 */
	BlogUser getBlogUserByNick(String nick) throws DAOException;

	/**
	 * Inserts the given user into the database.
	 * 
	 * @param  user         model of a user to insert
	 * @throws DAOException if something goes wrong in the persistence layer
	 *                      (e.g. user already exists)
	 */
	void insertUser(BlogUser user) throws DAOException;

	/**
	 * Fetches all the blog authors, which are essentially all registered users.
	 * 
	 * @return              all registered users
	 * @throws DAOException if something goes wrong in the persistence layer.
	 */
	List<BlogUser> getAuthors() throws DAOException;

	/**
	 * Fetches all the blog entries that the given user has created.
	 * 
	 * @param  user         user which blog entries are requested.
	 * @return              list of all the user's blog entries
	 * @throws DAOException if something goes wrong in the persistence layer.
	 */
	List<BlogEntry> getBlogEntries(BlogUser user) throws DAOException;

	/**
	 * Fetches all the blog comments which were posted for the given blog entry.
	 * 
	 * @param  blogEntry    entry which comments are requested
	 * @return              all comments made on the blog entry.
	 * @throws DAOException if something goes wrong in the persistence layer.
	 */
	List<BlogComment> getBlogComments(BlogEntry blogEntry) throws DAOException;

	/**
	 * Inserts the given blog entry into the database.
	 * 
	 * @param  blogEntry    blog entry to insert.
	 * @throws DAOException if something goes wrong in the persistence layer (e.
	 *                      g. blog entry already exists and is stored
	 *                      identically in the database)
	 */
	void insertBlogEntry(BlogEntry blogEntry) throws DAOException;

	/**
	 * Inserts a blog comment into the database.
	 * 
	 * @param  blogComment  blog comment to insert into the database
	 * @throws DAOException if something goes wrong in the persistence layer
	 *                      (e.g. identical blog comment is already stored in
	 *                      the database.)
	 */
	void insertBlogComment(BlogComment blogComment) throws DAOException;

}
