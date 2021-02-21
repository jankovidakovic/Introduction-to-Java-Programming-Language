package hr.fer.zemris.java.webapp2.servlets;

import java.io.IOException;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Filter for the font color of a funny story. Filter determines some random
 * font color and inserts that information in the request, which is then read by
 * JSP file to set the font color dynamically.
 */
@WebFilter("/stories/funny.jsp")
public class FontColorFilter implements Filter {

	// collection of random colors
	private static String[] colors = { "red", "green", "blue", "cyan",
			"magenta", "gold", "gray", "black", "crimson", };

	/**
	 * Default constructor.
	 */
	public FontColorFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		// set a new random color
		request.setAttribute("fontColor",
				colors[new Random().nextInt(colors.length)]);
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
