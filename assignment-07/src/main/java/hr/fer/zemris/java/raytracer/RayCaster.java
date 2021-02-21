package hr.fer.zemris.java.raytracer;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Simple ray casting program that draws some defined scene using ray tracing
 * algorithm.
 * 
 * @author jankovidakovic
 *
 */
public class RayCaster {

	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0),
				new Point3D(0, 0, 0), new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Returns a new object that can draw a scene
	 * 
	 * @return new scene producer
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {

			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp,
					double horizontal, double vertical, int width, int height,
					long requestNo, IRayTracerResultObserver observer,
					AtomicBoolean cancel) {

				System.out.println("Zapocinjem izracune...");
				short[] red = new short[width * height]; // red values
				short[] green = new short[width * height];// green values
				short[] blue = new short[width * height]; // blue values

				// upwards direction
				Point3D viewUpNormalized = viewUp.normalize();

				// direction from the viewer's eye to the center of the screen
				Point3D OGNormalized = view.sub(eye).normalize();

				// axes

				Point3D yAxis = viewUpNormalized.sub(OGNormalized
						.scalarMultiply(
								OGNormalized.scalarProduct(viewUpNormalized)))
						.normalize();
				Point3D xAxis = OGNormalized.vectorProduct(yAxis).normalize();
				Point3D zAxis = yAxis.vectorProduct(xAxis).normalize();

				// point which corresponds to upper left corner of the screen
				Point3D screenCorner =
						view.sub(xAxis.scalarMultiply(horizontal / 2))
								.add(yAxis.scalarMultiply(vertical / 2));

				// create scene for drawing
				Scene scene = RayTracerViewer.createPredefinedScene();

				short[] rgb = new short[3]; // rgb values for one specific pixel
				int offset = 0; // position in the main color array
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {

						// point which corresponds to the current pixel
						Point3D screenPoint = screenCorner
								.add(xAxis.scalarMultiply(
										x * horizontal / (width - 1)))
								.sub(yAxis.scalarMultiply(
										y * vertical / (height - 1)));

						// ray from viewer's eye to the screen point
						Ray ray = Ray.fromPoints(eye, screenPoint);

						// calculate point's color
						tracer(scene, ray, rgb);

						// set point's color
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];

						offset++;
					}
				}

				System.out.println("Izracuni gotovi...");
				// draw the scene
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}

	/**
	 * Calculates the outcome of the given ray on the given scene. If ray
	 * intersects some object, the color of point of intersection is calculated
	 * based on the light sources on the scene, and other objects that may
	 * obscure the light sources.
	 * 
	 * @param scene Scene at which the ray operates
	 * @param ray   ray
	 * @param rgb   array for storing color of pixel of the screen which the ray
	 *              intersects.
	 */
	private static void tracer(Scene scene, Ray ray, short[] rgb) {

		// default values - assume no intersection
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;

		// try to find intersection with a graphical object
		RayIntersection closest = findClosestIntersection(scene, ray);
		if (closest == null) {
			return;
		}

		// calculate pixel's color
		short[] colors = determineColorFor(scene, closest, ray.start);
		rgb[0] = colors[0];
		rgb[1] = colors[1];
		rgb[2] = colors[2];

	}

	/**
	 * Finds the intersection between given ray and any object at the scene.
	 * Finds only the intersection that is closest to the ray's origin, since
	 * that is the only intersection that matters.
	 * 
	 * @param  scene Scene
	 * @param  ray   Ray
	 * @return       Intersection if it is found, or <code>null></code> if ray
	 *               does not intersect any objects.
	 */
	private static RayIntersection findClosestIntersection(Scene scene,
			Ray ray) {

		RayIntersection closestIntersection = null;
		List<GraphicalObject> objects = scene.getObjects();
		double minDistance = -1;

		for (GraphicalObject obj : objects) {
			RayIntersection intersection = obj.findClosestRayIntersection(ray);
			if (intersection != null) { // intersects
				if (minDistance == -1
						|| intersection.getDistance() < minDistance) {
					minDistance = intersection.getDistance();
					closestIntersection = intersection;
				}
			}
		}
		return closestIntersection;
	}

	/**
	 * Determines the color of the pixel that the intersection corresponds to.
	 * Color is constructed using ambient, diffuse and reflective components.
	 * Components are calculated separately for each light source that is not
	 * obstructed by some other object.
	 * 
	 * @param  scene        Scene
	 * @param  intersection Intersection for which the color of the pixel is to
	 *                      be obtained
	 * @param  eye          position of the viewer's eye
	 * @return              array of red, green, and blue values of the pixel.
	 */
	private static short[] determineColorFor(Scene scene,
			RayIntersection intersection, Point3D eye) {

		// intersection starts at eye, closestIntersection starts at lightsource

		short[] rgb = new short[] { 15, 15, 15 }; // ambient component

		List<LightSource> lights = scene.getLights();
		for (LightSource light : lights) { // for each light source
			Ray lightRay =
					Ray.fromPoints(light.getPoint(), intersection.getPoint());
			// find first object that the light ray intersects
			RayIntersection closestIntersection =
					findClosestIntersection(scene, lightRay);

			if (closestIntersection != null) { // there exists at least one
				double distanceClosestToLS = closestIntersection.getPoint()
						.sub(light.getPoint()).norm();
				double distanceOriginalToLS =
						intersection.getPoint().sub(light.getPoint()).norm();
				if (distanceClosestToLS - distanceOriginalToLS < -1E-9) {
					continue; // some object obscures the light source, no color
								// is added
				} else { // light source contributes

					// diffuse component
					Point3D l = light.getPoint().sub(intersection
							.getPoint())
							.normalize();

					Point3D n = intersection.getNormal().normalize();

					rgb[0] += light.getR() * intersection.getKdr()
							* Math.max(l.scalarProduct(n), 0);
					rgb[1] += light.getG() * intersection.getKdg()
							* Math.max(l.scalarProduct(n), 0);
					rgb[2] += light.getB() * intersection.getKdb()
							* Math.max(l.scalarProduct(n), 0);

					// reflective component
					Point3D nTruncated = n.scalarMultiply(l.scalarProduct(n));
					Point3D r = nTruncated.scalarMultiply(2).sub(l).normalize();
					Point3D v = eye.sub(intersection.getPoint()).normalize();

					double dotProduct = r.scalarProduct(v);

					rgb[0] += light.getR() * intersection.getKrr()
							* Math.pow(dotProduct, intersection.getKrn());
					rgb[1] += light.getG() * intersection.getKrg()
							* Math.pow(dotProduct, intersection.getKrn());
					rgb[2] += light.getB() * intersection.getKrb()
							* Math.pow(dotProduct, intersection.getKrn());

				}
			} else { // impossible
				continue;

			}
		}

		return rgb;
	}
}
