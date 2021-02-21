package hr.fer.zemris.java.blogapp.dao.jpa;

import java.util.List;

import javax.persistence.NoResultException;

import hr.fer.zemris.java.blogapp.dao.DAO;
import hr.fer.zemris.java.blogapp.dao.DAOException;
import hr.fer.zemris.java.blogapp.model.BlogComment;
import hr.fer.zemris.java.blogapp.model.BlogEntry;
import hr.fer.zemris.java.blogapp.model.BlogUser;

/**
 * Concrete implementation of interraction with the persistence layer. In this
 * case, interraction is done through JPA.
 * 
 * @author jankovidakovic
 *
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		try {
			BlogEntry blogEntry =
					JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
			return blogEntry;
		} catch (NoResultException e) {
			System.out.println("FETCHED BLOG ENTRY: null");
			return null;
		} catch (Exception e) {
			throw new DAOException("Exception in persistence layer", e);
		}

	}

	@Override
	public BlogUser getBlogUserByNick(String nick) throws DAOException {
		try {
			BlogUser blogUser = JPAEMProvider.getEntityManager()
					.createQuery(
							"SELECT bu FROM BlogUser bu WHERE bu.nick = ?1",
							BlogUser.class)
					.setParameter(1, nick).getSingleResult();
			return blogUser;
		} catch (NoResultException e) {
			System.out.println("FETCHED BLOG USER: null");
			return null;
		} catch (Exception e) {
			throw new DAOException("Exception in persistence layer", e);
		}

	}

	@Override
	public void insertUser(BlogUser user) throws DAOException {
		try {
			JPAEMProvider.getEntityManager().persist(user);
		} catch (Exception e) {
			throw new DAOException("Exception in persistence layer", e);
		}

	}

	@Override
	public List<BlogUser> getAuthors() throws DAOException {
		try {
			List<BlogUser> authors = JPAEMProvider.getEntityManager()
					.createQuery("SELECT bu FROM BlogUser bu",
							BlogUser.class)
					.getResultList();
			return authors;
		} catch (NoResultException e) {
			System.out.println("FETCHED AUTHORS: null");
			return null;
		} catch (Exception e) {
			throw new DAOException("Exception in persistence layer", e);
		}
	}

	@Override
	public List<BlogEntry> getBlogEntries(BlogUser user) throws DAOException {
		try {
			List<BlogEntry> blogEntries = JPAEMProvider.getEntityManager()
					.createQuery(
							"SELECT be FROM BlogEntry be WHERE be.creator = ?1",
							BlogEntry.class)
					.setParameter(1, user).getResultList();

			return blogEntries;

		} catch (NoResultException e) {
			System.out.println("FETCHED BLOG ENTRIES: null");
			return null;
		} catch (Exception e) {
			throw new DAOException("Exception in persistence layer", e);
		}
	}

	@Override
	public List<BlogComment> getBlogComments(BlogEntry blogEntry) throws DAOException {
		try {
			List<BlogComment> blogComments = JPAEMProvider
					.getEntityManager()
					.createQuery(
							"SELECT bc FROM BlogComment bc WHERE bc.blogEntry = ?1",
							BlogComment.class
					).setParameter(1,blogEntry).getResultList();
			return blogComments;
		} catch (NoResultException e) {
			System.out.println("FETCHED BLOG COMMENTS: null");
			return null;
		} catch (Exception e) {
			throw new DAOException("Exception in the persistence layer", e);
		}
	}

	@Override
	public void insertBlogEntry(BlogEntry blogEntry) throws DAOException {
		try {
			JPAEMProvider.getEntityManager().persist(blogEntry);
		} catch (Exception e) {
			throw new DAOException("Exception in the persistence layer", e);
		}

	}

	@Override
	public void insertBlogComment(BlogComment blogComment) throws DAOException {
		try {
			JPAEMProvider.getEntityManager().persist(blogComment);
		} catch (Exception e) {
			throw new DAOException("Exception in the persistence layer", e);
		}

	}

}
