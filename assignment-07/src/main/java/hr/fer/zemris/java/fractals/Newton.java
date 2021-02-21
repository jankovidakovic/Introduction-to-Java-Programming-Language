package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

public class Newton {

	public static ComplexPolynomial polynomial;
	public static ComplexRootedPolynomial rootedPolynomial;

	/**
	 * Performs the Newton-Rhapson iteration over all dots in given y-range and
	 * saves the results in data array.
	 * 
	 * @param reMin  minimum real value for which the calculation will be
	 *               performed
	 * @param reMax  maximum real value
	 * @param imMin  minimum imaginary value
	 * @param imMax  maximum imaginary value
	 * @param width  width of the screen that display the fractal
	 * @param height height of the screen
	 * @param yMin   minimum y-value for which the calculation is performed
	 * @param yMax   maximum y-value for which the calculation is performed
	 * @param m      number of iterations
	 * @param data   array for storing results
	 * @param cancel calculation is cancelled when this parameter is set to
	 *               <code>true</code>.
	 */
	public static void calculate(double reMin, double reMax,
			double imMin, double imMax, int width, int height, int yMin,
			int yMax, int m,
			short[] data, AtomicBoolean cancel) {
		
		double convergenceTreshold = 1E-3;
		double rootTreshold = 0.002;
		int offset = width * yMin;
		for (int y = yMin; y <= yMax; y++) {
			if (cancel.get()) {
				break;
			}
			for (int x = 0; x < width; x++) {
				double realPart =
						(double) x / (double) (width - 1) * (reMax - reMin)
								+ reMin;
				double imagPart =
						(double) (height - 1 - y) / (double) (height - 1)
								* (imMax - imMin)
								+ imMin;
				Complex zn = new Complex(realPart, imagPart);
				Complex znold;
				int it = 0;
				do {
					znold = new Complex(zn.getReal(), zn.getImaginary());
					Complex numerator = polynomial.apply(zn);
					Complex denominator = polynomial.derive().apply(zn);
					Complex fraction = numerator.divide(denominator);
					zn = zn.sub(fraction);
					it++;
				} while (zn.sub(znold).module() > convergenceTreshold
						&& it < m);
				int colorIndex = rootedPolynomial.indexOfClosestRootFor(zn,
						rootTreshold);
				data[offset++] = (short) (colorIndex + 1);
				// System.out.println(colorIndex + 1);
			}
		}
	}
	public static void main(String[] args) {

		System.out.println(
				"Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println(
				"Please enter at least two roots, one per line. "
						+ "Enter 'done' when done.");
		List<Complex> roots = new ArrayList<>();
		String input;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("Root " + (roots.size() + 1) + "> ");
			input = sc.nextLine();
			if (input.equals("done")) {
				break;
			}
			try {
				roots.add(Complex.parseComplex(input));
			} catch (IllegalArgumentException ex) {
				System.out.println("Invalid syntax. please try again.");
			}

		} while (true);
		sc.close();

		System.out.println("Image of fractal will appear shortly. Thank you.");
		rootedPolynomial = new ComplexRootedPolynomial(Complex.ONE,
				roots.toArray(new Complex[0]));
		polynomial = rootedPolynomial.toComplexPolynom();
		FractalViewer.show(new NewtonProducer());
	}

	public static class IterationJob implements Callable<Void> {

		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		int yMin;
		int yMax;
		int m;
		short[] data;
		AtomicBoolean cancel;

		/**
		 * Constructs a new job which will be executed with the given
		 * parameters.
		 * 
		 * @param reMin  minimum real value of a complex number for which the
		 *               Newton-Rhapson iteration will be performed.
		 * @param reMax  maximum real value
		 * @param imMin  minimum imaginary value
		 * @param imMax  maximum imaginary value
		 * @param width  width of the raster used to draw the fractal
		 * @param height height of the raster
		 * @param yMin   minimum value of y-coordinate of the raster
		 * @param yMax   maximum value of y-coordinate
		 * @param m      number of iterations
		 * @param data   indexes of roots in which observed complex point c has
		 *               converged or 0 if no convergence to a root occurred.
		 * @param cancel
		 */
		public IterationJob(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, int m,
				short[] data, AtomicBoolean cancel) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
			this.cancel = cancel;
		}

		@Override
		public Void call() {
			Newton.calculate(reMin, reMax, imMin, imMax, width, height, yMin,
					yMax, m, data, cancel);
			return null;
		}

	}

	/**
	 * Produces the fractal image to be displayed. Uses thread pools for
	 * parallelization of calculations.
	 * 
	 * @author jankovidakovic
	 *
	 */
	public static class NewtonProducer implements IFractalProducer {

		@Override
		public void produce(double reMin, double reMax, double imMin,
				double imMax, int width, int height, long requestNo,
				IFractalResultObserver observer, AtomicBoolean cancel) {

			int m = 16 * 16 * 16;
			short[] data = new short[width * height];
			final int jobs = Runtime.getRuntime().availableProcessors() * 8; // 32
			int rangePerJob = height / jobs;

			ExecutorService pool = Executors.newFixedThreadPool(
					Runtime.getRuntime().availableProcessors(),
					new DaemonicThreadFactory());
			List<Future<Void>> results = new ArrayList<>();

			for (int i = 0; i < jobs; i++) {
				int yMin = i * rangePerJob;
				int yMax = (i + 1) * rangePerJob - 1;
				if (i == jobs - 1) {
					yMax = height - 1;
				}
				IterationJob job = new IterationJob(reMin, reMax, imMin, imMax,
						width, height, yMin, yMax, m, data, cancel);
				results.add(pool.submit(job));
			}

			for (Future<Void> job : results) {
				try {
					job.get();
				} catch (InterruptedException | ExecutionException ignored) {
				}
			}

			pool.shutdown();


			observer.acceptResult(data,
					(short) (polynomial.order() + 1),
					requestNo);
		}

	}

	/**
	 * Factory of threads that have the daemonic property.
	 * 
	 * @author jankovidakovic
	 *
	 */
	public static class DaemonicThreadFactory implements ThreadFactory {

		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setDaemon(true);
			return thread;
		}

	}

}
