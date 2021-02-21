package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Class that represents the context of a single HTTP request, implementing
 * standard context entries such as charset, mime type, parameters and cookies.
 * 
 * @author jankovidakovic
 *
 */
public class RequestContext {

	private OutputStream outputStream; // stream for writing the request
	private Charset charset; // charset used to encode and decode the request
	private String encoding; // encoding used to write the http request
	private int statusCode; // status code of the http response
	private String statusText; // status text of the http response
	private String mimeType; // mime type used in the request
	private Long contentLength; // length of the request
	private String sid; // session ID

	private Map<String, String> parameters; // params of the request, read-only
	private Map<String, String> temporaryParameters; // temporary params
	private Map<String, String> persistentParameters; // persistent params

	private List<RCCookie> outputCookies; // cookies

	private boolean headerGenerated; // flag for header generation

	private IDispatcher dispatcher; // dispatcher which handles the request and
									// response
	
	/**
	 * Creates a new cookie content with given properties, and sets other
	 * properties to their default values.
	 * 
	 * @param outputStream         output stream for writing the response
	 * @param parameters           parameters of the request
	 * @param persistentParameters parameters that persist through the session
	 * @param outputCookies        cookies of the request
	 */
	public RequestContext(OutputStream outputStream,
			Map<String, String> parameters,
			Map<String, String> persistentParameters,
			List<RCCookie> outputCookies) {

		Objects.requireNonNull(outputStream); // must be able to write a
												// response
		this.outputStream = outputStream;

		this.parameters =
				parameters == null ? new HashMap<String, String>() : parameters;
		this.persistentParameters = persistentParameters == null
				? new HashMap<String, String>() : persistentParameters;

		this.outputCookies = outputCookies == null ? new ArrayList<RCCookie>()
				: outputCookies;

		this.temporaryParameters = new HashMap<String, String>();

		this.charset = Charset.forName("UTF-8");
		this.encoding = "UTF-8";
		this.statusCode = 200;
		this.statusText = "OK";
		this.mimeType = "text/html";
		this.contentLength = null;

		this.headerGenerated = false;
	}


	/**
	 * Creates a request context and sets request properties to given values.
	 * 
	 * @param outputStream         output stream at which the response will be
	 *                             written
	 * @param parameters           parameters of the request
	 * @param temporaryParameters  temporary parameters
	 * @param persistentParameters parameters that persist through sessions
	 * @param outputCookies        cookies
	 * @param dispatcher           object that can dispatch the request
	 */
	public RequestContext(OutputStream outputStream,
			Map<String, String> parameters,
			Map<String, String> temporaryParameters,
			Map<String, String> persistentParameters,
			List<RCCookie> outputCookies, IDispatcher dispatcher, String sid) {
		this(outputStream, parameters, persistentParameters, outputCookies);
		this.temporaryParameters = temporaryParameters;
		this.dispatcher = dispatcher;
		this.sid = sid;
	}

	/**
	 * Sets the encoding to the given value.
	 * 
	 * @param  encoding         new encoding
	 * @throws RuntimeException if the header was already generated and written,
	 *                          since the change cannot influence already
	 *                          written header.
	 */
	public void setEncoding(String encoding) {
		if (headerGenerated) {
			throw new RuntimeException("Header was already generated.");
		}
		this.encoding = encoding;
	}

	/**
	 * Sets the charset to the given value
	 * 
	 * @param  charset          new charset
	 * @throws RuntimeException if the header was already generated and written,
	 *                          since the change cannot influence already
	 *                          written header.
	 */
	public void setCharset(Charset charset) {
		if (headerGenerated) {
			throw new RuntimeException("Header was already generated.");
		}
		this.charset = charset;
	}

	/**
	 * Sets the status code to the given value.
	 * 
	 * @param  statusCode       new status code
	 * @throws RuntimeException if the header was already generated and written,
	 *                          since the change cannot influence already
	 *                          written header.
	 */
	public void setStatusCode(int statusCode) {
		if (headerGenerated) {
			throw new RuntimeException("Header was already generated.");
		}
		this.statusCode = statusCode;
	}

	/**
	 * Sets the status text to the given value.
	 * 
	 * @param  statusText       new status text
	 * @throws RuntimeException if the header was already generated and written,
	 *                          since the change cannot influence already
	 *                          written header.
	 */
	public void setStatusText(String statusText) {
		if (headerGenerated) {
			throw new RuntimeException("Header was already generated.");
		}
		this.statusText = statusText;
	}

	/**
	 * Sets the mime type to the given value.
	 * 
	 * @param  mimeType         new mime type
	 * @throws RuntimeException if the header was already generated and written,
	 *                          since the change cannot influence already
	 *                          written header.
	 */
	public void setMimeType(String mimeType) {
		if (headerGenerated) {
			throw new RuntimeException("Header was already generated.");
		}
		this.mimeType = mimeType;
	}

	/**
	 * Sets the content length to the given value.
	 * 
	 * @param  contentLength    new content length
	 * @throws RuntimeException if the header was already generated and written,
	 *                          since the change cannot influence already
	 *                          written header.
	 */
	public void setContentLength(Long contentLength) {
		if (headerGenerated) {
			throw new RuntimeException("Header was already generated.");
		}
		this.contentLength = contentLength;
	}

