package hr.fer.zemris.java.raytracer;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerAnimator;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

public class RayCasterParallel2 {

	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), getIRayTracerAnimator(),
				30, 30);

	}

	private static IRayTracerAnimator getIRayTracerAnimator() {
		return new IRayTracerAnimator() {
			long time;

			@Override
			public void update(long deltaTime) {
				time += deltaTime;
			}

			@Override
			public Point3D getViewUp() { // fixed in time
				return new Point3D(0, 0, 10);
			}

			@Override
			public Point3D getView() { // fixed in time
				return new Point3D(-2, 0, -0.5);
			}

			@Override
			public long getTargetTimeFrameDuration() {
				return 100; // redraw scene each 150 milliseconds
			}

			@Override
			public Point3D getEye() { // changes in time
				double t = (double) time / 10000 * 2 * Math.PI;
				double t2 = (double) time / 5000 * 2 * Math.PI;
				double x = 50 * Math.cos(t);
				double y = 50 * Math.sin(t);
				double z = 30 * Math.sin(t2);
				return new Point3D(x, y, z);
			}

		};
	}

	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp,
					double horizontal, double vertical, int width, int height,
					long requestNo, IRayTracerResultObserver observer,
					AtomicBoolean cancel) {
				System.out.println("Starting...");
				
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(new RayCastingAction(eye, view, viewUp, horizontal,
						vertical, width, height, 0, height, red, green,
						blue, cancel));

				pool.shutdown();

				System.out.println("Done.");
				observer.acceptResult(red, green, blue, requestNo);
			}
		};

	}

	public static class RayCastingAction extends RecursiveAction {
		private Point3D eye;
		private Point3D view;
		private Point3D viewUp;
		double horizontal;
		double vertical;
		int width;
		int height;
		int yMin;
		int yMax;
		short[] red;
		short[] green;
		short[] blue;
		AtomicBoolean cancel;

		static final int threshold = 16;


		/**
		 * Constructs a new ray casting action.
		 * 
		 * @param eye        viewer's eye
		 * @param view       point of view
		 * @param viewUp     vector of upwards direction
		 * @param horizontal horizontal vector length
		 * @param vertical   vertical vector length
		 * @param width      width of the screen
		 * @param height     height of the screen
		 * @param yMin       minimum y-value for which the action will be
		 *                   performed
		 * @param yMax       maximum y-value for which the action will be
		 *                   performed
		 * @param red        red component
		 * @param green      green component
		 * @param blue       blue component
		 * @param cancel     if set to <code>true</code>, calculation is
		 *                   cancelled
		 */
		public RayCastingAction(Point3D eye, Point3D view, Point3D viewUp,
				double horizontal, double vertical, int width, int height,
				int yMin, int yMax, short[] red, short[] green,
				short[] blue,
				AtomicBoolean cancel) {
			super();
			this.eye = eye;
			this.view = view;
			this.viewUp = viewUp;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.cancel = cancel;
		}

		@Override
		public void compute() {
			if (cancel.get()) {
				return;
			}
			if (yMax - yMin < threshold) {
				computeWithoutDecomposition();
			} else {
				invokeAll(new RayCastingAction(eye, view, viewUp, horizontal,
						vertical, width, height, yMin, yMin + (yMax - yMin) / 2, //was without -1
						red, green, blue,
						cancel),
						new RayCastingAction(eye, view, viewUp, horizontal,
								vertical, width, height,
								yMin + (yMax - yMin) / 2, yMax, red, green, //was without -1
								blue, cancel));
			}
		}

		/**
		 * Computes the calculation directly, without splitting.
		 */
		public void computeWithoutDecomposition() {


			Point3D viewUpNormalized = viewUp.normalize();
			Point3D OGNormalized = view.sub(eye).normalize();

			Point3D yAxis = viewUpNormalized
					.sub(OGNormalized.scalarMultiply(
							OGNormalized.scalarProduct(viewUpNormalized)))
					.normalize();
			Point3D xAxis = OGNormalized.vectorProduct(yAxis).normalize();
			Point3D zAxis = yAxis.vectorProduct(xAxis).normalize();

			Point3D screenCorner =
					view.sub(xAxis.scalarMultiply(horizontal / 2))
							.add(yAxis.scalarMultiply(vertical / 2));

			Scene scene = RayTracerViewer.createPredefinedScene2(); //WAS WITHOUT 2

			short[] rgb = new short[3];
			int offset = yMin * height;
			for (int y = yMin; y < yMax; y++) {
				for (int x = 0; x < width; x++) {


					Point3D screenPoint = screenCorner
							.add(xAxis.scalarMultiply(
									x * horizontal / (width - 1)))
							.sub(yAxis.scalarMultiply(
									y * vertical / (height - 1)));

					Ray ray = Ray.fromPoints(eye, screenPoint);

					tracer(scene, ray, rgb);

					try {
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
					} catch (ArrayIndexOutOfBoundsException ignored) {
						//exception can happen due to the viewer window dimensions being stretched out
					}

					offset++;
				}
			}

		}

	}




	/**
	 * Traces a ray through a scene and finds out the color of the pixel that it
	 * passes through.
	 * 
	 * @param scene Scene
	 * @param ray   Ray
	 * @param rgb   array for storing rgb color value
	 */
	private static void tracer(Scene scene, Ray ray, short[] rgb) {

		// assume ray does not intersect any object and therefore it's black
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;

		// try to find the intersection
		RayIntersection closest = findClosestIntersection(scene, ray);
		if (closest == null) { // no intersection
			return;
		}

		// determine the pixel's color
		short[] colors = determineColorFor(scene, closest, ray.start);
		rgb[0] = colors[0];
		rgb[1] = colors[1];
		rgb[2] = colors[2];

	}

	/**
	 * Finds the closest object to the ray's starting point that the ray
	 * intersects.
	 * 
	 * @param  scene Scene
	 * @param  ray   Ray
	 * @return       Intersection object, if there exists an intersection, or
	 *               <code>null</code> if it does not.
	 */
	private static RayIntersection findClosestIntersection(Scene scene,
			Ray ray) {

		RayIntersection closestIntersection = null; // assume there is none
		List<GraphicalObject> objects = scene.getObjects();
		double minDistance = -1;

		for (GraphicalObject obj : objects) { // check every object
			// try to find intersection
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

		// intersection starts at eye, closestIntersection starts at light source

		short[] rgb = new short[] { 15, 15, 15 }; // ambient component
		List<LightSource> lights = scene.getLights();
		for (LightSource light : lights) { // for each light source
			Ray lightRay =
					Ray.fromPoints(light.getPoint(), intersection.getPoint());
			// find first object that the light ray hits
			RayIntersection closestIntersection =
					findClosestIntersection(scene, lightRay);
			if (closestIntersection != null) { // there exists at least one
				double distanceClosestToLS = closestIntersection.getPoint()
						.sub(light.getPoint()).norm();
				double distanceOriginalToLS =
						intersection.getPoint().sub(light.getPoint()).norm();
				if (distanceClosestToLS - distanceOriginalToLS >= -1E-9) {

					// diffuse component
					Point3D l = light.getPoint().sub(intersection.getPoint())
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
