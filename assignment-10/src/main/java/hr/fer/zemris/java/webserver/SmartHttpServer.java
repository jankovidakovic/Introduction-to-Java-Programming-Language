package hr.fer.zemris.java.webserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Implementation of a simple server that can process HTTP requests and generate
 * static and dynamic web pages. Port that the server listens to is 5721.
 * 
 * @author jankovidakovic
 *
 */
public class SmartHttpServer {

	private String address; // IP address of the server
	private String domainName; // domain name
	private int port; // TCP port
	private int workerThreads; // number of threads that will process requests
	private int sessionTimeout; // maximum time that the session is allowed to
								// exist, in seconds
	private Map<String, String> mimeTypes = new HashMap<String, String>(); // mime
	private ServerThread serverThread; // main thread, that recieves requests
	private ExecutorService threadPool; // thread pool for processing requests
	private Path documentRoot; // root of the documents that the server can
								// serve

	private Map<String, IWebWorker> workersMap =
			new HashMap<String, IWebWorker>(); // map of available workers

	private Map<String, SessionMapEntry> sessions =
			new HashMap<String, SmartHttpServer.SessionMapEntry>(); // active
																	// sessions
	private Random sessionRandom = new Random(); // rng for sid generation

	/**
	 * Creates and configures a new smart http server using the given
	 * configuration file.
	 * 
	 * @param configFileName path to configuration file used to configure the
	 *                       server. Expected is the path to a standard
	 *                       .properties file
	 */
	public SmartHttpServer(String configFileName) {

		Properties properties = new Properties();
		try {
			properties.load(Files.newInputStream(Paths.get(configFileName)));
		} catch (IOException e) { // no such file
			System.out.println("Error: missing config file!");
			System.exit(-1);
		}

		// configure the server using the file

		address = properties.getProperty("server.address");
		domainName = properties.getProperty("server.domainName");
		port = Integer.parseInt(properties.getProperty("server.port"));
		workerThreads = Integer
				.parseInt(properties.getProperty("server.workerThreads"));
		documentRoot = Paths.get(properties.getProperty("server.documentRoot"));

		Properties mimeConfig = new Properties();
		try {
			mimeConfig.load(Files.newInputStream(
					Paths.get(properties.getProperty("server.mimeConfig"))));
			for (Entry<Object, Object> entry : mimeConfig.entrySet()) {
				mimeTypes.put((String) entry.getKey(),
						(String) entry.getValue());
			}
		} catch (IOException e) {
			System.out.println(
					"No mime config file found! "
							+ "The server may not be able to render all requests correctly.");
		}

		sessionTimeout =
				Integer.parseInt(properties.getProperty("session.timeout"));

		Properties workers = new Properties();
		try {
			workers.load(Files.newInputStream(
					Paths.get(properties.getProperty("server.workers"))));
			for (Entry<Object, Object> entry : workers.entrySet()) {
				try {
					Class<?> referenceToClass = this.getClass().getClassLoader()
							.loadClass((String) entry.getValue());
					Object newObject = referenceToClass.newInstance();
					IWebWorker iww = (IWebWorker) newObject;

					workersMap.put((String) entry.getKey(), iww);
				} catch (Exception e) {
					System.out.println("Cannot load given worker.");
				}

			}
		} catch (IOException e) {
			System.out.println("Error: missing workers config file!");
		}


		// create the main server thread
		serverThread = new ServerThread();
	}

	/**
	 * Starts the server. If the server was previously started, method does
	 * nothing. Also starts the daemonic garbage collector, which erases expired
	 * sessions periodically.
	 */
	protected synchronized void start() {
		if (serverThread.isAlive()) {
			return;
		} else {
			serverThread.start();
		}

		threadPool = Executors.newFixedThreadPool(workerThreads);

		// add garbage collector for expired sessions
		Timer garbageCollector = new Timer(true);
		garbageCollector.schedule(new TimerTask() {
			@Override
			public void run() {
				Set<String> sessionsToDelete = new HashSet<String>();
				for (Entry<String, SessionMapEntry> entry : sessions.entrySet()) {
					if (entry.getValue().validUntil
							< System.currentTimeMillis()) {
						sessionsToDelete.add(entry.getKey());
					}
				}
				for (String sid : sessionsToDelete) {
					sessions.remove(sid);
				}
			}
		}, 0, 5 * 60 * 1000); // every 5 minutes
	}

