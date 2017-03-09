/**
 * 
 */
package nl.cwi.reo.templates;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

/**
 * Contains a number of primitive processes.
 */
public class Processes {

	/**
	 * Template for a producer process that repeatedly offers a single datum at
	 * its output port.
	 * 
	 * @param name
	 *            name of this process
	 * @param port
	 *            output port
	 * @param datum
	 *            offered datum
	 * @param k
	 *            number of repetitions. If null, then the number of repetitions
	 *            is infinite.
	 */
	public static void producer(String name, OutputPort port, Object datum, Integer k) {
		if (k == null)
			while (true)
				port.putUninterruptibly(datum);
		else {
			long a = System.nanoTime();
			for (int i = 0; i < k; i++)
				port.putUninterruptibly(datum);
			long b = System.nanoTime();
			String time = String.format("%f", (b - a) / 1000000000.0);
			String c = name != null ? name : "Producer";
			System.out.println(c + " finished after " + time + " sec.");
		}
	}

	/**
	 * Template for a consumer process that repeatedly requests a single datum
	 * at its input port.
	 * 
	 * @param name
	 *            name of this process
	 * @param port
	 *            output port
	 * @param datum
	 *            offered datum
	 * @param k
	 *            number of repetitions. If null, then the number of repetitions
	 *            is infinite.
	 */
	public static void cons(String name, InputPort port, Integer k) {
		if (k == null)
			while (true)
				System.out.println(port.getUninterruptibly());
		else {
			long a = System.nanoTime();
			for (int i = 0; i < k; i++)
				System.out.println(port.getUninterruptibly());
			long b = System.nanoTime();
			String time = String.format("%f", (b - a) / 1000000000.0);
			String c = name != null ? name : "Consumer";
			System.out.println(c + " finished after " + time + " sec.");
		}
	}

	public static void Red(OutputPort port) {
		while (true) {
			for (int i = 0; i < 30000000; ++i)
				;
			Object datum = "Hello, ";
			port.putUninterruptibly(datum);
		}
	}

	public static void Green(OutputPort port) {
		while (true) {
			for (int i = 0; i < 50000000; ++i)
				;
			Object datum = "world! ";
			port.putUninterruptibly(datum);
		}
	}

	public static void Blue(InputPort port) {
		for (int k = 0; k < 10; ++k) {
			for (int i = 0; i < 40000000; ++i)
				;
			Object datum = port.getUninterruptibly();
			System.out.print(datum);
		}
	}
}
