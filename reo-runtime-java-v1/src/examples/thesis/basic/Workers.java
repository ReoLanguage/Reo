package nl.cwi.pr.runtime.examples.thesis.basic;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Workers {

	public static void Producer(OutputPort port) {
		while (true) {
			Object datum = Thread.currentThread().getId();
			port.putUninterruptibly(datum);
		}
	}

	public static void Consumer(InputPort port) {
		while (true) {
			Object datum = port.getUninterruptibly();
			System.out.println(Thread.currentThread().getId() + ".get() = "
					+ datum);
		}
	}
}
