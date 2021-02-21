package hr.fer.zemris.java.blogapp.dao.jpa;

import javax.persistence.EntityManager;

import hr.fer.zemris.java.blogapp.dao.DAOException;

/**
 * Entity manager provider for all clients that need to communicate with the
 * persistence layer to insert, update, delete, or do something else with the
 * persistent data of the application.
 * 
 * @author jankovidakovic
 *
 */
public class JPAEMProvider {

	// thread local context used to associate entity manager with the thread
	// that needs to use it.
	private static ThreadLocal<EntityManager> locals = new ThreadLocal<>();

	public static EntityManager getEntityManager() {
		// check whether an entity manager has already been provided to this
		// client
		EntityManager em = locals.get();
		if (em == null) { // this is the first request for the entity manager
			// get a new entity manager
			em = JPAEMFProvider.getEmf().createEntityManager();
			// begin transaction (client is unaware of this and does not have to
			// worry about it)
			em.getTransaction().begin();
			// associate entity manager with the client thread
			locals.set(em);
		}
		return em;
	}

	public static void close() throws DAOException {
		EntityManager em = locals.get();
		if (em == null) { // client didn't request nor use an entity manager
			return;
		} else {
			// do not throw exceptions when they occur, because entity manager
			// needs to be removed from the thread local before the method is
			// exited.
			DAOException dex = null;
			try {
				// end client transaction
				em.getTransaction().commit();
			} catch (Exception ex) {
				dex = new DAOException("Unable to commit transaction", ex);
			}
			
			try {
				// close entity manager
				em.close();
			} catch (Exception ex) {
				if (dex != null) {
					dex = new DAOException("Unable to close entity manager.",
							ex);
				}
			}
			locals.remove(); // now we can throw
			if (dex != null) {
				throw dex;
			}
		}
	}
}
