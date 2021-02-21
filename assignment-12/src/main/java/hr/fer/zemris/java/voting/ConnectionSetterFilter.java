package hr.fer.zemris.java.voting;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.sql.DataSource;

import hr.fer.zemris.java.voting.dao.sql.SQLConnectionProvider;

/**
 * Filter that performs dynamic connection renting. Whenever a request is made
 * to the web application, which needs to interact with the persistence layer
 * in the designated request dispatcher, this filter rents a connection to the
 * request, and then the request dispatcher can use the connection to interract
 * with the persistence layer. Once the request is processed and the response is
 * ready, the filter destroys the rented connection, as it is no longer needed.
 * By convention, requests which need persistence layer are mapped to the url
 * pattern /servleti/*.
 * 
 * @author jankovidakovic
 *
 */
@WebFilter(filterName="f1",urlPatterns={"/servleti/*"})
public class ConnectionSetterFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	@Override
	public void destroy() {
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		DataSource ds = (DataSource)request.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			throw new IOException("Unable to access the database.", e);
		}
		SQLConnectionProvider.setConnection(con);
		try {
			chain.doFilter(request, response);
		} finally {
			SQLConnectionProvider.setConnection(null);
			try { con.close(); } catch(SQLException ignored) {}
		}
	}
	
}