package hr.fer.zemris.java.webserver;

/**
 * Interface that adds the capability of processing some HTTP request.
 * @author jankovidakovic
 *
 */
public interface IWebWorker {

	/**
	 * Processes the given request. Concrete implementations of this method are
	 * expected to create content for the client.
	 * 
	 * @param  context   request's context
	 * @throws Exception if something goes wrong
	 */
	public void processRequest(RequestContext context) throws Exception;
}