	/**
	 * Stops the server, shutting down its main thread and killing the worker
	 * thread pool.
	 */
	protected synchronized void stop() {
		serverThread.interrupt();
		threadPool.shutdown();

	}

	/**
	 * Main server thread that processes the requests.
	 * 
	 * @author jankovidakovic
	 *
	 */
	protected class ServerThread extends Thread {
		@Override
		public void run() {
			try {

				@SuppressWarnings("resource")
				ServerSocket serverSocket = new ServerSocket();
				serverSocket
						.bind(new InetSocketAddress((InetAddress) null,
								port));

				while (true) { // accept new requests
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
				return;
			}
		}
	}

	/**
	 * Client worker that processes one specific request
	 * 
	 * @author jankovidakovic
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {

		private Socket csocket; // client socket
		private InputStream istream; // input stream
		private OutputStream ostream; // output stream
		private String version; // http version
		private String method; // http method
		private String host; // host that sent the request
		private Map<String, String> params = new HashMap<String, String>();
		private Map<String, String> tempParams = new HashMap<String, String>();
		private Map<String, String> permParams = new HashMap<String, String>();

		private List<RCCookie> outputCookies =
				new ArrayList<RequestContext.RCCookie>();
		private String SID; // session identifier

		private RequestContext context = null;

		/**
		 * Creates a new client worker and binds it to the given socket
		 * 
		 * @param csocket socket that the client sent the request from
		 */
		private ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}

		/**
		 * Dispatches the request denoted by the given URL path.
		 * 
		 * @param  urlPath    path requested by the client
		 * @param  directCall <code>true</code> if someone else sent the
		 *                    dispatch request, <code>false</code> if the
		 *                    dispatch request was from within the server
		 * @throws Exception  if anything goes wrong during dispatching
		 */
		private void internalDispatchRequest(String urlPath, boolean directCall)
				throws Exception {

			// check if access is allowed
			if (urlPath.startsWith("/private") && directCall) {
				// files from private map are now allowed to be accessed
				// by requests that were recieved from outside the server
				sendSimpleResponse(404, "Not found");
				return;
			}

			// create full path that was requested, relative to document
			// root
			Path fullPath = Paths.get(documentRoot.toString() + urlPath);
			// check if requested path is allowed for access
			if (!(fullPath.toAbsolutePath()
					.startsWith(documentRoot.toAbsolutePath()))) {
				sendSimpleResponse(403, "Forbidden");
				return;
			}

			// check if requested path is mapped to some IWebWorker

			if (workersMap.get(urlPath.toString()) != null) {
				if (context == null) {
					context = new RequestContext(ostream, params, tempParams,
							permParams, outputCookies, this, SID);
				}
				workersMap.get(urlPath.toString()).processRequest(context);
				ostream.flush();
				return;
			}

			// check if requested path is trying to access the worker directly
			if (urlPath.startsWith("/ext")) {
				// get worker name
				String workerName = urlPath.split("/")[2];
				// load worker class
				try {
					Class<?> referenceToClass = this.getClass().getClassLoader()
							.loadClass("hr.fer.zemris.java.webserver.workers."
									+ workerName);
					Object newObject = referenceToClass.newInstance();
					IWebWorker iww = (IWebWorker) newObject;

					if (context == null) {
						context =
								new RequestContext(ostream, params, tempParams,
										permParams, outputCookies, this, SID);
					}
					iww.processRequest(context);
					ostream.flush();
					return;

				} catch (Exception e) {
					System.out.println("Cannot load given worker.");
				}
			}

			// check if requested path exists, is file and is readable
			if (!Files.exists(fullPath) || !Files.isRegularFile(fullPath)
					|| !Files.isReadable(fullPath)) {
				sendSimpleResponse(404, "Not found");
				return;
			}

			// extract file extension
			String[] dotSeparation = fullPath.toString().split("\\.");
			String extension = dotSeparation.length == 1 ? "" // no extension
					: dotSeparation[dotSeparation.length - 1];

			// get mime type for given extension
			String mimeType = mimeTypes.get(extension);
			if (mimeType == null) { // assume default
				mimeType = "application/octet-stream";
			}

			// check if smart script was requested
			if (extension.equals("smscr")) {
				String scriptContent = new String(
						new BufferedInputStream(Files.newInputStream(fullPath))
								.readAllBytes());

				SmartScriptParser parser = new SmartScriptParser(scriptContent);

				// something's not right
				if (context == null) {
					context = new RequestContext(ostream, params, tempParams,
							permParams, outputCookies, this, SID);
				}

				SmartScriptEngine engine = new SmartScriptEngine(
						parser.getDocumentNode(), context);

				engine.execute();
				ostream.flush();
				return;
			}

			// finally, create request context
			if (context == null) {
				context = new RequestContext(ostream, params, tempParams,
						permParams, outputCookies, this, SID);
			}

			context.setMimeType(mimeType);
			context.setStatusCode(200);
			context.setStatusText("OK");
			context.setContentLength(fullPath.toFile().length());
			context.write(
					new BufferedInputStream(Files.newInputStream(fullPath))
							.readAllBytes());

			ostream.flush();
			// done ?
		}

