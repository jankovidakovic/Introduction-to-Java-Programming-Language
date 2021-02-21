package hr.fer.zemris.java.webserver;

/**
 * Interface which represents functionality of dispatching some request that the
 * server has recieved.
 * 
 * @author jankovidakovic
 *
 */
public interface IDispatcher {

	/**
	 * Dispatches the request that requests the given url path. Concrete
	 * implementations of this method are expected to perform all the neccessary
	 * operations needed to create a response, and send the response to the
	 * client which recieved the request.
	 * 
	 * @param  urlPath   requested url path
	 * @throws Exception if something goes wrong during processing the request
	 */
	void dispatchRequest(String urlPath) throws Exception;

}
