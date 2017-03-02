package nl.cwi.reo.runtime.java;

@SuppressWarnings("initialization")
public class PortBusyWait<T> implements Port<T> {
	private Component prod;
	private Component cons;
	private volatile T put;
	private volatile boolean get;
	public void setProducer(Component p) { prod = p; }
	public void setConsumer(Component c) { cons = c; }	
	public void setGet() { get = true; }
	public void setPut(T datum) { put = datum; }
	public boolean hasGet() { return get; }
	public boolean hasPut() { return put != null; }
	public T take() { 
		T datum;
		while ((datum = put) == null) { }
		put = null; 
		get = false; 
		prod.activate();
		return datum; 
	}
	public void activateProducer() { prod.activate(); }
	public void activateConsumer() { cons.activate(); }
	public void put(T datum) {
		if (datum == null) throw new NullPointerException();
		// Signal the scheduler
		while (put != null) { }
		put = datum; 
		cons.activate();
		// Wait for signal from scheduler
		while (!get && put != null) { }
	}
	public T get() {
		// Signal the scheduler
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
	public boolean put(T datum, long timeout) {
		if (datum == null) throw new NullPointerException();
		// Signal the scheduler
		long deadline = System.nanoTime() + timeout;
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
	public T get(long timeout) {
		// Signal the scheduler
		long deadline = System.nanoTime() + timeout;
		get = true; 
		prod.activate();
		// Wait for signal from scheduler
		T datum;
		while ((datum = put) == null) {
			if (System.nanoTime() >= deadline) {
				put = null;  // use compare and set here
				return null; // not thread safe, since datum may have been taken
			}
		}
		put = null; 
		get = false; 
		prod.activate();
		return datum;
	}
}