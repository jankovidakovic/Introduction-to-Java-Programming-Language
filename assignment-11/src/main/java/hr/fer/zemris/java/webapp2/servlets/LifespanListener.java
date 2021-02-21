package hr.fer.zemris.java.webapp2.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listener that listens for the initialization of servlet context and remembers
 * the timestamp. This information is then used to dynamically display
 * information about web application running time.
 *
 */
@WebListener
public class LifespanListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public LifespanListener() {
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
		sce.getServletContext()
				.setAttribute("startTime",
				System.currentTimeMillis());
    }
	
}
