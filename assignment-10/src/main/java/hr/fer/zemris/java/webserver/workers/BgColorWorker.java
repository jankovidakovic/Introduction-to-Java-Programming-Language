package hr.fer.zemris.java.webserver.workers;

import java.awt.Color;
import java.io.IOException;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Worker that renders a web page which displays the information about an
 * attempt to change the background color of homepage.
 * 
 * @author jankovidakovic
 *
 */
public class BgColorWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String bgcolor = context.getParameter("bgcolor");
		try {
			Color.decode("#" + bgcolor);
			context.setPersistentParameter("bgcolor", bgcolor);
			generateMessageDoc("Color was succesfully updated.", context);
		} catch (NumberFormatException ex) { // wrong color was specified
			generateMessageDoc("Color was not updated.", context);
		}
	}

	/**
	 * Generates a simple HTML document that displays some message.
	 * 
	 * @param  message     message to be displayed
	 * @param  context     context of the request which will get this webpage as
	 *                     a response
	 * @throws IOException if something goes wrong during writing a response
	 */
	private void generateMessageDoc(String message, RequestContext context)
			throws IOException {

		context.write("<html><head></head><body>");
		context.write("<h1>" + message + "</h1>");
		context.write("<a href=\"/index2.html\">Početna stranica</a>");
		context.write("</body></html>");
	}

}
