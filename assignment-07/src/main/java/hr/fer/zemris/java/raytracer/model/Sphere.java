package hr.fer.zemris.java.raytracer.model;
/**
 * Model of a sphere that is appropriate for the ray scanning program.
 * 
 * @author jankovidakovic
 *
 */
public class Sphere extends GraphicalObject {

	private final Point3D center; // center of the sphere
	private final double radius; // radius
	private final double kdr; // scaling factor for red diffuse component
	private final double kdg; // scaling factor for green diffusive component
	private final double kdb; // scaling factor for blue diffusive component
	private final double krr; // scaling factor for red reflective component
	private final double krg; // scaling factor for green reflective component
	private final double krb; // scaling factor for blue reflective component
	private final double krn; // shininess factor

	/**
	 * Constructs a sphere with given parameters
	 * 
	 * @param center center of a sphere
	 * @param radius sphere's radius
	 * @param kdr    parameter for the red part of the diffuse component
	 * @param kdg    parameter for the green part of the diffuse component
	 * @param kdb    parameter for the blue part of the diffuse component
	 * @param krr    parameter for the red part of the reflective component
	 * @param krg    parameter for the green part of the reflective component
	 * @param krb    parameter for the blue part of the reflective component
	 * @param krn    shininess factor
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg,
			double kdb, double krr, double krg, double krb, double krn) {
		super();
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}


	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		// vector from center to start of the ray
		Point3D l = ray.start.sub(center);
		// parameters for the quadratic equation
		double a = ray.direction.scalarProduct(ray.direction);
		double b = 2 * ray.direction.scalarProduct(l);
		double c = l.scalarProduct(l) - radius * radius;
		// finds the root of the quadratic equation that is closest to ray's
		// origin
		double t = findClosestRoot(a, b, c);
		if (!Double.isNaN(t)) { // equation has at least one root
			Point3D pointOfIntersection =
					ray.start.add(ray.direction.scalarMultiply(t));
			double distance = pointOfIntersection.sub(ray.start).norm();
			return new RayIntersection(pointOfIntersection, distance,
					pointOfIntersection.sub(center).norm()
							> radius) {

				@Override
				public Point3D getNormal() {
					return pointOfIntersection.sub(center);
				}

				@Override
				public double getKrr() {
					return krr;
				}

				@Override
				public double getKrn() {
					return krn;
				}

				@Override
				public double getKrg() {
					return krg;
				}

				@Override
				public double getKrb() {
					return krb;
				}

				@Override
				public double getKdr() {
					return kdr;
				}

				@Override
				public double getKdg() {
					return kdg;
				}

				@Override
				public double getKdb() {
					return kdb;
				}
			};
		} else {
			return null;
		}
	}

	/**
	 * Solves the quadratic equation with given parameters and returns the
	 * smallest found real solution. Returns NaN if there are no solutions.
	 * 
	 * @param  a second degree factor
	 * @param  b first degree factor
	 * @param  c zeroth degree factor
	 * @return   smallest real solution, or Double.NaN if there are no real
	 *           solutions.
	 */
	private double findClosestRoot(double a, double b, double c) {
		double disc = b * b - 4 * a * c; // discriminant
		if (disc < 0) { // no real solutions
			return Double.NaN;
		} else if (disc == 0) {
			return b / 2 * a;
		} else {
			double x1 = (Math.sqrt(disc) - b) / 2 * a;
			double x2 = -1 * (Math.sqrt(disc) + b) / 2 * a;
			return Math.min(x1, x2);
		}
	}
}
