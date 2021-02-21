package hr.fer.zemris.java.webserver.workers;

import java.util.Set;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Worker that renders a webpage which simply displays the URI parameters that
 * the request contained.
 * 
 * @author jankovidakovic
 *
 */
public class EchoWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		
		// set response header parameters
		context.setMimeType("text/html");
		context.setStatusCode(200);
		context.setStatusText("OK");

		// write response body
		context.write("<html><head><title>Tablica parametara</title>");
		context.write(
				"<style> table, th { border: 1px solid black; } </style></head>");
		context.write("<body><h1>Parametri URLa</h1>");
		context.write("<table><thead>" + "<tr><th>"
				+ "Ime parametra</th><th>Vrijednost parametra"
				+ "</th></tr></thead>");
		context.write("<tbody>");
		
		Set<String> paramNames = context.getParameterNames();
		for (String name : paramNames) {
			context.write("<tr><td>" + name + "</td><td>"
					+ context.getParameter(name) + "</td></tr>");
		}
		context.write("</tbody></table></body></html>");

	}

}
