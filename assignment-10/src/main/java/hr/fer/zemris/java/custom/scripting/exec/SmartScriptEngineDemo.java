package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

public class SmartScriptEngineDemo {

	public static void main(String[] args) throws IOException {

		demo1("webroot/scripts/osnovni.smscr", System.out);
	}

	public static void demo1(String scriptName,
			OutputStream os)
			throws IOException {
		String documentBody = readFromDisk(scriptName);
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> pParams = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();

		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(os, params,
						pParams,
						cookies))
						.execute();
	}

	public static void demo2(String scriptName, OutputStream os)
			throws IOException {
		String docBody = readFromDisk(scriptName);
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> pParams = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();

		params.put("a", "4");
		params.put("b", "2");

		new SmartScriptEngine(
				new SmartScriptParser(docBody)
						.getDocumentNode(),
				new RequestContext(os, params, pParams, cookies)).execute();

	}

	public static void demo3(String scriptName, OutputStream os)
			throws IOException {
		String docBody = readFromDisk(scriptName);
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> pParams = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();

		RequestContext rc = new RequestContext(System.out, params, pParams, cookies);
		pParams.put("brojPoziva", "3");
		new SmartScriptEngine(
				new SmartScriptParser(docBody)
						.getDocumentNode(),
				rc).execute();

		System.out.println("Vrijednost u mapi: "
				+ rc.getPersistentParameter("brojPoziva"));
	}

	public static String readFromDisk(String fileName) throws IOException {
			return Files.readString(Paths.get(fileName));
	}
}
