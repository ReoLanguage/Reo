package nl.cwi.pr.runtime;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

import nl.cwi.pr.runtime.api.InputPort;

public class BenchmarkConsumer extends Thread {

	//
	// FIELDS
	//

	private final InputPort port;
	private final boolean report;

	//
	// CONSTRUCTORS
	//

	public BenchmarkConsumer(final InputPort port) {
		this(port, true);
	}

	public BenchmarkConsumer(final InputPort port, final boolean report) {
		this.port = port;
		this.report = report;
	}

	//
	// METHODS
	//

	@Override
	public void run() {
		Benchmark.SEMAPHORE.release();

		/*
		 * Warm up
		 */

		try {
			while (!Thread.interrupted())
				port.get();
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
				port.get();
				i++;

				if (Benchmark.CONS_WORK_TIME > 0)
					Benchmark.spin(random.nextInt(Benchmark.CONS_WORK_TIME));
			}
		}

		catch (InterruptedException exception) {
		}

		if (report)
			Benchmark.N_GETS.addAndGet(i);
		Benchmark.SEMAPHORE.release();

		/*
		 * Cool down
		 */

		while (true)
			try {
				port.resume();
				port.get();
			} catch (InterruptedException exception) {
			}
	}
}
