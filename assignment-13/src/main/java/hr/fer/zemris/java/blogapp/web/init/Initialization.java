package hr.fer.zemris.java.blogapp.web.init;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import hr.fer.zemris.java.blogapp.dao.jpa.JPAEMFProvider;

/**
 * Listener which provides the web application with an entity manager factory
 * object, upon application startup, and closes the factory upon application
 * shutdown.
 * 
 * @author jankovidakovic
 *
 */
@WebListener
public class Initialization implements ServletContextListener {

	/**
	 * Closes the entity manager factory upon application shutdown
	 */
	@Override
    public void contextDestroyed(ServletContextEvent sce)  { 

		// remove entity manager factory from the provider
		JPAEMFProvider.setEmf(null);

		EntityManagerFactory emf =
				(EntityManagerFactory) sce
				.getServletContext().getAttribute("blog.app.emf");
		if (emf != null) {
			emf.close(); // remove emf from servlet context
		}
    }

	/**
	 * Sets up the application with an instance of the entity manager factory.
	 */
	@Override
    public void contextInitialized(ServletContextEvent sce)  { 
		// create entity manager factory
		EntityManagerFactory emf =
				Persistence.createEntityManagerFactory("blog.database");
		// set as global attribute which should be used by all clients that have
		// access to the servlet context
		sce.getServletContext().setAttribute("blog.app.emf", emf);
		// initialize EMF Provider for all clients that cannot access the
		// servlet context, but still work with the persistence layer.
		JPAEMFProvider.setEmf(emf);
    }
	
}
