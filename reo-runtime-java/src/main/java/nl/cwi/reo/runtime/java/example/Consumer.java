package nl.cwi.reo.runtime.java.example;

import nl.cwi.reo.runtime.java.Component;
import nl.cwi.reo.runtime.java.Port;

public class Consumer implements Component {

	private volatile Port<Integer> a;
	
	public Consumer(Port<Integer> a) {
		a.setConsumer(this);
		this.a = a;
	}

	public void run() {
		MyTestComponents.consume(a);
	}
	
	public synchronized void activate() {
		notify();
	}
}
