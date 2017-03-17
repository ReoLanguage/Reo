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

		OutputPort _1 = Ports.newOutputPort();
		OutputPort _18 = Ports.newOutputPort();
		OutputPort _28 = Ports.newOutputPort();
		OutputPort _29 = Ports.newOutputPort();
		InputPort _13 = Ports.newInputPort();
		InputPort _14 = Ports.newInputPort();
		InputPort _17 = Ports.newInputPort();
		InputPort _27 = Ports.newInputPort();
		InputPort _5 = Ports.newInputPort();
		InputPort _9 = Ports.newInputPort();

		new Protocol_Chess(
			_1,
			_18,
			_28,
			_29,
			_13,
			_14,
			_17,
			_27,
			_5,
			_9
		);

		Thread worker0 = new Worker_Engine_1(
			_18,
			_27
		);

		Thread worker1 = new Worker_Engine_0(
			_29,
			_9
		);

		Thread worker2 = new Worker_Engine_3(
			_1,
			_17
		);

		Thread worker3 = new Worker_Engine_2(
			_28,
			_5
		);

		Thread worker4 = new Worker_Display_4(
			_13,
			_14
		);

		worker0.start();
		worker1.start();
		worker2.start();
		worker3.start();
		worker4.start();

		long timeToInitialize = time + System.nanoTime();

		/*
		 * Run the application as a benchmark 
		 */

		if (Benchmark.IS_ENABLED) {

			/*
			 * Warm up
			 */

			Benchmark.SEMAPHORE.acquireUninterruptibly(5);
			Benchmark.BARRIER = new CyclicBarrier(5 + 1);
			Benchmark.N_GETS.set(0);
			Benchmark.N_PUTS.set(0);

			try {
				Thread.sleep(Benchmark.WARM_UP_TIME * 1000);
			} catch (InterruptedException e) {
			}

			worker0.interrupt();
			worker1.interrupt();
			worker2.interrupt();
			worker3.interrupt();
			worker4.interrupt();

			//Benchmark.SEMAPHORE.acquireUninterruptibly(5);
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
			worker3.interrupt();
			worker4.interrupt();

			Benchmark.SEMAPHORE.acquireUninterruptibly(5);
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
					worker3.join();
					worker4.join();
					windowsThread.join();
					break;
				} catch (InterruptedException exception) {
				}
		}

		System.exit(0);
	}
}
