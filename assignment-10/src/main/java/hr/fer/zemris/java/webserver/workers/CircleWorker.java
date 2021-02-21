package hr.fer.zemris.java.webserver.workers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Worker that renders a simple circle
 * 
 * @author jankovidakovic
 *
 */
public class CircleWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {

		BufferedImage bim =
				new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D g2d = bim.createGraphics();
		// draw circle
		g2d.drawOval(0, 0, 199, 199);
		g2d.setColor(Color.RED);
		g2d.fillOval(0, 0, 201, 201);
		g2d.dispose();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bim, "png", bos);
			context.setMimeType("image/png");
			context.setStatusCode(200);
			context.setStatusText("OK");
			context.write(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
