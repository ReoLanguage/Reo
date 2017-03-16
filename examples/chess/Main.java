import java.text.*;
import java.util.*;
import java.util.concurrent.*;

import nl.cwi.pr.runtime.*;
import nl.cwi.pr.runtime.api.*;

public class Main {

	//
	// MAIN
	//

	public static void main(String[] args) {
		long time = -System.nanoTime();

		OutputPort _19 = Ports.newOutputPort();
		OutputPort _3 = Ports.newOutputPort();
		InputPort _13 = Ports.newInputPort();
		InputPort _16 = Ports.newInputPort();
		InputPort _17 = Ports.newInputPort();
		InputPort _18 = Ports.newInputPort();

		new Protocol_Chess(
			_19,
			_3,
			_13,
			_16,
			_17,
			_18
		);

		Thread worker0 = new Worker_Engine_1(
			_3,
			_18
		);

		Thread worker1 = new Worker_Engine_0(
			_19,
			_13
		);

		Thread worker2 = new Worker_Display_2(
			_16,
			_17
		);

		worker0.start();
		worker1.start();
		worker2.start();

		long timeToInitialize = time + System.nanoTime();

		/*
		 * Run the application as a benchmark 
		 */

		if (Benchmark.IS_ENABLED) {

			/*
			 * Warm up
			 */

			Benchmark.SEMAPHORE.acquireUninterruptibly(3);
			Benchmark.BARRIER = new CyclicBarrier(3 + 1);
			Benchmark.N_GETS.set(0);
			Benchmark.N_PUTS.set(0);

			try {
				Thread.sleep(Benchmark.WARM_UP_TIME * 1000);
			} catch (InterruptedException e) {
			}

			worker0.interrupt();
			worker1.interrupt();
			worker2.interrupt();

			//Benchmark.SEMAPHORE.acquireUninterruptibly(3);
			try {
				Benchmark.BARRIER.await();
			} catch (InterruptedException | BrokenBarrierException exception) {
				exception.printStackTrace();
				System.exit(0);
			}

			/*
			 * Measure
			 */

			try {
				Thread.sleep(Benchmark.MEASURE_TIME * 1000);
			} catch (InterruptedException e) {
			}

			worker0.interrupt();
			worker1.interrupt();
			worker2.interrupt();

			Benchmark.SEMAPHORE.acquireUninterruptibly(3);
			long nPuts = Benchmark.N_PUTS.get();
			long nGets = Benchmark.N_GETS.get();

			System.out
					.println(new DecimalFormat("0.000")
							.format(timeToInitialize / 1e9)
							+ " "
							+ nPuts
							+ " "
							+ nGets);
		}

		/*
		 * Run the application normally
		 */

		else {
			final Map<String, InputPort> freeInputPorts = new HashMap<>();
			final Map<String, OutputPort> freeOutputPorts = new HashMap<>();
			Thread windowsThread = new Thread() {
				@Override
				public void run() {
					if (!freeInputPorts.isEmpty() || !freeOutputPorts.isEmpty())
						PortWindows.openThenWait(freeInputPorts, freeOutputPorts);
				}
			};

			windowsThread.start();

			while (true)
				try {
					worker0.join();
					worker1.join();
					worker2.join();
					windowsThread.join();
					break;
				} catch (InterruptedException exception) {
				}
		}

		System.exit(0);
	}
}
