package hr.fer.zemris.java.blogapp.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Provider for the entity manager factory object. Should be used by clients
 * which need the entity manager factory to create entity managers, but cannot
 * access it in any other way (e. g. when the servlet context is not visible)
 * 
 * @author jankovidakovic
 *
 */
public class JPAEMFProvider {

	// entity manager factory which is offered to the clients
	private static EntityManagerFactory emf;

	/**
	 * Provides the entity manager factory to the client that requests it
	 * 
	 * @return instance of entity manager factory
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Sets the providable entity manager factory to the given one.
	 * 
	 * @param emf new entity manager factory which will be provided
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}