		@Override
		public void run() {
			try {
				// obrain socket streams
				istream = new BufferedInputStream(csocket.getInputStream());
				ostream = new BufferedOutputStream(csocket.getOutputStream());

				// read request
				List<String> request = readRequest();

				// check for validity
				if (request.isEmpty()) {
					sendSimpleResponse(400, "Bad request");
					return;
				}

				// set default host
				host = domainName;

				// check if specific host was set in the request
				for (String requestLine : request) {
					if (requestLine.startsWith("Host: ")) {
						host = requestLine.split(": ")[1].split(":")[0];
						// could cause arrayindexexception if not careful
					}
				}

				// check if session already exists, or create a new one if it
				// doesnt
				checkSession(request);

				// load session parameters
				permParams = sessions.get(SID).map;

				// get first line, and extract fields
				String[] firstLine = request.get(0).split(" ");

				// check number of fields
				if (firstLine.length != 3) {
					sendSimpleResponse(400, "Bad request");
					return;
				}

				// extract method
				method = firstLine[0].toUpperCase();
				if (!method.equals("GET")) { // only GET is supported
					sendSimpleResponse(405, "Method not allowed");
					return;
				}

				// check http version
				version = firstLine[2].toUpperCase();
				if (!(version.equals("HTTP/1.1")
						|| version.equals("HTTP/1.0"))) {
					sendSimpleResponse(505, "HTTP Version Not Supported");
					return;
				}

				// extract requested path
				String[] requestedPath = firstLine[1].split("\\?");
				String path = requestedPath[0];

				// parse parameters if any were given
				if (requestedPath.length > 1) {
					parseParameters(requestedPath[1]);
				}

				// delegate request handling to the designated dispatcher
				internalDispatchRequest(path, true);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					csocket.close();
				} catch (IOException ex) {
					System.out.println(ex.getMessage());
				}

			}

		}

		/**
		 * Checks whether request header defines a cookie in which a session
		 * identifier is set. If that is the case, it tries to load the session
		 * that goes by the given SID, or creates a new session if unable to.
		 * 
		 * @param headerLines lines of the request header
		 */
		private synchronized void checkSession(List<String> headerLines) {
			String sidCandidate = null;
			for (String line : headerLines) {
				if (line.startsWith("Cookie:")) {
					String cookiesContent = line.substring(8);
					String[] cookies = cookiesContent.split(";");
					for (int i = 0; i < cookies.length; i++) {
						String[] cookie = cookies[i].split("=");
						if (cookie[0].equals("sid")) { // candidate
							sidCandidate = cookie[1].substring(1,
									cookie[1].length() - 1);
						}
					}
				}
			}
			SessionMapEntry sessionCandidate = sessions.get(sidCandidate);
			if (sessionCandidate == null) {
				SID = createNewSession();
			} else if (sessionCandidate.host.equals(host) == false) {
				createNewSession();
			} else if (sessionCandidate.validUntil
					< System.currentTimeMillis()) {
				SID = createNewSession();
			} else { // finally, a valid session object
				sessionCandidate.validUntil =
						System.currentTimeMillis() * 1000 + sessionTimeout;
				SID = sessionCandidate.sid;
			}
		}

		/**
		 * Creates a new session
		 * 
		 * @return
		 */
		private String createNewSession() {
			// create session object
			SessionMapEntry session = new SessionMapEntry();

			sessionRandom.setSeed(System.currentTimeMillis());

			session.sid = "";
			for (int i = 0; i < 20; i++) {
				session.sid +=
						Character.toString(sessionRandom.nextInt(26) + 65);
			}
			session.host = host;
			session.map = new ConcurrentHashMap<String, String>();
			session.validUntil =
					System.currentTimeMillis() * 1000 + sessionTimeout;

			// store session object into sessions map
			sessions.put(session.sid, session);

			// add session cookie
			outputCookies
					.add(new RCCookie("sid", session.sid, null, host, "/"));

			return session.sid;
		}

		/**
		 * Parses URL parameters and stores them into the map.
		 * 
		 * @param parameters String consisting of URL parameters
		 */
		private void parseParameters(String parameters) {
			Arrays.stream(parameters.split("&")).forEach(param -> {
				String[] paramEntry = param.split("=");
				params.put(paramEntry[0], paramEntry[1]);
			});
		}

		/**
		 * Creates and sends a simple http response, with only the header and no
		 * content.
		 * 
		 * @param  statusCode  status code of the response
		 * @param  statusText  status text of the response
		 * @throws IOException if unable to write the response to output stream
		 */
		private void sendSimpleResponse(int statusCode,
				String statusText)
				throws IOException {
			RequestContext rc = new RequestContext(ostream, null, null, null);
			rc.setStatusCode(statusCode);
			rc.setStatusText(statusText);
			rc.write("");

			ostream.flush();
		}

		/**
		 * Reads the HTTP request into an array of bytes
		 * 
		 * @return             array of bytes representing the request
		 * @throws IOException if unable to read request
		 */
		private byte[] readRequestBytes() throws IOException {

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
			l: while (true) {
				int b = istream.read();
				if (b == -1)
					return null;
				if (b != 13) {
					bos.write(b);
				}
				switch (state) {
				case 0:
					if (b == 13) {
						state = 1;
					} else if (b == 10)
						state = 4;
					break;
				case 1:
					if (b == 10) {
						state = 2;
					} else
						state = 0;
					break;
				case 2:
					if (b == 13) {
						state = 3;
					} else
						state = 0;
					break;
				case 3:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				case 4:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				}
			}
			return bos.toByteArray();
		}

		/**
		 * Reads the HTTP request and parses it into a list of lines, each line
		 * representing a single line the request.
		 * 
		 * @return             list of lines of HTTP request
		 * @throws IOException if something goes wrong while reading the request
		 */
		private List<String> readRequest() throws IOException {
			String requestHeader =
					new String(readRequestBytes(), StandardCharsets.ISO_8859_1);
			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			for (String s : requestHeader.split("\n")) {
				if (s.isEmpty())
					break;
				char c = s.charAt(0);
				if (c == 9 || c == 32) {
					currentLine += s;
				} else {
					if (currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if (!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			return headers;
		}
	}

	/**
	 * Representation of one session
	 * 
	 * @author jankovidakovic
	 *
	 */
	private static class SessionMapEntry {
		String sid; // session identifier
		String host; // which host uses the session
		long validUntil; // time until the session expires
		Map<String, String> map; // persistent parameters of the session

	}

	/**
	 * Main method from which the whole application is started.
	 * 
	 * @param args command-line arguments. None expected.
	 */
	public static void main(String[] args) {

		SmartHttpServer server = new SmartHttpServer("src/main/resources/server.properties");
		server.start();

	}
}
