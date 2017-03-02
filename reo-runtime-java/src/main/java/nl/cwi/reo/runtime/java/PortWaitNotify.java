package nl.cwi.reo.runtime.java;

@SuppressWarnings("initialization")
public class PortWaitNotify<T> implements Port<T> {
	private Component prod;
	private Component cons;
	private volatile T put;
	private volatile boolean get;
	public void setProducer(Component p) { prod = p; }
	public void setConsumer(Component c) { cons = c; }
	public void setGet() { get = true; }
	public void setPut(T datum) { put = datum; }
	public T take() { 
		T datum = put;
		put = null; 
		get = false; 
		return datum;
	}
	public boolean hasGet() { return get && put == null; }
	public boolean hasPut() { return put != null; }
	public void activateProducer() { prod.activate(); }
	public void activateConsumer() { cons.activate(); }
	public void put(T datum) {
		if (datum == null) throw new NullPointerException();
		while (put != null) { }
		put = datum; 
		cons.activate();
		if (!get && put != null)
			synchronized (prod) {
				while (!get && put != null)
					try	{ prod.wait(); } catch (InterruptedException e) { }
			}
	}
	public T get() {
		get = true; 
		prod.activate();
		if (put == null)
			synchronized (cons) {
				while (put == null) 
					try	{ cons.wait(); } catch (InterruptedException e) { }
			}
		T datum = put;
		put = null; 
		get = false; 
		prod.activate();
		return datum;
	}
}