package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Worker that processes the calculation of the sum of two integer numbers,
 * which are given as URI parameters
 * 
 * @author jankovidakovic
 *
 */
public class SumWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {

		// get operands
		String a = context.getParameter("a");
		String b = context.getParameter("b");
		int sum = 0;

		int aNum = 0;
		int bNum = 0;
		try {
			aNum = Integer.parseInt(a);
		} catch (NumberFormatException e) {
			aNum = 1;
		}
		try {
			bNum = Integer.parseInt(b);
		} catch (NumberFormatException e) {
			bNum = 2;
		}
		sum += aNum;
		sum += bNum;

		// set temporary parameters
		context.setTemporaryParameter("varA", Integer.toString(aNum));
		context.setTemporaryParameter("varB", Integer.toString(bNum));

		context.setTemporaryParameter("zbroj", Integer.toString(sum));

		// set appropriate image to be displayed, based on the result
		String imgName =
				sum % 2 == 0 ? "/images/image1.png" : "/images/image2.gif";

		context.setTemporaryParameter("imgName", imgName);

		// delegate the content rendering to a smart script
		context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
	}

}
