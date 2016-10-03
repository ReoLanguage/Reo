package nl.cwi.reo.runtime.java;

public class PortBusyWait<T> implements Port<T> {
	private Component prod;
	private Component cons;
	private volatile T put;
	private volatile boolean get;
	public void setProducer(Component p) { prod = p; }
	public void setConsumer(Component c) { cons = c; }	
	public void setGet() { get = true; }
	public void setPut(T datum) { put = datum; }
	public boolean canPut() { return get && put == null; }
	public boolean canGet() { return put != null; }
	public void activateProducer() { prod.activate(); }
	public void activateConsumer() { cons.activate(); }
	public void put(T datum) {
		if (datum == null) throw new NullPointerException();
		while (put != null) { }
		put = datum; 
		cons.activate();
		// Wait for signal from scheduler
		while (!get && put != null) { }
	}
	public boolean put(T datum, long t) {
		if (datum == null) throw new NullPointerException();
		long deadline = System.nanoTime() + t;
		while (put != null) { }
		put = datum; 
		cons.activate();
		// Wait for signal from scheduler
		while (!get && put != null) { 
			if (System.nanoTime() >= deadline) {
				put = null;  // use compare and set here
				return false; // not thread safe, since datum may have been taken
			}
		}
		return true;
	}
	public T get() {
		get = true; 
		prod.activate();
		// Wait for signal from scheduler
		T datum;
		while ((datum = put) == null) { }
		put = null; 
		get = false; 
		prod.activate();
		return datum;
	}
}