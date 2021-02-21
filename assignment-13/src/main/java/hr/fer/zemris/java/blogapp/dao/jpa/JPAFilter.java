package hr.fer.zemris.java.blogapp.dao.jpa;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Web filter which manages entity managers for the requests that use the
 * persistence layer.
 * 
 * @author jankovidakovic
 *
 */
@WebFilter("/servleti/*")
public class JPAFilter implements Filter {


	@Override
	public void destroy() {
		// do nothing
	}

	/**
	 * Filters the requests and closes their entity managers after they have
	 * been processed fully.
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		try {
			chain.doFilter(request, response);
		} finally {
			JPAEMProvider.close();
		}

	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		// do nothing
	}

}
