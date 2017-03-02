package nl.cwi.reo.runtime.java;

@SuppressWarnings("initialization")
public class PortRingBuffer<T> implements Port<T> {
	private Component prod;
	private Component cons;
	private volatile T[] put;
	private volatile int head = 0;
	private volatile int tail = 0;
	@SuppressWarnings("unchecked")
	public PortRingBuffer(int c) { 
		if (c <= 0) throw new IllegalArgumentException();
		put = (T[]) new Object[c + 1]; 
	}
	public void setProducer(Component p) { prod = p; }
	public void setConsumer(Component c) { cons = c; }	
	public void setPut(T datum) { 
	    int n = head + 1;
		if (n == put.length) n = 0;
		put[n] = datum;
		head = n;
	}
	public void setGet() { }
	public boolean hasPut() { return head != tail; }
	public boolean hasGet() { 
		int n;
		return tail != ((n = head + 1) == put.length ? 0 : n); 
	}
	public T take() { 
		int n;
		tail = (n = tail + 1) == put.length ? 0 : n;
	    return put[tail];
	}
	public void activateProducer() { prod.activate(); }
	public void activateConsumer() { cons.activate(); }
	public void put(T datum) {
		setPut(datum);
		cons.activate();
		int n;
		while (tail == ((n = head + 1) == put.length ? 0 : n)) { }
	}
	public T get() {
		T datum = null;
		int n;
		if (tail == ((n = head + 1) == put.length ? 0 : n)) {
			datum = take();
			prod.activate();
		} else {
			while (head == tail) { }
			datum = take();
		}
		return datum;
	}
}