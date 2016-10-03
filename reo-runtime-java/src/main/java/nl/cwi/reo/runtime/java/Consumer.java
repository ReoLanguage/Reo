package nl.cwi.reo.runtime.java;

public class Consumer implements Component {

	private volatile Port<Integer> a;
	
	public Consumer(Port<Integer> a) {
		a.setConsumer(this);
		this.a = a;
	}

	public void run() {
		MyProgram.consume(a);
	}
	
	public synchronized void activate() {
		notify();
	}
}
