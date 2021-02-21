package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Worker that processes the requests that request the home page, found under
 * URI /index2.html
 * 
 * @author jankovidakovic
 *
 */
public class Home implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {

		String bgcolor = context.getPersistentParameter("bgcolor");
		if (bgcolor == null) { // not yet set
			context.setTemporaryParameter("background", "7F7F7F");
		} else {
			context.setTemporaryParameter("background", bgcolor);
		}

		// delegate content rendering to a smart script
		context.getDispatcher().dispatchRequest("/private/pages/home.smscr");

	}

}