	/**
	 * Retrieves the value of the parameter with given name.
	 * 
	 * @param  name Name which value is requested
	 * @return      value of the parameter with the given name, or
	 *              <code>null</code> if there is no value associated with the
	 *              given name.
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Retrieves names of all parameters in parameters map
	 * 
	 * @return read-only set consisting of names of all parameters.
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}

	/**
	 * Retrieves value of the persistent parameter with given name.
	 * 
	 * @param  name name which value is requested
	 * @return      value of the persistent parameter associated with the given
	 *              name, or <code>null</code> if no value is associated with
	 *              it.
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * Retrieves names of all persistent parameters.
	 * 
	 * @return read-only set consisting of names of all persistent parameters.
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}

	/**
	 * Sets a new persistent parameter, with given name and value. If parameter
	 * with given name already exists, its value will be overwritten with the
	 * new value.
	 * 
	 * @param name  name associated with the persistent parameter
	 * @param value value of the persistent parameter
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	/**
	 * Removes the persistent parameter with the given name.
	 * 
	 * @param name name of the persistent parameter that is to be removed. If no
	 *             such parameter is defined, nothing happens.
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.put(name, null);
	}

	/**
	 * Retrieves value of a temporary parameter associated with the given name,
	 * or <code>null</code> if no such association exists.
	 * 
	 * @param  name name of the requested temporary parameter
	 * @return      value of the parameter, or <code>null</code> if value is not
	 *              defined.
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}

	/**
	 * Retrieves names of all defined temporary parameters
	 * 
	 * @return read-only set of all temporary parameter names.
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}

	/**
	 * Retrieves the identifier whith is unique for current user session.
	 * 
	 * @return session Identifier
	 */
	public String getSessionID() {
		return sid;
	}

	/**
	 * Defines a new temporary parameter with given name that stores given
	 * value. If temporary parameter with given name already exists, its value
	 * is overwritten with the new value.
	 * 
	 * @param name  Name of the temporary parameter
	 * @param value value to be associated with given name
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}

	/**
	 * Deletes the value associated with the temporaray parameter of the given
	 * name.
	 * 
	 * @param name Name of the temporary parameter to be deleted.
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.put(name, null);
	}

	/**
	 * Returns the dispatcher that is responsible for handling the request and
	 * response actions
	 * 
	 * @return dispatcher
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	public RequestContext write(byte[] data) throws IOException {
		if (!headerGenerated) {
			generateHeader();
		}
		outputStream.write(data);
		return this;
	}

	public RequestContext write(byte[] data, int offset, int len)
			throws IOException {
		if (!headerGenerated) {
			generateHeader();
		}
		outputStream.write(data, offset, len);
		return this;
	}

	public RequestContext write(String text) throws IOException {
		if (!headerGenerated) {
			generateHeader();
		}
		outputStream.write(text.getBytes(Charset.forName(encoding)));
		return this;
	}

	/**
	 * Generates the header of an HTTP response. Header is constructed using the
	 * inner properties of the instance which calls this method.
	 */
	private void generateHeader() throws IOException {
		this.charset = Charset.forName(encoding);
		StringBuilder sb = new StringBuilder();

		// write http status
		sb.append("HTTP/1.1" + " " + statusCode + " " + statusText + "\r\n");

		// write contentType
		sb.append("Content-type:" + " " + mimeType);
		if (mimeType.startsWith("text/")) {
			sb.append("; charset=" + charset.displayName());
		}
		sb.append("\r\n");

		// write contentLength
		if (contentLength != null) {
			sb.append("Content-Length:" + " " + contentLength.toString()
					+ "\r\n");
		}

		// write cookies
		for (RCCookie cookie : outputCookies) {
			sb.append(
					"Set-Cookie: " + cookie.name + "=\"" + cookie.value + "\"");
			if (cookie.domain != null) {
				sb.append("; Domain=" + cookie.domain);
			}
			if (cookie.path != null) {
				sb.append("; Path=" + cookie.path);
			}
			if (cookie.maxAge != null) {
				sb.append("; Max-Age=" + cookie.maxAge);
			}
			sb.append("; HttpOnly");
			sb.append("\r\n");
		}
		sb.append("\r\n"); // end of header
		outputStream.write(sb.toString().getBytes(StandardCharsets.ISO_8859_1));
		this.headerGenerated = true;
	}

	/**
	 * Implementation of a http request cookie, which contains name, value,
	 * path, domain, and maxAge
	 * 
	 * @author jankovidakovic
	 *
	 */
	public static class RCCookie {

		private final String name; // cookie name
		private final String value; // cookie value
		private final String domain; // domain that the cookie belongs to
		private final String path; // path within the domain that is associated
									// with the cookie
		private final Integer maxAge; // time until the cookie expires

		/**
		 * Constructs a new cookie with given parameters
		 * 
		 * @param name   name that the cookie is associated with
		 * @param value  value that the cookie stores
		 * @param domain domain that the cookie belongs to
		 * @param path   specific path within the domain that the cookie belongs
		 *               to
		 * @param maxAge maximum amount of time until the cookie expires
		 */
		public RCCookie(String name, String value, Integer maxAge,
				String domain, String path) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @return the domain
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * @return the path
		 */
		public String getPath() {
			return path;
		}

		/**
		 * @return the maxAge
		 */
		public Integer getMaxAge() {
			return maxAge;
		}

	}

	/**
	 * Defines a new cookie and adds it to the requext context.
	 * 
	 * @param cookie new cookie to be added.
	 */
	public void addRCCookie(RCCookie cookie) {
		outputCookies.add(cookie);
	}

}
