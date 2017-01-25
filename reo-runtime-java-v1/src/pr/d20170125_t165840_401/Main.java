package pr.d20170125_t165840_401;

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

		OutputPort BlackReturn$1 = Ports.newOutputPort();
		OutputPort WhiteReturn$1$1 = Ports.newOutputPort();
		OutputPort WhiteReturn$1$2 = Ports.newOutputPort();
		OutputPort WhiteReturn$1$3 = Ports.newOutputPort();
		InputPort BlackCall$1 = Ports.newInputPort();
		InputPort BlackMove$1 = Ports.newInputPort();
		InputPort WhiteCall$1$1 = Ports.newInputPort();
		InputPort WhiteCall$1$2 = Ports.newInputPort();
		InputPort WhiteCall$1$3 = Ports.newInputPort();
		InputPort WhiteMove$1 = Ports.newInputPort();

		new pr.d20170125_t165840_401.Protocol_d20170125_t165840_528_Chess(
			BlackReturn$1,
			WhiteReturn$1$1,
			WhiteReturn$1$2,
			WhiteReturn$1$3,
			BlackCall$1,
			BlackMove$1,
			WhiteCall$1$1,
			WhiteCall$1$2,
			WhiteCall$1$3,
			WhiteMove$1
		);

		Thread worker0 = new pr.d20170125_t165840_401.Worker_d20170125_t165840_961_Engine(
			WhiteReturn$1$3,
			WhiteCall$1$3
		);

		Thread worker1 = new pr.d20170125_t165840_401.Worker_d20170125_t165840_920_Display(
			BlackMove$1,
			WhiteMove$1
		);

		Thread worker2 = new pr.d20170125_t165840_401.Worker_d20170125_t165840_948_Engine(
			WhiteReturn$1$2,
			WhiteCall$1$2
		);

		Thread worker3 = new pr.d20170125_t165840_401.Worker_d20170125_t165840_974_Engine(
			BlackReturn$1,
			BlackCall$1
		);

		Thread worker4 = new pr.d20170125_t165840_401.Worker_d20170125_t165840_934_Engine(
			WhiteReturn$1$1,
			WhiteCall$1$1
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
