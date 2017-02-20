package nl.cwi.pr.runtime;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

import nl.cwi.pr.runtime.api.OutputPort;

public class BenchmarkProducer extends Thread {

	//
	// FIELDS
	//

	private final Datum datum;
	private final OutputPort port;
	private final boolean report;

	//
	// CONSTRUCTORS
	//

	public BenchmarkProducer(final OutputPort port) {
		this(port, true);
	}

	public BenchmarkProducer(final OutputPort port, boolean report) {
		this(port, report, new Datum() {
			@Override
			public Object produce() {
				return 0;
			}
		});
	}

	public BenchmarkProducer(final OutputPort port, Datum datum) {
		this(port, true, datum);
	}

	public BenchmarkProducer(final OutputPort port, boolean report, Datum datum) {
		this.port = port;
		this.datum = datum;
		this.report = report;
	}

	//
	// METHODS
	//

	@Override
	public void run() {
		Object object;

		Benchmark.SEMAPHORE.release();
		
		/*
		 * Warm up
		 */

		try {
			while (!Thread.interrupted()) {
				object = datum.produce();
				port.put(object);
			}
		}

		catch (InterruptedException exception) {
		}
		
		try {
			Benchmark.BARRIER.await();
		} catch (InterruptedException | BrokenBarrierException exception) {
			exception.printStackTrace();
			System.exit(0);
		}

		/*
		 * Measure
		 */

		Random random = new Random();
		int i = 0;

		try {
			port.resume();
			while (!Thread.interrupted()) {
				if (Benchmark.PROD_WORK_TIME > 0)
					Benchmark.spin(random.nextInt(Benchmark.PROD_WORK_TIME));

				object = datum.produce();
				port.put(object);
				i++;
			}
		}

		catch (InterruptedException exception) {
		}

		if (report)
			Benchmark.N_PUTS.addAndGet(i);
		Benchmark.SEMAPHORE.release();

		/*
		 * Cool down
		 */

		while (true)
			try {
				port.resume();
				port.put(datum.produce());
			} catch (InterruptedException exception) {
			}
	}

	//
	// STATIC - INTERFACES
	//

	public static interface Datum {
		public Object produce();
	}
}
